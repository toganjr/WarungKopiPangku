package com.example.warungkopipangku.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListMenu_Response {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("list_menu")
    @Expose
    private List<ListMenu> listMenu = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<ListMenu> getListMenu() {
        return listMenu;
    }

    public void setListMenu(List<ListMenu> listMenu) {
        this.listMenu = listMenu;
    }
}
