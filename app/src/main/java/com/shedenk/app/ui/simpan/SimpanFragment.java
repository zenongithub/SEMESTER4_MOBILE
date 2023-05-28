package com.shedenk.app.ui.simpan;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.shedenk.app.R;
import com.shedenk.app.RecyclerViewListener;
import com.shedenk.app.SessionManager;
import com.shedenk.app.databinding.FragmentSimpanBinding;
import com.shedenk.app.produk.DetailProdukSimpan;
import com.shedenk.app.produk.ProdukItemModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SimpanFragment extends Fragment implements RecyclerViewListener {

    TextView id_akun;
    SessionManager sessionManager;
    ProdukItemModel produkItemModel;
    AdapterProdukSimpan adapterProdukSimpan;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private Context context;
    ArrayList<ProdukItemModel> data;
    private FragmentSimpanBinding binding;

    private void loadData(Context context, String sid){

        data = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(

                Request.Method.POST, "http://192.168.252.194:8000/api/datasimpan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(context, "Berhasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    JSONArray jo = new JSONArray(response);

                    JSONObject obj;
                    JSONObject objectgambar;
                    for (int i = 0; i < jo.length(); i++){
                        obj = jo.getJSONObject(i);
                        JSONObject produk = new JSONObject(obj.getString("produk"));
                        JSONObject kategori = new JSONObject(produk.getString("kategori"));
                        JSONArray gam = new JSONArray(produk.getString("gambar"));
                        for (int a = 0; a < gam.length(); a++){
                            objectgambar = gam.getJSONObject(a);
                            data.add(new ProdukItemModel(produk.getString("id_produk"), produk.getString("nama_produk"), produk.getString("harga"),(kategori.getString("nama_kategori")),produk.getString("deskripsi"), "http://192.168.252.194:8000" + "/produk_img/" + objectgambar.getString("nama_gambar"),""));
                        }
                    }
                    layoutManager = new GridLayoutManager(getActivity(),1);
                    recyclerView.setLayoutManager(layoutManager);
                    adapterProdukSimpan = new AdapterProdukSimpan(data, SimpanFragment.this);
                    recyclerView.setAdapter(adapterProdukSimpan);

                    queue.getCache().clear();

                } catch (JSONException e) {
                    Toast.makeText(context,"Gagal " + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("id_akun", sid);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simpan,container,false);

        context = container.getContext();
        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();

        id_akun = view.findViewById(R.id.id_akun_simpan);

        HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);
        id_akun.setText(sid);

        loadData(container.getContext(), sid);

        recyclerView = view.findViewById(R.id.recycler_view_simpan);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClickItem(View view, int position) {
                Intent intent = new Intent(view.getContext(), DetailProdukSimpan.class);

                intent.putExtra("id", data.get(position).getId());
                intent.putExtra("nama", data.get(position).getNama());
                intent.putExtra("harga", data.get(position).getHarga());
                intent.putExtra("kategori", data.get(position).getKategori());
                intent.putExtra("deskripsi", data.get(position).getDeskripsi());
                intent.putExtra("gambar", data.get(position).getGambar());

                startActivityForResult(intent, 1);
    }

    @Override
    public void onClickHapusSimpan(View view, int position) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apa Yakin Menghapus?")
                .setConfirmText("Ya, Hapus!")
                .setCancelText("Batal")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        String hid_produk = data.get(position).getId();
                        String hid_akun = id_akun.getText().toString();

                        HapusSimpan(hid_produk, hid_akun);
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

    private void HapusSimpan(String hid_produk, String hid_akun) {

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.252.194:8000/api/hapussimpan",
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
                params.put("id_produk", hid_produk);
                params.put("id_akun", hid_akun);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}