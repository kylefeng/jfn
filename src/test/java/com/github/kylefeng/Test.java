package com.github.kylefeng;

import com.github.kylefeng.function.Fn;
import com.github.kylefeng.function.Functions;

public class Test {
    public static void main(String[] args) {
        Fn f = Functions.from(F.class, "apply");
        f.apply("a", "b", "c");
    }

    public static class F {
        public void apply(String a, String b, String c) {
            System.out.printf("a=%s,b=%s,c=%s", a, b, c);
        }
    }
}
