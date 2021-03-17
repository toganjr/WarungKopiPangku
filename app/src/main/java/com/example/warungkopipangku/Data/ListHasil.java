package com.example.warungkopipangku.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListHasil {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("Total Bulan")
    @Expose
    private String totalBulan;
    @SerializedName("Total Hari")
    @Expose
    private String totalHari;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getTotalBulan() {
        return totalBulan;
    }

    public void setTotalBulan(String totalBulan) {
        this.totalBulan = totalBulan;
    }

    public String getTotalHari() {
        return totalHari;
    }

    public void setTotalHari(String totalHari) {
        this.totalHari = totalHari;
    }
}
