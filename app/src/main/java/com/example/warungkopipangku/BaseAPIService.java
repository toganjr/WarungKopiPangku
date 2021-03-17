package com.example.warungkopipangku;

import com.example.warungkopipangku.Data.ListHasil;
import com.example.warungkopipangku.Data.ListMenu_Response;
import com.example.warungkopipangku.Data.ListPesananHasil_Response;
import com.example.warungkopipangku.Data.ListPesananProduct_Response;
import com.example.warungkopipangku.Data.ListPesanan_Response;
import com.example.warungkopipangku.Data.MsgInfo_Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseAPIService {

    @FormUrlEncoded
    @POST("get_menu.php")
    Call<ListMenu_Response> getMenu(@Field("no") int no);

    @FormUrlEncoded
    @POST("get_history.php")
    Call<ListPesanan_Response> getHistory(@Field("no") int no);

    @FormUrlEncoded
    @POST("get_historyproduct.php")
    Call<ListPesananProduct_Response> getHistoryProduct(@Field("no") int no);

    @FormUrlEncoded
    @POST("get_hasilproduct.php")
    Call<ListPesananHasil_Response> getHasilProduct(@Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("get_hasil.php")
    Call<ListHasil> getHasil(@Field("tanggal") String tanggal,
                             @Field("bulan") String bulan,
                             @Field("tahun") String tahun
                             );

    @FormUrlEncoded
    @POST("tambah_pesanan.php")
    Call<MsgInfo_Response> tambahPesanan(@Field("id_menu") int id_menu,
                                         @Field("jumlah") int jumlah
                                               );

    @FormUrlEncoded
    @POST("tambah_pemesan.php")
    Call<MsgInfo_Response> tambahPemesan(@Field("nama") String nama,
                                         @Field("tanggal") String tanggal
    );
}
