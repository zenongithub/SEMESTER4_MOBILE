package com.shedenk.app.transaksi;

public class TransaksiModel {

    String id_akun;
    String id_transaksi;
    String tanggal;
    String total_produk;
    String total_harga;
    String status_transaksi;


    public TransaksiModel(String id_akun, String id_transaksi, String tanggal, String total_produk, String total_harga, String status_transaksi) {
        this.id_akun = id_akun;
        this.id_transaksi = id_transaksi;
        this.tanggal = tanggal;
        this.total_produk = total_produk;
        this.total_harga = total_harga;
        this.status_transaksi = status_transaksi;
    }
    public String getId_akun() {
        return id_akun;
    }
    public String getId_transaksi() {
        return id_transaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getTotal_produk() {
        return total_produk;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public String getStatus_transaksi() {
        return status_transaksi;
    }

}
