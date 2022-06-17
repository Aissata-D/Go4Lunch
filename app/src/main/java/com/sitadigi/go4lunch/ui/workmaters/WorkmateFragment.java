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

import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkmateFragment extends Fragment {

    private FragmentWorkmatersBinding binding;
    private RecyclerView mRecyclerView;
    private List<User> users = new ArrayList<>();
    private String placeNameSelected;
    private TextView tvNoWorkmate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkmateViewModel workmateViewModel =
                new ViewModelProvider(this).get(WorkmateViewModel.class);
        MainViewViewModel mainViewViewModel =
                new ViewModelProvider(requireActivity()).get(MainViewViewModel.class);

        binding = FragmentWorkmatersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mRecyclerView = binding.recyclerviewWorkmaters;
        tvNoWorkmate = binding.noWorkmateFound;
        tvNoWorkmate.setVisibility(View.GONE);

        workmateViewModel.getAllUser().observe(getViewLifecycleOwner(), usersLiveData -> {
            List<User> mItems = usersLiveData;
            users.clear();
            users.addAll(mItems);
            initRecyclerView();
        });

        mainViewViewModel.getResultSearchPlaceName().observe(getViewLifecycleOwner(), PlaceNameResponse -> {
            placeNameSelected = PlaceNameResponse;
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
                if (filter.getUserRestoName().equals(placeNameSelected)) {
                    listOfUserFiltered.add(filter);
                }
            }
            if (listOfUserFiltered.size() == 0) {
                tvNoWorkmate.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                tvNoWorkmate.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                WorkmateAdapter workmateAdapter = new WorkmateAdapter(listOfUserFiltered);
                mRecyclerView.setAdapter(workmateAdapter);
            }

        } else {
            tvNoWorkmate.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            WorkmateAdapter workmateAdapter = new WorkmateAdapter(users);
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