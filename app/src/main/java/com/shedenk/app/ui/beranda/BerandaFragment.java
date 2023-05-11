package com.shedenk.app.ui.beranda;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.shedenk.app.R;
import com.shedenk.app.databinding.FragmentBerandaBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class BerandaFragment extends Fragment{

    SliderView sliderView;
    int[] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4,};
    RecyclerView recyclerView;
    AdapterRecyclerView adapterRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ProdukItemModel> data;
    private FragmentBerandaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda,container,false);

        sliderView = view.findViewById(R.id.image_slider);
        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<>();
        for (int i = 0 ; i <ProdukItem.id.length; i++){
            data.add(new ProdukItemModel(
                   ProdukItem.id[i],
                   ProdukItem.namaproduk[i],
                   ProdukItem.harga[i],
                   ProdukItem.deskripsi[i],
                   ProdukItem.ukuran[i],
                   ProdukItem.gambar[i]
            ));
        }

        adapterRecyclerView = new AdapterRecyclerView(data);
        recyclerView.setAdapter(adapterRecyclerView);

        return view;
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}