package com.example.warungkopipangku.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListPesananHasil {
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("totalBeli")
    @Expose
    private String totalBeli;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTotalBeli() {
        return totalBeli;
    }

    public void setTotalBeli(String totalBeli) {
        this.totalBeli = totalBeli;
    }
}
