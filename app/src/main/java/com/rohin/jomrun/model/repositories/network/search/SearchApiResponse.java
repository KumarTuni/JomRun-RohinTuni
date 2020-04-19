package com.rohin.jomrun.model.repositories.network.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rohin.jomrun.model.data.Movie;

import java.util.List;

public class SearchApiResponse {

    @SerializedName("Search")
    private List<Movie> movies;
    @SerializedName("Response")
    private boolean response;
    @SerializedName("totalResults")
    private int totalResults;
    @SerializedName("Error")
    private String errorMsg;

    @Expose(serialize = false,deserialize = false)
    private boolean running = true;
    @Expose(serialize = false,deserialize = false)
    private boolean empty;

    public SearchApiResponse(List<Movie> movies, boolean response, int totalResults, String errorMsg, boolean running,boolean empty) {
        this.movies = movies;
        this.response = response;
        this.totalResults = totalResults;
        this.errorMsg = errorMsg;
        this.running = running;
        this.empty = empty;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public static SearchApiResponse success(SearchApiResponse searchApiResponse){
        return new SearchApiResponse(searchApiResponse.movies,true,searchApiResponse.totalResults,null,false,false);
    }

    public static SearchApiResponse loading(List<Movie> movies){
        return new SearchApiResponse(movies,false,-1,null,true,false);
    }

    public static SearchApiResponse error(List<Movie> movies,String errorMsg){
        return new SearchApiResponse(movies,false,-1,errorMsg,false,false);
    }

    public static SearchApiResponse empty(List<Movie> movies){
        return new SearchApiResponse(movies,false,-1,null,false,false);
    }


}
