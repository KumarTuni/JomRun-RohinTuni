package com.rohin.jomrun.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rohin.jomrun.JomRunApplication;
import com.rohin.jomrun.R;
import com.rohin.jomrun.adapters.MoviesAdapter;
import com.rohin.jomrun.databinding.FragmentDashboardBinding;
import com.rohin.jomrun.model.data.Movie;
import com.rohin.jomrun.ui.home.HomeFragment;
import com.rohin.jomrun.ui.utils.listners.MovieItemListeners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    private FragmentDashboardBinding binding;
    private MoviesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoritesViewModel = new ViewModelProvider.NewInstanceFactory().create(FavoritesViewModel.class);
        adapter = new MoviesAdapter(new MovieItemListeners() {
            @Override
            public void onToggleFavorite(Movie movie) {

            }

            @Override
            public void onShare(Movie movie) {
                JomRunApplication.getInstance().shareMovie(FavoritesFragment.this, movie);

            }
        });
        adapter.setHideFavorite(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favoritesViewModel.getMoviesPagedListLvD().observe(getViewLifecycleOwner(), movies -> {
            adapter.submitList(movies);
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.moviesPRV.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            favoritesViewModel.reloadInitial();
            binding.swipeRefresh.setRefreshing(false);
        });
    }
}
