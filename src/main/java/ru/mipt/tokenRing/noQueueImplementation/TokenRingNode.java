package ru.mipt.tokenRing.noQueueImplementation;

import ru.mipt.tokenRing.DataPackage;

public interface TokenRingNode {
   void sendDataPackage(DataPackage dataPackage);
   void receiveDataPackage(DataPackage dataPackage);
}
