package com.shedenk.app.ui.transaksi;

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
import com.shedenk.app.produk.DetailProduk;
import com.shedenk.app.transaksiactivity.DetailTransaksi;
import com.shedenk.app.transaksiactivity.TransaksiModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransaksiFragment extends Fragment implements RecyclerViewListener {

    TextView id_akun;
    SessionManager sessionManager;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    AdapterTransaksi adapterTransaksi;
    ArrayList<TransaksiModel> data;
    private FragmentTransaksiBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi,container,false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();

        id_akun = view.findViewById(R.id.id_akun_riwayattransaksi);

        HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);

        id_akun.setText(sid);

        recyclerView = view.findViewById(R.id.recycler_view_riwayattransaksi);
        recyclerView.setHasFixedSize(true);

        data = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(container.getContext());

        String url = Env.BASE_URL + "datatransaksi";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(container.getContext(), "Berhasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    JSONArray jo = new JSONArray(response);
                    JSONObject object;

                    for (int i =0; i < jo.length(); i++){

                        object = jo.getJSONObject(i);
                        data.add(new TransaksiModel("", object.getString("id_transaksi"), object.getString("tgl_transaksi"), object.getString("jumlah_pesanan"),object.getString("total_harga")));

                    }

                    layoutManager = new GridLayoutManager(getActivity(),1);
                    recyclerView.setLayoutManager(layoutManager);

                    adapterTransaksi = new AdapterTransaksi(data, TransaksiFragment.this);
                    recyclerView.setAdapter(adapterTransaksi);

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
        Intent intent = new Intent(view.getContext(), DetailTransaksi.class);

        intent.putExtra("id_transaksi", data.get(position).getId_transaksi());
        intent.putExtra("tgl_transaksi", data.get(position).getTanggal());
        intent.putExtra("total_hargatransaksi", data.get(position).getTotal_harga());
        intent.putExtra("jumlah_produk", data.get(position).getTotal_produk());
//        intent.putExtra("status_transaksi", data.get(position).getStatus_transaksi());
        startActivity(intent);
    }

    @Override
    public void onClickHapusSimpan(View view, int position) {

    }
}