package com.sitadigi.go4lunch.ui.settings;

import static android.content.Context.MODE_PRIVATE;

import com.sitadigi.go4lunch.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sitadigi.go4lunch.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    public static final String YES = "YES";
    public static final String NO = "NO";
    public static final String SHARED_PREF_USER_INFO_NOTIFICATION = "SHARED_PREF_USER_INFO_NOTIFICATION";
    public static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    RadioGroup mRadioGroup;
    RadioButton btnRadioYes;
    RadioButton btnRadioNo;
    TextView textView;
    String statusNotification = "";
    private FragmentSettingsBinding binding;
    private String decision = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textGallery;
        mRadioGroup = binding.viewRadioGroup;
        btnRadioYes = binding.radioYes;
        btnRadioNo = binding.radioNo;
        statusNotification = getActivity().getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NOTIFICATION, YES);

        if (statusNotification.equals(YES)) {
            btnRadioYes.setChecked(true);
        }
        if (statusNotification.equals(NO)) {
            btnRadioNo.setChecked(true);
        }
        listenRadioButton();

        return root;
    }

    private void listenRadioButton() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int idChecked) {
                SharedPreferences preferences = getActivity().getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                final int yes = R.id.radio_yes;
                final int no = R.id.radio_no;

                switch (idChecked) {
                    case yes:
                        decision = YES;
                        editor.putString(SHARED_PREF_USER_INFO_NOTIFICATION, decision);
                        editor.apply();
                        Toast.makeText(getActivity().getApplicationContext(),
                                getString(R.string.receive_notification), Toast.LENGTH_SHORT).show();
                        break;
                    case no:
                        decision = NO;
                        editor.putString(SHARED_PREF_USER_INFO_NOTIFICATION, decision);
                        editor.apply();
                        Toast.makeText(getActivity().getApplicationContext(),
                                getString(R.string.not_receive_notification), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}