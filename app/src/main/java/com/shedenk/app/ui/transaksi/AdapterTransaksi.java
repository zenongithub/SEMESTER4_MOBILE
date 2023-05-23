package com.shedenk.app.ui.transaksi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shedenk.app.R;
import com.shedenk.app.RecyclerViewListener;
import com.shedenk.app.transaksiactivity.TransaksiModel;

import java.util.ArrayList;

public class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.ViewHolder> {

    ArrayList<TransaksiModel> dataItem;
    Context context;
    RecyclerViewListener recyclerVIewListener;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textid;
        TextView textTgl;
        TextView textTotal_barang;
        TextView textTotal_harga;
        TextView status_transaksi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textid = itemView.findViewById(R.id.id_transaksi);
            textTgl = itemView.findViewById(R.id.tanggal_transaksi);
            textTotal_barang = itemView.findViewById(R.id.Jumlah_Produktransaksi);
            textTotal_harga = itemView.findViewById(R.id.Total_Hargatransaksi);
            status_transaksi = itemView.findViewById(R.id.status_transaksi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerVIewListener.onClickItem(view, getAdapterPosition());
            recyclerVIewListener.onClickHapusSimpan(view, getAdapterPosition());
        }
    }

    AdapterTransaksi(ArrayList<TransaksiModel> data, RecyclerViewListener listener){
        this.recyclerVIewListener = listener;
        this.dataItem = data;
    }

    @NonNull
    @Override
    public AdapterTransaksi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.riwayat_transaksi,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTransaksi.ViewHolder holder, int position) {

        TransaksiModel transaksiModel = dataItem.get(position);

        holder.textid.setText("No Transaksi :  "+transaksiModel.getId_transaksi());
        holder.textTgl.setText("Tanggal Transaksi : " +transaksiModel.getTanggal());
        holder.textTotal_barang.setText("Total produk : " +transaksiModel.getTotal_produk());
        holder.textTotal_harga.setText("Total Harga : " +"Rp. "+transaksiModel.getTotal_harga());
        holder.status_transaksi.setText("Status : " +transaksiModel.getStatus_transaksi());
    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }
}
