package ru.mipt.tokenRing;

import lombok.Data;

@Data
public class DataPackage {
    private final int sender;
    private final int destination;
    private final long transferStartTime;
}
