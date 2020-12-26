package ru.mipt.tokenRing.withQueueImpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import ru.mipt.tokenRing.DataPackage;

import static java.lang.System.nanoTime;
import static java.lang.Thread.sleep;

@Data
@AllArgsConstructor
public class QueueThroughputTokenRingNode implements Runnable, QueuedTokenRingNode {
    private DataPackage dataPackage;
    private final int nodeNumber;
    private MediumQueue nextMedium;
    private MediumQueue previousMedium;
    private long latency;
    private long artificialNodeLatency = 0;
    private int msgsNumber = 0;
    private long throughputStartTime = 0L;
    private long throughput = 0L;

    public QueueThroughputTokenRingNode(int nodeNumber, MediumQueue previousMedium, MediumQueue nextMedium) {
        this.nodeNumber = nodeNumber;
        this.previousMedium = previousMedium;
        this.nextMedium = nextMedium;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            if (this.dataPackage != null) {
                if (this.dataPackage.getSender() == this.nodeNumber) {
                    sleep(artificialNodeLatency);
                    sendDataPackage(new DataPackage(this.nodeNumber, this.dataPackage.getDestination(), nanoTime()));
                } else {
                    sleep(artificialNodeLatency);
                    sendDataPackage(this.dataPackage);
                }
                this.dataPackage = null;
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
        } catch (NullPointerException ignored) {
        }
        if (this.dataPackage != null){
            if (this.msgsNumber == 0) {
                this.throughputStartTime = nanoTime();
            }
            if (nanoTime() - throughputStartTime <= 1000000000){
                msgsNumber++;
            } else {
                this.throughput = msgsNumber;
                this.throughputStartTime = nanoTime();
                msgsNumber = 1;
            }
        }
    }

    public void setMediums(MediumQueue previousMedium, MediumQueue nextMedium) {
        this.previousMedium = previousMedium;
        this.nextMedium = nextMedium;
    }

}
