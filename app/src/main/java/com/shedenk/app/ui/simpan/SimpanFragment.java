package com.shedenk.app.ui.simpan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shedenk.app.R;
import com.shedenk.app.databinding.FragmentSimpanBinding;


import java.util.ArrayList;


public class SimpanFragment extends Fragment {

    RecyclerView recyclerView;

    AdapterRecyclerViewSimpan adapterRecyclerViewSimpan;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ProdukItemSimpanModel> data2;
    private FragmentSimpanBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simpan,container,false);


        recyclerView = view.findViewById(R.id.recycler_view_simpan);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        data2 = new ArrayList<>();
        for (int i = 0; i < ProdukItemSimpan.namaproduksimpan.length; i++){
            data2.add(new ProdukItemSimpanModel(
                    ProdukItemSimpan.namaproduksimpan[i],
                    ProdukItemSimpan.hargasimpan[i],
                    ProdukItemSimpan.produksimpan[i],
                    ProdukItemSimpan.ukuransimpan[i]
                    ));
        }

        adapterRecyclerViewSimpan = new AdapterRecyclerViewSimpan(data2);

        recyclerView.setAdapter(adapterRecyclerViewSimpan);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}