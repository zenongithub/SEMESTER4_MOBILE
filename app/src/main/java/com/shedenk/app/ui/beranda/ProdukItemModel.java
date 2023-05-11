package com.shedenk.app.ui.beranda;

public class ProdukItemModel {

    String id, nama, harga, deskripsi, ukuran;
    int gambar;

    public ProdukItemModel(String id, String nama, String harga, String deskripsi, String ukuran, int gambar) {
        this.nama = nama;
        this.harga = harga;
        this.gambar = gambar;
        this.id = id;
        this.deskripsi = deskripsi;
        this.ukuran = ukuran;
    }
    public String getId() { return id; }
    public String getNama() {
        return nama;
    }

    public String getHarga() {
        return harga;
    }


    public String getDeskripsi() {
        return deskripsi;
    }

    public String getUkuran() {
        return ukuran;
    }

    public int getGambar() {
        return gambar;
    }


}
