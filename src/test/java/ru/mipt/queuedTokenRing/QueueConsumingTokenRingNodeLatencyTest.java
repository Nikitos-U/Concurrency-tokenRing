package ru.mipt.queuedTokenRing;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.mipt.tokenRing.DataPackage;
import ru.mipt.tokenRing.withQueueImpl.MediumQueue;
import ru.mipt.tokenRing.withQueueImpl.QueueConsumingTokenRingNode;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import static java.lang.System.nanoTime;
import static java.lang.Thread.sleep;
import static java.util.List.of;

public class QueueConsumingTokenRingNodeLatencyTest {
    MediumQueue queue0 = new MediumQueue();
    MediumQueue queue1 = new MediumQueue();
    MediumQueue queue2 = new MediumQueue();
    MediumQueue queue3 = new MediumQueue();
    MediumQueue queue4 = new MediumQueue();
    MediumQueue queue5 = new MediumQueue();
    QueueConsumingTokenRingNode node0 = new QueueConsumingTokenRingNode(0);
    QueueConsumingTokenRingNode node1 = new QueueConsumingTokenRingNode(1, queue0, queue1);
    QueueConsumingTokenRingNode node2 = new QueueConsumingTokenRingNode(2, queue1, queue2);
    QueueConsumingTokenRingNode node3 = new QueueConsumingTokenRingNode(3, queue2, queue3);
    QueueConsumingTokenRingNode node4 = new QueueConsumingTokenRingNode(4, queue3, queue4);
    QueueConsumingTokenRingNode node5 = new QueueConsumingTokenRingNode(5, queue4, queue5);
    Thread thread0 = new Thread(node0);
    Thread thread1 = new Thread(node1);
    Thread thread2 = new Thread(node2);
    Thread thread3 = new Thread(node3);
    Thread thread4 = new Thread(node4);
    Thread thread5 = new Thread(node5);
    ArrayList<QueueConsumingTokenRingNode> nodes = new ArrayList<>(of(node0, node1, node2, node3, node4, node5));
    ArrayList<MediumQueue> mediums = new ArrayList<>(of(queue0, queue1, queue2, queue3, queue4, queue5));
    ArrayList<Thread> threads = new ArrayList<>(of(thread0, thread1, thread2, thread3, thread4, thread5));

    void prepare(int nodesNumber) {
        node0.setMediums(mediums.get(nodesNumber - 1), queue0);
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
            queue0.put(new DataPackage(0, 1, nanoTime()));
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
        queue0.put(new DataPackage(0, 1, nanoTime()));
        queue1.put(new DataPackage(1, 2, nanoTime()));
        queue2.put(new DataPackage(2, 3, nanoTime()));
        queue3.put(new DataPackage(3, 4, nanoTime()));
        queue4.put(new DataPackage(4, 5, nanoTime()));
        queue5.put(new DataPackage(5, 0, nanoTime()));
        while (true) {
        }
    }

    @SneakyThrows
    @Test
    void wholeRingAllNodeNumbers() {
        prepare(6);
        File csvOutputFile = new File("/Users/a18535673/Projects/Concurrency-tokenRing/src/main/resources/queueLatencyTestResults.csv");
        PrintWriter pw = new PrintWriter(csvOutputFile);
        pw.println("num of nodes, num of messages, latency ns");
        warmUp();
        for (int i = 5; i >= 1; i--) {
            node0.setMediums(mediums.get(i), queue0);
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
        queue0.put(new DataPackage(0, 1, nanoTime()));
        queue1.put(new DataPackage(1, 2, nanoTime()));
        queue2.put(new DataPackage(2, 3, nanoTime()));
        queue3.put(new DataPackage(3, 4, nanoTime()));
        queue4.put(new DataPackage(4, 5, nanoTime()));
        sleep(1000);
    }

    private void sendFourMessages() throws InterruptedException {
        queue0.put(new DataPackage(0, 1, nanoTime()));
        queue1.put(new DataPackage(1, 2, nanoTime()));
        queue2.put(new DataPackage(2, 3, nanoTime()));
        queue3.put(new DataPackage(3, 4, nanoTime()));
        sleep(1000);
    }

    private void sendThreeMessages() throws InterruptedException {
        queue0.put(new DataPackage(0, 1, nanoTime()));
        queue1.put(new DataPackage(1, 2, nanoTime()));
        queue2.put(new DataPackage(2, 3, nanoTime()));
        sleep(1000);
    }

    private void sendTwoMessages() throws InterruptedException {
        queue0.put(new DataPackage(0, 1, nanoTime()));
        queue1.put(new DataPackage(1, 2, nanoTime()));
        sleep(1000);
    }

    private void sendOneMessage() throws InterruptedException {
        queue0.put(new DataPackage(0, 1, nanoTime()));
        sleep(1000);
    }


}
