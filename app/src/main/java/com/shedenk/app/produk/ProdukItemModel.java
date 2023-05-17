package com.shedenk.app.produk;

public class ProdukItemModel {

    String id;

    String kategori;
    String nama;
    String harga;
    String deskripsi;
    String ukuran;
    String gambar;

    public ProdukItemModel(String id, String nama, String harga, String kategori, String deskripsi, String ukuran, String gambar) {
        this.nama = nama;
        this.harga = harga;
        this.gambar = gambar;
        this.id = id;
        this.deskripsi = deskripsi;
        this.ukuran = ukuran;
        this.kategori = kategori;
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

    public String getGambar() {
        return gambar;
    }

    public String getKategori() {
        return kategori;
    }


}
