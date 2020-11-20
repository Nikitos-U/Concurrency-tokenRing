package ru.mipt.tokenRing;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.lang.System.nanoTime;
import static java.util.List.of;

class SimpleTokenRingNodeThroughputTest {
    SimpleTokenRingNode node0 = new SimpleTokenRingNode(0);
    SimpleTokenRingNode node1 = new SimpleTokenRingNode(1, node0);
    SimpleTokenRingNode node2 = new SimpleTokenRingNode(2, node1);
    SimpleTokenRingNode node3 = new SimpleTokenRingNode(3, node2);
    SimpleTokenRingNode node4 = new SimpleTokenRingNode(4, node3);
    SimpleTokenRingNode node5 = new SimpleTokenRingNode(5, node4);
    Thread thread0 = new Thread(node0);
    Thread thread1 = new Thread(node1);
    Thread thread2 = new Thread(node2);
    Thread thread3 = new Thread(node3);
    Thread thread4 = new Thread(node4);
    Thread thread5 = new Thread(node5);
    ArrayList<SimpleTokenRingNode> nodes = new ArrayList<>(of(node0, node1, node2, node3, node4, node5));
    ArrayList<Thread> threads = new ArrayList<>(of(thread0, thread1, thread2, thread3, thread4, thread5));

    void prepare(int nodesNumber) {
        node0.setNextNode(nodes.get(nodesNumber - 1));
        for (int i = 0; i < nodesNumber; i++) {
            threads.get(i).start();
        }
    }


    /**
     * n = 6
     * avg throughput (осред по количеству нод) при количестве сообщений равном n - 1: 17998
     * avg throughput (осред по количеству нод) при количестве сообщений равном n - 2: 14709
     * avg throughput (осред по количеству нод) при количестве сообщений равном 3: 14443
     * avg throughput (осред по количеству нод) при количестве сообщений равном 2: 13059
     * avg throughput (осред по количеству нод) при количестве сообщений равном 1: 13784
     */
    @SneakyThrows
    @Test
    void sixNodesThroughputTest() {
        prepare(6);
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
        node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
        node4.setDataPackage(new DataPackage(4, 5, nanoTime()));
        Thread.sleep(1000);
        nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .forEach(System.out::println);
        int sum = nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .reduce(0, Integer::sum);
        System.out.println("avg: " + (sum / 6));
    }

    /**
     * n = 5
     * avg throughput (сообщений/сек) (осред по количеству нод) при количестве сообщений равном 4: 12483
     * avg throughput (осред по количеству нод) при количестве сообщений равном 3: 17192
     * avg throughput (осред по количеству нод) при количестве сообщений равном 2: 16728
     * avg throughput (осред по количеству нод) при количестве сообщений равном 1: 23795
     */
    @SneakyThrows
    @Test
    void fiveNodesThroughputTest() {
        prepare(5);
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
        node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
        Thread.sleep(1000);
        nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .forEach(System.out::println);
        int sum = nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .reduce(0, Integer::sum);
        System.out.println("avg: " + (sum / 5));
    }

    /**
     * n = 4
     * avg throughput (осред по количеству нод) при количестве сообщений равном 3: 38998
     * avg throughput (осред по количеству нод) при количестве сообщений равном 2: 37444
     * avg throughput (осред по количеству нод) при количестве сообщений равном 1: 34549
     */
    @SneakyThrows
    @Test
    void fourNodesThroughputTest() {
        prepare(4);
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
        Thread.sleep(1000);
        nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .forEach(System.out::println);
        int sum = nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .reduce(0, Integer::sum);
        System.out.println("avg: " + (sum / 4));
    }

    /**
     * n = 3
     * avg throughput (осред по количеству нод) при количестве сообщений равном 2: 42828
     * avg throughput (осред по количеству нод) при количестве сообщений равном 1: 45087
     */
    @SneakyThrows
    @Test
    void threeNodesThroughputTest() {
        prepare(4);
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
//        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        Thread.sleep(1000);
        nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .forEach(System.out::println);
        int sum = nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .reduce(0, Integer::sum);
        System.out.println("avg: " + (sum / 3));
    }

    /**
     * n = 2
     * avg throughput (осред по количеству нод) при количестве сообщений равном 1: 78553
     */
    @SneakyThrows
    @Test
    void twoNodesThroughputTest() {
        prepare(2);
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        Thread.sleep(1000);
        nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .forEach(System.out::println);
        int sum = nodes.stream()
                .map(SimpleTokenRingNode::getMassagesAmount)
                .reduce(0, Integer::sum);
        System.out.println("avg: " + (sum / 2));
    }

}