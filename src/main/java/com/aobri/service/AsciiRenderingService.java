package com.aobri.service;

import com.aobri.model.GeographicCoordinates;

public interface AsciiRenderingService {

    void prepareUI();

    void drawPoint(GeographicCoordinates geographicCoordinates);

    void renderAsciiText();

    String getRenderedAsciiString();
}
