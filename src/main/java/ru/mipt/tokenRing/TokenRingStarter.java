package ru.mipt.tokenRing;

import lombok.SneakyThrows;
import ru.mipt.tokenRing.withQueueImpl.MediumQueue;
import ru.mipt.tokenRing.withQueueImpl.QueueThroughputTokenRingNode;

import static java.lang.System.nanoTime;

public class TokenRingStarter {
    @SneakyThrows
    public static void main(String[] args) {
        MediumQueue queue0 = new MediumQueue();
        MediumQueue queue1 = new MediumQueue();
        QueueThroughputTokenRingNode node0 = new QueueThroughputTokenRingNode(0, queue1, queue0);
        QueueThroughputTokenRingNode node1 = new QueueThroughputTokenRingNode(1, queue0, queue1);
        queue0.put(new DataPackage(1, 0, nanoTime()));

        Thread thread0 = new Thread(node0);
        Thread thread1 = new Thread(node1);

        thread0.start();
        thread1.start();

        for (int i = 0; i < 100; i++) {
//            System.out.println(node0.getMsgsNumber());
//            System.out.println(node1.getMsgsNumber());
            Thread.sleep(10);
        }
        System.out.println(node0.getThroughput());
        System.out.println(node1.getThroughput());
//        SimpleConsumingTokenRingNode node0 = new SimpleConsumingTokenRingNode(0);
//        SimpleConsumingTokenRingNode node1 = new SimpleConsumingTokenRingNode(1, node0);
//        SimpleConsumingTokenRingNode node2 = new SimpleConsumingTokenRingNode(2, node1);
//        SimpleConsumingTokenRingNode node3 = new SimpleConsumingTokenRingNode(3, node2);
//        SimpleConsumingTokenRingNode node4 = new SimpleConsumingTokenRingNode(4, node3);
//        SimpleConsumingTokenRingNode node5 = new SimpleConsumingTokenRingNode(5, node4);
//        node0.setNextNode(node5);
//
//        Thread thread0 = new Thread(node0);
//        Thread thread1 = new Thread(node1);
//        Thread thread2 = new Thread(node2);
//        Thread thread3 = new Thread(node3);
//        Thread thread4 = new Thread(node4);
//        Thread thread5 = new Thread(node5);
//
//
//        thread0.start();
//        thread1.start();
//        thread2.start();
//        thread3.start();
//        thread4.start();
//        thread5.start();
//
//        node0.setDataPackage(new DataPackage(0, 1, System.currentTimeMillis()));
//        node1.setDataPackage(new DataPackage(1, 2, System.currentTimeMillis()));
//        node2.setDataPackage(new DataPackage(2, 2, System.currentTimeMillis()));
//        node3.setDataPackage(new DataPackage(3, 2, System.currentTimeMillis()));
//        node4.setDataPackage(new DataPackage(4, 2, System.currentTimeMillis()));
//        node5.setDataPackage(new DataPackage(5, 2, System.currentTimeMillis()));
    }
}