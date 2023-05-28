package com.shedenk.app.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

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
import com.shedenk.app.HomeActivity;
import com.shedenk.app.LoginActivity;
import com.shedenk.app.R;
import com.shedenk.app.RegisterActivity;
import com.shedenk.app.SessionManager;
import com.shedenk.app.VolleyConnection;
import com.shedenk.app.databinding.FragmentKeranjangBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailProdukKeranjang extends AppCompatActivity {

    TextView id_akunkeranjang;
    TextView id_produkkeranjang;
    TextView namakeranjang;
    TextView hargakeranjang;
    TextView deskripsikeranjang;
    TextView kategorikeranjang;
    TextView ukurankeranjang;
    ImageView gambarkeranjang;
    Button btn_hapuskeranjang;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk_keranjang);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        id_akunkeranjang = findViewById(R.id.id_akun_detail_produkkeranjang);
        id_produkkeranjang = findViewById(R.id.id_detail_produkkeranjang);
        namakeranjang = findViewById(R.id.nama_detail_produkkeranjang);
        hargakeranjang = findViewById(R.id.harga_detail_produkkeranjang);
        kategorikeranjang = findViewById(R.id.kategori_detail_produkkeranjang);
        deskripsikeranjang = findViewById(R.id.deskripsi_detail_produkkeranjang);
//        ukurankeranjang = findViewById(R.id.ukuran_detail_produkkeranjang);
        gambarkeranjang = findViewById(R.id.image_detail_produkkeranjang);
        btn_hapuskeranjang = findViewById(R.id.btn_hapus_keranjang);

        id_produkkeranjang.setText(getIntent().getExtras().getString("id"));
        namakeranjang.setText(getIntent().getExtras().getString("nama"));
        hargakeranjang.setText("Rp. "+getIntent().getExtras().getString("harga"));
        kategorikeranjang.setText("Kategori : " +getIntent().getExtras().getString("kategori"));
        deskripsikeranjang.setText(getIntent().getExtras().getString("deskripsi"));
//        ukurankeranjang.setText("Ukuran : "+getIntent().getExtras().getString("ukuran"));
        int image = getIntent().getIntExtra("gambar",0);
        gambarkeranjang.setImageResource(image);

        HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);
        id_akunkeranjang.setText(sid);

        btn_hapuskeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hid_akun = id_akunkeranjang.getText().toString();
                String hid_produk = id_produkkeranjang.getText().toString();

                HapusKeranjang(hid_akun, hid_produk );
                Intent inten = new Intent();
                setResult(RESULT_OK, inten);
                finish();
            }
        });
    }

    private void HapusKeranjang(String hid_akun, String hid_produk) {
        if (checkNetworkConnection()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.252.194:8000/api/hapuskeranjang",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("pesan");
                                if (resp.equals("Berhasil Menghapus")) {
                                    Toast.makeText(getApplicationContext(), "Berhasil Menghapus", Toast.LENGTH_SHORT).show();

//                                    Intent intent = new Intent(DetailProdukKeranjang.this, FragmentKeranjangBinding.class);
//                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal Menghapus", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Gagal Menghapus Data", Toast.LENGTH_SHORT).show();
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

            VolleyConnection.getInstance(DetailProdukKeranjang.this).addToRequestQue(stringRequest);

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

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}