package ru.mipt.tokenRing.withQueueImpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.mipt.tokenRing.DataPackage;

import static java.lang.System.nanoTime;

@Data
@AllArgsConstructor
public class QueueConsumingTokenRingNode implements Runnable, QueuedTokenRingNode {
    private DataPackage dataPackage;
    private final int nodeNumber;
    private MediumQueue nextMedium;
    private MediumQueue previousMedium;
    private long latency;

    public QueueConsumingTokenRingNode(int nodeNumber, MediumQueue previousMedium, MediumQueue nextMedium) {
        this.nodeNumber = nodeNumber;
        this.previousMedium = previousMedium;
        this.nextMedium = nextMedium;
    }

    public QueueConsumingTokenRingNode(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    @Override
    public void run() {
        while (true) {
            if (this.dataPackage != null) {
                if (this.dataPackage.getDestination() != this.nodeNumber) {
                    sendDataPackage(this.dataPackage);
                } else {
                    this.latency = nanoTime() - this.dataPackage.getTransferStartTime();
                    if (this.dataPackage.getSender() == this.nodeNumber) {
                        sendDataPackage(new DataPackage(this.nodeNumber, this.dataPackage.getDestination(), nanoTime()));
                    } else {
                        sendDataPackage(this.dataPackage);
                    }
                    this.dataPackage = null;
                }
            }
            while (this.dataPackage == null) {
                receiveDataPackage();
            }
        }
    }

    @Override
    public void sendDataPackage(DataPackage inputDataPackage) {
        this.nextMedium.put(inputDataPackage);
    }

    @Override
    public void receiveDataPackage() {
        try {
            this.dataPackage = this.previousMedium.getData();
        }
        catch (NullPointerException ignored){
        }
    }

    public void setMediums(MediumQueue previousMedium, MediumQueue nextMedium){
        this.previousMedium = previousMedium;
        this.nextMedium = nextMedium;
    }
}
