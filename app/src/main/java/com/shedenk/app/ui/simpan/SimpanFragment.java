package com.shedenk.app.ui.simpan;

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


public class SimpanFragment extends Fragment implements RecyclerViewListener {

    TextView id_akun;

    SessionManager sessionManager;
    ProdukItemModel produkItemModel;
    AdapterProdukSimpan adapterProdukSimpan;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ProdukItemModel> data;
    private FragmentSimpanBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simpan,container,false);


        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();

        id_akun = view.findViewById(R.id.id_akun_simpan);

        HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);
        id_akun.setText(sid);

        recyclerView = view.findViewById(R.id.recycler_view_simpan);
        recyclerView.setHasFixedSize(true);

        data = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(container.getContext());


        StringRequest stringRequest = new StringRequest(

                Request.Method.POST, "http://192.168.86.194:8000/api/datasimpan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(container.getContext(), "Berhasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    JSONArray jo = new JSONArray(response);
                    JSONObject object;

                    for (int i =0; i < jo.length(); i++){

                        object = jo.getJSONObject(i);
                        data.add(new ProdukItemModel(object.getString("id_produk"), object.getString("nama_produk"), object.getString("harga"),(object.getString("nama_kategori")),object.getString("deskripsi"),object.getString("ukuran"), "https://plus.unsplash.com/premium_photo-1666264200754-1a2d5f2f6695?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80", object.getString("id_akun")));
                    }

                    layoutManager = new GridLayoutManager(getActivity(),1);
                    recyclerView.setLayoutManager(layoutManager);

                    adapterProdukSimpan = new AdapterProdukSimpan(data, SimpanFragment.this);
                    recyclerView.setAdapter(adapterProdukSimpan);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(container.getContext(), "Gagal Mengambil Data" + error, Toast.LENGTH_SHORT).show();
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
                intent.putExtra("ukuran", data.get(position).getUkuran());
                intent.putExtra("gambar", data.get(position).getGambar());

                view.getContext().startActivity(intent);
    }

    @Override
    public void onClickHapusSimpan(View view, int position) {
                String hid_produk = data.get(position).getId();
                String hid_akun = data.get(position).getId_akun();

                HapusSimpan(hid_produk, hid_akun);
    }

    private void HapusSimpan(String hid_produk, String hid_akun) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.86.194:8000/api/hapussimpan",
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
                params.put("id_akun", hid_akun);
                params.put("id_produk", hid_produk);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}