package com.example.warungkopipangku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warungkopipangku.Data.ListMenu;
import com.example.warungkopipangku.Data.ListMenu_Response;
import com.example.warungkopipangku.Data.MsgInfo_Response;
import com.example.warungkopipangku.DataTambah.DataNote;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    BaseAPIService mApiService;
    ListAdapter mListadapter;
    Button btnTambah;
    EditText etNama;
    TextView txtNihil;
    Calendar calendar;
    int [] id,harga,tipe,idsearch,hargasearch,tipesearch;
    String [] nama,namasearch;
    static int [] idSementara,jumlahSementara;

    RecyclerView listview;

    // TODO: Rename and change types of parameters

    public TambahFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tambah, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tambah");
        mApiService = UtilsApi.getClient().create(BaseAPIService.class);

        listview =(RecyclerView) v.findViewById(R.id.listTambah);
        btnTambah = (Button) v.findViewById(R.id.btn_tambah);
        etNama = (EditText) v.findViewById(R.id.editText1);
        txtNihil = (TextView) v.findViewById(R.id.TV_texttidakditemukan);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);

        initListView(1,"");
        getMenuLength(1);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etNama.getText().toString().isEmpty())  {
                    int totalPesanan = 0;
                    for (int x = 0; x < idSementara.length; x++) {
                        totalPesanan += jumlahSementara[x];
                    }
                    if (totalPesanan > 0) {
                        tambahPemesan(etNama.getText().toString());
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0 ; i< idSementara.length; i++){
                                    if (jumlahSementara[i] > 0) {
                                        tambahPesanan(idSementara[i],jumlahSementara[i]);
                                    }
                                }
                            }
                        }, 3000);
                        Toast.makeText(getActivity(), "Pesanan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        Intent i = new Intent(getActivity().getBaseContext(),
                                MainActivity.class);
                        i.putExtra("STATUS", 1);
                        getActivity().startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), "Anda belum Memasukkan jumlah pesanan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Silahkan masukkan nama Pelanggan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu,inflater);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                initListView(1,query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                initListView(1,newText);
                return true;
            }
        });
    }

    public void getMenuLength(int no) {
        Call<ListMenu_Response> getMenu = mApiService.getMenu(
                no
        );
        getMenu.enqueue(new Callback<ListMenu_Response>() {
            @Override
            public void onResponse(Call<ListMenu_Response> call, Response<ListMenu_Response> response) {
                boolean iserror_ = response.body().getError();
                if (iserror_ == false) {
                    List<ListMenu> list = new ArrayList<>();
                    list = response.body().getListMenu();
                    idSementara = new int[list.size()];
                    jumlahSementara = new int[list.size()];
                }
            }
            @Override
            public void onFailure(Call<ListMenu_Response> call, Throwable t) { ;
                Toast.makeText(getActivity(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tambahPemesan(String nama) {
        calendar = Calendar.getInstance();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);

        Call<MsgInfo_Response> tambahPemesan = mApiService.tambahPemesan(
                nama,
                formattedDate);
        tambahPemesan.enqueue(new Callback<MsgInfo_Response>() {
            @Override
            public void onResponse(Call<MsgInfo_Response> call, Response<MsgInfo_Response> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<MsgInfo_Response> call, Throwable t) {
                Toast.makeText(getActivity(), "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tambahPesanan(int id_menu ,int jumlah){
        Call<MsgInfo_Response> tambahPesanan = mApiService.tambahPesanan(
                id_menu,
                jumlah
        );
        tambahPesanan.enqueue(new Callback<MsgInfo_Response>() {
            @Override
            public void onResponse(Call<MsgInfo_Response> call, Response<MsgInfo_Response> response) {
                if (response.isSuccessful()) {

                } else {

                }
            }
            @Override
            public void onFailure(Call<MsgInfo_Response> call, Throwable t) {
                Toast.makeText(getActivity(), "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void initListView(int no, final String text){
        Call<ListMenu_Response> getMenu = mApiService.getMenu(
                no
        );
        getMenu.enqueue(new Callback<ListMenu_Response>() {
            @Override
            public void onResponse(Call<ListMenu_Response> call, Response<ListMenu_Response> response) {
                boolean iserror_ = response.body().getError();
                if (iserror_ == false) {
                    List<ListMenu> list = new ArrayList<>();
                    list = response.body().getListMenu();
                    id = new int[list.size()];
                    nama = new String[list.size()];
                    harga = new int[list.size()];
                    tipe = new int[list.size()];
                    idsearch = new int[list.size()];
                    namasearch = new String[list.size()];
                    hargasearch = new int[list.size()];
                    tipesearch = new int[list.size()];
                    for (int cc=0; cc < list.size(); cc++){
                        for (int i =0;i<list.size();i++) {
                            id[i] = list.get(i).getId();
                            nama[i] = list.get(i).getNama();
                            harga[i] = list.get(i).getHarga();
                            tipe[i] = list.get(i).getTipe();
                        }
                    }
                    if(text.isEmpty()){
                        for (int cc=0; cc < list.size(); cc++){
                            ArrayList data = new ArrayList<DataNote>();
                            for (int i = 0; i < list.size(); i++)
                            {
                                data.add(
                                        new DataNote
                                                (
                                                        id[i],
                                                        nama[i],
                                                        harga[i],
                                                        tipe[i]
                                                ));
                            }

                            mListadapter = new ListAdapter(data);
                            listview.setAdapter(mListadapter);
                        }
                    } else{
                        for (int cc=0; cc < list.size(); cc++){
                            if(nama[cc].toLowerCase().contains(text.toLowerCase())) {
                                idsearch[cc] = id[cc];
                                namasearch[cc] = nama[cc];
                                hargasearch[cc] = harga[cc];
                                tipesearch[cc] = tipe[cc];
                            }
                        }

                        ArrayList data = new ArrayList<DataNote>();
                        for (int i = 0; i < list.size(); i++)
                        {
                            if ((idsearch[i] != 0) && (tipesearch[i] % 2 == 1)) {

                                data.add(
                                        new DataNote
                                                (
                                                        idsearch[i],
                                                        namasearch[i],
                                                        hargasearch[i],
                                                        tipesearch[i]
                                                ));

                            } else {

                            }
                        }

                        if (!data.isEmpty()) {
                            listview.setVisibility(View.VISIBLE);
                            txtNihil.setVisibility(View.INVISIBLE);
                        } else {
                            listview.setVisibility(View.INVISIBLE);
                            txtNihil.setVisibility(View.VISIBLE);
                        }

                        mListadapter = new ListAdapter(data);
                        listview.setAdapter(mListadapter);
                    }
                    ////////////
//                    for (int i =0;i<list.size();i++) {
//                        id[i] = list.get(i).getId();
//                        nama[i] = list.get(i).getNama();
//                        harga[i] = list.get(i).getHarga();
//                        tipe[i] = list.get(i).getTipe();
//                    }
//
//                    ArrayList data = new ArrayList<DataNote>();
//                    for (int i = 0; i < list.size(); i++)
//                    {
//                        data.add(
//                                new DataNote
//                                        (
//                                                id[i],
//                                                nama[i],
//                                                harga[i],
//                                                tipe[i]
//
//
//                                        ));
//                    }
//
//                    mListadapter = new ListAdapter(data);
//                    listview.setAdapter(mListadapter);

                }
            }
            @Override
            public void onFailure(Call<ListMenu_Response> call, Throwable t) { ;
                Toast.makeText(getActivity(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
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
            TextView textViewHarga;
            TextView textViewJumlah;
            TextView textViewTitle;
            Button btnAdd;
            Button btnRemove;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textViewTitle = (TextView) itemView.findViewById(R.id.TV_menutitle);
                this.textViewHarga = (TextView) itemView.findViewById(R.id.TV_hargamenu);
                this.textViewNama = (TextView) itemView.findViewById(R.id.TV_namamenu);
                this.textViewJumlah = (TextView) itemView.findViewById(R.id.TV_jumlah);
                this.btnAdd = (Button) itemView.findViewById(R.id.btn_menuadd);
                this.btnRemove = (Button) itemView.findViewById(R.id.btn_menurmv);
            }
        }

                @Override
        public int getItemViewType(int position) {
            int tipe = dataList.get(position).getTipe();
            if (tipe%2 == 0) {
                return  0;
            } else {
                return 1;
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            if (viewType == 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_menutitle, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_menu, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }
        }

        @Override
        public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position)
        {
            if ( dataList.get(position).getTipe() %2 == 0) {
                holder.textViewTitle.setText(dataList.get(position).getNama());
            } else {
                String uangHarga = NumberFormat.getNumberInstance(Locale.US).format(dataList.get(position).getHarga()); //add
                holder.textViewNama.setText(dataList.get(position).getNama());
                holder.textViewHarga.setText("Rp. "+uangHarga);
                int jumlah = jumlahSementara[findIndexOf(dataList.get(position).getId(),id)];
                holder.textViewJumlah.setText(String.valueOf(jumlah));

                holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        idSementara[findIndexOf(dataList.get(position).getId(),id)] = dataList.get(position).getId();
                        jumlahSementara[findIndexOf(dataList.get(position).getId(),id)] += 1;
                        int jumlah = jumlahSementara[findIndexOf(dataList.get(position).getId(),id)];
                        holder.textViewJumlah.setText(String.valueOf(jumlah));
                    }
                });

                holder.btnRemove.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if ( jumlahSementara[findIndexOf(dataList.get(position).getId(),id)] != 0 ) {
                                idSementara[findIndexOf(dataList.get(position).getId(),id)] = dataList.get(position).getId();
                                jumlahSementara[findIndexOf(dataList.get(position).getId(),id)] -= 1;
                                int jumlah = jumlahSementara[findIndexOf(dataList.get(position).getId(),id)];
                                holder.textViewJumlah.setText(String.valueOf(jumlah));
                        } else {
                                int jumlah = jumlahSementara[findIndexOf(dataList.get(position).getId(),id)];
                                holder.textViewJumlah.setText(String.valueOf(jumlah));
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }

    static int findIndexOf(int V, int[] arr) {
        return IntStream.range(0, arr.length)
                .filter(i->arr[i]==V)
                .findFirst()
                .getAsInt();
    }

}
