package com.shedenk.app.transaksiactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shedenk.app.R;
import com.shedenk.app.RecyclerViewListener;
import com.shedenk.app.produk.ProdukItemModel;

import java.util.ArrayList;

public class AdapterDetailTransaksi extends RecyclerView.Adapter<AdapterDetailTransaksi.ViewHolder> {
    private
    ArrayList<ProdukItemModel> dataItem;
    RecyclerViewListener recyclerVIewListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textId;
        TextView textNama;
        TextView textHarga;
        TextView textDeskripsi;
        TextView textUkuran;
        TextView textKategori;
        ImageView imageProduk;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textId = itemView.findViewById(R.id.id_produk_detailtransaksi);
            textNama = itemView.findViewById(R.id.nama_produk_detailtransaksi);
            textHarga = itemView.findViewById(R.id.harga_produk_detailtransaksi);
            textKategori = itemView.findViewById(R.id.kategori_produk_detailtransaksi);
            textDeskripsi = itemView.findViewById(R.id.deskripsi_produk_detailtransaksi);
            imageProduk = itemView.findViewById(R.id.image_produk_detailtransaksi);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerVIewListener.onClickItem(view, getAdapterPosition());
        }
    }

    AdapterDetailTransaksi(ArrayList<ProdukItemModel> data, RecyclerViewListener listener){
        this.recyclerVIewListener = listener;
        this.dataItem = data;
    }

    @NonNull
    @Override
    public AdapterDetailTransaksi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.katalog_detailtransaksi,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDetailTransaksi.ViewHolder holder, int position) {

        ProdukItemModel produkItemModel = dataItem.get(position);

        holder.textId.setText(produkItemModel.getId());
        holder.textNama.setText(produkItemModel.getNama());
        holder.textHarga.setText("Rp. " +produkItemModel.getHarga());
        holder.textKategori.setText(produkItemModel.getKategori());
        holder.textDeskripsi.setText(produkItemModel.getDeskripsi());
        Glide.with(context).load(produkItemModel.getGambar()).apply(new RequestOptions().centerCrop()).into(holder.imageProduk);

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }
}
