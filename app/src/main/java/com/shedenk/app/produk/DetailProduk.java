package com.shedenk.app.produk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shedenk.app.LoginActivity;
import com.shedenk.app.R;
import com.shedenk.app.RegisterActivity;
import com.shedenk.app.SessionManager;
import com.shedenk.app.VolleyConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailProduk extends AppCompatActivity {

    TextView id_produk;
    TextView id_akun;
    TextView nama;
    TextView harga;
    TextView deskripsi;
    TextView kategori;
    TextView ukuran;
    ImageView gambar;
    Button btn_simpan;
    Button btn_keranjang;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        btn_simpan = findViewById(R.id.btn_simpan);
        btn_keranjang = findViewById(R.id.btn_keranjang);
        id_akun = findViewById(R.id.id_akun_detail_produk);
        id_produk = findViewById(R.id.id_detail_produk);
        nama = findViewById(R.id.nama_detail_produk);
        harga = findViewById(R.id.harga_detail_produk);
        kategori = findViewById(R.id.kategori_detail_produk);
        deskripsi = findViewById(R.id.deskripsi_detail_produk);
        ukuran = findViewById(R.id.ukuran_detail_produk);
        gambar = findViewById(R.id.image_detail_produk);

        id_produk.setText(getIntent().getExtras().getString("id"));
        nama.setText(getIntent().getExtras().getString("nama"));
        harga.setText("Rp. "+getIntent().getExtras().getString("harga"));
        kategori.setText("Kategori : " +getIntent().getExtras().getString("kategori"));
        deskripsi.setText(getIntent().getExtras().getString("deskripsi"));
        ukuran.setText("Ukuran : "+getIntent().getExtras().getString("ukuran"));
        int image = getIntent().getIntExtra("gambar",0);
        gambar.setImageResource(image);

        HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);
        id_akun.setText(sid);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Sid_akun = id_akun.getText().toString();
                String Sid_produk = id_produk.getText().toString();

                TambahSimpan(Sid_akun, Sid_produk );
            }
        });

        btn_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Kid_akun = id_akun.getText().toString();
                String Kid_produk = id_produk.getText().toString();

                TambahKeranjang(Kid_akun, Kid_produk );
            }
        });
    }
    private void TambahSimpan(String sid_akun, String sid_produk) {
        if (checkNetworkConnection()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.86.194:8000/api/tambahsimpan",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                if (resp.equals("Berhasil Menambahkan")) {
                                    Toast.makeText(getApplicationContext(), "Berhasil Menambahkan Simpan", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal Menambahkan Simpan", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Gagal Menyimpan Data", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_akun", sid_akun);
                    params.put("id_produk", sid_produk);
                    return params;
                }
            };

            VolleyConnection.getInstance(DetailProduk.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void TambahKeranjang(String id_akun, String id_produk) {

        if (checkNetworkConnection()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.86.194:8000/api/tambahkeranjang",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                if (resp.equals("Berhasil Menambahkan")) {
                                    Toast.makeText(getApplicationContext(), "Berhasil Menambahkan Keranjang", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal Menambahkan Keranjang", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Gagal Menyimpan Data", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_akun", id_akun);
                    params.put("id_produk", id_produk);
                    return params;
                }
            };

            VolleyConnection.getInstance(DetailProduk.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}