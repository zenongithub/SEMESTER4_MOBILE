package com.shedenk.app.ui.beranda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shedenk.app.R;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {

    ArrayList<ProdukItemModel> dataItem;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textNama;
        TextView textHarga;
        ImageView imageProduk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textNama = itemView.findViewById(R.id.nama_produk);
            textHarga = itemView.findViewById(R.id.harga_produk);
            imageProduk = itemView.findViewById(R.id.image_produk);
        }
    }

    AdapterRecyclerView(ArrayList<ProdukItemModel> data){
        this.dataItem = data;
    }

    @NonNull
    @Override
    public AdapterRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.katalog_produk,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerView.ViewHolder holder, int position) {

        TextView text_nama = holder.textNama;
        TextView text_harga = holder.textHarga;
        ImageView image_produk = holder.imageProduk;

        text_nama.setText(dataItem.get(position).getNama());
        text_harga.setText(dataItem.get(position).getHarga());
        image_produk.setImageResource(dataItem.get(position).getProduk());

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }
}
