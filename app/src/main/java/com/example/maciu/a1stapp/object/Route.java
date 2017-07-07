package com.example.maciu.a1stapp.object;

import com.example.maciu.a1stapp.list.activity.ListActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciu on 06.07.2017.
 */
public class Route {
    private Integer thumbId;
    private String name;
    private LatLng latLng;
    private double distance;
    private double score;

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Integer getThumbId() {
        return thumbId;
    }

    public void setThumbId(Integer thumbId) {
        this.thumbId = thumbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Route() {
        this.name = "";
        this.thumbId = 0;
        this.latLng = new LatLng(0, 0);
        this.distance = 0;
        this.score = 0;
    }

    public Route(String name, int thumbid, LatLng loc, double distance, double score) {
        this.name = name;
        this.thumbId = thumbid;
        this.latLng = loc;
        this.distance = distance;
        this.score = score;
    }

    public Route(Card card) {
        this.name = card.getName();
        this.thumbId = card.getThumbId();
        this.latLng = new LatLng(card.getLatitude(), card.getLongitude());
        this.distance = card.getDistance();
        this.score = card.getScore();
    }
}
