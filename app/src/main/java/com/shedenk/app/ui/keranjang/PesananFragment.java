package com.shedenk.app.ui.keranjang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shedenk.app.Env;
import com.shedenk.app.R;
import com.shedenk.app.RecyclerViewListener;
import com.shedenk.app.SessionManager;
import com.shedenk.app.databinding.FragmentTransaksiBinding;
import com.shedenk.app.pesanan.DetailPesanan;
import com.shedenk.app.pesanan.PesananModel;
import com.shedenk.app.produk.DetailProduk;
import com.shedenk.app.produk.DetailProdukSimpan;
import com.shedenk.app.transaksiactivity.DetailTransaksi;
import com.shedenk.app.transaksiactivity.TransaksiModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PesananFragment extends Fragment implements RecyclerViewListener {

    TextView id_akun;
    SessionManager sessionManager;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Context context;
    AdapterPesanan adapterPesanan;
    ArrayList<PesananModel> data;

    private void loadData(Context context, String id){
        data = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Env.BASE_URL + "dataantrian";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(container.getContext(), "Berhasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    JSONArray jo = new JSONArray(response);
                    JSONObject object;

                    for (int i =0; i < jo.length(); i++){

                        object = jo.getJSONObject(i);
                        data.add(new PesananModel("", object.getString("id_antrian"), object.getString("tgl_transaksi"), object.getString("total_barang"),object.getString("total_harga")));
                    }

                    layoutManager = new GridLayoutManager(getActivity(),1);
                    recyclerView.setLayoutManager(layoutManager);

                    adapterPesanan = new AdapterPesanan(data, PesananFragment.this);
                    recyclerView.setAdapter(adapterPesanan);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Gagal Mengambil Data" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_akun", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesanan,container,false);

        context = container.getContext();
        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();

        id_akun = view.findViewById(R.id.id_akun_pesanan);

        HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);

        id_akun.setText(sid);
        recyclerView = view.findViewById(R.id.recycler_pesanan);
        recyclerView.setHasFixedSize(true);

        loadData(container.getContext(), sid);
        return view;
    }

    @Override
    public void onClickItem(View view, int position) {

        Intent intent = new Intent(view.getContext(), DetailPesanan.class);

        intent.putExtra("id_pesanan", data.get(position).getId_pesanan());

        startActivityForResult(intent, 1);

    }

    @Override
    public void onClickHapusSimpan(View view, int position) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apa Yakin Menghapus Pesanan?")
                .setConfirmText("Ya, Hapus!")
                .setCancelText("Batal")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        String hid_pesanan = data.get(position).getId_pesanan();
                        String hid_akun = id_akun.getText().toString();

                        HapusPesanan(hid_pesanan, hid_akun);
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void HapusPesanan(String hid_pesanan, String hid_akun) {
        String url = Env.BASE_URL + "hapusantrian";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("pesan");
                            if (resp.equals("Berhasil Menghapus")) {
                                Toast.makeText(getActivity(), "Berhasil Menghapus", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Gagal Menghapus", Toast.LENGTH_SHORT).show();
                            }
                            HashMap<String, String> user = sessionManager.getUserDetail();
                            String sid = user.get(sessionManager.ID);
                            loadData(context, sid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Gagal Menghapus Data", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_antrian", hid_pesanan);
                params.put("id_akun", hid_akun);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}