package com.shedenk.app.transaksiactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.shedenk.app.produk.ProdukItemModel;
import com.shedenk.app.ui.keranjang.AdapterProdukKeranjang;
import com.shedenk.app.ui.simpan.AdapterProdukSimpan;
import com.shedenk.app.ui.simpan.SimpanFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailTransaksi extends AppCompatActivity implements RecyclerViewListener {

    TextView id_transaksi, total_harga, tgl_transaksi, status;
    AdapterDetailTransaksi adapterDetailTransaksi;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Context context;
    ArrayList<ProdukItemModel> data;

    private void loadData(String id_transaksi){
        data = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Env.BASE_URL + "datadetailtransaksi";
        StringRequest stringRequest = new StringRequest(

                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(getApplicationContext(), "Berhasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    JSONArray jo = new JSONArray(response);
                    JSONObject object;
                    JSONObject objectgambar;

                    for (int i =0; i < jo.length(); i++){
                        object = jo.getJSONObject(i);
                        JSONObject produk = new JSONObject(object.getString("produk"));
                        JSONObject kategori = new JSONObject(produk.getString("kategori"));
                        JSONArray gam = new JSONArray(produk.getString("gambar"));
                        for (int a = 0; a < gam.length(); a++){
                            objectgambar = gam.getJSONObject(a);
                            data.add(new ProdukItemModel(produk.getString("id_produk"), produk.getString("nama_produk"), produk.getString("harga"),(kategori.getString("nama_kategori")),produk.getString("deskripsi"), Env.IMAGE_URL + objectgambar.getString("nama_gambar"),""));
                        }
                    }

                    layoutManager = new GridLayoutManager(getApplicationContext(),1);
                    recyclerView.setLayoutManager(layoutManager);

                    adapterDetailTransaksi = new AdapterDetailTransaksi(data, DetailTransaksi.this);
                    recyclerView.setAdapter(adapterDetailTransaksi);

                    queue.getCache().clear();

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
                params.put("id_transaksi", id_transaksi);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);


        id_transaksi = findViewById(R.id.id_detailtransaksi);
        total_harga = findViewById(R.id.total_harga_detailtransaksi);
        tgl_transaksi = findViewById(R.id.tgl_detailtransaksi);
        status = findViewById(R.id.status_detailtransaksi);


        recyclerView = findViewById(R.id.recycler_view_detailtransaksi);
        recyclerView.setHasFixedSize(true);


        id_transaksi.setText(getIntent().getExtras().getString("id_transaksi"));
        total_harga.setText(getIntent().getExtras().getString("total_hargatransaksi"));
        tgl_transaksi.setText(getIntent().getExtras().getString("tgl_transaksi"));
//        status.setText(getIntent().getExtras().getString("status_transaksi"));

        loadData(id_transaksi.getText().toString());
    }

    @Override
    public void onClickItem(View view, int position) {

    }

    @Override
    public void onClickHapusSimpan(View view, int position) {

    }
}