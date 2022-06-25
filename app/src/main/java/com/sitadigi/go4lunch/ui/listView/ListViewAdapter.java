package com.sitadigi.go4lunch.ui.listView;

import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_ID;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_NAME;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_OPENINGHOURS;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_PHONE_NUMBER;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_PHOTO_URL;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_RATING;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_TYPE;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_ADDRESS;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_WEBSITE;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
import com.sitadigi.go4lunch.DetailActivity;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;


import java.util.ArrayList;
import java.util.List;

import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.utils.UtilsDetailActivity;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

    /**
     * The list of restaurant adapter
     */
    @NonNull
    private final List<GoogleMapApiClass.Result> mRestaurants;
    /**
     * The list of All user using application
     */
    private final List<User> mUsers;
    public int mPosition;
    String urlPart1 = "";
    String urlPart2 = "";
    String urlPart3 = "";
    String urlConcat = "";
    String mOriginDistance;
    private MainViewViewModel mMainViewViewModel;
    private UtilsDetailActivity utilsDetailActivity;

    /**
     * Instantiates a new RestaurantAdapter.
     *
     * @param restaurants the list of restaurant the adapter
     */
    public ListViewAdapter(@NonNull final List<GoogleMapApiClass.Result> restaurants,
                           List<User> mUsers, MainViewViewModel mainViewViewModel, String mOriginsDistance) {
        this.mRestaurants = restaurants;
        this.mUsers = mUsers;
        this.mMainViewViewModel = mainViewViewModel;
        this.mOriginDistance = mOriginsDistance;
    }

    /**
     * Updates the list of tasks the adapter deals with.
     */

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
                Intent intentDetail = new Intent(v.getContext(), DetailActivity.class);
                intentDetail.putExtra(RESTAURANT_ID, restaurant.getPlaceId());
                intentDetail.putExtra(RESTAURANT_NAME, restaurant.getName());
                if ((restaurant.getTypes()) != null && (restaurant.getTypes().get(0)) != null) {
                    String restaurantAddress = restaurant.getVicinity();
                    intentDetail.putExtra(RESTAURANT_ADDRESS, restaurantAddress);
                    String restaurantType = restaurant.getTypes().get(0);
                    intentDetail.putExtra(RESTAURANT_TYPE, restaurantType);
                }
                if (restaurant.getOpeningHours() != null && restaurant.getOpeningHours().getOpenNow() != null) {
                    intentDetail.putExtra(RESTAURANT_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                }
                if (restaurant.getPhotos() != null) {
                    if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                        intentDetail.putExtra(RESTAURANT_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
                    }
                }
                if (restaurant.getRating() != null) {
                    float rating = restaurant.getRating().floatValue();
                    intentDetail.putExtra(RESTAURANT_RATING, rating);
                }
                List<String> restaurantNumberAndWebSite = mMainViewViewModel
                        .loadRestaurantPhoneNumberAndWebSite(restaurant);
                if (restaurantNumberAndWebSite.size() >= 1) {
                    if (restaurantNumberAndWebSite.get(0) != null) {
                        String restaurantPhoneNumber = restaurantNumberAndWebSite.get(0);
                        intentDetail.putExtra(RESTAURANT_PHONE_NUMBER, restaurantPhoneNumber);
                        Log.e("DETAIL", "onMarkerClick: Phone " + restaurantPhoneNumber);
                    }
                    if (restaurantNumberAndWebSite.size() >= 2) {
                        if (restaurantNumberAndWebSite.get(1) != null) {
                            String restaurantWebSite = restaurantNumberAndWebSite.get(1);
                            intentDetail.putExtra(RESTAURANT_WEBSITE, restaurantWebSite);
                        }
                    } else {
                        Log.e("DETAIL", "onMarkerClick: phone size<2 ");
                    }
                } else {
                    Log.e("DETAIL", "onMarkerClick: website size<1 ");

                }
                v.getContext().startActivity(intentDetail);
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
     * <p>ViewHolder for task items in the tasks list</p>
     *
     * @author Gaëtan HERFRAY
     */
    class ListViewHolder extends RecyclerView.ViewHolder {

        /**
         * The TextView displaying the name of the restaurent
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
         * @param itemView the view of the task item
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
         * @param restaurant the task to bind in the item view
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
                    restaurantOpeningHour.setText(R.string.resto_open_now);
                } else {
                    restaurantOpeningHour.setText(R.string.resto_close);
                }
            } else {
                restaurantOpeningHour.setText(R.string.restoopening_hour_unknow);
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
            String destination = restaurant.getGeometry().getLocation().getLat() + "," + restaurant.getGeometry().getLocation().getLng();
            int restaurantDistance = mMainViewViewModel.getRestaurantDistance(mOriginDistance, destination);
            String restaurantDistanceText = (restaurantDistance)  +" m";
            this.restaurantDistance.setText(restaurantDistanceText);

        }
    }
}
