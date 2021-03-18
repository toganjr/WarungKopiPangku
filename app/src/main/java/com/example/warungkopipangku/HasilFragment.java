package com.example.warungkopipangku;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warungkopipangku.Data.ListHasil;
import com.example.warungkopipangku.Data.ListMenu;
import com.example.warungkopipangku.Data.ListMenu_Response;
import com.example.warungkopipangku.Data.ListPesananHasil;
import com.example.warungkopipangku.Data.ListPesananHasil_Response;
import com.example.warungkopipangku.DataHasil.DataNote;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HasilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText tanggal;
    RecyclerView listHasil;
    TextView notifi,hasilBulan,hasilHari;

    private Calendar dateTime = Calendar.getInstance();
    private DatePickerDialog mTanggalPilih;
    private String tanggalPilih;

    BaseAPIService mApiService;
    ListAdapter mListadapter;

    String [] nama,total;

    // TODO: Rename and change types of parameters

    public HasilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hasil, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Hasil");
        mApiService = UtilsApi.getClient().create(BaseAPIService.class);

        tanggal = (EditText) v.findViewById(R.id.editTextTanggal);
        listHasil = (RecyclerView) v.findViewById(R.id.listHasil);
        notifi = (TextView) v.findViewById(R.id.TV_texttidakadapesanan);
        hasilBulan = (TextView) v.findViewById(R.id.TV_hasilbulan);
        hasilHari = (TextView) v.findViewById(R.id.TV_hasilhari);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        String dateNumber = sd.format(c);

        String monthNumber  = (String) DateFormat.format("MM",    c); // 06
        String yearNumber         = (String) DateFormat.format("yyyy",  c);

        getHasil(dateNumber,monthNumber,yearNumber);
        initListView(String.valueOf(sd));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listHasil.setLayoutManager(layoutManager);

        tanggal.setText(formattedDate);
        tanggal.setInputType(InputType.TYPE_NULL);

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTanggalPilih.show();
            }
        });

        initDateTimePickerDialog();

        return v;
    }

    public void getHasil(String tanggal, String bulan, String tahun){
        Call<ListHasil> getHasil = mApiService.getHasil(
                tanggal,
                bulan,
                tahun
        );
        getHasil.enqueue(new Callback<ListHasil>() {
            @Override
            public void onResponse(Call<ListHasil> call, Response<ListHasil> response) {
                boolean iserror_ = response.body().getError();
                if (iserror_ == false) {

                    String uangBulan = NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(response.body().getTotalBulan())); //add
                    String uangHari = NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(response.body().getTotalHari())); //add
                    hasilBulan.setText("Rp. "+uangBulan);
                    hasilHari.setText("Rp. "+uangHari);
                }
            }
            @Override
            public void onFailure(Call<ListHasil> call, Throwable t) { ;
                Toast.makeText(getActivity(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initDateTimePickerDialog(){
        final SimpleDateFormat dateFormatterText = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        final SimpleDateFormat jsonFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        mTanggalPilih = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateTime.set(year, monthOfYear, dayOfMonth);
                tanggalPilih = jsonFormat.format(dateTime.getTime());
                tanggal.setText(dateFormatterText.format(dateTime.getTime()));
                String dateNumber = jsonFormat.format(dateTime.getTime());
                String monthNumber  = (String) DateFormat.format("MM",    dateTime.getTime()); // 06
                String yearNumber       = (String) DateFormat.format("yyyy",  dateTime.getTime());
                getHasil(dateNumber,monthNumber,yearNumber);
               // initListView(String.valueOf(tanggalPilih));
                initListView(String.valueOf(monthNumber));
            }

        },  dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH));

    }

    public void initListView(String tanggal){
        Call<ListPesananHasil_Response> getHasilProduct = mApiService.getHasilProduct(
                tanggal
        );
        getHasilProduct.enqueue(new Callback<ListPesananHasil_Response>() {
            @Override
            public void onResponse(Call<ListPesananHasil_Response> call, Response<ListPesananHasil_Response> response) {
                boolean iserror_ = response.body().getError();
                if (iserror_ == false) {
                    List<ListPesananHasil> list = new ArrayList<>();
                    list = response.body().getListPesanan();
                    if (list.size() != 0) {
                        notifi.setVisibility(View.GONE);
                        listHasil.setVisibility(View.VISIBLE);
                        nama = new String[list.size()];
                        total = new String[list.size()];
                        for (int cc=0; cc < list.size(); cc++){
                            for (int i =0;i<list.size();i++) {
                                nama[i] = list.get(i).getNama();
                                total[i] = list.get(i).getTotalBeli();
                            }
                        }
                        for (int cc=0; cc < list.size(); cc++){
                            ArrayList data = new ArrayList<DataNote>();
                            for (int i = 0; i < list.size(); i++)
                            {
                                data.add(
                                        new DataNote
                                                (
                                                        nama[i],
                                                        total[i]
                                                ));
                            }

                            mListadapter = new ListAdapter(data);
                            listHasil.setAdapter(mListadapter);
                        }
                    } else {
                        listHasil.setVisibility(View.GONE);
                        notifi.setVisibility(View.VISIBLE);
                    }

                }
             }
            @Override
            public void onFailure(Call<ListPesananHasil_Response> call, Throwable t) { ;
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
            TextView textViewJumlah;

            public ViewHolder(View itemView)
            {
                super(itemView);
                this.textViewNama = (TextView) itemView.findViewById(R.id.TV_hasilnamaproduct);
                this.textViewJumlah = (TextView) itemView.findViewById(R.id.TV_hasiltotalproduct);
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_hasil, parent, false);

            ListAdapter.ViewHolder viewHolder = new ListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position)
        {
            holder.textViewNama.setText(dataList.get(position).getNama());
            holder.textViewJumlah.setText(dataList.get(position).getTotal());
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }

}
