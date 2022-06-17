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
import com.sitadigi.go4lunch.models.User;

import java.util.List;

public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateAdapter.WorkmatersViewHolder>{

        /**
         * The list of restaurent adapter
         */
        @NonNull
        private final List<User> mUsers ;
        /**
         * Instantiates a new RestaurentAdapter.
         *
         * @param users the list of tasks the adapter deals with to set
         */

    public WorkmateAdapter(@NonNull List<User> users) {
        mUsers = users;
    }
        @NonNull
        @Override
        public WorkmateAdapter.WorkmatersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_workmaters, viewGroup, false);
            return new WorkmatersViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull WorkmatersViewHolder holder, int position) {
       holder.bind(mUsers.get(position));

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
        class WorkmatersViewHolder extends RecyclerView.ViewHolder {

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
            WorkmatersViewHolder(@NonNull View itemView) {
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
                String userText= users.getUsername()+" hasn't decided yet";;
                if(!users.getUserRestoId().equals("NoRestoChoice")){
                    if(!users.getUserRestoName().equals("restoNameCreated"))
                    userText = users.getUsername() +" is eating "+ users.getUserRestoType() +" "+"(" + users.getUserRestoName()+")";
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
