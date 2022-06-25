package com.sitadigi.go4lunch.ui.workmaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.utils.OpenDetailActivityUtils;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

import java.util.List;

public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateAdapter.WorkmateViewHolder> {
    /**
     * The list of user adapter
     */
    @NonNull
    private final List<User> mUsers;
    public int mPosition;
    MainViewViewModel mMainViewViewModel;
    List<GoogleMapApiClass.Result> mRestaurants;

    /**
     * Instantiates a new userAdapter.
     *
     * @param users
     */

    public WorkmateAdapter(@NonNull List<User> users, MainViewViewModel mainViewViewModel
            , List<GoogleMapApiClass.Result> restaurants) {
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
                OpenDetailActivityUtils openDetailActivityUtils = new OpenDetailActivityUtils();
                openDetailActivityUtils.clickOnOpenDetailActivityInWorkmateFragment(v.getContext()
                ,mRestaurants,mUsers,mPosition,mMainViewViewModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    /**
     * <p>ViewHolder for user items in the mUsers list</p>
     *
     * @author Aissata DIASSANA
     */
    class WorkmateViewHolder extends RecyclerView.ViewHolder {
        /**
         * The TextView displaying the name of user
         */
        ImageView imgUser;
        TextView tvUser;

        /**
         * Instantiates a new WorkmateViewHolder.
         *
         * @param itemView the view of the user item
         */
        WorkmateViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imageView_item_workmaters);
            tvUser = itemView.findViewById(R.id.user_name_item_workmaters);
        }
        /**
         * Binds a user to the item view.
         *
         * @param users the user to bind in the item view
         */
        void bind(User users) {
            String userText = users.getUsername() + " hasn't decided yet";
            if (!users.getUserRestaurantId().equals("restaurantIdCreated")) {
                if (!users.getUserRestaurantName().equals("restaurantNameCreated"))
                    userText = users.getUsername() + " is eating " + users.getUserRestaurantType() + " " + "(" + users.getUserRestaurantName() + ")";
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

