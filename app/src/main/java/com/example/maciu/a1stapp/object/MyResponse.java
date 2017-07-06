package com.example.maciu.a1stapp.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by maciu on 04.07.2017.
 */
public class MyResponse {
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("routes")
    @Expose
    private List<Card> routes = null;
    @SerializedName("status")
    @Expose
    private String status;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Card> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Card> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
