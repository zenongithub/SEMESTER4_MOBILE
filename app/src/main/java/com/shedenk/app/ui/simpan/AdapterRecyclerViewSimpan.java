package com.shedenk.app.ui.simpan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shedenk.app.R;
import com.shedenk.app.ui.simpan.ProdukItemSimpanModel;

import java.util.ArrayList;

public class AdapterRecyclerViewSimpan extends RecyclerView.Adapter<AdapterRecyclerViewSimpan.ViewHolder> {

    ArrayList<ProdukItemSimpanModel> dataItem2;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textNama;
        TextView textHarga;

        TextView textUkuran;
        ImageView imageProduk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textNama = itemView.findViewById(R.id.nama_produk_simpan);
            textUkuran = itemView.findViewById(R.id.ukuran_simpan_produk);
            textHarga = itemView.findViewById(R.id.harga_produk_simpan);
            imageProduk = itemView.findViewById(R.id.image_produk_simpan);
        }
    }

    AdapterRecyclerViewSimpan(ArrayList<ProdukItemSimpanModel> data2){
        this.dataItem2 = data2;
    }

    @NonNull
    @Override
    public AdapterRecyclerViewSimpan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.katalog_simpan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerViewSimpan.ViewHolder holder, int position) {

        TextView text_nama = holder.textNama;
        TextView text_harga = holder.textHarga;
        TextView text_ukuran = holder.textUkuran;
        ImageView image_produk = holder.imageProduk;


        text_ukuran.setText(dataItem2.get(position).getUkuran());
        text_nama.setText(dataItem2.get(position).getNama());
        text_harga.setText(dataItem2.get(position).getHarga());
        image_produk.setImageResource(dataItem2.get(position).getProduk());

    }

    @Override
    public int getItemCount() {
        return dataItem2.size();
    }
}
