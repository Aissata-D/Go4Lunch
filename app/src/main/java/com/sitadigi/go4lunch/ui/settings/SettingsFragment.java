package com.sitadigi.go4lunch.ui.settings;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.databinding.FragmentSettingsBinding;
import com.sitadigi.go4lunch.factory.UserViewModelFactory;
import com.sitadigi.go4lunch.repository.UserRepository;
import com.sitadigi.go4lunch.utils.ShowSignOutDialogueAlertAndDetailActivity;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

public class SettingsFragment extends Fragment {

    public static final String YES = "YES";
    public static final String NO = "NO";
    public static final String SHARED_PREF_USER_INFO_NOTIFICATION = "SHARED_PREF_USER_INFO_NOTIFICATION";
    public static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    UserViewModel mUserViewModel;
    RadioGroup mRadioGroup;
    RadioButton btnRadioYes;
    RadioButton btnRadioNo;
    TextView textView;
    Button btnDeleteAccount;
    Button btnUpdateName;
    EditText edtUpdateName;
    String statusNotification = "";
    private FragmentSettingsBinding binding;
    private String decision = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        UserRepository userRepository = new UserRepository();
        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        textView = binding.textSettings;
        mRadioGroup = binding.viewRadioGroup;
        btnRadioYes = binding.radioYes;
        btnRadioNo = binding.radioNo;
        btnDeleteAccount = binding.btnDeleteAccount;
        btnUpdateName = binding.btnUpdateUsername;
        edtUpdateName = binding.edtUpdateName;
        statusNotification = getActivity().getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NOTIFICATION, YES);

        if (statusNotification.equals(YES)) {
            btnRadioYes.setChecked(true);
        }
        if (statusNotification.equals(NO)) {
            btnRadioNo.setChecked(true);
        }
        listenRadioButton();

        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUpdateName.length() > 0) {
                    String newUserName = edtUpdateName.getText().toString();
                    mUserViewModel.updateUsername(newUserName);
                    Toast.makeText(view.getContext(), "Name change to: " + newUserName, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), getString(R.string.give_valid_name), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowSignOutDialogueAlertAndDetailActivity showSignOutDialogueAlertAndDetailActivity
                        = new ShowSignOutDialogueAlertAndDetailActivity();
                showSignOutDialogueAlertAndDetailActivity.showAlertDialogDeleteAccount(view.getContext());
            }
        });

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