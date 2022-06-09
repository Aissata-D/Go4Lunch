package com.sitadigi.go4lunch.ui.listView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sitadigi.go4lunch.databinding.FragmentListViewBinding;
import com.sitadigi.go4lunch.databinding.FragmentSlideshowBinding;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.ui.mapView.MapViewViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {

    private FragmentListViewBinding binding;
   // ListViewViewModel mListViewViewModel;
    List<GoogleClass1.Result> listOfRestaurent = new ArrayList<>();
    private RecyclerView mRecyclerView;
    String restoId;
    String restoName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
      //  ListViewViewModel listViewViewModel =
        //        new ViewModelProvider(this).get(ListViewViewModel.class);
        MapViewViewModel mapViewViewModel =
                     new ViewModelProvider(requireActivity()).get(MapViewViewModel.class);


        binding = FragmentListViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mRecyclerView = binding.recyclerviewListView;
        String location = "45.7714678,4.8901636";
        //listViewViewModel= new ViewModelProvider(ListViewFragment.this).get(ListViewViewModel.class);
       // listViewViewModel.loadRestaurentData(location);
        mapViewViewModel.getRestaurent().observe(getViewLifecycleOwner(), RestaurentResponse -> {
            List<GoogleClass1.Result> mItems = RestaurentResponse;
            listOfRestaurent.clear();
            listOfRestaurent.addAll(mItems);
            //mAdapter.notifyDataSetChanged();
            initRecyclerView();

        });

        Log.e("TAG", "onCreateView: "+listOfRestaurent );
        Bundle arguments = getArguments();
        if (arguments != null) {
            restoName = arguments.getString("RESTO_NAME");
            restoId = arguments.getString("RESTO_ID");
            Log.e("TAG", "onCreateView ListView: "+restoName +" "+ restoId);
        }

        return root;
    }
    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ListViewAdapter listViewAdapter = new ListViewAdapter( listOfRestaurent);
        mRecyclerView.setAdapter(listViewAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}