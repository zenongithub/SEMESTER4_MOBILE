package com.shedenk.app.produk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shedenk.app.R;
import com.shedenk.app.SessionManager;
import com.shedenk.app.VolleyConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailProdukSimpan extends AppCompatActivity {

    TextView id_produksimpan;
    TextView id_akunsimpan;
    TextView namasimpan;
    TextView hargasimpan;
    TextView deskripsisimpan;
    TextView kategorisimpan;
    TextView ukuransimpan;
    ImageView gambarsimpan;
    Button tambah_keranjangsimpan;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk_simpan);

            sessionManager = new SessionManager(this);
            sessionManager.checkLogin();

            tambah_keranjangsimpan = findViewById(R.id.btn_tambah_keranjangsimpan);
            id_produksimpan = findViewById(R.id.id_detail_produksimpan);
            id_akunsimpan = findViewById(R.id.id_akun_detail_produksimpan);
            namasimpan = findViewById(R.id.nama_detail_produksimpan);
            hargasimpan = findViewById(R.id.harga_detail_produksimpan);
            kategorisimpan = findViewById(R.id.kategori_detail_produksimpan);
            deskripsisimpan = findViewById(R.id.deskripsi_detail_produksimpan);
//            ukuransimpan = findViewById(R.id.ukuran_detail_produksimpan);
            gambarsimpan = findViewById(R.id.image_detail_produksimpan);

            id_produksimpan.setText(getIntent().getExtras().getString("id"));
            namasimpan.setText(getIntent().getExtras().getString("nama"));
            hargasimpan.setText("Rp. "+getIntent().getExtras().getString("harga"));
            kategorisimpan.setText("Kategori : " +getIntent().getExtras().getString("kategori"));
            deskripsisimpan.setText(getIntent().getExtras().getString("deskripsi"));
//            ukuransimpan.setText("Ukuran : "+getIntent().getExtras().getString("ukuran"));
            String image = getIntent().getStringExtra("gambar");
            Glide.with(DetailProdukSimpan.this).load(image).apply(new RequestOptions().centerCrop()).into(gambarsimpan);

            HashMap<String,String> user = sessionManager.getUserDetail();
            String sid = user.get(sessionManager.ID);
            id_akunsimpan.setText(sid);

            tambah_keranjangsimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String Kid_akun = id_akunsimpan.getText().toString();
                    String Kid_produk = id_produksimpan.getText().toString();

                    TambahKeranjang(Kid_akun, Kid_produk );
                }
            });
    }

    private void TambahKeranjang(String kid_akun, String kid_produk) {
        if (checkNetworkConnection()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.252.194:8000/api/tambahkeranjang",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                if (resp.equals("Berhasil Menambahkan")) {
//                                    Toast.makeText(getApplicationContext(), "Berhasil Menambahkan Keranjang", Toast.LENGTH_SHORT).show();
                                    new SweetAlertDialog(DetailProdukSimpan.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Berhasil Menambahkan Keranjang")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    finish();
                                                    sweetAlertDialog.dismiss();
                                                }
                                            }).show();
                                } else {
//                                    Toast.makeText(getApplicationContext(), "Gagal Menambahkan Keranjang", Toast.LENGTH_SHORT).show();
                                    new SweetAlertDialog(DetailProdukSimpan.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Produk Sudah Ditambahkan!")
                                            .show();
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
                    params.put("id_akun", kid_akun);
                    params.put("id_produk", kid_produk);
                    return params;
                }
            };

            VolleyConnection.getInstance(DetailProdukSimpan.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
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