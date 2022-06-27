package com.sitadigi.go4lunch.ui.workmaters;


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

import com.sitadigi.go4lunch.databinding.FragmentWorkmatersBinding;
import com.sitadigi.go4lunch.factory.MainViewModelFactory;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkmateFragment extends Fragment {

    MainViewViewModel mainViewViewModel;
    private FragmentWorkmatersBinding binding;
    private RecyclerView mRecyclerView;
    private List<User> users;
    private List<GoogleMapApiClass.Result> allRestaurant = new ArrayList<>();
    private String placeNameSelected;
    private TextView tvNoWorkmate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        GoogleMapApiCallsRepository googleMapApiCallsRepository = new GoogleMapApiCallsRepository();
        mainViewViewModel = new ViewModelProvider(requireActivity(), new MainViewModelFactory(
                googleMapApiCallsRepository)).get(MainViewViewModel.class);
        binding = FragmentWorkmatersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        users = new ArrayList<>();
        allRestaurant = new ArrayList<>();
        mRecyclerView = binding.recyclerviewWorkmaters;
        tvNoWorkmate = binding.noWorkmateFound;
        tvNoWorkmate.setVisibility(View.GONE);

        mainViewViewModel.getAllUser().observe(getViewLifecycleOwner(), usersLiveData -> {
            users.clear();
            users.addAll(usersLiveData);
            initRecyclerView();
        });

        mainViewViewModel.getResultSearchPlaceName().observe(getViewLifecycleOwner(), PlaceNameResponse -> {
            placeNameSelected = PlaceNameResponse;
            initRecyclerView();

        });
        mainViewViewModel.getRestaurant().observe(getViewLifecycleOwner(), Restaurants -> {
            allRestaurant.clear();
            allRestaurant.addAll(Restaurants);
            initRecyclerView();
        });
        return root;
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        List<User> listOfUserFiltered = new ArrayList<>();
        if (placeNameSelected != null) {
            for (User filter : users) {
                if (filter.getUserRestaurantName().equals(placeNameSelected)) {
                    listOfUserFiltered.add(filter);
                }
            }
            if (listOfUserFiltered.size() == 0) {
                tvNoWorkmate.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                tvNoWorkmate.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                WorkmateAdapter workmateAdapter = new WorkmateAdapter(listOfUserFiltered
                        , mainViewViewModel, allRestaurant);
                mRecyclerView.setAdapter(workmateAdapter);
            }

        } else {
            tvNoWorkmate.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            WorkmateAdapter workmateAdapter = new WorkmateAdapter(users, mainViewViewModel, allRestaurant);
            mRecyclerView.setAdapter(workmateAdapter);

        }
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}