package serg.chuprin.mvp_core.view.nullView;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import serg.chuprin.mvp_core.MvpUtils;
import serg.chuprin.mvp_core.view.MvpView;

class NullViewProxy {

    @SuppressWarnings("unchecked")
    static <T> T of(Class<T> clazz) {
        Class<?> interfaceClass = null;
        for (Class<?> aClass : clazz.getInterfaces()) {
            if (MvpUtils.isInterfaceSubType(aClass, MvpView.class)) {
                interfaceClass = aClass;
                break;
            }
        }
        if (interfaceClass == null) {
            throw new IllegalStateException("View should implement interface" +
                    " inherited from MvpView");
        }
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new DefaultValueInvocationHandler());
    }


    private static class DefaultValueInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return Defaults.defaultValue(method.getReturnType());
        }
    }
}