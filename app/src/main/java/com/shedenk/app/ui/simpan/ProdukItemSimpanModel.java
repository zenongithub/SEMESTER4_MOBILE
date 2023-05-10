package com.shedenk.app.ui.simpan;

public class ProdukItemSimpanModel {

    String nama, harga;
    int produk;

    public ProdukItemSimpanModel(String nama, String harga, int produk) {
        this.nama = nama;
        this.harga = harga;
        this.produk = produk;
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
}
