package ru.mipt.tokenRing;

public class TokenRingStarter {
    public static void main(String[] args) {
        TokenRingNode zeroNode = new TokenRingNode(0);
        TokenRingNode node1 = new TokenRingNode(1, zeroNode);
        TokenRingNode node2 = new TokenRingNode(2, node1);
        TokenRingNode node3 = new TokenRingNode(3, node2);
        TokenRingNode node4 = new TokenRingNode(4, node3);
        TokenRingNode node5 = new TokenRingNode(5, node4);
        zeroNode.setNextNode(node5);


        Thread thread0 = new Thread(zeroNode);
        Thread thread1 = new Thread(node1);
        Thread thread2 = new Thread(node2);
        Thread thread3 = new Thread(node3);
        Thread thread4 = new Thread(node4);
        Thread thread5 = new Thread(node5);

        zeroNode.setDataPackage(new DataPackage(0, 1, "some data for 1"));
        node1.setDataPackage(new DataPackage(1, 2, "ANOTHER data for 2"));
        node4.setDataPackage(new DataPackage(4, 2, "lolololololol"));

        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

    }
}