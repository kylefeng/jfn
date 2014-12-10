package com.github.kylefeng.benchmark;

import static java.lang.String.format;

public class Benchmark {

    /**
     * ����Ĭ�ϵĵ������ý������ܲ��ԣ�
     * <ul>
     * <li>Ԥ�ȳ���ִ�д�����10000</li>
     * <li>������������10000</li>
     * <li>ÿ�������������еĴ�����1000</li>
     * </ul>
     * 
     * @param name ��������
     * @param isOpen �����Ƿ���
     * @param senario ����ʵ��
     */
    public static void benchmark(String name, boolean isOpen, Senario senario) {
        Benchmarker b = new Benchmarker(name);
        b.senario = senario;
        b.isOpen = isOpen;
        b.start();
    }

    /** ���ܲ��Գ��� */
    public static abstract class Senario {

        /**
         * ����һ���������ܲ��Եĳ�����
         * 
         * @throws Throwable
         */
        public abstract void run() throws Throwable;
    }

    /**
     * �������ܲ��Թ�������
     * 
     * @return ������ʵ��
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** ���ܲ��Թ����� */
    public static class Builder {
        /** Ԥ�ȳ���ִ�д��� */
        private int     warmUp     = 10000;

        /** ������  */
        private int     iterations = 10000;

        /** ÿ�ε����������еĴ��� */
        private int     partitions = 1000;

        /** ���ܲ������� */
        private String  name;

        /** �����Ƿ��� */
        private boolean isOpen     = true;

        /** ���Գ��� */
        private Senario senario;

        /**
         * ���ò��Ե����ơ�
         * 
         * @param name ���Ե�����
         * @return Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * ����Ԥ�ȵĳ���ִ�д�����
         * 
         * @param warmUpIterations Ԥ�ȳ���ִ�д���
         * @return Builder
         */
        public Builder warmUp(int warmUp) {
            this.warmUp = warmUp;
            return this;
        }

        /**
         * ���õ�������
         * 
         * @param iterations ��������
         * @return BUilder
         */
        public Builder iterations(int iterations) {
            this.iterations = iterations;
            return this;
        }

        /**
         * ÿ�ε������������д�����
         * 
         * @param partitions ÿ�ε����������еĴ���  
         * @return Builder
         */
        public Builder partitions(int partitions) {
            this.partitions = partitions;
            return this;
        }

        /**
         * ���ò��Եĳ�����
         * 
         * @param senario ���Գ���
         * @return Builder
         */
        public Builder senario(Senario senario) {
            this.senario = senario;
            return this;
        }

        /**
         * �����Ƿ�����
         * 
         * @param isOpen �����Ƿ���
         * @return Builder
         */
        public Builder isOpen(boolean isOpen) {
            this.isOpen = isOpen;
            return this;
        }

        /**
         * �������ܲ�������
         * 
         * @return �����õ����ܲ�����
         */
        public Benchmarker build() {
            Benchmarker benchmarker = new Benchmarker(name, isOpen, warmUp, iterations, partitions,
                senario);
            return benchmarker;
        }
    }

    public static class Benchmarker {

        /** Ԥ�ȳ���ִ�д��� */
        private int     warmUp     = 10000;

        /** ������  */
        private int     iterations = 10000;

        /** ÿ�ε����������еĴ��� */
        private int     partitions = 1000;

        /** ���� */
        private Senario senario;

        /** ���ܲ������� */
        private String  name;

        /** �����Ƿ��� */
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
