package com.shedenk.app.pesanan;

public class PesananModel {
    String id_akun, id_pesanan, tanggal, total_produk, total_harga;

    public PesananModel(String id_akun, String id_pesanan, String tanggal, String total_produk, String total_harga) {
        this.id_akun = id_akun;
        this.id_pesanan= id_pesanan;
        this.tanggal = tanggal;
        this.total_produk = total_produk;
        this.total_harga = total_harga;

    }
    public String getId_akun() {
        return id_akun;
    }

    public String getId_pesanan() {
        return id_pesanan;
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
}
