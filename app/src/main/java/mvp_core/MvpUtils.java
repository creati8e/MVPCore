package mvp_core;

public class MvpUtils {
    public static boolean isInterfaceSubType(Class<?> clazz, Class<?> targetClass) {
        if (clazz.equals(targetClass)) {
            return true;
        }
        Class[] superInterfaces = clazz.getInterfaces();
        for (Class superInterface : superInterfaces) {
            if (isInterfaceSubType(superInterface, targetClass)) {
                return true;
            }
        }
        return false;
    }

    static boolean isClassSubType(Class<?> clazz, Class<?> targetClass) {
        if (clazz == null) {
            return false;
        }
        if (clazz.equals(targetClass)) {
            return true;
        }
        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null) {
            if (superclass.equals(MvpPresenter.class)) {
                return true;
            }
            superclass = superclass.getSuperclass();
        }
        return false;
    }
}
