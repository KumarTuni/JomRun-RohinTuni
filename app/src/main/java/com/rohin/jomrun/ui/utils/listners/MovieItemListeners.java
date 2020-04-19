package com.rohin.jomrun.ui.utils.listners;

import com.rohin.jomrun.model.data.Movie;

public interface MovieItemListeners {

    void onToggleFavorite(Movie movie);

    void onShare(Movie movie);
}
