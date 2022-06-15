package com.sitadigi.go4lunch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sitadigi.go4lunch.models.User;

import java.util.List;

public class DetailActivityAdapter extends RecyclerView.Adapter<DetailActivityAdapter.DetailActivityViewHolder> {

    /**
     * The list of user in a same restaurent
     */
    @NonNull
    private List<User> mUsers;

    /**
     * Instantiates a new UserAdapter.
     *
     * @param users the list of tasks the adapter deals with to set
     */

    public DetailActivityAdapter(@NonNull List<User> users) {
        mUsers = users;
    }

    @NonNull
    @Override
    public DetailActivityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_detail_restaurant, viewGroup, false);
        return new DetailActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailActivityViewHolder holder, int position) {
        holder.bind(mUsers.get(position));

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    /**
     * Updates the list of tasks the adapter deals with.
     *
     * @param users the list of tasks the adapter deals with to set
     */
    void updateUsers(@NonNull final List<User> users) {
        // this.mUsers.clear();
        this.mUsers.addAll(users);
        notifyDataSetChanged();
    }

    /**
     * <p>ViewHolder for task items in the tasks list</p>
     *
     * @author Gaëtan HERFRAY
     */
    class DetailActivityViewHolder extends RecyclerView.ViewHolder {

        /**
         * The TextView displaying the name of the restaurent
         */
        ImageView imgUserDetailActivity;
        TextView tvUserDetailActivity;

        /**
         * Instantiates a new ListViewHolder.
         *
         * @param itemView the view of the task item
         */
        DetailActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUserDetailActivity = itemView.findViewById(R.id.imageView_detail_restaurant);
            tvUserDetailActivity = itemView.findViewById(R.id.textView_detail_restaurant);
        }

        /**
         * Binds a restaurent to the item view.
         *
         * @param users the task to bind in the item view
         */
        void bind(User users) {
            String userIsJoining = users.getUsername() + " is joining!";
            tvUserDetailActivity.setText(userIsJoining);
            //GLIDE TO SHOW PHOTO
            Glide.with(imgUserDetailActivity.getContext())
                    .load(users.getUrlPicture())
                    .apply(RequestOptions.noTransformation())
                    .circleCrop()
                    .placeholder(R.drawable.img_user_avatar)
                    .into(imgUserDetailActivity);
        }
    }
}
