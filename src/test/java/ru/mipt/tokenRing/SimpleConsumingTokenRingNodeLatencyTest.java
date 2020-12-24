package ru.mipt.tokenRing;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.mipt.tokenRing.noQueueImplementation.SimpleConsumingTokenRingNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import static java.lang.System.*;
import static java.lang.Thread.sleep;
import static java.util.List.of;

class SimpleConsumingTokenRingNodeLatencyTest {
    SimpleConsumingTokenRingNode node0 = new SimpleConsumingTokenRingNode(0);
    SimpleConsumingTokenRingNode node1 = new SimpleConsumingTokenRingNode(1, node0);
    SimpleConsumingTokenRingNode node2 = new SimpleConsumingTokenRingNode(2, node1);
    SimpleConsumingTokenRingNode node3 = new SimpleConsumingTokenRingNode(3, node2);
    SimpleConsumingTokenRingNode node4 = new SimpleConsumingTokenRingNode(4, node3);
    SimpleConsumingTokenRingNode node5 = new SimpleConsumingTokenRingNode(5, node4);
    SimpleConsumingTokenRingNode node6 = new SimpleConsumingTokenRingNode(6, node5);
    SimpleConsumingTokenRingNode node7 = new SimpleConsumingTokenRingNode(7, node6);
    SimpleConsumingTokenRingNode node8 = new SimpleConsumingTokenRingNode(8, node7);
    SimpleConsumingTokenRingNode node9 = new SimpleConsumingTokenRingNode(9, node8);
    SimpleConsumingTokenRingNode node10 = new SimpleConsumingTokenRingNode(10, node9);
    SimpleConsumingTokenRingNode node11 = new SimpleConsumingTokenRingNode(11, node10);
    SimpleConsumingTokenRingNode node12 = new SimpleConsumingTokenRingNode(12, node11);
    SimpleConsumingTokenRingNode node13 = new SimpleConsumingTokenRingNode(13, node12);
    SimpleConsumingTokenRingNode node14 = new SimpleConsumingTokenRingNode(14, node13);
    SimpleConsumingTokenRingNode node15 = new SimpleConsumingTokenRingNode(15, node14);
    Thread thread0 = new Thread(node0);
    Thread thread1 = new Thread(node1);
    Thread thread2 = new Thread(node2);
    Thread thread3 = new Thread(node3);
    Thread thread4 = new Thread(node4);
    Thread thread5 = new Thread(node5);
    Thread thread6 = new Thread(node6);
    Thread thread7 = new Thread(node7);
    Thread thread8 = new Thread(node8);
    Thread thread9 = new Thread(node9);
    Thread thread10 = new Thread(node10);
    Thread thread11 = new Thread(node11);
    Thread thread12 = new Thread(node12);
    Thread thread13 = new Thread(node13);
    Thread thread14 = new Thread(node14);
    Thread thread15 = new Thread(node15);
    ArrayList<SimpleConsumingTokenRingNode> nodes = new ArrayList<>(of(node0, node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, node13, node14, node15));
    ArrayList<Thread> threads = new ArrayList<>(of(thread0, thread1, thread2, thread3, thread4, thread5, thread6, thread7, thread8, thread9, thread10, thread11, thread12, thread13, thread14, thread15));

    void prepare(int nodesNumber) {
        node0.setNextNode(nodes.get(nodesNumber - 1));
        for (int i = 0; i < nodesNumber; i++) {
            threads.get(i).start();
        }
    }

    void threadsStop() {
        threads.forEach(Thread::interrupt);
    }

    @SneakyThrows
    void warmUp() {
        for (int i = 0; i < 5000; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            sleep(10);
        }
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
        warmUp();
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
        node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
        node4.setDataPackage(new DataPackage(4, 5, nanoTime()));
        node5.setDataPackage(new DataPackage(5, 0, nanoTime()));
        while (true) {
        }
    }

    @SneakyThrows
    @Test
    void maxNodesNumberTest(){
        File csvOutputFile = new File("");
        PrintWriter pw = new PrintWriter(new FileOutputStream(csvOutputFile, true));
//        pw.println("num of nodes, number of node, num of msg, latency ns");
        prepare(10);
        warmUp();
        for (int i = 0; i < 10; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
            node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
            node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
            node4.setDataPackage(new DataPackage(4, 5, nanoTime()));
            node5.setDataPackage(new DataPackage(5, 6, nanoTime()));
            node6.setDataPackage(new DataPackage(6, 7, nanoTime()));
            node7.setDataPackage(new DataPackage(7, 8, nanoTime()));
            node8.setDataPackage(new DataPackage(8, 9, nanoTime()));
//            node9.setDataPackage(new DataPackage(9, 10, nanoTime()));
//            node10.setDataPackage(new DataPackage(10, 11, nanoTime()));
//            node11.setDataPackage(new DataPackage(11, 12, nanoTime()));
//            node12.setDataPackage(new DataPackage(12, 13, nanoTime()));
//            node13.setDataPackage(new DataPackage(13, 14, nanoTime()));
//            node14.setDataPackage(new DataPackage(14, 15, nanoTime()));
            sleep(1000);
            for (SimpleConsumingTokenRingNode node : nodes) {
                pw.println("10, " + node.getNodeNumber() + ", 9, " + node.getLatency());
            }
        }
        pw.close();
    }

    @SneakyThrows
    @Test
    void wholeRingAllNodeNumbers() {
        prepare(6);
        File csvOutputFile = new File("/Users/a18535673/Projects/Concurrency-tokenRing/src/main/resources/latencyTestResults.csv");
        PrintWriter pw = new PrintWriter(csvOutputFile);
        pw.println("num of nodes, num of messages, latency ns");
        warmUp();
        for (int i = 5; i >= 1; i--) {
            node0.setNextNode(nodes.get(i));
            for (int k = 0; k < 100; k++) {
                switch (i) {
                    case 5:
                        sendFiveMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 5);
                        sendFourMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 4);
                        sendThreeMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 3);
                        sendTwoMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 2);
                        sendOneMessage();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 1);
                        break;
                    case 4:
                        sendFourMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 4);
                        sendThreeMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 3);
                        sendTwoMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 2);
                        sendOneMessage();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 1);
                        break;
                    case 3:
                        sendThreeMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 3);
                        sendTwoMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 2);
                        sendOneMessage();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 1);
                        break;
                    case 2:
                        sendTwoMessages();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 2);
                        sendOneMessage();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 1);
                        break;
                    case 1:
                        sendOneMessage();
                        printForNumberOfNodesAndNumberOfMsg(pw, i, 1);
                        break;
                }
            }
            pw.println("end of: " + i + " nodes ring test");
        }
        pw.close();
    }

    private void printForNumberOfNodesAndNumberOfMsg(PrintWriter pw, int i, int msgs) {
        for (int j = 1; j < msgs + 1; j++) {
            pw.println((i + 1) + ", " + msgs + ", " + nodes.get(j).getLatency());
        }
    }

    private void sendFiveMessages() throws InterruptedException {
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
        node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
        node4.setDataPackage(new DataPackage(4, 5, nanoTime()));
        sleep(1000);
    }

    private void sendFourMessages() throws InterruptedException {
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
        node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
        sleep(1000);
    }

    private void sendThreeMessages() throws InterruptedException {
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
        sleep(1000);
    }

    private void sendTwoMessages() throws InterruptedException {
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
        sleep(1000);
    }

    private void sendOneMessage() throws InterruptedException {
        node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
        sleep(1000);
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
        warmUp();
        long sum = 0;
        for (int i = 0; i < 20; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
            node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
            node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
            node4.setDataPackage(new DataPackage(4, 5, nanoTime()));
            sleep(1000);
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
        warmUp();
        long sum = 0;
        ArrayList<Long> avgByRun = new ArrayList<>();
        long sumByRun;
        for (int i = 0; i < 25; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
            node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
            node3.setDataPackage(new DataPackage(3, 4, nanoTime()));
            sleep(1000);
            sum += nodes.stream()
                    .map(SimpleConsumingTokenRingNode::getLatency)
                    .reduce(0L, Long::sum);
            sumByRun = nodes.stream()
                    .map(SimpleConsumingTokenRingNode::getLatency)
                    .reduce(0L, Long::sum);
            avgByRun.add(sumByRun / 4000);
        }
        System.out.println("avg: " + (sum / 100000) + " µs");
        avgByRun.forEach(out::println);
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
        warmUp();
        long sum = 0;
        for (int i = 0; i < 50; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
//            node2.setDataPackage(new DataPackage(2, 3, nanoTime()));
            sleep(1000);
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
     * avg (по 100 измерениям) Latency при отправке 1 сообщения: 391 µs
     */
    @SneakyThrows
    @Test
    void threeNodesRingPackagesTransferBenchmark() {
        prepare(3);
        warmUp();
        long sum = 0;
        for (int i = 0; i < 100; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
//            node1.setDataPackage(new DataPackage(1, 2, nanoTime()));
            sleep(1000);
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
        warmUp();
        long sum = 0;
        for (int i = 0; i < 100; i++) {
            node0.setDataPackage(new DataPackage(0, 1, nanoTime()));
            sleep(1000);
            sum += nodes.stream()
                    .map(SimpleConsumingTokenRingNode::getLatency)
                    .reduce(0L, Long::sum);
        }
        System.out.println("avg: " + (sum / 100000) + " µs");
        threadsStop();
    }

}