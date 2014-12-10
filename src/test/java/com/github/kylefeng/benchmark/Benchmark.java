package com.github.kylefeng.benchmark;

import static java.lang.String.format;

public class Benchmark {

    /**
     * 按照默认的迭代配置进行性能测试：
     * <ul>
     * <li>预热场景执行次数：10000</li>
     * <li>场景迭代数：10000</li>
     * <li>每个迭代场景运行的次数：1000</li>
     * </ul>
     * 
     * @param name 测试名称
     * @param isOpen 测试是否开启
     * @param senario 场景实例
     */
    public static void benchmark(String name, boolean isOpen, Senario senario) {
        Benchmarker b = new Benchmarker(name);
        b.senario = senario;
        b.isOpen = isOpen;
        b.start();
    }

    /** 性能测试场景 */
    public static abstract class Senario {

        /**
         * 运行一次用于性能测试的场景。
         * 
         * @throws Throwable
         */
        public abstract void run() throws Throwable;
    }

    /**
     * 创建性能测试构建器。
     * 
     * @return 构建器实例
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** 性能测试构建器 */
    public static class Builder {
        /** 预热场景执行次数 */
        private int     warmUp     = 10000;

        /** 迭代数  */
        private int     iterations = 10000;

        /** 每次迭代场景运行的次数 */
        private int     partitions = 1000;

        /** 性能测试名称 */
        private String  name;

        /** 测试是否开启 */
        private boolean isOpen     = true;

        /** 测试场景 */
        private Senario senario;

        /**
         * 设置测试的名称。
         * 
         * @param name 测试的名称
         * @return Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * 设置预热的场景执行次数。
         * 
         * @param warmUpIterations 预热场景执行次数
         * @return Builder
         */
        public Builder warmUp(int warmUp) {
            this.warmUp = warmUp;
            return this;
        }

        /**
         * 设置迭代数。
         * 
         * @param iterations 迭代数。
         * @return BUilder
         */
        public Builder iterations(int iterations) {
            this.iterations = iterations;
            return this;
        }

        /**
         * 每次迭代场景的运行次数。
         * 
         * @param partitions 每次迭代场景运行的次数  
         * @return Builder
         */
        public Builder partitions(int partitions) {
            this.partitions = partitions;
            return this;
        }

        /**
         * 设置测试的场景。
         * 
         * @param senario 测试场景
         * @return Builder
         */
        public Builder senario(Senario senario) {
            this.senario = senario;
            return this;
        }

        /**
         * 测试是否开启。
         * 
         * @param isOpen 测试是否开启
         * @return Builder
         */
        public Builder isOpen(boolean isOpen) {
            this.isOpen = isOpen;
            return this;
        }

        /**
         * 构建性能测试器。
         * 
         * @return 构建好的性能测试器
         */
        public Benchmarker build() {
            Benchmarker benchmarker = new Benchmarker(name, isOpen, warmUp, iterations, partitions,
                senario);
            return benchmarker;
        }
    }

    public static class Benchmarker {

        /** 预热场景执行次数 */
        private int     warmUp     = 10000;

        /** 迭代数  */
        private int     iterations = 10000;

        /** 每次迭代场景运行的次数 */
        private int     partitions = 1000;

        /** 场景 */
        private Senario senario;

        /** 性能测试名称 */
        private String  name;

        /** 测试是否开启 */
        private boolean isOpen     = true;

        public Benchmarker(String name) {
            this.name = name;
        }

        public Benchmarker(String name, boolean isOpen, int warmUp, int iterations, int partitions,
                           Senario senario) {
            this.name = name;
            this.isOpen = isOpen;
            this.partitions = partitions;
            this.warmUp = warmUp;
            this.iterations = iterations;
            this.senario = senario;
        }

        public void start() {
            if (!isOpen) {
                return;
            }

            try {
                System.out.println(format("warm up: %s", name));
                execute(warmUp);
                System.out.println(format("benchmarking: %s", name));
                long start = System.nanoTime();
                execute(iterations);
                long end = System.nanoTime();
                double durationMS = 1.0d * (end - start) / 1000000d;
                double rate = (double) partitions * iterations / durationMS;
                System.out.println(format(
                    "name: %s, duration: %,.3f ms, rate: %,.2f executions/sec", name, durationMS,
                    rate));
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        private void execute(int iterations) throws Throwable {
            for (int i = 0; i < iterations; i++) {
                for (int j = 0; j < partitions; j++) {
                    senario.run();
                }
            }
        }

    }
}
