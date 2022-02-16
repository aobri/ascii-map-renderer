package com.aobri.service;

import com.aobri.model.PostalCoordinates;

import java.util.List;

public interface CsvService {

    void initService(String csvFilePath);

    PostalCoordinates getNextCoordinates();

    List<PostalCoordinates> getNextKCoordinates(int k);

    boolean isAvailable();

    void close();
}
