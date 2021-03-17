package com.example.warungkopipangku.DataTambah;

public class DataNote {
    int id,harga,tipe;
    String nama;

    public DataNote(int id,String nama, int harga, int tipe) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.tipe = tipe;

    }

    public int getId() { return id;}

    public int getHarga() { return harga;}

    public int getTipe() {
        return tipe;
    }

    public String getNama() {
        return nama;
    }

}
