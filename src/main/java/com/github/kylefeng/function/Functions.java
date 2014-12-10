package com.github.kylefeng.function;

import java.util.Map;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;

public class Functions {

    /** 函数缓存 */
    private static final Map<String, Fn> cache       = Maps.newConcurrentMap();

    /** 非法的缓存 key */
    private static final String          ILLEGAL_KEY = "";

    /**
     * 某个类的某个方法生成函数实例。若函数已经生成过，那么从缓存直接获取。
     * 
     * @param clazz 类实例
     * @param methodName 方法名称
     * @return 函数实例
     */
    public static Fn from(final Class<?> clazz, final String methodName) {
        String cacheKey = getFnCacheKey(clazz, methodName);
        if (cacheKey == ILLEGAL_KEY) {
            return null;
        }

        Fn f = (Fn) cache.get(clazz);
        if (f != null) {
            return f;
        }

        synchronized (cache) {
            f = cache.get(cacheKey);
            if (f == null) {
                try {

                    final Object target = clazz.newInstance();
                    final MethodAccess methodAccess = MethodAccess.get(clazz);
                    final int methodIndex = methodAccess.getIndex(methodName);

                    f = new Fn() {

                        @Override
                        public Object apply(Object... args) {
                            return methodAccess.invoke(target, methodIndex, args);
                        }
                    };

                    cache.put(cacheKey, f);

                } catch (InstantiationException e) {
                    // TODO 日志化 
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO 日志化 
                    e.printStackTrace();
                }
            }
            return f;
        }
    }

    /**
     * 获取函数缓存的 key，即：类名#方法名。
     * 
     * @param clazz 类实例
     * @param methodName 方法名
     * @return 函数缓存的 key
     */
    public static String getFnCacheKey(Class<?> clazz, String methodName) {
        if (clazz == null || methodName == null || methodName.length() == 0) {
            return "";
        }

        return clazz.getName() + "#" + methodName;
    }
}
