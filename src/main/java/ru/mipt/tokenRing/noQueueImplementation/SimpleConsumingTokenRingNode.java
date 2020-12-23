package ru.mipt.tokenRing.noQueueImplementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.mipt.tokenRing.DataPackage;

@Data
@AllArgsConstructor
public class SimpleConsumingTokenRingNode implements Runnable, TokenRingNode {
    private volatile DataPackage dataPackage;
    private final int nodeNumber;
    private SimpleConsumingTokenRingNode nextNode;
    private long latency;

    public SimpleConsumingTokenRingNode(int nodeNumber, SimpleConsumingTokenRingNode nextNode) {
        this.nodeNumber = nodeNumber;
        this.nextNode = nextNode;
    }

    public SimpleConsumingTokenRingNode(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    @Override
    public void run() {
        while (true) {
            if (this.dataPackage != null) {
                if (this.dataPackage.getDestination() != this.nodeNumber){
                    sendDataPackage(this.dataPackage);
                } else {
                    this.latency = System.nanoTime() - this.dataPackage.getTransferStartTime();
                }
                this.dataPackage = null;
            }
        }
    }


    public void sendDataPackage(DataPackage inputDataPackage) {
        this.nextNode.receiveDataPackage(inputDataPackage);
    }

    public void receiveDataPackage(DataPackage outputDataPackage) {
        while (this.dataPackage != null) {
        }
        this.dataPackage = outputDataPackage;
    }
}
