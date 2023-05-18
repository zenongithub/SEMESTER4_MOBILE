package com.shedenk.app.ui.beranda;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shedenk.app.R;
import com.shedenk.app.databinding.FragmentBerandaBinding;
import com.shedenk.app.produk.ProdukItemModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BerandaFragment extends Fragment{

    SliderView sliderView;
    int[] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4,};
    RecyclerView recyclerView;
    AdapterProdukBeranda adapterRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ProdukItemModel> data;
    private FragmentBerandaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda,container,false);

        sliderView = view.findViewById(R.id.image_slider);
        ImageSlideAdapter sliderAdapter = new ImageSlideAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        recyclerView = view.findViewById(R.id.recycler_view_beranda);
        recyclerView.setHasFixedSize(true);

        data = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(container.getContext());


        StringRequest stringRequest = new StringRequest(


                Request.Method.GET, "http://192.168.86.194:8000/api/getDataProduk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(container.getContext(), "Berhasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    JSONArray jo = new JSONArray(response);
                    JSONObject object;

                    for (int i =0; i < jo.length(); i++){
                        object = jo.getJSONObject(i);
                        data.add(new ProdukItemModel(object.getString("id_produk"), object.getString("nama_produk"), object.getString("harga"),(object.getString("nama_kategori")),object.getString("deskripsi"),object.getString("ukuran"), "https://plus.unsplash.com/premium_photo-1666264200754-1a2d5f2f6695?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80"));
                    }

                    layoutManager = new GridLayoutManager(getActivity(),2);
                    recyclerView.setLayoutManager(layoutManager);

                    adapterRecyclerView = new AdapterProdukBeranda(data);
                    recyclerView.setAdapter(adapterRecyclerView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(container.getContext(), "Gagal Mengambil Data" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

        return view;
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}