package com.rohin.jomrun.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.rohin.jomrun.JomRunApplication;
import com.rohin.jomrun.R;
import com.rohin.jomrun.adapters.MoviesAdapter;
import com.rohin.jomrun.databinding.FragmentHomeBinding;
import com.rohin.jomrun.model.data.Movie;
import com.rohin.jomrun.ui.utils.listners.MovieItemListeners;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private MoviesAdapter adapter;

    private String type;
    private String q;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider.NewInstanceFactory().create(HomeViewModel.class);
        adapter = new MoviesAdapter(new MovieItemListeners() {
            @Override
            public void onToggleFavorite(Movie movie) {
                movie.setFavorite(!movie.isFavorite());
                homeViewModel.updateMovie(movie);
                adapter.notifyItemChanged(adapter.getCurrentList().indexOf(movie));
                Toast.makeText(getActivity(), movie.isFavorite() ? "Added to Favorites :) " : "Removed from Favorites :(", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onShare(Movie movie) {
                JomRunApplication.getInstance().shareMovie(HomeFragment.this, movie);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel.getMoviesPagedListLvD().observe(getViewLifecycleOwner(), movies -> {
            adapter.submitList(movies);
        });

        homeViewModel.getApiResponseLiveData().observe(getViewLifecycleOwner(), searchApiResponse -> {
            if (searchApiResponse.isRunning()) {
                if (homeViewModel.getCurrentPageIdx() == 1) {
                    binding.moviesPRV.loading();
                } else {
                    binding.moviesPRV.loadingMore();
                }
            } else if (!searchApiResponse.isResponse()) {
                if (homeViewModel.getCurrentPageIdx() == 1) {
                    binding.moviesPRV.error(searchApiResponse.getErrorMsg());
                } else {
                    binding.moviesPRV.loadingMoreError(searchApiResponse.getErrorMsg());
                }
            } else if (searchApiResponse.isEmpty()) {
                if (homeViewModel.getCurrentPageIdx() == 1)
                    binding.moviesPRV.empty();
            } else if (searchApiResponse.isResponse()) {
                binding.moviesPRV.success();

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        ((EditText) binding.searchView.findViewById(R.id.search_src_text)).setTextColor(Color.BLACK);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.moviesPRV.setAdapter(adapter);
        binding.moviesPRV.setRetryListner(() -> {
            if (homeViewModel.getCurrentPageIdx() == 1)
                homeViewModel.reloadInitial();
            else
                homeViewModel.reloadCurrentPage();
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (homeViewModel.getCurrentPageIdx() == 0) {
                binding.swipeRefresh.setRefreshing(false);
                return;
            }
            homeViewModel.reloadInitial();
            binding.swipeRefresh.setRefreshing(false);
        });

        binding.typeChG.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case -1:
                    type = null;
                    break;
                case R.id.moviesChips:
                    type = "movie";
                    break;
                case R.id.seriesChps:
                    type = "series";
                    break;
                case R.id.episodsChips:
                    type = "episode";
                    break;
            }
            homeViewModel.loadInitial(q, type);

        });

        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                JomRunApplication.hideKeyboard(getActivity());
            }
        });
        binding.searchView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                JomRunApplication.hideKeyboard(getActivity());
            }
        });

        binding.searchView.findViewById(R.id.search_close_btn).setOnClickListener(v -> {
            type = null;
            q = null;
            binding.typeChG.clearCheck();
            homeViewModel.loadInitial(q, type);
            binding.searchView.setQuery("", false);
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                q = query;
                homeViewModel.loadInitial(query, type);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
