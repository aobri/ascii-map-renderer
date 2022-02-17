package com.aobri.service;

import com.aobri.model.Borders;
import com.aobri.model.GeographicCoordinates;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AsciiRenderingServiceImpl implements AsciiRenderingService {

    private static final int FONT_SIZE = 12;
    private static final int IMAGE_PADDING = 3;
    private static final double RADIUS_MAJOR = 6_378_137.0d;
    private static final double RADIUS_MINOR = 6_356_752.3142d;
    private int windowHeight = 1080;
    private int windowWidth = 2160;
    private BufferedImage bufferedImage;
    private JFrame frame;
    private Graphics2D graphics2D;
    private String renderedAsciiText;
    private JTextArea textArea;

    public AsciiRenderingServiceImpl() {
        Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();
        windowWidth = dimensions.width;
        windowHeight = dimensions.width/2;
        prepareUI();
    }


    @Override
    public void prepareUI() {
        bufferedImage = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
        graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(windowWidth, windowHeight);
        frame.setTitle("ASCII Map Renderer");
        frame.getContentPane().setBackground(Color.BLACK);

        textArea = new JTextArea();
        textArea.setFont(new Font("Courier New", Font.PLAIN, FONT_SIZE));
        textArea.setSize(frame.getSize());
        textArea.setForeground(Color.GREEN);
        textArea.setBackground(Color.BLACK);
        frame.add(textArea);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * A function that uses Mercator Projection to convert Geographic Coordinates to Cartesian Coordinates
     * And draws a point at the converted X,Y values of this service's bufferedImage
     *
     * @param geographicCoordinates containing longitude and latitude coordinates
     */
    @Override
    public void drawPoint(GeographicCoordinates geographicCoordinates) {

        int x = projectLongitudeToX(geographicCoordinates.getLongitude());
        int y = projectLatitudeToY(geographicCoordinates.getLatitude());
        // render points on image for each csv line/point,drawPoints on buffered image
        graphics2D.drawLine(x, y, x, y);
    }

    private int projectLongitudeToX(Double longitude) {
        int unscaledX =  (int) (Math.toRadians(longitude) * RADIUS_MAJOR);
        return (int) ((windowWidth / 69_238_579d) * (34_619_289d + unscaledX));
    }

    private int projectLatitudeToY(Double latitude) {
//        return (int) (Math.log(Math.tan(Math.PI / 4 + Math.toRadians(latitude) / 2)) * RADIUS_MAJOR);

        latitude = Math.min(Math.max(latitude, -89.5), 89.5);
        double earthDimensionalRateNormalized = 1.0 - Math.pow(RADIUS_MINOR / RADIUS_MAJOR, 2);

        double inputOnEarthProj = Math.sqrt(earthDimensionalRateNormalized) * Math.sin(Math.toRadians(latitude));

        inputOnEarthProj = Math.pow(((1.0 - inputOnEarthProj) / (1.0 + inputOnEarthProj)),
                0.5 * Math.sqrt(earthDimensionalRateNormalized));

        double inputOnEarthProjNormalized =
                Math.tan(0.5 * ((Math.PI * 0.5) - Math.toRadians(latitude))) / inputOnEarthProj;

        int unscaledY = (int) ((-1) * RADIUS_MAJOR * Math.log(inputOnEarthProjNormalized));
        return (int) ((windowHeight / 40_075_017d) * (20_037_508d - unscaledY));
    }

    @Override
    public void renderAsciiText() {

        Borders imageBorders = findImageBounds(bufferedImage);

        // convert image to ascii string
        String ascii = "*";
        for (int y = imageBorders.getBottom(); y < imageBorders.getTop(); y++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = imageBorders.getLeft(); x < imageBorders.getRight(); x++) {
                ascii = (ascii.equals("*")) ? "#" : "*";
                stringBuilder.append(bufferedImage.getRGB(x, y) == Color.BLACK.getRGB() ? " " : ascii);
            }
            textArea.append(stringBuilder + "\n");
        }
        renderedAsciiText = textArea.getText();
    }

    private Borders findImageBounds(BufferedImage bufferedImage) {
        Borders borders = new Borders();
        // Find left boundary
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                if (bufferedImage.getRGB(x, y) != Color.BLACK.getRGB()) {
                    borders.setLeft(x - IMAGE_PADDING);
                    x = windowWidth;
                    break;
                }
            }
        }
        // Find right boundary
        for (int x = bufferedImage.getWidth() - 1; x >= 0; x--) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                if (bufferedImage.getRGB(x, y) != Color.BLACK.getRGB()) {
                    borders.setRight(x + IMAGE_PADDING);
                    x = -1;
                    break;
                }
            }
        }
        // Find bottom boundary
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                if (bufferedImage.getRGB(x, y) != Color.BLACK.getRGB()) {
                    borders.setBottom(y - IMAGE_PADDING);
                    y = windowHeight;
                    break;
                }
            }
        }
        // Find top boundary
        for (int y = bufferedImage.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                if (bufferedImage.getRGB(x, y) != Color.BLACK.getRGB()) {
                    borders.setTop(y + IMAGE_PADDING);
                    y = -1;
                    break;
                }
            }
        }
        return borders;
    }

    @Override
    public String getRenderedAsciiString() {
        return renderedAsciiText;
    }


}
