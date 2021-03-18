package com.example.warungkopipangku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warungkopipangku.Data.ListPesananUser;
import com.example.warungkopipangku.Data.ListPesananUser_Response;
import com.example.warungkopipangku.DataPesanan.DataNote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LihatPesananActivity extends AppCompatActivity {

    String namaPemesan,statusPemesan,totalPemesan;
    int idPemesan;
    String [] namaPesanan;
    int [] jumlahPesanan,hargaPesanan;

    EditText editNama, editTotal;
    Button btnEdit,btnSelesai,btnHapus;
    RecyclerView listPesanan;

    Context mContext;
    BaseAPIService mApiService;
    ListAdapter mListadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_pesanan);
        mContext = this;
        mApiService = UtilsApi.getClient().create(BaseAPIService.class);

        idPemesan = getIntent().getIntExtra("EXTRA_ID",0);
        namaPemesan = getIntent().getStringExtra("EXTRA_NAMA");
        statusPemesan = getIntent().getStringExtra("EXTRA_STATUS");
        totalPemesan = getIntent().getStringExtra("EXTRA_TOTAL");

        editNama = (EditText) findViewById(R.id.editText1);
        editTotal = (EditText) findViewById(R.id.editText2);

        listPesanan = (RecyclerView) findViewById(R.id.listPesanan);

        btnEdit = (Button) findViewById(R.id.Button_Edit);
        btnSelesai = (Button) findViewById(R.id.Button_Selesai);
        btnHapus = (Button) findViewById(R.id.Button_Hapus);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listPesanan.setLayoutManager(layoutManager);

        checkStatus(statusPemesan);
        initListView(idPemesan);
        Log.d("ID PEMESAN ", "onCreate: "+idPemesan);

    }

    private void checkStatus(String status){
        editNama.setText(namaPemesan);
        editTotal.setText(totalPemesan);

        if (status.equalsIgnoreCase("selesai")){
            btnEdit.setVisibility(View.GONE);
            btnSelesai.setVisibility(View.GONE);
        }
    }

    public void initListView(int id){
        Call<ListPesananUser_Response> getPesananUser = mApiService.getPesananUser(
                id
        );
        getPesananUser.enqueue(new Callback<ListPesananUser_Response>() {
            @Override
            public void onResponse(Call<ListPesananUser_Response> call, Response<ListPesananUser_Response> response) {
                boolean iserror_ = response.body().getError();
                if (iserror_ == false) {
                    List<ListPesananUser> list = new ArrayList<>();
                    list = response.body().getListPesanan();
                    namaPesanan = new String[list.size()];
                    jumlahPesanan = new int[list.size()];
                    hargaPesanan = new int[list.size()];
                    for (int cc=0; cc < list.size(); cc++){
                        for (int i =0;i<list.size();i++) {
                            namaPesanan[i] = list.get(i).getNama();
                            jumlahPesanan[i] = list.get(i).getTotalJumlah();
                            hargaPesanan[i] = list.get(i).getTotalHarga();
                        }
                    }
                    for (int cc=0; cc < list.size(); cc++){
                        ArrayList data = new ArrayList<DataNote>();
                        for (int i = 0; i < list.size(); i++)
                        {
                            data.add(
                                    new DataNote
                                            (
                                                    namaPesanan[i],
                                                    hargaPesanan[i],
                                                    jumlahPesanan[i]
                                            ));
                        }

                        mListadapter = new ListAdapter(data);
                        listPesanan.setAdapter(mListadapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<ListPesananUser_Response> call, Throwable t) { ;
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
    {
        private ArrayList<DataNote> dataList;

        public ListAdapter(ArrayList<DataNote> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textViewNama;
            TextView textViewJumlah;
            TextView textViewHarga;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textViewNama = (TextView) itemView.findViewById(R.id.TV_pesanannamaproduct);
                this.textViewJumlah = (TextView) itemView.findViewById(R.id.TV_pesanantotalproduct);
                this.textViewHarga = (TextView) itemView.findViewById(R.id.TV_pesanantotalHarga);
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_pesananuser, parent, false);

            ListAdapter.ViewHolder viewHolder = new ListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position)
        {
            holder.textViewNama.setText(dataList.get(position).getNama());
            holder.textViewJumlah.setText(String.valueOf(dataList.get(position).getJumlah()));
            holder.textViewHarga.setText(String.valueOf(dataList.get(position).getHarga()));
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }
}
