package com.sitadigi.go4lunch.ui.listView;

import static com.sitadigi.go4lunch.DetailActivity.RESTO_ID;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_NAME;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_OPENINGHOURS;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_PHOTO_URL;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_TYPE_ADRESSES;

import android.content.Intent;
import android.net.Uri;
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
import com.sitadigi.go4lunch.models.GoogleClass1;


import java.util.List;
import com.sitadigi.go4lunch.R;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {

    String urlPart1 ="";
    String urlPart2 = "";
    String urlPart3 = "";
    String urlConcat = "";

    /**
     * The list of restaurent adapter
     */
    @NonNull
    private final List<GoogleClass1.Result> mRestaurents ;
    public int mPosition;


    /**
     * Instantiates a new RestaurentAdapter.
     *
     * @param restaurents the list of tasks the adapter deals with to set
     */
    public ListViewAdapter(@NonNull final List<GoogleClass1.Result> restaurents) {
        this.mRestaurents = restaurents;

    }

    /**
     * Updates the list of tasks the adapter deals with.
     *
    // * @param tasks the list of tasks the adapter deals with to set
     */
    /*void updateTasks(@NonNull final List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_listview, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int position) {


        listViewHolder.bind(mRestaurents.get(position));

// Open DetailsActivity of restaurant clicked
        listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = listViewHolder.getAdapterPosition();
                GoogleClass1.Result restaurant = mRestaurents.get(mPosition);
                Intent intentDetail = new Intent(v.getContext(), DetailActivity.class);
                intentDetail.putExtra(RESTO_ID, restaurant.getPlaceId());
                intentDetail.putExtra(RESTO_NAME, restaurant.getName());
                if((restaurant.getTypes()) != null && (restaurant.getTypes().get(0))!=null) {
                    String restoAdressesAndTypeForDetailActivity = restaurant.getTypes().get(0) + " - "
                            + restaurant.getVicinity();
                    intentDetail.putExtra(RESTO_TYPE_ADRESSES, restoAdressesAndTypeForDetailActivity);
                }
                if(restaurant.getOpeningHours() != null && restaurant.getOpeningHours().getOpenNow() != null) {
                    intentDetail.putExtra(RESTO_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                }

                if (restaurant.getPhotos() != null) {
                    if (mRestaurents.get(mPosition).getPhotos().get(0).getPhotoReference() != null) {
                        intentDetail.putExtra(RESTO_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
                    }
                }
                v.getContext().startActivity(intentDetail);
            }
    }
    );
    }

    @Override
    public int getItemCount() {
        return mRestaurents.size();
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
        private final TextView restoWorkmaterNumber;
        private final TextView restoLikeNumber;
        private final ImageView restoImageView;



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
            restoWorkmaterNumber = itemView.findViewById(R.id.resto_workmater_number_item_listview);
            restoLikeNumber = itemView.findViewById(R.id.resto_like_number_item_listview);
            restoImageView = itemView.findViewById(R.id.resto_imageview_item_listview);


        }

        /**
         * Binds a restaurent to the item view.
         *
         * @param restaurant the task to bind in the item view
         */
        void bind(GoogleClass1.Result restaurant) {

            if(restaurant.getPhotos() != null/*  .size() >= 1*/) {
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
/*
         Intent intentDetail = new Intent(this.getActivity(), DetailActivity.class);
                intentDetail.putExtra(RESTO_ID,restaurant.getPlaceId());
                intentDetail.putExtra(RESTO_NAME,restoName);*/
               // if(restaurant.getPhotos() != null/*  .size() >= 1*/) {
            /*if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                intentDetail.putExtra(RESTO_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
            }
        }
        startActivity(intentDetail); */