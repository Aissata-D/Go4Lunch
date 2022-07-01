package com.sitadigi.go4lunch.ui.listView;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.utils.OpenDetailActivityUtils;
import com.sitadigi.go4lunch.utils.UtilsDetailActivity;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

import java.util.ArrayList;
import java.util.List;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

    public int mPosition;
    String urlPart1 = "";
    String urlPart2 = "";
    String urlPart3 = "";
    String urlConcat = "";
    LatLng mOriginDistance;
    /**
     * The list of All user using application
     */
    private final List<User> mUsers;
    /**
     * The list of restaurant adapter
     */
    @NonNull
    private List<GoogleMapApiClass.Result> mRestaurants = new ArrayList<>();
    private MainViewViewModel mMainViewViewModel;


    /**
     * Instantiates a new RestaurantAdapter.
     *
     * @param restaurants the list of restaurant the adapter
     */
    public ListViewAdapter(@NonNull final List<GoogleMapApiClass.Result> restaurants,
                           List<User> mUsers, MainViewViewModel mainViewViewModel, LatLng originsDistance) {
        this.mRestaurants = restaurants;
        this.mUsers = mUsers;
        this.mMainViewViewModel = mainViewViewModel;
        this.mOriginDistance = originsDistance;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_listview, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int position) {

        listViewHolder.bind(mRestaurants.get(position));
        // Open DetailsActivity of restaurant clicked
        listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = listViewHolder.getAdapterPosition();
                GoogleMapApiClass.Result restaurant = mRestaurants.get(mPosition);
                OpenDetailActivityUtils openDetailActivityUtils = new OpenDetailActivityUtils();
                openDetailActivityUtils.clickOnOpenDetailActivityInLisViewAdapter(restaurant,
                        v.getContext(), mMainViewViewModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    /**
     * get URL of restaurant image.
     */
    Uri getUrl(String base) {
        return Uri.parse(base);
    }

    /**
     * <p>ViewHolder for restaurant items </p>
     *
     * @author Aissata Diassana
     */
    class ListViewHolder extends RecyclerView.ViewHolder {

        /**
         * The TextView displaying the name of the restaurant
         */
        private final TextView restaurantName;
        private final TextView restaurantTypeAddress;
        private final TextView restaurantOpeningHour;
        private final TextView restaurantDistance;
        private final TextView restaurantWorkmateNumber;
        private final ImageView restaurantImageView;
        private final RatingBar restaurantRatingBar;

        /**
         * Instantiates a new ListViewHolder.
         *
         * @param itemView the view of the restaurant item
         */
        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.resto_name_item_listview);
            restaurantTypeAddress = itemView.findViewById(R.id.resto_type_adresse_item_listview);
            restaurantOpeningHour = itemView.findViewById(R.id.resto_opening_hour_item_listview);
            restaurantDistance = itemView.findViewById(R.id.distance_item_listview);
            restaurantWorkmateNumber = itemView.findViewById(R.id.resto_workmater_number_item_listview);
            restaurantImageView = itemView.findViewById(R.id.resto_imageview_item_listview);
            restaurantRatingBar = itemView.findViewById(R.id.list_view_item_rating_bar_star);
        }

        /**
         * Binds a restaurant to the item view.
         *
         * @param restaurant  to bind in the item view
         */
        void bind(GoogleMapApiClass.Result restaurant) {

            if (restaurant.getPhotos() != null) {
                if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                    urlPart2 = restaurant.getPhotos().get(0).getPhotoReference();
                }
            }
            urlPart1 = "https://maps.googleapis.com/maps/api/place/photo?maxheigth=500&maxwidth=800&photo_reference=";
            urlPart3 = "&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08";
            urlConcat = urlPart1 + urlPart2 + urlPart3;
            //GLIDE TO SHOW PHOTO
            Glide.with(restaurantImageView.getContext())
                    .load(getUrl(urlConcat))
                    .apply(RequestOptions.noTransformation())
                    .centerCrop()
                    .placeholder(R.drawable.img_resto_placeholder)
                    .into(restaurantImageView);

            restaurantName.setText(restaurant.getName());
            String restaurantAddressAndType = restaurant.getTypes().get(0) + " - "
                    + restaurant.getVicinity();
            restaurantTypeAddress.setText(restaurantAddressAndType);
            if (restaurant.getOpeningHours() != null && restaurant.getOpeningHours().getOpenNow() != null) {
                if (restaurant.getOpeningHours().getOpenNow()) {
                    restaurantOpeningHour.setText(R.string.restaurant_open_now);
                } else {
                    restaurantOpeningHour.setText(R.string.restaurant_close);
                }
            } else {
                restaurantOpeningHour.setText(R.string.restaurant_opening_hour_unknown);
            }
            if (restaurant.getRating() != null) {
                double ratingOld = restaurant.getRating() * 0.6;
                float rating = (float) ratingOld;
                // utilsDetailActivity.setRatingIcon1(restaurantRatingBar, rating);
                restaurantRatingBar.setRating(rating);

            }
            //Filtered list workmate who eats in a same restaurant
            List<User> workmateInSameRestaurant = new ArrayList<>();
            for (User user : mUsers) {
                if (user.getUserRestaurantId().equals(restaurant.getPlaceId())) {
                    workmateInSameRestaurant.add(user);
                }
            }
            String workmateNumber = "( " + workmateInSameRestaurant.size() + " )";
            restaurantWorkmateNumber.setText(workmateNumber);
            GoogleMapApiClass.Location destination = restaurant.getGeometry().getLocation();
            double restaurantDistance = MainViewViewModel.getRestaurantDistance(mOriginDistance.latitude
                    , destination.getLat(),mOriginDistance.longitude,destination.getLng(),0.0,0.0);
            // Retrieve integer part of the double restaurantDistance with ==> Math.floor(restaurantDistance)
            String restaurantDistanceText = (Math.floor(restaurantDistance)) + " m ";
            this.restaurantDistance.setText(restaurantDistanceText);
        }
    }
}
