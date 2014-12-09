package com.github.kylefeng.function;

import java.util.Map;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;

public class FN {

    private static final Map<Class<?>, Function> cache = Maps.newConcurrentMap();

    public static Function fnFrom(Class<?> clazz, String methodName) {
        Function f = (Function) cache.get(clazz);
        if (f != null) {
            return f;
        }

        synchronized (cache) {
            try {
                final Object target = clazz.newInstance();
                final MethodAccess methodAccess = MethodAccess.get(clazz);
                final int methodIndex = methodAccess.getIndex(methodName);
                f = new Function() {
                    @Override
                    public Object apply(Object... args) {
                        return methodAccess.invoke(target, methodIndex, args);
                    }
                };

                cache.put(clazz, f);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return f;
        }
    }
}
