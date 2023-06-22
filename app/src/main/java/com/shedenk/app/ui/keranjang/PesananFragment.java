package com.shedenk.app.ui.keranjang;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shedenk.app.Env;
import com.shedenk.app.MultipartRequest;
import com.shedenk.app.R;
import com.shedenk.app.RecyclerViewListener;
import com.shedenk.app.SessionManager;
import com.shedenk.app.databinding.FragmentTransaksiBinding;
import com.shedenk.app.pesanan.DetailPesanan;
import com.shedenk.app.pesanan.PembayaranModel;
import com.shedenk.app.pesanan.PesananModel;
import com.shedenk.app.produk.DetailProduk;
import com.shedenk.app.produk.DetailProdukSimpan;
import com.shedenk.app.produk.ProdukItemModel;
import com.shedenk.app.transaksiactivity.DetailTransaksi;
import com.shedenk.app.transaksiactivity.TransaksiModel;
import com.shedenk.app.ui.simpan.AdapterProdukSimpan;
import com.shedenk.app.ui.simpan.SimpanFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PesananFragment extends Fragment implements RecyclerViewListener {

    TextView id_akun;
    SessionManager sessionManager;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Context context;
    AdapterPesanan adapterPesanan;
    ArrayList<PesananModel> data;
    ArrayList<PembayaranModel> data2;
    TextView idpesanan, total_hargapesanan;
    Button pembayaran;
    ImageView viewPembayaran;

    private void loadData(Context context, String id){
        data = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Env.BASE_URL + "dataantrian";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(container.getContext(), "Berhasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    JSONArray jo = new JSONArray(response);
                    JSONObject object;

                    for (int i =0; i < jo.length(); i++){

                        object = jo.getJSONObject(i);
                        data.add(new PesananModel("", object.getString("id_antrian"), object.getString("tgl_transaksi"), object.getString("total_barang"),object.getString("total_harga")));
                    }

                    layoutManager = new GridLayoutManager(getActivity(),1);
                    recyclerView.setLayoutManager(layoutManager);

                    adapterPesanan = new AdapterPesanan(data, PesananFragment.this);
                    recyclerView.setAdapter(adapterPesanan);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Gagal Mengambil Data" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_akun", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesanan,container,false);

        context = container.getContext();
        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();

        id_akun = view.findViewById(R.id.id_akun_pesanan);

        HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);

        id_akun.setText(sid);
        recyclerView = view.findViewById(R.id.recycler_pesanan);
        recyclerView.setHasFixedSize(true);

        loadData(container.getContext(), sid);
        return view;
    }

    @Override
    public void onClickItem(View view, int position) {
        String hid_pesanan = data.get(position).getId_pesanan();
        String htotal_harga = data.get(position).getTotal_harga();

        showDialog(hid_pesanan, htotal_harga);
    }

    private void showDialog(String id_pesanan, String total_harga) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_detail_pesanan);

        idpesanan = dialog.findViewById(R.id.id_detailpesanan);
        total_hargapesanan = dialog.findViewById(R.id.totalharga_detailpesanan);
        pembayaran = dialog.findViewById(R.id.btn_pembayaran);
        viewPembayaran = dialog.findViewById(R.id.view_pembayaran);

        idpesanan.setText(id_pesanan);
        total_hargapesanan.setText("Total Harga : Rp. " +total_harga);

        LoadGambar(id_pesanan);

//        Glide.with(context).load(data2.get(position).getGambar()).into(viewPembayaran);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
        }
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void LoadGambar(String id) {
        data2 = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);

        String url = Env.BASE_URL + "gambarpembayaran";
        StringRequest stringRequest = new StringRequest(

                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jo = new JSONArray(response);
                    JSONObject obj;

                    for (int i = 0; i < jo.length(); i++){
                        obj = jo.getJSONObject(i);
                        data2.add(new PembayaranModel(Env.IMAGE_URL + obj.getString("nama_pembayaran")));
                        Glide.with(context).load(Env.IMAGE_URL + obj.getString("nama_pembayaran")).into(viewPembayaran);
                    }
                    queue.getCache().clear();
                } catch (JSONException e) {
                    Toast.makeText(context,"Gagal " + e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Gagal Mengambil Data" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_antrian", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

                viewPembayaran.setImageBitmap(bitmap);

                uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        final String tags = idpesanan.getText().toString().trim();

        String url = Env.BASE_URL + "tambahpembayaran";
        MultipartRequest volleyMultipartRequest = new MultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            String resp = jsonObject.getString("success");
                            if (resp.equals("Berhasil Menambahkan")) {
                                Toast.makeText(context, "Berhasil Melakukan Pembayaran", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Gagal Melakukan Pembayaran", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_antrian", tags);
                return params;
            }

            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("gambar", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }

    @Override
    public void onClickHapusSimpan(View view, int position) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apa Yakin Menghapus Pesanan?")
                .setConfirmText("Ya, Hapus!")
                .setCancelText("Batal")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        String hid_pesanan = data.get(position).getId_pesanan();
                        String hid_akun = id_akun.getText().toString();

                        HapusPesanan(hid_pesanan, hid_akun);
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void HapusPesanan(String hid_pesanan, String hid_akun) {
        String url = Env.BASE_URL + "hapusantrian";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("pesan");
                            if (resp.equals("Berhasil Menghapus")) {
                                Toast.makeText(getActivity(), "Berhasil Menghapus", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Gagal Menghapus", Toast.LENGTH_SHORT).show();
                            }
                            HashMap<String, String> user = sessionManager.getUserDetail();
                            String sid = user.get(sessionManager.ID);
                            loadData(context, sid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Gagal Menghapus Data", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_antrian", hid_pesanan);
                params.put("id_akun", hid_akun);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}