package com.shedenk.app.ui.simpan;

public class ProdukItemSimpanModel {

    String nama, harga, ukuran;
    int produk;

    public ProdukItemSimpanModel(String nama, String harga, int produk, String ukuran) {
        this.nama = nama;
        this.harga = harga;
        this.produk = produk;
        this.ukuran = ukuran;
    }

    public String getNama() {
        return nama;
    }

    public String getHarga() {
        return harga;
    }

    public int getProduk() {
        return produk;
    }

    public String getUkuran() {
        return ukuran;
    }
}
