package ru.mipt.tokenRing;

public interface TokenRingNode {
   void sendDataPackage(DataPackage dataPackage);
   void receiveDataPackage(DataPackage dataPackage);
}
