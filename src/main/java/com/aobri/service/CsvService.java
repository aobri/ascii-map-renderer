package com.aobri.service;

import com.aobri.model.GeographicCoordinates;

import java.util.List;

public interface CsvService {

    void initService(String csvFilePath);

    GeographicCoordinates getNextCoordinates();

    List<GeographicCoordinates> getNextKCoordinates(int k);

    boolean isAvailable();

    boolean hasNext();

    void close();
}
