package com.shedenk.app.ui.beranda;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.shedenk.app.R;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {

    private
    ArrayList<ProdukItemModel> dataItem;

    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

//        TextView textId;
        TextView textNama;
        TextView textHarga;
        TextView textDeskripsi;
        TextView textUkuran;
        ImageView imageProduk;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textNama = itemView.findViewById(R.id.nama_produk);
            textHarga = itemView.findViewById(R.id.harga_produk);
            textDeskripsi = itemView.findViewById(R.id.deskripsi_produk);
            textUkuran = itemView.findViewById(R.id.ukuran_produk);
            imageProduk = itemView.findViewById(R.id.image_produk);
        }
    }

    AdapterRecyclerView(ArrayList<ProdukItemModel> data){
        this.dataItem = data;
    }

    @NonNull
    @Override
    public AdapterRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.katalog_produk,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerView.ViewHolder holder, int position) {

        ProdukItemModel produkItemModel = dataItem.get(position);
//        TextView text_id = holder.textId;
//        TextView text_nama = holder.textNama;
//        TextView text_deskripsi = holder.textDeskripsi;
//        TextView text_ukuran = holder.textUkuran;
//        TextView text_harga = holder.textHarga;
//        ImageView image_produk = holder.imageProduk;
//
////        text_id.setText(dataItem.get(position).getId());
//        text_nama.setText(dataItem.get(position).getNama());
//        text_deskripsi.setText(dataItem.get(position).getDeskripsi());
//        text_ukuran.setText(dataItem.get(position).getUkuran());
//        text_harga.setText(dataItem.get(position).getHarga());
//        image_produk.setImageResource(dataItem.get(position).getGambar());

        holder.textNama.setText(produkItemModel.getNama());
        holder.textHarga.setText(produkItemModel.getHarga());
        holder.textDeskripsi.setText(produkItemModel.getDeskripsi());
        holder.textUkuran.setText(produkItemModel.getUkuran());
//      holder.imageProduk.setImageResource(produkItemModel.getGambar());
        Glide.with(context)
                        .load(produkItemModel.getGambar()).apply(new RequestOptions().centerCrop()).into(holder.imageProduk);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),DetailProduk.class);
                intent.putExtra("nama", produkItemModel.getNama());
                intent.putExtra("harga", produkItemModel.getHarga());
                intent.putExtra("deskripsi", produkItemModel.getDeskripsi());
                intent.putExtra("ukuran", produkItemModel.getUkuran());
                intent.putExtra("gambar", produkItemModel.getGambar());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }
}
