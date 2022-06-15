package com.sitadigi.go4lunch.ui.lunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sitadigi.go4lunch.databinding.FragmentLunchBinding;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;
import com.sitadigi.go4lunch.utils.MapViewUtils;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

public class LunchFragment extends Fragment {

    String userLastRestoId;
    String userRestoName;
    TextView textView;
    private FragmentLunchBinding binding;
    private MapViewUtils mMapViewUtils;
    private UserViewModel mUserViewModel;
    private Button btnLunch;
    private MainViewViewModel mMainViewViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
       mMainViewViewModel =
                new ViewModelProvider(this).get(MainViewViewModel.class);

        binding = FragmentLunchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textHome;
        btnLunch = binding.btnSeeLunch;
       // showDetailActivity();
        btnLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //showDetailActivity();
                textView.setText("Vous n'avez choisit aucun restaurent");

            }
        });

        return root;
    }


    /*private void showDetailActivity() {
        List<GoogleMapApiClass.Result> resultList = new ArrayList<>();
        List<GoogleMapApiClass.Result> listOfRestaurent = new ArrayList<>();

        mMainViewViewModel.getRestaurent().observe(getViewLifecycleOwner(), RestaurentResponse -> {
            listOfRestaurent.clear();
            listOfRestaurent.addAll(RestaurentResponse);

            //  When getting response, we update UI
            if (listOfRestaurent != null) {
                for (GoogleMapApiClass.Result restaurant : listOfRestaurent) {
                    if (!resultList.contains(restaurant)) {
                        resultList.add(restaurant);
                    }
                }
                String userUid = mUserViewModel.getCurrentUser().getUid();
                // Get userRestoId on firebaseFirestore
                DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
                userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                userLastRestoId = document.getString("userRestoId");
                                userRestoName = document.getString("userRestoName");

                                if ((!userLastRestoId.equals("restoIdCreated"))
                                        && (!userRestoName.equals("restoNameCreated"))) {


                                    for (GoogleMapApiClass.Result restaurant : resultList) {
                                        // LatLng restoPosition = new LatLng(restaurant.getGeometry().getLocation().getLat(),
                                        //    restaurant.getGeometry().getLocation().getLng());
                                        String restoName = restaurant.getName();
                                        String restoId = restaurant.getPlaceId();
                                        if ((restoName.equals(userRestoName)) && (restoId.equals(userLastRestoId))) {
                                            Intent intentDetail = new Intent(getActivity(), DetailActivity.class);
                                            intentDetail.putExtra(RESTO_ID, restaurant.getPlaceId());
                                            intentDetail.putExtra(RESTO_NAME, restoName);
                                            if ((restaurant.getOpeningHours() != null) && (restaurant.getOpeningHours().getOpenNow()) != null) {
                                                intentDetail.putExtra(RESTO_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                                            }
                                            if (restaurant.getPhotos() != null) {
                                                if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                                                    intentDetail.putExtra(RESTO_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
                                                }
                                            }
                                            if ((restaurant.getTypes()) != null && (restaurant.getTypes().get(0)) != null) {
                                                String restoAdresses = restaurant.getVicinity();
                                                intentDetail.putExtra(RESTO_ADRESSES, restoAdresses);
                                                String restoType = restaurant.getTypes().get(0);
                                                intentDetail.putExtra(RESTO_TYPE, restoType);
                                            }
                                            intentDetail.putExtra(RESTO_ID, restaurant.getPlaceId());
                                            startActivity(intentDetail);
                                        }
                                    }
                                } else {
                                    textView.setText("Vous n'avez choisit aucun restaurent");

                                }
                            }
                        }
                    }
                });
            }
        });

    }
*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}