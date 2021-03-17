package com.example.warungkopipangku.DataHistory;

public class DataNote {
    int id;
    String nama,total,status,tanggal;

    public DataNote(int id,String nama, String total, String status, String tanggal) {
        this.id = id;
        this.nama = nama;
        this.total = total;
        this.status = status;
        this.tanggal = tanggal;

    }

    public int getId() { return id;}

    public String getTotal() { return total;}

    public String getStatus() {
        return status;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal() { return tanggal;}

}
