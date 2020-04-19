package com.rohin.jomrun.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rohin.jomrun.R;
import com.rohin.jomrun.databinding.MovieItemLayoutBinding;
import com.rohin.jomrun.model.data.Movie;
import com.rohin.jomrun.ui.utils.listners.MovieItemListeners;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesAdapter extends PagedListAdapter<Movie, MoviesAdapter.ViewHolder> {

    private static DiffUtil.ItemCallback<Movie> diffCallback = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.equals(newItem);
        }
    };

    private int lastPosition = -1;
    private MovieItemListeners movieItemListeners;
    private boolean hideFavorite;

    public MoviesAdapter(MovieItemListeners movieItemListeners) {
        super(diffCallback);
        this.movieItemListeners = movieItemListeners;
        hideFavorite = false;
    }

    public MoviesAdapter() {
        super(diffCallback);
        hideFavorite = true;
    }

    public void setHideFavorite(boolean hideFavorite) {
        this.hideFavorite = hideFavorite;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_item_layout, parent, false);
        binding.setMovieItemListener(movieItemListeners);
        binding.setHideFavorite(hideFavorite);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
        //setAnimation(holder.itemView, position);

    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), position % 2 == 0 ? R.anim.slide_enter_left_anim : R.anim.slide_enter_right_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        MovieItemLayoutBinding binding;

        ViewHolder(MovieItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Movie movie) {
            binding.setMovie(movie);
        }
    }
}
