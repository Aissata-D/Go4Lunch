package com.sitadigi.go4lunch.ui.listView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sitadigi.go4lunch.databinding.FragmentListViewBinding;
import com.sitadigi.go4lunch.databinding.FragmentSlideshowBinding;

public class ListViewFragment extends Fragment {

    private FragmentListViewBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListViewViewModel listViewViewModel =
                new ViewModelProvider(this).get(ListViewViewModel.class);

        binding = FragmentListViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.listView;
        listViewViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}