package com.reece.platform.products.branches.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GeocodingService {

    private final GeoApiContext context;
    private final GeometryFactory geometryFactory;

    @Autowired
    public GeocodingService(@Value("${google_api_key:null}") String googleApiKey) {
        this.context = new GeoApiContext.Builder().apiKey(googleApiKey).build();
        this.geometryFactory = new GeometryFactory();
    }

    public Point getLocationFromAddress(String address) throws IOException, InterruptedException, ApiException {
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();

        var coords = results[0].geometry.location;

        return getPointFromLatLng(coords.lat, coords.lng);
    }

    public Point getPointFromLatLng(Double lat, Double lng) {
        return geometryFactory.createPoint(new Coordinate(lng, lat));
    }
}
