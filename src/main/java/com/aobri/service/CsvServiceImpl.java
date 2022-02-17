package com.aobri.service;

import com.aobri.model.GeographicCoordinates;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvServiceImpl implements CsvService {

    private Scanner scanner;

    public CsvServiceImpl() {
    }

    @Override
    public void initService(String csvFilePath) {

        try {
            scanner = new Scanner(new FileInputStream(csvFilePath));
            String headers = scanner.nextLine();
            if (!validHeaders(headers)) {
                System.out.println("Invalid CSV file headers! Exiting program.");
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean validHeaders(String headers) {
        String[] headersArray = headers.split(",");
        return (headersArray[0].equals("id") && headersArray[1].equals("postcode") &&
                headersArray[2].equals("latitude") && headersArray[3].equals("longitude"));
    }

    @Override
    public GeographicCoordinates getNextCoordinates() {
        if (scanner != null && scanner.hasNextLine()) {
            String[] geographicCoordinatesRecord = scanner.nextLine().split(",");
            return new GeographicCoordinates(
                    Integer.parseInt(geographicCoordinatesRecord[0]),
                    geographicCoordinatesRecord[1],
                    Double.parseDouble(geographicCoordinatesRecord[2]),
                    Double.parseDouble(geographicCoordinatesRecord[3])
            );
        } else {
            System.out.println("CSV Scanner not ready! initService first");
            return null;
        }
    }

    @Override
    public List<GeographicCoordinates> getNextKCoordinates(int k) {
        if (scanner != null) {
            int counter = 0;
            List<GeographicCoordinates> geographicCoordinatesList = new ArrayList<>();

            while (scanner.hasNextLine() && (counter < k)) {
                // read a new postal area records
                String[] postalAreaRecord = scanner.nextLine().split(",");
                geographicCoordinatesList.add(
                        new GeographicCoordinates(
                                Integer.parseInt(postalAreaRecord[0]),
                                postalAreaRecord[1],
                                Double.parseDouble(postalAreaRecord[2]),
                                Double.parseDouble(postalAreaRecord[3])
                        ));
                counter++;
            }
            return geographicCoordinatesList;
        } else {
            System.out.println("CSV Scanner not ready! initService first");
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isAvailable() {
        return scanner != null;
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
