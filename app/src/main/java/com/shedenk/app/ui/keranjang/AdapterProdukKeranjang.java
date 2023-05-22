package com.shedenk.app.ui.keranjang;

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

public class AdapterProdukKeranjang extends RecyclerView.Adapter<AdapterProdukKeranjang.ViewHolder> {

    private
    ArrayList<ProdukItemModel> dataItem;
    RecyclerViewListener recyclerVIewListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textIdakun;
        TextView textId;
        TextView textNama;
        TextView textHarga;
        TextView textDeskripsi;
        TextView textUkuran;
        TextView textKategori;
        ImageView imageProduk;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textIdakun = itemView.findViewById(R.id.idakun_keranjang);
            textId = itemView.findViewById(R.id.id_produk_keranjang);
            textNama = itemView.findViewById(R.id.nama_produk_keranjang);
            textHarga = itemView.findViewById(R.id.harga_produk_keranjang);
            textKategori = itemView.findViewById(R.id.kategori_produk_keranjang);
            textDeskripsi = itemView.findViewById(R.id.deskripsi_produk_keranjang);
            textUkuran = itemView.findViewById(R.id.ukuran_produk_keranjang);
            imageProduk = itemView.findViewById(R.id.image_produk_keranjang);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerVIewListener.onClickItem(view, getAdapterPosition());
        }
    }

    AdapterProdukKeranjang(ArrayList<ProdukItemModel> data, RecyclerViewListener listener){
        this.recyclerVIewListener = listener;
        this.dataItem = data;
    }

    @NonNull
    @Override
    public AdapterProdukKeranjang.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.katalog_keranjang,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdukKeranjang.ViewHolder holder, int position) {

        ProdukItemModel produkItemModel = dataItem.get(position);

        holder.textId.setText(produkItemModel.getId());
        holder.textNama.setText(produkItemModel.getNama());
        holder.textHarga.setText("Rp. " +produkItemModel.getHarga());
        holder.textKategori.setText("Kategori : " +produkItemModel.getKategori());
        holder.textDeskripsi.setText(produkItemModel.getDeskripsi());
        holder.textUkuran.setText("Ukuran : "+produkItemModel.getUkuran());
        Glide.with(context).load(produkItemModel.getGambar()).apply(new RequestOptions().centerCrop()).into(holder.imageProduk);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), DetailProdukKeranjang.class);
//
//                intent.putExtra("id", produkItemModel.getId());
//                intent.putExtra("nama", produkItemModel.getNama());
//                intent.putExtra("harga", produkItemModel.getHarga());
//                intent.putExtra("kategori", produkItemModel.getKategori());
//                intent.putExtra("deskripsi", produkItemModel.getDeskripsi());
//                intent.putExtra("ukuran", produkItemModel.getUkuran());
//                intent.putExtra("gambar", produkItemModel.getGambar());
//                view.getContext().startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }
}
