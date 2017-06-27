package serg.chuprin.processor;

import com.google.common.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.annotations.InjectViewState;
import serg.chuprin.mvp_core.view.MvpView;

@SupportedAnnotationTypes("serg.chuprin.mvp_core.annotations.InjectViewState")
public class MvpProcessor extends AbstractProcessor {

    private static Messager messager;
    private Types typeUtils;
    private Filer filer;

    static void error(Element element, String message, Object... args) {
        message(Diagnostic.Kind.ERROR, element, message, args);
    }

    private static void message(Diagnostic.Kind kind, Element element, String message, Object... args) {
        messager.printMessage(
                kind,
                String.format(message, args),
                element);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        List<Pair<TypeElement, String>> presenterViewParis = new ArrayList<>();

        for (Element annotatedElem : roundEnv.getElementsAnnotatedWith(InjectViewState.class)) {

            if (annotatedElem.getKind().isInterface()) {
                error(annotatedElem, "Only classes can be annotated with @%s",
                        InjectViewState.class.getSimpleName());
                return true;
            }

            if (!isPresenterSubclass(annotatedElem)) {
                error(annotatedElem, "Cannot inject MvpViewState in class which are not child of MvpPresenter");
                return true;
            }

            TypeElement elemAsType = (TypeElement) annotatedElem;
            TypeMirror viewType = ((DeclaredType) elemAsType.getSuperclass()).getTypeArguments().get(0);

            String viewStateClassName = new ViewStateGenerator(viewType, filer, typeUtils).generate();
            if (viewStateClassName.isEmpty()) {
                error(annotatedElem, "Failed to generate class");
                return true;
            }
            presenterViewParis.add(new Pair<>(elemAsType, viewStateClassName));
        }
        if (!presenterViewParis.isEmpty()) {
            if (!new ViewStateProviderGenerator(filer, presenterViewParis).generate()) {
                error(null, "Failed to generate MvpViewState factory class");
                return true;
            }
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private boolean isPresenterSubclass(Element annotatedElement) {
        TypeElement presenterElem = (TypeElement) annotatedElement;

        TypeToken<MvpPresenter<? extends MvpView>> presenterToken = new TypeToken<MvpPresenter<? extends MvpView>>() {
        };
        String presenterClassName = presenterToken.getRawType().getName();

        String presenterSuperclassName = typeUtils.erasure(presenterElem
                .getSuperclass())
                .toString();

        return presenterSuperclassName.equals(presenterClassName);
    }
}
