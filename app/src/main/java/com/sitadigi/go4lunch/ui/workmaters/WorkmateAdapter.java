package com.sitadigi.go4lunch.ui.workmaters;

import static com.sitadigi.go4lunch.DetailActivity.RESTO_ADRESSES;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_ID;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_NAME;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_OPENINGHOURS;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_PHONE_NUMBER;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_PHOTO_URL;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_RATING;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_TYPE;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_WEBSITE;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sitadigi.go4lunch.DetailActivity;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

import java.util.List;

public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateAdapter.WorkmateViewHolder> {

    /**
     * The list of restaurant adapter
     */
    @NonNull
    private final List<User> mUsers;
    MainViewViewModel mMainViewViewModel;
    public int mPosition;
    List<GoogleMapApiClass.Result> mRestaurants;

    /**
     * Instantiates a new RestaurentAdapter.
     *
     * @param users the list of tasks the adapter deals with to set
     */

    public WorkmateAdapter(@NonNull List<User> users, MainViewViewModel mainViewViewModel
            ,List<GoogleMapApiClass.Result> restaurants) {
        mUsers = users;
        mMainViewViewModel = mainViewViewModel;
        mRestaurants = restaurants;
    }

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_workmaters, viewGroup, false);
        return new WorkmateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position) {
        holder.bind(mUsers.get(position));
        // Open DetailsActivity of restaurant clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = holder.getAdapterPosition();
                //GoogleMapApiClass.Result restaurant = mRestaurants.get(mPosition);
                for(GoogleMapApiClass.Result result : mRestaurants){
                    if(result.getPlaceId().equals(mUsers.get(mPosition).getUserRestoId())
                            && result.getName().equals(mUsers.get(mPosition).getUserRestoName())){
                        GoogleMapApiClass.Result restaurant = result;
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
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    /**
     * <p>ViewHolder for task items in the tasks list</p>
     *
     * @author Gaëtan HERFRAY
     */
    class WorkmateViewHolder extends RecyclerView.ViewHolder {

        /**
         * The TextView displaying the name of the restaurent
         */
        ImageView imgUser;
        TextView tvUser;

        /**
         * Instantiates a new ListViewHolder.
         *
         * @param itemView the view of the task item
         */
        WorkmateViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.imageView_item_workmaters);
            tvUser = itemView.findViewById(R.id.user_name_item_workmaters);
        }

        /**
         * Binds a restaurent to the item view.
         *
         * @param users the task to bind in the item view
         */
        void bind(User users) {
            String userText = users.getUsername() + " hasn't decided yet";
            ;
            if (!users.getUserRestoId().equals("NoRestoChoice")) {
                if (!users.getUserRestoName().equals("restoNameCreated"))
                    userText = users.getUsername() + " is eating " + users.getUserRestoType() + " " + "(" + users.getUserRestoName() + ")";
            }
            tvUser.setText(userText);
            //GLIDE TO SHOW PHOTO
            Glide.with(imgUser.getContext())
                    .load(users.getUrlPicture())
                    .apply(RequestOptions.noTransformation())
                    .circleCrop()
                    .placeholder(R.drawable.img_user_avatar)
                    .into(imgUser);
        }
    }
}

