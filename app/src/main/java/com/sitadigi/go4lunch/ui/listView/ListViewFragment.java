package com.sitadigi.go4lunch.ui.listView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sitadigi.go4lunch.databinding.FragmentListViewBinding;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.ui.mapView.MapViewViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {

    List<GoogleClass1.Result> listOfRestaurent = new ArrayList<>();
    String placeNameSelected;
    private FragmentListViewBinding binding;
    private RecyclerView mRecyclerView;
    private TextView tvNoRestoFound;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        MapViewViewModel mapViewViewModel =
                new ViewModelProvider(requireActivity()).get(MapViewViewModel.class);


        binding = FragmentListViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mRecyclerView = binding.recyclerviewListView;
        tvNoRestoFound = binding.noRestoFound;
        tvNoRestoFound.setVisibility(View.GONE);
        mapViewViewModel.getRestaurent().observe(getViewLifecycleOwner(), RestaurentResponse -> {
            listOfRestaurent.clear();
            listOfRestaurent.addAll(RestaurentResponse);
            initRecyclerView();
        });

        mapViewViewModel.getResultSearchPlaceName().observe(getViewLifecycleOwner(), PlaceNameResponse -> {
            placeNameSelected = PlaceNameResponse;
            initRecyclerView();
        });

        return root;
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (placeNameSelected != null) {
            List<GoogleClass1.Result> listOfRestaurentFiltered = new ArrayList<>();
            for (GoogleClass1.Result filter : listOfRestaurent) {
                if (filter.getName().equals(placeNameSelected)) {
                    listOfRestaurentFiltered.add(filter);
                }
            }
            if (listOfRestaurentFiltered.size() == 0) {
                tvNoRestoFound.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                tvNoRestoFound.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                ListViewAdapter listViewAdapter = new ListViewAdapter(listOfRestaurentFiltered);
                mRecyclerView.setAdapter(listViewAdapter);
            }
        } else {
            tvNoRestoFound.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            ListViewAdapter listViewAdapter = new ListViewAdapter(listOfRestaurent);
            mRecyclerView.setAdapter(listViewAdapter);
        }
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}