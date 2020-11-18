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
            if (dataPackage != null) {
                if (dataPackage.getDestination() != this.nodeNumber) {
                    sendDataPackage();
                }
            }
        }
    }

    public synchronized void sendDataPackage(){
        this.nextNode.receiveDataPackage(this.dataPackage);
        System.out.println("following data: " + this.dataPackage.getData() + " was sent by: " + this.nodeNumber);
        this.setDataPackage(null);
    }

    public synchronized void receiveDataPackage(DataPackage dataPackage){
        while (this.dataPackage != null) {
        }
        setDataPackage(dataPackage);
        System.out.println("following data: " + this.dataPackage.getData() + " received by: " + this.nodeNumber);
    }
}
