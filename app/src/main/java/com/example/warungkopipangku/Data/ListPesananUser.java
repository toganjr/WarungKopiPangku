package com.example.warungkopipangku.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListPesananUser {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("totalJumlah")
    @Expose
    private Integer totalJumlah;
    @SerializedName("totalHarga")
    @Expose
    private Integer totalHarga;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getTotalJumlah() {
        return totalJumlah;
    }

    public void setTotalJumlah(Integer totalJumlah) {
        this.totalJumlah = totalJumlah;
    }

    public Integer getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(Integer totalHarga) {
        this.totalHarga = totalHarga;
    }

}
