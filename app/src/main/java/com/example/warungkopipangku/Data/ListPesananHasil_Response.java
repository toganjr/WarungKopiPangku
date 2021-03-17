package com.example.warungkopipangku.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListPesananHasil_Response {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("list_pesanan")
    @Expose
    private List<ListPesananHasil> listPesanan = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<ListPesananHasil> getListPesanan() {
        return listPesanan;
    }

    public void setListPesanan(List<ListPesananHasil> listPesanan) {
        this.listPesanan = listPesanan;
    }
}
