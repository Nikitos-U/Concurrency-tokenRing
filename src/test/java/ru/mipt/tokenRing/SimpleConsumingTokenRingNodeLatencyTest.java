package ru.mipt.tokenRing;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.lang.System.*;
import static java.util.List.of;

class SimpleConsumingTokenRingNodeLatencyTest {
    SimpleConsumingTokenRingNode node0 = new SimpleConsumingTokenRingNode(0);
    SimpleConsumingTokenRingNode node1 = new SimpleConsumingTokenRingNode(1, node0);
    SimpleConsumingTokenRingNode node2 = new SimpleConsumingTokenRingNode(2, node1);
    SimpleConsumingTokenRingNode node3 = new SimpleConsumingTokenRingNode(3, node2);
    SimpleConsumingTokenRingNode node4 = new SimpleConsumingTokenRingNode(4, node3);
    SimpleConsumingTokenRingNode node5 = new SimpleConsumingTokenRingNode(5, node4);
    Thread thread0 = new Thread(node0);
    Thread thread1 = new Thread(node1);
    Thread thread2 = new Thread(node2);
    Thread thread3 = new Thread(node3);
    Thread thread4 = new Thread(node4);
    Thread thread5 = new Thread(node5);
    ArrayList<SimpleConsumingTokenRingNode> nodes = new ArrayList<>(of(node0, node1, node2, node3, node4, node5));
    ArrayList<Thread> threads = new ArrayList<>(of(thread0, thread1, thread2, thread3, thread4, thread5));

    void prepare(int nodesNumber) {
        node0.setNextNode(nodes.get(nodesNumber - 1));
        for (int i = 0; i < nodesNumber; i++) {
             threads.get(i).start();
        }
    }

    void threadsStop() {
        threads.forEach(Thread::interrupt);
    }

    /**
     * Простая реализация ноды для токен ринг не позволяет осуществить отправку пакетов
     * по одному с каждой ноды одновременно
     * while (true) используется чтобы показать, что отправка
     * пакетов не происходит на любом временном промежутке
     */
    @SneakyThrows
    @Test
    void wholeRingAllPackagesTransferBenchmark() {
        prepare(6);
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
        node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
        node4.setDataPackage(new DataPackage(4, 5, nanoTime()));
        node5.setDataPackage(new DataPackage(5, 0, nanoTime()));
        while (true) {
        }
    }

    /**
     * при n = 6
     * avg (по 100 измерениям) Latency при отправке 5 сообщений: 3642 µs
     * avg (по 100 измерениям) Latency для при отправке 4 сообщений: 2167 µs
     * avg (по 100 измерениям) Latency для при отправке 3 сообщений: 2947 µs
     * avg (по 100 измерениям) Latency для при отправке 2 сообщений: 1274 µs
     * avg (по 100 измерениям) Latency для при отправке 1 сообщения: 613 µs
     */
    @SneakyThrows
    @Test
    void wholeRingPackagesTransferBenchmark() {
        prepare(6);
        long sum = 0;
        for (int i = 0; i < 20; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
            node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
            node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
            node4.setDataPackage(new DataPackage(4, 5, nanoTime()));
            Thread.sleep(1000);
            sum += nodes.stream()
                    .map(SimpleConsumingTokenRingNode::getLatency)
                    .reduce(0L, Long::sum);
        }
        System.out.println("avg: " + (sum / 100000) + " µs");
        threadsStop();
    }

    /**
     * при n = 5
     * avg (по 100 измерениям) Latency при отправке 4 сообщений: 3175 µs
     * avg (по 100 измерениям) Latency при отправке 3 сообщений: 1941 µs
     * avg (по 100 измерениям) Latency при отправке 2 сообщений: 1185 µs
     * avg (по 100 измерениям) Latency при отправке 1 сообщения: 567 µs
     */
    @SneakyThrows
    @Test
    void fiveNodesRingPackagesTransferBenchmark() {
        prepare(5);
        long sum = 0;
        for (int i = 0; i < 25; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
            node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
            node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
            Thread.sleep(1000);
            sum += nodes.stream()
                    .map(SimpleConsumingTokenRingNode::getLatency)
                    .reduce(0L, Long::sum);
        }
        System.out.println("avg: " + (sum / 100000) + " µs");
        threadsStop();
    }

    /**
     * при n = 4
     * avg (по 100 измерениям) Latency при отправке 3 сообщений: 1234 µs
     * avg (по 100 измерениям) Latency при отправке 2 сообщений: 826 µs
     * avg (по 100 измерениям) Latency при отправке 1 сообщения: 411 µs
     */
    @SneakyThrows
    @Test
    void fourNodesRingPackagesTransferBenchmark() {
        prepare(4);
        long sum = 0;
        for (int i = 0; i < 50; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
//            node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
            Thread.sleep(1000);
            sum += nodes.stream()
                    .map(SimpleConsumingTokenRingNode::getLatency)
                    .reduce(0L, Long::sum);
        }
        System.out.println("avg: " + (sum / 100000) + " µs");
        threadsStop();
    }

    /**
     * при n = 3
     * avg (по 100 измерениям) Latency при отправке 2 сообщений: 1100 µs
     * avg (по 100 измерениям) Latency при отправке 1 сообщения: 667 µs
     */
    @SneakyThrows
    @Test
    void threeNodesRingPackagesTransferBenchmark() {
        prepare(3);
        long sum = 0;
        for (int i = 0; i < 50; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
            Thread.sleep(1000);
            sum += nodes.stream()
                    .map(SimpleConsumingTokenRingNode::getLatency)
                    .reduce(0L, Long::sum);
        }
        System.out.println("avg: " + (sum / 100000) + " µs");
        threadsStop();
    }

    /**
     * при n = 2
     * avg (по 100 измерениям) Latency при отправке 1 сообщения: 488 µs
     */
    @SneakyThrows
    @Test
    void twoNodesRingPackagesTransferBenchmark() {
        prepare(2);
        long sum = 0;
        for (int i = 0; i < 100; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            Thread.sleep(1000);
            sum += nodes.stream()
                    .map(SimpleConsumingTokenRingNode::getLatency)
                    .reduce(0L, Long::sum);
        }
        System.out.println("avg: " + (sum / 100000) + " µs");
        threadsStop();
    }

}