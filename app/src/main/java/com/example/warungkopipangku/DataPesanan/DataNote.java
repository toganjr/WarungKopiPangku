package com.example.warungkopipangku.DataPesanan;

public class DataNote {
    int harga,jumlah;
    String nama;

    public DataNote(String nama, int harga, int jumlah) {
        this.nama = nama;
        this.harga = harga;
        this.jumlah = jumlah;

    }

    public int getHarga() { return harga;}

    public int getJumlah() { return jumlah;}

    public String getNama() {
        return nama;
    }

}
