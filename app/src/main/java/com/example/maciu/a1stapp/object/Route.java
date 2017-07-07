package com.example.maciu.a1stapp.object;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by maciu on 06.07.2017.
 */
public class Route {
    private Integer thumbId;
    private String name;
    private LatLng latLng;

    public Integer getThumbId() {
        return thumbId;
    }
    public void setThumbId(Integer thumbId) {
        this.thumbId = thumbId;
    }
    public String getName() {
        return name;
    }
    public void setName (String name) {this.name = name;}
    public LatLng getLocation() {
        return latLng;
    }
    public void setLatLng (LatLng latLng) {this.latLng = latLng;}

    public Route(){
        this.name = "";
        this.thumbId = 0;
        this.latLng = new LatLng(0,0);
    }
    public Route(String name, int thumbid, LatLng loc){
        this.name = name;
        this.thumbId = thumbid;
        this.latLng = loc;
    }
}
