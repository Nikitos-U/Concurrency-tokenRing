package ru.mipt.tokenRing.withQueueImpl;

import lombok.Data;
import lombok.SneakyThrows;
import ru.mipt.tokenRing.DataPackage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Data
public class MediumQueue {
    private final BlockingQueue<DataPackage> blockingQueue = new ArrayBlockingQueue<>(10);

    @SneakyThrows
    public void put(DataPackage dataPackage){
        blockingQueue.put(dataPackage);
    }

    @SneakyThrows
    public DataPackage getData(){
        return blockingQueue.take();
    }
}
