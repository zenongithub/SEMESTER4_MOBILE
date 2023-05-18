package com.shedenk.app.ui.simpan;

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
import com.shedenk.app.SessionManager;
import com.shedenk.app.databinding.FragmentSimpanBinding;
import com.shedenk.app.produk.ProdukItemModel;
import com.shedenk.app.ui.keranjang.AdapterProdukKeranjang;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SimpanFragment extends Fragment {

    TextView id_akun;

    SessionManager sessionManager;

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
                        data.add(new ProdukItemModel(object.getString("id_produk"), object.getString("nama_produk"), object.getString("harga"),(object.getString("nama_kategori")),object.getString("deskripsi"),object.getString("ukuran"), "https://plus.unsplash.com/premium_photo-1666264200754-1a2d5f2f6695?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"));
                    }

                    layoutManager = new GridLayoutManager(getActivity(),1);
                    recyclerView.setLayoutManager(layoutManager);

                    adapterProdukSimpan = new AdapterProdukSimpan(data);
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
}