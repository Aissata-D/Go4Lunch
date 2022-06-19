package com.sitadigi.go4lunch.ui.listView;

import android.location.Location;
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
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {

    List<GoogleMapApiClass.Result> listOfRestaurent = new ArrayList<>();
    MainViewViewModel mainViewViewModel;
    String placeNameSelected;
    private FragmentListViewBinding binding;
    private RecyclerView mRecyclerView;
    private TextView tvNoRestoFound;
    List<User> mUsers = new ArrayList<>();
    Location mLocation;
    String mOrigineDistance;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mainViewViewModel =
                new ViewModelProvider(requireActivity()).get(MainViewViewModel.class);


        binding = FragmentListViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mRecyclerView = binding.recyclerviewListView;
        tvNoRestoFound = binding.noRestoFound;
        tvNoRestoFound.setVisibility(View.GONE);

        mainViewViewModel.getLocationMutableLiveData().observe(getViewLifecycleOwner()
                ,LocationMutableLiveData -> { mLocation = LocationMutableLiveData;
                    mOrigineDistance = mLocation.getLatitude()+ "," +mLocation.getLongitude();
                    initRecyclerView();
                });

        mainViewViewModel.getRestaurant().observe(getViewLifecycleOwner(), RestaurantResponse -> {
            listOfRestaurent.clear();
            listOfRestaurent.addAll(RestaurantResponse);
            initRecyclerView();
        });

        mainViewViewModel.getResultSearchPlaceName().observe(getViewLifecycleOwner(), PlaceNameResponse -> {
            placeNameSelected = PlaceNameResponse;
            initRecyclerView();
        });
        mainViewViewModel.getAllUser().observe(getViewLifecycleOwner(),AllUsers ->{
            mUsers.clear();
            mUsers = AllUsers;
            initRecyclerView();

        });

        return root;
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (placeNameSelected != null) {
            List<GoogleMapApiClass.Result> listOfRestaurentFiltered = new ArrayList<>();
            for (GoogleMapApiClass.Result filter : listOfRestaurent) {
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
                ListViewAdapter listViewAdapter = new ListViewAdapter(listOfRestaurentFiltered, mUsers,mainViewViewModel,mOrigineDistance);
                mRecyclerView.setAdapter(listViewAdapter);
            }
        } else {
            tvNoRestoFound.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            ListViewAdapter listViewAdapter = new ListViewAdapter(listOfRestaurent, mUsers,mainViewViewModel,mOrigineDistance);
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