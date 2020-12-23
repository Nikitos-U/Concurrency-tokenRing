package ru.mipt.tokenRing.noQueueImplementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.mipt.tokenRing.DataPackage;

import static java.lang.System.nanoTime;

@Data
@AllArgsConstructor
public class SimpleTokenRingNode implements TokenRingNode,Runnable{
    private volatile DataPackage dataPackage;
    private final int nodeNumber;
    private SimpleTokenRingNode nextNode;
    private long throughputTimestamp;
    private int massagesAmount;

    public SimpleTokenRingNode(int nodeNumber, SimpleTokenRingNode nextNode) {
        this.nodeNumber = nodeNumber;
        this.nextNode = nextNode;
    }

    public SimpleTokenRingNode(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    @Override
    public void run() {
        while (true) {
            if (this.dataPackage != null) {
                if (this.massagesAmount == 0){
                    this.throughputTimestamp = this.dataPackage.getTransferStartTime();
                }
                sendDataPackage(this.dataPackage);
                if (nanoTime() - this.throughputTimestamp <= 1000_000_000)
                {
                    massagesAmount++;
                }
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
