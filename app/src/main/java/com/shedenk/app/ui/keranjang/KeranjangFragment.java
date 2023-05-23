package com.shedenk.app.ui.keranjang;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.shedenk.app.RecyclerViewListener;
import com.shedenk.app.SessionManager;
import com.shedenk.app.databinding.FragmentKeranjangBinding;
import com.shedenk.app.produk.DetailProdukKeranjang;
import com.shedenk.app.produk.ProdukItemModel;

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
import java.util.Map;

public class KeranjangFragment extends Fragment implements RecyclerViewListener {

    Button btn_pesan;
    TextView id_akun, email_akun, nama_akun;
    TextView total_harga;
    TextView total_barang;
    Bitmap bitmap, scaleBitmap;
    int pageWidth = 1200;
    Date dateTime;
    DateFormat dateFormat;
    SessionManager sessionManager;
    AdapterProdukKeranjang adapterProdukKeranjang;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Context context;
    ArrayList<ProdukItemModel> data;
    int total = 0;
    private FragmentKeranjangBinding binding;

    private void loadData(Context context, String id){
        data = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(

                Request.Method.POST, "http://192.168.86.194:8000/api/datakeranjang", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(context, "Berhasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    JSONArray jo = new JSONArray(response);
                    JSONObject object;

                    for (int i =0; i < jo.length(); i++){

                        object = jo.getJSONObject(i);
                        data.add(new ProdukItemModel(object.getString("id_produk"), object.getString("nama_produk"), object.getString("harga"),(object.getString("nama_kategori")),object.getString("deskripsi"),object.getString("ukuran"), "https://plus.unsplash.com/premium_photo-1666264200754-1a2d5f2f6695?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80",""));

                        total += Integer.valueOf(object.getString("harga"));

                    }
                    total_barang.setText(String.valueOf(data.size()));
                    total_harga.setText(String.valueOf(total));

                    layoutManager = new GridLayoutManager(getActivity(),1);
                    recyclerView.setLayoutManager(layoutManager);

                    adapterProdukKeranjang = new AdapterProdukKeranjang(data, KeranjangFragment.this);
                    recyclerView.setAdapter(adapterProdukKeranjang);

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
        View view = inflater.inflate(R.layout.fragment_keranjang, container, false);
        context = container.getContext();
        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();

        btn_pesan = view.findViewById(R.id.btncheckout);
        id_akun = view.findViewById(R.id.id_akun_keranjang);
        nama_akun = view.findViewById(R.id.nama_akun_keranjang);
        email_akun = view.findViewById(R.id.email_akun_keranjang);
        total_harga = view.findViewById(R.id.total_harga);
        total_barang = view.findViewById(R.id.total_barang);

    HashMap<String,String> user = sessionManager.getUserDetail();
        String sid = user.get(sessionManager.ID);
        String snama = user.get(sessionManager.NAMA);
        String semail = user.get(sessionManager.EMAIL);

        nama_akun.setText(snama);
        email_akun.setText(semail);
        id_akun.setText(sid);

        recyclerView = view.findViewById(R.id.recycler_view_keranjang);
        recyclerView.setHasFixedSize(true);

        loadData(container.getContext(), sid);

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
                        email_akun.getText().toString().length() == 0 ||
                        total_harga.getText().toString().length() == 0 ||
                        total_harga.getText().toString().length() == 0 )
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


                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(35f);
                    canvas.drawText("Nama Pemesan: " + nama_akun.getText(), 20, 590, paint);
                    canvas.drawText("Email: " + email_akun.getText(), 20, 640, paint);

                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("No. Pesanan: " + "PS0001", pageWidth - 20, 590, paint);

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

                    canvas.drawLine(180, 790, 180, 840, paint);
                    canvas.drawLine(880, 790, 880, 840, paint);

                    canvas.drawText("1", 40, 920, paint);
                    canvas.drawText("Sweetshirt Hitam", 200, 920, paint);
                    canvas.drawText("Rp. 5000", 980, 920, paint);

                    paint.setColor(Color.rgb(247, 147, 30));
                    canvas.drawRect(680, 1350, pageWidth - 20, 1450, paint);

                    paint.setColor(Color.BLACK);
                    paint.setTextSize(50f);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Total", 700, 1415, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(String.valueOf(total), pageWidth - 40, 1415, paint);

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

    @Override
    public void onClickItem(View view, int position) {
                Intent intent = new Intent(view.getContext(), DetailProdukKeranjang.class);

                intent.putExtra("id", data.get(position).getId());
                intent.putExtra("nama", data.get(position).getNama());
                intent.putExtra("harga", data.get(position).getHarga());
                intent.putExtra("kategori", data.get(position).getKategori());
                intent.putExtra("deskripsi", data.get(position).getDeskripsi());
                intent.putExtra("ukuran", data.get(position).getUkuran());
                intent.putExtra("gambar", data.get(position).getGambar());
                startActivityForResult(intent, 1);
            }

    @Override
    public void onClickHapusSimpan(View view, int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                HashMap<String,String> user = sessionManager.getUserDetail();
                String sid = user.get(sessionManager.ID);
                loadData(context, sid);
            }
        }
    }
}