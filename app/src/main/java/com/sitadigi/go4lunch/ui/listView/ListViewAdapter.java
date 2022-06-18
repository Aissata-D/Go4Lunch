package com.sitadigi.go4lunch.ui.listView;

import static com.sitadigi.go4lunch.DetailActivity.RESTO_ID;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_NAME;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_OPENINGHOURS;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_PHONE_NUMBER;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_PHOTO_URL;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_RATING;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_TYPE;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_ADRESSES;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_WEBSITE;

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
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

    String urlPart1 ="";
    String urlPart2 = "";
    String urlPart3 = "";
    String urlConcat = "";

    /**
     * The list of restaurant adapter
     */
    @NonNull
    private final List<GoogleMapApiClass.Result> mRestaurants ;
    public int mPosition;
    /**
     * The list of All user using application
     *
     */
    private final List<User> mUsers;
    private MainViewViewModel mMainViewViewModel;

    /**
     * Instantiates a new RestaurentAdapter.
     *
     * @param restaurants the list of restaurant the adapter
     */
    public ListViewAdapter(@NonNull final List<GoogleMapApiClass.Result> restaurants,
                           List<User> mUsers, MainViewViewModel mainViewViewModel) {
        this.mRestaurants = restaurants;
        this.mUsers = mUsers;
        this.mMainViewViewModel = mainViewViewModel;


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
                intentDetail.putExtra(RESTO_ID, restaurant.getPlaceId());
                intentDetail.putExtra(RESTO_NAME, restaurant.getName());
                if((restaurant.getTypes()) != null && (restaurant.getTypes().get(0))!=null) {
                    String restoAdresses = restaurant.getVicinity();
                    intentDetail.putExtra(RESTO_ADRESSES, restoAdresses);
                    String restoType = restaurant.getTypes().get(0);
                    intentDetail.putExtra(RESTO_TYPE, restoType);
                }
                if(restaurant.getOpeningHours() != null && restaurant.getOpeningHours().getOpenNow() != null) {
                    intentDetail.putExtra(RESTO_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                }
                if (restaurant.getPhotos() != null) {
                    if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                        intentDetail.putExtra(RESTO_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
                    }
                }
                if(restaurant.getRating()!=null){
                    float rating =  restaurant.getRating().floatValue();
                    intentDetail.putExtra(RESTO_RATING, rating);
                }
                List<String> restaurantNumberAndWebSite =mMainViewViewModel
                        .loadRestaurantPhoneNumberAndWebSite(restaurant);
                if(restaurantNumberAndWebSite.size() >=1){
                    if(restaurantNumberAndWebSite.get(0) != null){
                        String restaurantPhoneNumber = restaurantNumberAndWebSite.get(0);
                        intentDetail.putExtra(RESTO_PHONE_NUMBER, restaurantPhoneNumber);
                        Log.e("DETAIL", "onMarkerClick: Phone "+restaurantPhoneNumber );
                    }
                    if(restaurantNumberAndWebSite.size() >=2) {
                        if (restaurantNumberAndWebSite.get(1) != null) {
                            String restaurantWebSite = restaurantNumberAndWebSite.get(1);
                            intentDetail.putExtra(RESTO_WEBSITE, restaurantWebSite);
                        }
                    }else{
                        Log.e("DETAIL", "onMarkerClick: phone size<2 " );
                    }
                }   else{
                    Log.e("DETAIL", "onMarkerClick: website size<1 " );

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
     * <p>ViewHolder for task items in the tasks list</p>
     *
     * @author Gaëtan HERFRAY
     */
    class ListViewHolder extends RecyclerView.ViewHolder {

        /**
         * The TextView displaying the name of the restaurent
         */
        private final TextView restoName;
        private final TextView restoTypeAdresse;
        private final TextView restoOpeningHour;
        private final TextView restoDistance;
        private final TextView restoWorkmateNumber;
        private final ImageView restoImageView;
        private final RatingBar restaurantRatingBar;


        /**
         * Instantiates a new ListViewHolder.
         *
         * @param itemView the view of the task item
         */
        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            restoName = itemView.findViewById(R.id.resto_name_item_listview);
            restoTypeAdresse = itemView.findViewById(R.id.resto_type_adresse_item_listview);
            restoOpeningHour = itemView.findViewById(R.id.resto_opening_hour_item_listview);
            restoDistance = itemView.findViewById(R.id.distance_item_listview);
            restoWorkmateNumber = itemView.findViewById(R.id.resto_workmater_number_item_listview);
            restoImageView = itemView.findViewById(R.id.resto_imageview_item_listview);
            restaurantRatingBar = itemView.findViewById(R.id.list_view_item_rating_bar_star);
        }

        /**
         * Binds a restaurent to the item view.
         * @param restaurant the task to bind in the item view
         */
        void bind(GoogleMapApiClass.Result restaurant) {

            if(restaurant.getPhotos() != null) {
                if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                    urlPart2 = restaurant.getPhotos().get(0).getPhotoReference();
                }
            }
            urlPart1 = "https://maps.googleapis.com/maps/api/place/photo?maxheigth=500&maxwidth=800&photo_reference=";
            urlPart3 = "&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08";
            urlConcat = urlPart1 + urlPart2 + urlPart3;
            //GLIDE TO SHOW PHOTO
            Glide.with(restoImageView.getContext())
                    .load(getUrl(urlConcat))
                    .apply(RequestOptions.noTransformation())
                    .centerCrop()
                    .placeholder(R.drawable.img_resto_placeholder)
                    .into(restoImageView);

            restoName.setText(restaurant.getName());
            String restoAdressesAndType = restaurant.getTypes().get(0) +" - "
                    +restaurant.getVicinity();
            restoTypeAdresse.setText(restoAdressesAndType);
            if(restaurant.getOpeningHours() != null && restaurant.getOpeningHours().getOpenNow() != null){
                if(restaurant.getOpeningHours().getOpenNow()){
                    restoOpeningHour.setText(R.string.resto_open_now);
                }else {restoOpeningHour.setText(R.string.resto_close);}
            }else {restoOpeningHour.setText(R.string.restoopening_hour_unknow);}
            if(restaurant.getRating()!=null){
                float rating =  restaurant.getRating().floatValue();
                restaurantRatingBar.setRating(rating);

            }
            //Filtred list workmate who eats in a same restaurant
            List<User> workmateInSameRestaurant = new ArrayList<>();
            for(User user : mUsers){
                if(user.getUserRestoId().equals(restaurant.getPlaceId())){
                        if (workmateInSameRestaurant.size()==0) {
                            workmateInSameRestaurant.add(user);
                        }else{
                            for (User user1 : workmateInSameRestaurant){
                                if (!user1.equals(user)){
                                    workmateInSameRestaurant.add(user);
                                }
                            }
                       }
                }
            }
            String workmateNumber = "( "+ workmateInSameRestaurant.size()+" )";
            restoWorkmateNumber.setText(workmateNumber);
        }
    }

    /**
     * get URL of restaurant image.
     *
     */
    Uri getUrl(String base){
        Uri uri = Uri.parse( base );
        return uri;
    }
}
