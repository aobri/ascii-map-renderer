package com.aobri;

import com.aobri.model.GeographicCoordinates;
import com.aobri.service.AsciiRenderingService;
import com.aobri.service.AsciiRenderingServiceImpl;
import com.aobri.service.CsvService;
import com.aobri.service.CsvServiceImpl;

import java.util.List;

public class Main {

    private static final int COORDINATES_BATCH_SIZE = 10_000;

    /**
     * A simple program that creates ASCII art from postal area coordinates provided from a csv file.
     *
     * @param args args[0] may contain a csv file path.
     */
    public static void main(String[] args) {

        String csvFile = "ukpostcodes.csv";

        if (args != null && args.length > 0 && !args[0].isEmpty() && args[0].endsWith(".csv")) {
            csvFile = args[0];
        }

        // Read csv file for long-lat points
        CsvService csvService = new CsvServiceImpl();
        csvService.initService(csvFile);

        if (!csvService.isAvailable()) {
            System.out.println("CSV file not available or contains corrupt data. Program exiting!");
            return;
        }

        // configure window for display
        AsciiRenderingService renderingService = new AsciiRenderingServiceImpl();

        while (csvService.hasNext()) {
            List<GeographicCoordinates> geographicCoordinates = csvService.getNextKCoordinates(COORDINATES_BATCH_SIZE);
            for (GeographicCoordinates coordinates : geographicCoordinates) {
                renderingService.drawPoint(coordinates);
            }
        }
        renderingService.renderAsciiText();

        // TODO: organize exception handling
        // TODO: centralize constants in one class
        // TODO: revise documentation
    }
}