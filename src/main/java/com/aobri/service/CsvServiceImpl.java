package com.aobri.service;

import com.aobri.model.PostalCoordinates;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
//            validateHeaders(headers);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());

        }
    }

    @Override
    public PostalCoordinates getNextCoordinates() {
        if (scanner != null && scanner.hasNextLine()) {
            String[] postalAreaRecord = scanner.nextLine().split(",");
            System.out.println(Arrays.toString(postalAreaRecord));

            return new PostalCoordinates(
                    Integer.parseInt(postalAreaRecord[0]),
                    postalAreaRecord[1],
                    Double.parseDouble(postalAreaRecord[2]),
                    Double.parseDouble(postalAreaRecord[3])
            );

        } else {
            System.out.println("CSV Scanner not ready! initService first");
            return null;
        }
    }

    @Override
    public List<PostalCoordinates> getNextKCoordinates(int k) {
        if (scanner != null) {
            int counter = 0;
            List<PostalCoordinates> postalCoordinatesList = new ArrayList<>();

            while (scanner.hasNextLine() && (counter < k)) {
                // read a new postal area records
                String[] postalAreaRecord = scanner.nextLine().split(",");
                System.out.println(Arrays.toString(postalAreaRecord));
                postalCoordinatesList.add(
                        new PostalCoordinates(
                                Integer.parseInt(postalAreaRecord[0]),
                                postalAreaRecord[1],
                                Double.parseDouble(postalAreaRecord[2]),
                                Double.parseDouble(postalAreaRecord[3])
                        ));
                counter++;
            }
            return postalCoordinatesList;
        } else {
            System.out.println("CSV Scanner not ready! initService first");
            return null;
        }
    }

    @Override
    public boolean isAvailable() {
        return scanner != null;
    }

    @Override
    public void close() {
        scanner.close();
    }
}
