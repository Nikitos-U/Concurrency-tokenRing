package ru.mipt.queuedTokenRing;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.mipt.tokenRing.DataPackage;
import ru.mipt.tokenRing.withQueueImpl.MediumQueue;
import ru.mipt.tokenRing.withQueueImpl.QueueConsumingTokenRingNode;
import ru.mipt.tokenRing.withQueueImpl.QueueThroughputTokenRingNode;
import ru.mipt.tokenRing.withQueueImpl.QueuedTokenRingNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import static java.lang.System.nanoTime;
import static java.lang.Thread.sleep;
import static java.util.List.of;

public class QueueThroughputTest {
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
    QueueThroughputTokenRingNode node0 = new QueueThroughputTokenRingNode(0, queue9, queue0);
    QueueThroughputTokenRingNode node1 = new QueueThroughputTokenRingNode(1, queue0, queue1);
    QueueThroughputTokenRingNode node2 = new QueueThroughputTokenRingNode(2, queue1, queue2);
    QueueThroughputTokenRingNode node3 = new QueueThroughputTokenRingNode(3, queue2, queue3);
    QueueThroughputTokenRingNode node4 = new QueueThroughputTokenRingNode(4, queue3, queue4);
    QueueThroughputTokenRingNode node5 = new QueueThroughputTokenRingNode(5, queue4, queue5);
    QueueThroughputTokenRingNode node6 = new QueueThroughputTokenRingNode(6, queue5, queue6);
    QueueThroughputTokenRingNode node7 = new QueueThroughputTokenRingNode(7, queue6, queue7);
    QueueThroughputTokenRingNode node8 = new QueueThroughputTokenRingNode(8, queue7, queue8);
    QueueThroughputTokenRingNode node9 = new QueueThroughputTokenRingNode(9, queue8, queue9);
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
    ArrayList<QueueThroughputTokenRingNode> nodes = new ArrayList<>(of(node0, node1, node2, node3, node4, node5, node6, node7, node8, node9));
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
        QueueThroughputTokenRingNode node0 = new QueueThroughputTokenRingNode(0, queue2, queue0);
        QueueThroughputTokenRingNode node1 = new QueueThroughputTokenRingNode(1, queue0, queue1);
        QueueThroughputTokenRingNode node2 = new QueueThroughputTokenRingNode(2, queue1, queue2);
        Thread thread0 = new Thread(node0);
        Thread thread1 = new Thread(node1);
        Thread thread2 = new Thread(node2);
        thread0.start();
        thread1.start();
        thread2.start();
        queue0.put(new DataPackage(1, 0, nanoTime()));
        for (int i = 0; i < 1000; i++) {
            node0.getThroughput();
            sleep(30);
        }
        thread0.interrupt();
        thread1.interrupt();
        thread2.interrupt();
    }

    @SneakyThrows
    @Test
    void wholeRingAllPackagesTransferBenchmark() {
        File csvOutputFile = new File("/Users/a18535673/Projects/Concurrency-tokenRing/src/main/resources/queueWithArtificialLatencyThroughputTest.csv");
        PrintWriter pw = new PrintWriter(new FileOutputStream(csvOutputFile, true));
//        pw.println("buffer size, load %, const latency ms, throughput 1/s");
        Long artificialLatency = 10L;
        for (QueueThroughputTokenRingNode node : nodes) {
            node.setArtificialNodeLatency(artificialLatency);
        }
        startThreads();
        for (int i = 0; i < 16; i++) {
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
        for (int i = 0; i < 5; i++) {
            for (QueueThroughputTokenRingNode node : nodes){
                pw.println("20, 80, 10, " + node.getThroughput());
            }
            sleep(1000);
        }
        pw.close();
        threadsStop();
    }
}
