package com.github.kylefeng.benchmark.function;

import java.lang.reflect.Method;

import com.github.kylefeng.benchmark.Benchmark;
import com.github.kylefeng.benchmark.Benchmark.Senario;
import com.github.kylefeng.function.Fn;
import com.github.kylefeng.function.Functions;

public class FnBenchmark {
    public static void main(String[] args) throws SecurityException, NoSuchMethodException,
                                          InterruptedException {
        int warmUp = 10000;
        int iterations = 1000000;

        Benchmark.newBuilder().name("direct").warmUp(warmUp).iterations(iterations)
            .senario(new Senario() {
                AFn f1 = new AFn();

                @Override
                public void run() throws Throwable {
                    f1.apply("test");
                }
            }).isOpen(true).build().start();

        System.out.println("===============");

        Benchmark.newBuilder().name("reflectASM").warmUp(warmUp).iterations(iterations)
            .senario(new Senario() {
                Fn f2 = Functions.from(AFn.class, "apply");

                @Override
                public void run() throws Throwable {
                    f2.apply("test");
                }
            }).isOpen(true).build().start();

        System.out.println("===============");

        Benchmark.newBuilder().name("reflection").warmUp(warmUp).iterations(iterations)
            .senario(new Senario() {
                AFn    f3 = new AFn();
                Method m  = f3.getClass().getMethod("apply", String.class);

                @Override
                public void run() throws Throwable {
                    m.invoke(f3, "test");
                }
            }).isOpen(true).build().start();
    }

    public static class AFn {
        public Long apply(String a) {
            int N = 100;
            long c = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    c++;
                }
            }
            return Long.valueOf(c);
        }
    }

}
