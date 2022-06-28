package com.sitadigi.go4lunch.ui.listView;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.sitadigi.go4lunch.factory.MainViewModelFactory;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListViewFragment extends Fragment {

    List<GoogleMapApiClass.Result> listOfRestaurant = new ArrayList<>();
    List<GoogleMapApiClass.Result> listOfRestaurantSort = new ArrayList<>();
    MainViewViewModel mainViewViewModel;
    String placeNameSelected;
    List<User> mUsers = new ArrayList<>();
    Location mLocation;
    String mOriginDistance;
    String destination = "";
    int restaurantDistance = 0;
    private FragmentListViewBinding binding;
    private RecyclerView mRecyclerView;
    private TextView tvNoRestaurantFound;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        GoogleMapApiCallsRepository googleMapApiCallsRepository = new GoogleMapApiCallsRepository();
        mainViewViewModel = new ViewModelProvider(requireActivity()
                , new MainViewModelFactory(googleMapApiCallsRepository)).get(MainViewViewModel.class);

        binding = FragmentListViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mRecyclerView = binding.recyclerviewListView;
        tvNoRestaurantFound = binding.noRestoFound;
        tvNoRestaurantFound.setVisibility(View.GONE);

        mainViewViewModel.getLocationMutableLiveData().observe(getViewLifecycleOwner()
                , LocationMutableLiveData -> {
                    mLocation = LocationMutableLiveData;
                    mOriginDistance = mLocation.getLatitude() + "," + mLocation.getLongitude();
                    initRecyclerView();
                });

        mainViewViewModel.getRestaurant().observe(getViewLifecycleOwner(), RestaurantResponse -> {
            listOfRestaurant.clear();
            listOfRestaurant.addAll(RestaurantResponse);
            listOfRestaurantSort.clear();
            listOfRestaurantSort.addAll(listOfRestaurant);
            Collections.sort(listOfRestaurantSort, ComparatorRestaurantDistance);
            Log.e("NEW", "onCreateView: " + listOfRestaurantSort);
            initRecyclerView();
        });

        mainViewViewModel.getResultSearchPlaceName().observe(getViewLifecycleOwner(), PlaceNameResponse -> {
            placeNameSelected = PlaceNameResponse;
            initRecyclerView();
        });
        mainViewViewModel.getAllUser().observe(getViewLifecycleOwner(), AllUsers -> {
            mUsers.clear();
            mUsers = AllUsers;
            initRecyclerView();
        });

        return root;
    }

    public int getRestaurantDistance(GoogleMapApiClass.Result restaurant) {
        destination = restaurant.getGeometry().getLocation().getLat()
                + "," + restaurant.getGeometry().getLocation().getLng();
        restaurantDistance = mainViewViewModel.getRestaurantDistance(mOriginDistance, destination);
        return restaurantDistance;
    }

    Comparator<GoogleMapApiClass.Result> ComparatorRestaurantDistance
            = new Comparator<GoogleMapApiClass.Result>() {

        @Override
        public int compare(GoogleMapApiClass.Result r1, GoogleMapApiClass.Result r2) {
            return (int) (getRestaurantDistance(r1) - getRestaurantDistance(r2));
        }
    };

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (placeNameSelected != null) {
            List<GoogleMapApiClass.Result> listOfRestaurantFiltered = new ArrayList<>();
            for (GoogleMapApiClass.Result filter : listOfRestaurant) {
                if (filter.getName().equals(placeNameSelected)) {
                    listOfRestaurantFiltered.add(filter);
                }
            }
            if (listOfRestaurantFiltered.size() == 0) {
                tvNoRestaurantFound.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                tvNoRestaurantFound.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                ListViewAdapter listViewAdapter = new ListViewAdapter(listOfRestaurantFiltered, mUsers, mainViewViewModel, mOriginDistance);
                mRecyclerView.setAdapter(listViewAdapter);
            }
        } else {
            tvNoRestaurantFound.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            ListViewAdapter listViewAdapter = new ListViewAdapter(listOfRestaurantSort, mUsers, mainViewViewModel, mOriginDistance);
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