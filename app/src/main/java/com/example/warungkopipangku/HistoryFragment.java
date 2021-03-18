package com.example.warungkopipangku;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warungkopipangku.Data.ListPesanan;
import com.example.warungkopipangku.Data.ListPesananProduct;
import com.example.warungkopipangku.Data.ListPesananProduct_Response;
import com.example.warungkopipangku.Data.ListPesanan_Response;
import com.example.warungkopipangku.DataHistory.DataNote;
import com.example.warungkopipangku.DataHistory.DataNote2;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    BaseAPIService mApiService;
    ListAdapter mListadapter;
    ListAdapter2 mListadapter2;
    RecyclerView listview;
    TextView txtNihil;
    Spinner spinner_sort;
    int [] id,idsearch;
    String [] nama,total,status,tanggal,namasearch,totalSearch,statusSearch,tanggalSearch;

    // TODO: Rename and change types of parameters

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("History");
        mApiService = UtilsApi.getClient().create(BaseAPIService.class);

        listview =(RecyclerView) v.findViewById(R.id.listHistory);
        txtNihil = (TextView) v.findViewById(R.id.TV_texttidakditemukan);
        spinner_sort = v.findViewById(R.id.spinner1);
        String[] items = new String[]{"Pesanan", "Produk"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner_sort.setAdapter(adapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);

        spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch(position){
                    //Case statements
                    case 0: initListView(1,"");
                        break;
                    case 1: initListView2(1,"");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing happen
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

                if (spinner_sort.getSelectedItemPosition() == 0){
                    initListView(1,query);
                } else {
                    initListView2(1,query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (spinner_sort.getSelectedItemPosition() == 0){
                    initListView(1,newText);
                } else {
                    initListView2(1,newText);
                }
                return true;
            }
        });
    }

    public void initListView(int no, final String text){
        Call<ListPesanan_Response> getPesanan = mApiService.getHistory(
                no
        );
        getPesanan.enqueue(new Callback<ListPesanan_Response>() {
            @Override
            public void onResponse(Call<ListPesanan_Response> call, Response<ListPesanan_Response> response) {
                boolean iserror_ = response.body().getError();
                if (iserror_ == false) {
                    List<ListPesanan> list = new ArrayList<>();
                    list = response.body().getListPesanan();
                    id = new int[list.size()];
                    nama = new String[list.size()];
                    total = new String[list.size()];
                    status = new String[list.size()];
                    tanggal = new String[list.size()];
                    idsearch = new int[list.size()];
                    namasearch = new String[list.size()];
                    totalSearch = new String[list.size()];
                    statusSearch = new String[list.size()];
                    tanggalSearch =  new String[list.size()];
                    for (int cc=0; cc < list.size(); cc++){
                        for (int i =0;i<list.size();i++) {
                            id[i] = list.get(i).getId();
                            nama[i] = list.get(i).getNama();
                            total[i] = list.get(i).getTotalBayar();
                            status[i] = list.get(i).getStatus();
                            tanggal[i] = list.get(i).getTanggal();
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
                                                        total[i],
                                                        status[i],
                                                        tanggal[i]
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
                                totalSearch[cc] = total[cc];
                                statusSearch[cc] = status[cc];
                            }
                        }

                        ArrayList data = new ArrayList<DataNote>();
                        for (int i = 0; i < list.size(); i++)
                        {
                            if (idsearch[i] != 0) {

                                data.add(
                                        new DataNote
                                                (
                                                        idsearch[i],
                                                        namasearch[i],
                                                        totalSearch[i],
                                                        statusSearch[i],
                                                        tanggalSearch[i]
                                                ));

                            } else {
                            }
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
            public void onFailure(Call<ListPesanan_Response> call, Throwable t) { ;
                Toast.makeText(getActivity(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initListView2(int no, final String text){
        Call<ListPesananProduct_Response> getPesananProduct = mApiService.getHistoryProduct(
                no
        );
        getPesananProduct.enqueue(new Callback<ListPesananProduct_Response>() {
            @Override
            public void onResponse(Call<ListPesananProduct_Response> call, Response<ListPesananProduct_Response> response) {
                boolean iserror_ = response.body().getError();
                if (iserror_ == false) {
                    List<ListPesananProduct> list = new ArrayList<>();
                    list = response.body().getListPesananProduct();
                    id = new int[list.size()];
                    nama = new String[list.size()];
                    total = new String[list.size()];
                    tanggal = new String[list.size()];
                    idsearch = new int[list.size()];
                    namasearch = new String[list.size()];
                    totalSearch = new String[list.size()];
                    tanggalSearch =  new String[list.size()];
                    for (int cc=0; cc < list.size(); cc++){
                        for (int i =0;i<list.size();i++) {
                            nama[i] = list.get(i).getNama();
                            total[i] = list.get(i).getTotalBeli();
                            tanggal[i] = list.get(i).getTanggal();
                        }
                    }
                    if(text.isEmpty()){
                        for (int cc=0; cc < list.size(); cc++){
                            ArrayList data = new ArrayList<DataNote2>();
                            for (int i = 0; i < list.size(); i++)
                            {
                                data.add(
                                        new DataNote2
                                                (
                                                        nama[i],
                                                        total[i],
                                                        tanggal[i]
                                                ));
                            }

                            mListadapter2 = new ListAdapter2(data);
                            listview.setAdapter(mListadapter2);
                        }
                    } else{
                        for (int cc=0; cc < list.size(); cc++){
                            if(nama[cc].toLowerCase().contains(text.toLowerCase())) {
                                namasearch[cc] = nama[cc];
                                totalSearch[cc] = total[cc];
                                tanggalSearch[cc] = tanggal[cc];
                            }
                        }

                        ArrayList data = new ArrayList<DataNote2>();
                        for (int i = 0; i < list.size(); i++)
                        {
                            if (namasearch[i] != null) {
                                data.add(
                                        new DataNote2
                                                (
                                                        namasearch[i],
                                                        totalSearch[i],
                                                        tanggalSearch[i]
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

                        mListadapter2 = new ListAdapter2(data);
                        listview.setAdapter(mListadapter2);
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
            public void onFailure(Call<ListPesananProduct_Response> call, Throwable t) { ;
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
            TextView textViewTotal;
            TextView textViewStatus;
            TextView textViewTanggal;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textViewNama = (TextView) itemView.findViewById(R.id.TV_historynama);
                this.textViewTotal = (TextView) itemView.findViewById(R.id.TV_historytotal);
                this.textViewStatus = (TextView) itemView.findViewById(R.id.TV_historystatus);
                this.textViewTanggal = (TextView) itemView.findViewById(R.id.TV_historytanggal);
            }
        }


        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_history, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position)
        {
            String uangTotal = NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(dataList.get(position).getTotal())); //add
            holder.textViewNama.setText(dataList.get(position).getNama());
            holder.textViewTotal.setText("Rp. "+uangTotal);
            if (dataList.get(position).getStatus().equalsIgnoreCase("proses")) {
                holder.textViewStatus.setTextColor(getResources().getColor(R.color.green));
            } else {
                holder.textViewStatus.setTextColor(getResources().getColor(R.color.textColor));
            }
            holder.textViewStatus.setText(dataList.get(position).getStatus());
            holder.textViewTanggal.setText(dataList.get(position).getTanggal());


            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), LihatPesananActivity.class);
                    intent.putExtra("EXTRA_ID", dataList.get(position).getId());
                    intent.putExtra("EXTRA_NAMA", dataList.get(position).getNama());
                    intent.putExtra("EXTRA_TOTAL", dataList.get(position).getTotal());
                    intent.putExtra("EXTRA_STATUS", dataList.get(position).getStatus());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }

    public class ListAdapter2 extends RecyclerView.Adapter<ListAdapter2.ViewHolder>
    {
        private ArrayList<DataNote2> dataList;

        public ListAdapter2(ArrayList<DataNote2> data)
        {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textViewNama;
            TextView textViewTotal;
            TextView textViewTanggal;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textViewNama = (TextView) itemView.findViewById(R.id.TV_historynamaproduct);
                this.textViewTotal = (TextView) itemView.findViewById(R.id.TV_historytotalproduct);
                this.textViewTanggal = (TextView) itemView.findViewById(R.id.TV_historytanggalproduct);
            }
        }


        @Override
        public ListAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_historyproduct, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ListAdapter2.ViewHolder holder, final int position)
        {
            holder.textViewNama.setText(dataList.get(position).getNama());
            holder.textViewTotal.setText(dataList.get(position).getTotal());
            holder.textViewTanggal.setText(dataList.get(position).getTanggal());


            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            });
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }
}
