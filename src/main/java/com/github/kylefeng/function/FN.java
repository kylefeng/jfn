package com.github.kylefeng.function;

import java.util.Map;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;

public class FN {

    /** �������� */
    private static final Map<String, Function> cache       = Maps.newConcurrentMap();

    /** �Ƿ��Ļ��� key */
    private static final String                ILLEGAL_KEY = "";

    /**
     * ĳ�����ĳ���������ɺ���ʵ�����������Ѿ����ɹ�����ô�ӻ���ֱ�ӻ�ȡ��
     * 
     * @param clazz ��ʵ��
     * @param methodName ��������
     * @return ����ʵ��
     */
    public static Function fnFrom(Class<?> clazz, String methodName) {
        String cacheKey = getFnCacheKey(clazz, methodName);
        if (cacheKey == ILLEGAL_KEY) {
            return null;
        }

        Function f = (Function) cache.get(clazz);
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
                    f = new Function() {
                        @Override
                        public Object apply(Object... args) {
                            return methodAccess.invoke(target, methodIndex, args);
                        }
                    };

                    cache.put(cacheKey, f);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return f;
        }
    }

    /**
     * ��ȡ��������� key����������#��������
     * 
     * @param clazz ��ʵ��
     * @param methodName ������
     * @return ��������� key
     */
    public static String getFnCacheKey(Class<?> clazz, String methodName) {
        if (clazz == null || methodName == null || methodName.length() == 0) {
            return "";
        }

        return clazz.getName() + "#" + methodName;
    }
}
