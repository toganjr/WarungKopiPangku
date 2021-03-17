package com.example.warungkopipangku.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListPesananProduct {

    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("totalBeli")
    @Expose
    private String totalBeli;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;

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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
