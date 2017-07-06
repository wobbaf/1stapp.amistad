package com.example.maciu.a1stapp.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by maciu on 04.07.2017.
 */
public class Pagination {
    @SerializedName("currentPage")
    @Expose
    private Integer currentPage;
    @SerializedName("pageCount")
    @Expose
    private Integer pageCount;
    @SerializedName("itemCountPerPage")
    @Expose
    private Integer itemCountPerPage;
    @SerializedName("totalItemCount")
    @Expose
    private Integer totalItemCount;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getItemCountPerPage() {
        return itemCountPerPage;
    }

    public void setItemCountPerPage(Integer itemCountPerPage) {
        this.itemCountPerPage = itemCountPerPage;
    }

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

}
