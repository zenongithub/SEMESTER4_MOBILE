package com.shedenk.app.ui.keranjang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shedenk.app.R;
import com.shedenk.app.RecyclerViewListener;
import com.shedenk.app.pesanan.PesananModel;
import com.shedenk.app.transaksiactivity.TransaksiModel;

import java.util.ArrayList;

public class AdapterPesanan extends RecyclerView.Adapter<AdapterPesanan.ViewHolder> {

    ArrayList<PesananModel> dataItem;
    Context context;
    RecyclerViewListener recyclerVIewListener;
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textid;
        TextView textTgl;
        TextView textTotal_barang;
        TextView textTotal_harga;
        Button btn_hapuspesanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textid = itemView.findViewById(R.id.id_pesanan);
            textTgl = itemView.findViewById(R.id.tanggal_pesanan);
            textTotal_barang = itemView.findViewById(R.id.Jumlah_Produkpesanan);
            textTotal_harga = itemView.findViewById(R.id.Total_Hargapesanan);
            btn_hapuspesanan = itemView.findViewById(R.id.btn_hapus_pesanan);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerVIewListener.onClickItem(view, getAdapterPosition());
                }
            });
            btn_hapuspesanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerVIewListener.onClickHapusSimpan(btn_hapuspesanan, getAdapterPosition());
                }
            });
        }
    }

    AdapterPesanan(ArrayList<PesananModel> data, RecyclerViewListener listener){
        this.recyclerVIewListener = listener;
        this.dataItem = data;
    }

    @Override
    public AdapterPesanan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pesanan,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPesanan.ViewHolder holder, int position) {

        PesananModel pesananModel = dataItem.get(position);

        holder.textid.setText("No Pesanan :  "+pesananModel.getId_pesanan());
        holder.textTgl.setText("Tanggal Transaksi : " +pesananModel.getTanggal());
        holder.textTotal_barang.setText("Jumlah Pesanan : " +pesananModel.getTotal_produk());
        holder.textTotal_harga.setText("Total Harga : " +"Rp. "+pesananModel.getTotal_harga());
    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }
}
