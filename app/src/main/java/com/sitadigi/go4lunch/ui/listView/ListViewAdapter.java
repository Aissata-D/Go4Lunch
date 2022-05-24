package com.sitadigi.go4lunch.ui.listView;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.sitadigi.go4lunch.models.Restaurent;

import java.util.List;
import com.sitadigi.go4lunch.R;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewHolder> {



    /**
     * The list of restaurent adapter
     */
    @NonNull
    private List<Restaurent> mRestaurents ; //Type mutable



    /**
     * Instantiates a new RestaurentAdapter.
     *
     * @param restaurents the list of tasks the adapter deals with to set
     */
    ListViewAdapter(@NonNull final List<Restaurent> restaurents) {
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
    }

    @Override
    public int getItemCount() {
        if(mRestaurents == null){return 0;}
        else{
            return mRestaurents.size();}
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


        /**
         * Instantiates a new ListViewHolder.
         *
         * @param itemView the view of the task item
         */
        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            restoName = itemView.findViewById(R.id.resto_name);

        }

        /**
         * Binds a restaurent to the item view.
         *
         * @param restaurent the task to bind in the item view
         */
        void bind(Restaurent restaurent) {
            restoName.setText("restaurent.getName()");

        }
    }
}
