package serg.chuprin.mvp_core.processor;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class Utils {

    static boolean isSubclass(Types typeUtils, TypeElement element, Class<?> clazz) {
        String className = clazz.getName();

        TypeMirror superclass = element.getSuperclass();

        while (superclass.getKind() != TypeKind.NONE) {
            if (superclass.toString().contains(className)) {
                return true;
            }
            superclass = ((TypeElement) typeUtils.asElement(superclass)).getSuperclass();
        }
        return false;
    }

    static boolean isImplementingInterface(Elements elemUtils,
                                           Types typeUtils,
                                           TypeElement element,
                                           Class<?> interfaceClass) {

        TypeElement mvpViewElem = elemUtils.getTypeElement(interfaceClass.getName());
        return typeUtils.isSubtype(element.asType(), mvpViewElem.asType());
    }

}
