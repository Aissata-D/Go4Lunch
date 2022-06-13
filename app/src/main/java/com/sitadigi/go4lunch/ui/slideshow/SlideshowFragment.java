package com.sitadigi.go4lunch.ui.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.sitadigi.go4lunch.databinding.FragmentSlideshowBinding;
import com.sitadigi.go4lunch.utils.DialogClass;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

public class SlideshowFragment extends Fragment {
    private final UserViewModel mUserViewModel = UserViewModel.getInstance();

    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
   /* private void showAlert() {
        new AlertDialog.Builder(this.getActivity().getApplicationContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  deleteSuggestions(position);
                        mUserViewModel.signOut(getContext());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showAlertDialogFilterByRoom() {
        final String SELECT_ROOM = "Select Room";
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity());


        alertDialog.setTitle("Select room")
        .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  deleteSuggestions(position);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}