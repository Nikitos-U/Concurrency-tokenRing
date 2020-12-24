package ru.mipt.queuedTokenRing;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.mipt.tokenRing.DataPackage;
import ru.mipt.tokenRing.withQueueImpl.MediumQueue;
import ru.mipt.tokenRing.withQueueImpl.QueueConsumingTokenRingNode;

import java.io.File;
import java.io.FileOutputStream;
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
    MediumQueue queue6 = new MediumQueue();
    MediumQueue queue7 = new MediumQueue();
    MediumQueue queue8 = new MediumQueue();
    MediumQueue queue9 = new MediumQueue();
    QueueConsumingTokenRingNode node0 = new QueueConsumingTokenRingNode(0, queue9, queue0);
    QueueConsumingTokenRingNode node1 = new QueueConsumingTokenRingNode(1, queue0, queue1);
    QueueConsumingTokenRingNode node2 = new QueueConsumingTokenRingNode(2, queue1, queue2);
    QueueConsumingTokenRingNode node3 = new QueueConsumingTokenRingNode(3, queue2, queue3);
    QueueConsumingTokenRingNode node4 = new QueueConsumingTokenRingNode(4, queue3, queue4);
    QueueConsumingTokenRingNode node5 = new QueueConsumingTokenRingNode(5, queue4, queue5);
    QueueConsumingTokenRingNode node6 = new QueueConsumingTokenRingNode(6, queue5, queue6);
    QueueConsumingTokenRingNode node7 = new QueueConsumingTokenRingNode(7, queue6, queue7);
    QueueConsumingTokenRingNode node8 = new QueueConsumingTokenRingNode(8, queue7, queue8);
    QueueConsumingTokenRingNode node9 = new QueueConsumingTokenRingNode(9, queue8, queue9);
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
    ArrayList<QueueConsumingTokenRingNode> nodes = new ArrayList<>(of(node0, node1, node2, node3, node4, node5, node6, node7, node8, node9));
    ArrayList<MediumQueue> mediums = new ArrayList<>(of(queue0, queue1, queue2, queue3, queue4, queue5, queue6, queue7, queue8, queue9));
    ArrayList<Thread> threads = new ArrayList<>(of(thread0, thread1, thread2, thread3, thread4, thread5, thread6, thread7, thread8, thread9));

    void startThreads() {
        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();
    }

    void threadsStop() {
        threads.forEach(Thread::interrupt);
    }

    @SneakyThrows
    @BeforeAll
    static void warmUp() {
        MediumQueue queue0 = new MediumQueue();
        MediumQueue queue1 = new MediumQueue();
        MediumQueue queue2 = new MediumQueue();
        QueueConsumingTokenRingNode node0 = new QueueConsumingTokenRingNode(0, queue2, queue0);
        QueueConsumingTokenRingNode node1 = new QueueConsumingTokenRingNode(1, queue0, queue1);
        QueueConsumingTokenRingNode node2 = new QueueConsumingTokenRingNode(2, queue1, queue2);
        Thread thread0 = new Thread(node0);
        Thread thread1 = new Thread(node1);
        Thread thread2 = new Thread(node2);
        thread0.start();
        thread1.start();
        thread2.start();
        queue0.put(new DataPackage(1, 0, nanoTime()));
        sleep(30000);
        thread0.interrupt();
        thread1.interrupt();
        thread2.interrupt();
    }

    @SneakyThrows
    @Test
    void wholeRingAllPackagesTransferBenchmark() {
        File csvOutputFile = new File("");
        PrintWriter pw = new PrintWriter(new FileOutputStream(csvOutputFile, true));
        warmUp();
        startThreads();
        for (int i = 0; i < 4; i++) {
            queue0.put(new DataPackage(1, 0, nanoTime()));
            queue1.put(new DataPackage(2, 1, nanoTime()));
            queue2.put(new DataPackage(3, 2, nanoTime()));
            queue3.put(new DataPackage(4, 3, nanoTime()));
            queue4.put(new DataPackage(5, 4, nanoTime()));
            queue5.put(new DataPackage(6, 5, nanoTime()));
            queue6.put(new DataPackage(7, 6, nanoTime()));
            queue7.put(new DataPackage(8, 7, nanoTime()));
            queue8.put(new DataPackage(9, 8, nanoTime()));
            queue9.put(new DataPackage(0, 9, nanoTime()));
        }
        for (int i = 0; i < 100; i++) {
            for (QueueConsumingTokenRingNode node : nodes) {
                pw.println("5, 80, " + node.getLatency());
            }
            sleep(10);
        }
        pw.close();
        threadsStop();
    }

    @SneakyThrows
    @Test
    void wholeRingAllNodeNumbers() {
        startThreads();
        File csvOutputFile = new File("/Users/a18535673/Projects/Concurrency-tokenRing/src/main/resources/queueNoArtificialLatencyTestResults.csv");
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
