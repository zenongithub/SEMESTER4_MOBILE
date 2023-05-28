package com.shedenk.app.produk;

public class ProdukItemModel {

    String id, kategori, nama, harga, deskripsi, gambar, id_akun;

    public ProdukItemModel(String id, String nama, String harga, String kategori, String deskripsi, String gambar, String id_akun) {
        this.nama = nama;
        this.harga = harga;
        this.gambar = gambar;
        this.id = id;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.id_akun = id_akun;
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
    public String getGambar() {
        return gambar;
    }

    public String getKategori() {
        return kategori;
    }

    public String getId_akun() {
        return id_akun;
    }


}

