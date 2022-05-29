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


import java.util.ArrayList;
import java.util.List;

public class WorkmatersFragment extends Fragment  {

    private FragmentWorkmatersBinding binding;
    TextView textView;
    private RecyclerView mRecyclerView;
    List<User> users = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkmatersViewModel workmatersViewModel =
                new ViewModelProvider(this).get(WorkmatersViewModel.class);

        binding = FragmentWorkmatersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        mRecyclerView = binding.recyclerviewWorkmaters;

        // workmatersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
         workmatersViewModel.getAllUser().observe(getViewLifecycleOwner(), usersLiveData -> {
         List<User> mItems = usersLiveData;
         users.clear();
         users.addAll(mItems);
         initRecyclerView();

         });

        return root;
    }
    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        WorkmatersAdapter workmatersAdapter = new WorkmatersAdapter(users);
        mRecyclerView.setAdapter(workmatersAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }

        @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}