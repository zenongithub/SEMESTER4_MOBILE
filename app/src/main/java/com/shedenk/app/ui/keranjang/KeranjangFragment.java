package com.shedenk.app.ui.keranjang;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shedenk.app.R;
import com.shedenk.app.SessionManager;
import com.shedenk.app.databinding.FragmentKeranjangBinding;
import com.shedenk.app.produk.ProdukItemModel;
import com.shedenk.app.ui.beranda.AdapterProdukBeranda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeranjangFragment extends Fragment {

    Button btn_pesan;
    TextView id_akun, email_akun, nama_akun;
    TextView total_harga;
    Bitmap bitmap, scaleBitmap;
    int pageWidth = 1200;
    Date dateTime;
    DateFormat dateFormat;
    SessionManager sessionManager;
    AdapterProdukKeranjang adapterProdukKeranjang;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ProdukItemModel> data;
    private FragmentKeranjangBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keranjang, container, false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();

        btn_pesan = view.findViewById(R.id.btncheckout);
        id_akun = view.findViewById(R.id.id_akun_keranjang);
        nama_akun = view.findViewById(R.id.nama_akun_keranjang);
        email_akun = view.findViewById(R.id.email_akun_keranjang);
        total_harga = view.findViewById(R.id.total_harga);

    HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);
        String snama = user.get(sessionManager.NAMA);
        String semail = user.get(sessionManager.EMAIL);

        nama_akun.setText(snama);
        email_akun.setText(semail);
        id_akun.setText(sid);

        recyclerView = view.findViewById(R.id.recycler_view_keranjang);
        recyclerView.setHasFixedSize(true);

        data = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(container.getContext());


        StringRequest stringRequest = new StringRequest(

                Request.Method.POST, "http://192.168.86.194:8000/api/datakeranjang", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(container.getContext(), "Berhasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    JSONArray jo = new JSONArray(response);
                    JSONObject object;

                    for (int i =0; i < jo.length(); i++){

                        object = jo.getJSONObject(i);
                        data.add(new ProdukItemModel(object.getString("id_produk"), object.getString("nama_produk"), object.getString("harga"),(object.getString("nama_kategori")),object.getString("deskripsi"),object.getString("ukuran"), "https://plus.unsplash.com/premium_photo-1666264200754-1a2d5f2f6695?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"));

                        int total = 0;
                        total_harga.setText(total + object.getString("harga"));
                    }

                    layoutManager = new GridLayoutManager(getActivity(),1);
                    recyclerView.setLayoutManager(layoutManager);

                    adapterProdukKeranjang = new AdapterProdukKeranjang(data);
                    recyclerView.setAdapter(adapterProdukKeranjang);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(container.getContext(), "Gagal Mengambil Data" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_akun", sid);
                return params;
            }
        };
        queue.add(stringRequest);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide2);
        scaleBitmap = Bitmap.createScaledBitmap(bitmap, 1200, 518, false);

        ActivityCompat.requestPermissions( getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        createInvoice();

        return view;
    }

    private void createInvoice() {
        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTime = new Date();

                //get input
                if (nama_akun.getText().toString().length() == 0 ||
                        email_akun.getText().toString().length() == 0)
//                        etJmlOne.getText().toString().length() == 0 ||
//                        etJmlTwo.getText().toString().length() == 0)
                        {
                    Toast.makeText(getActivity(), "Data tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else {

                    PdfDocument pdfDocument = new PdfDocument();
                    Paint paint = new Paint();
                    Paint titlePaint = new Paint();

                    PdfDocument.PageInfo pageInfo
                            = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                    Canvas canvas = page.getCanvas();
                    canvas.drawBitmap(scaleBitmap, 0, 0, paint);

//                    paint.setColor(Color.WHITE);
//                    paint.setTextSize(30f);
//                    paint.setTextAlign(Paint.Align.RIGHT);
//                    canvas.drawText("Berbagai macam jenis Baju Thrift", 1160, 40, paint);
//                    canvas.drawText("Pesan di : Shedenk Thrift Shop", 1160, 80, paint);
//
//                    titlePaint.setTextAlign(Paint.Align.CENTER);
//                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    titlePaint.setColor(Color.WHITE);
//                    titlePaint.setTextSize(70);
//                    canvas.drawText("Tagihan Anda", pageWidth / 2, 500, titlePaint);

                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(35f);
                    canvas.drawText("Nama Pemesan: " + nama_akun.getText(), 20, 590, paint);
                    canvas.drawText("Email: " + email_akun.getText(), 20, 640, paint);

                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("No. Pesanan: " + "TR0001", pageWidth - 20, 590, paint);

                    dateFormat = new SimpleDateFormat("dd/MM/yy");
                    canvas.drawText("Tanggal: " + dateFormat.format(dateTime), pageWidth - 20, 640, paint);

                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    canvas.drawText("Pukul: " + dateFormat.format(dateTime), pageWidth - 20, 690, paint);

                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(2);
                    canvas.drawRect(20, 780, pageWidth - 20, 860, paint);

                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawText("No.", 40, 830, paint);
                    canvas.drawText("Menu Pesanan", 200, 830, paint);
                    canvas.drawText("Harga", 980, 830, paint);
//                    canvas.drawText("Jumlah", 900, 830, paint);
//                    canvas.drawText("Total", 1050, 830, paint);

                    canvas.drawLine(180, 790, 180, 840, paint);
//                    canvas.drawLine(680, 790, 680, 840, paint);
                    canvas.drawLine(880, 790, 880, 840, paint);
//                    canvas.drawLine(1030, 790, 1030, 840, paint);

                    float totalOne = 0, totalTwo = 0;
//                    if (itemSpinnerOne.getSelectedItemPosition() != 0) {
//                        canvas.drawText("1.", 40, 950, paint);
//                        canvas.drawText(itemSpinnerOne.getSelectedItem().toString(), 200, 950, paint);
//                        canvas.drawText(String.valueOf(total_harga[itemSpinnerOne.getSelectedItemPosition()]), 700, 950, paint);
//                        canvas.drawText(etJmlOne.getText().toString(), 900, 950, paint);
//                        totalOne = Float.parseFloat(etJmlOne.getText().toString()) * total_harga[itemSpinnerOne.getSelectedItemPosition()];
//                        paint.setTextAlign(Paint.Align.RIGHT);
//                        canvas.drawText(String.valueOf(totalOne), pageWidth - 40, 950, paint);
//                        paint.setTextAlign(Paint.Align.LEFT);
//                    }
//
//                    if (itemSpinnerTwo.getSelectedItemPosition() != 0) {
//                        canvas.drawText("2.", 40, 1050, paint);
//                        canvas.drawText(itemSpinnerTwo.getSelectedItem().toString(), 200, 1050, paint);
//                        canvas.drawText(String.valueOf(total_harga[itemSpinnerTwo.getSelectedItemPosition()]), 700, 1050, paint);
//                        canvas.drawText(etJmlTwo.getText().toString(), 900, 1050, paint);
//                        totalTwo = Float.parseFloat(etJmlTwo.getText().toString()) * total_harga[itemSpinnerTwo.getSelectedItemPosition()];
//                        paint.setTextAlign(Paint.Align.RIGHT);
//                        canvas.drawText(String.valueOf(totalTwo), pageWidth - 40, 1050, paint);
//                        paint.setTextAlign(Paint.Align.LEFT);
//                    }

                    float subTotal = totalOne + totalTwo;
//                    canvas.drawLine(400, 1200, pageWidth - 20, 1200, paint);
//                    canvas.drawText("Sub Total", 700, 1250, paint);
//                    canvas.drawText(":", 900, 1250, paint);
//                    paint.setTextAlign(Paint.Align.RIGHT);
//                    canvas.drawText(String.valueOf(subTotal), pageWidth - 40, 1250, paint);
//
//                    paint.setTextAlign(Paint.Align.LEFT);
//                    canvas.drawText("PPN (10%)", 700, 1300, paint);
//                    canvas.drawText(":", 900, 1300, paint);
//                    paint.setTextAlign(Paint.Align.RIGHT);
//                    canvas.drawText(String.valueOf(subTotal * 10 / 100), pageWidth - 40, 1300, paint);
//                    paint.setTextAlign(Paint.Align.LEFT);

                    paint.setColor(Color.rgb(247, 147, 30));
                    canvas.drawRect(680, 1350, pageWidth - 20, 1450, paint);

                    paint.setColor(Color.BLACK);
                    paint.setTextSize(50f);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Total", 700, 1415, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(String.valueOf(subTotal + (subTotal * 10 / 100)), pageWidth - 40, 1415, paint);

                    pdfDocument.finishPage(page);

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"/Pesanan.pdf");
                    try {
                        pdfDocument.writeTo(new FileOutputStream(file.toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    pdfDocument.close();

                    Toast.makeText(getActivity(), "PDF sudah dibuat", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}