package ru.mipt.tokenRing.withQueueImpl;

import ru.mipt.tokenRing.DataPackage;

public interface QueuedTokenRingNode {
    void sendDataPackage(DataPackage dataPackage);
    void receiveDataPackage();
}
