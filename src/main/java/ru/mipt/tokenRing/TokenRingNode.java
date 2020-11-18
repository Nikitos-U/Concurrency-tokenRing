package ru.mipt.tokenRing;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRingNode implements Runnable {
    private volatile DataPackage dataPackage;
    private final int nodeNumber;
    private TokenRingNode nextNode;

    public TokenRingNode(int nodeNumber, TokenRingNode nextNode) {
        this.nodeNumber = nodeNumber;
        this.nextNode = nextNode;
    }

    public TokenRingNode(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    @Override
    public void run() {
        while (true) {
            if (this.dataPackage != null) {
                sendDataPackage(this.dataPackage);
                this.dataPackage = null;
            }
        }
    }


    public void sendDataPackage(DataPackage inputDataPackage) {
        System.out.println(inputDataPackage + " was sent by node" + this.nodeNumber);
        this.nextNode.receiveDataPackage(inputDataPackage);
    }

    public void receiveDataPackage(DataPackage outputDataPackage) {
        while (this.dataPackage != null) {
        }
        this.dataPackage = outputDataPackage;
        System.out.println(outputDataPackage + " received by node" + this.nodeNumber);
    }
}
