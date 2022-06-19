package com.sitadigi.go4lunch.ui.lunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sitadigi.go4lunch.databinding.FragmentLunchBinding;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

public class LunchFragment extends Fragment {

    String userLastRestoId;
    String userRestoName;
    TextView textView;
    private FragmentLunchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        MainViewViewModel mainViewViewModel = new ViewModelProvider(this).get(MainViewViewModel.class);

        binding = FragmentLunchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textView = binding.textHome;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}