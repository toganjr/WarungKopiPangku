package com.example.warungkopipangku.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListPesananProduct_Response {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("list_pesanan")
    @Expose
    private List<ListPesananProduct> listPesanan = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<ListPesananProduct> getListPesananProduct() {
        return listPesanan;
    }

    public void setListPesanan(List<ListPesananProduct> listPesanan) {
        this.listPesanan = listPesanan;
    }
}
