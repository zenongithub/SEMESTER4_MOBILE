package com.shedenk.app.ui.beranda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.shedenk.app.R;

public class DetailProduk extends AppCompatActivity {

    TextView nama;
    TextView harga;
    TextView deskripsi;
    TextView ukuran;
    ImageView gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        nama = findViewById(R.id.nama_detail_produk);
        harga = findViewById(R.id.harga_detail_produk);
        deskripsi = findViewById(R.id.deskripsi_detail_produk);
        ukuran = findViewById(R.id.ukuran_detail_produk);
        gambar = findViewById(R.id.image_detail_produk);

        nama.setText(getIntent().getExtras().getString("nama"));
        harga.setText(getIntent().getExtras().getString("harga"));
        deskripsi.setText(getIntent().getExtras().getString("deskripsi"));
        ukuran.setText(getIntent().getExtras().getString("ukuran"));
        int image = getIntent().getIntExtra("gambar",0);
        gambar.setImageResource(image);

    }
}