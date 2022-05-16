package com.sitadigi.go4lunch.ui.workmaters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sitadigi.go4lunch.databinding.FragmentSlideshowBinding;
import com.sitadigi.go4lunch.databinding.FragmentWorkmatersBinding;

public class WorkmatersFragment extends Fragment {

    private FragmentWorkmatersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkmatersViewModel workmatersViewModel =
                new ViewModelProvider(this).get(WorkmatersViewModel.class);

        binding = FragmentWorkmatersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.workmatersText;
        workmatersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}