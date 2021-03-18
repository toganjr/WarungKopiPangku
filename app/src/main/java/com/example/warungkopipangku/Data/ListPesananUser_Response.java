package com.example.warungkopipangku.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListPesananUser_Response {@SerializedName("error")
@Expose
private Boolean error;
    @SerializedName("list_pesanan")
    @Expose
    private List<ListPesananUser> listPesanan = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<ListPesananUser> getListPesanan() {
        return listPesanan;
    }

    public void setListPesanan(List<ListPesananUser> listPesanan) {
        this.listPesanan = listPesanan;
    }

}
