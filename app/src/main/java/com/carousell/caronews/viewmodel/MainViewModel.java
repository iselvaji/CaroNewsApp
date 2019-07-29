package com.carousell.caronews.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.carousell.caronews.model.pojo.ApiResponse;
import com.carousell.caronews.model.pojo.News;
import com.carousell.caronews.model.repository.NewsRepository;

import java.util.List;

import javax.inject.Inject;


public class MainViewModel extends ViewModel {

    private final NewsRepository mRepository;

    @Inject
    public MainViewModel(NewsRepository repository) {
        this.mRepository = repository;
    }

    public LiveData<ApiResponse> getNewsDetails() {
        return mRepository.getNews();
    }

    public LiveData<ApiResponse> getSortedNews(List<News> data, int filterType) {
        return mRepository.getSortedData(data, filterType);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(mRepository != null)
            mRepository.clear();
    }
}
