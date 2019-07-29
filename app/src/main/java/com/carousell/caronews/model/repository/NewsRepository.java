package com.carousell.caronews.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.carousell.caronews.model.pojo.ApiResponse;
import com.carousell.caronews.model.pojo.News;
import com.carousell.caronews.model.rest.ApiService;
import com.carousell.caronews.util.AppUtils;
import com.carousell.caronews.util.ErrorHandler;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.carousell.caronews.util.AppConstants.FILTER_BY_RANK;

public class NewsRepository {

    private final ApiService mApiService;
    private CompositeDisposable mDisposable;

    @Inject
    public NewsRepository(ApiService apiService) {
        this.mApiService = apiService;
        this.mDisposable = new CompositeDisposable();
    }

    public LiveData<ApiResponse> getNews() {
        final MutableLiveData<ApiResponse> responseLiveData =  new MutableLiveData<>();
        responseLiveData.setValue(ApiResponse.loading());

        mDisposable.add(mApiService.getNewsDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<News>, List<News>>() {
                    @Override
                    public List<News> apply(List<News> newsList) throws Exception {
                       return AppUtils.getSortedListByDate(newsList);
                    }
                })
                .subscribeWith(new DisposableSingleObserver<List<News>>() {
                                   @Override
                                   public void onSuccess(List<News> value) {
                                       responseLiveData.setValue(ApiResponse.success(value));
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       responseLiveData.setValue(ApiResponse.error(ErrorHandler.parseException(e)));
                                   }
                               }
                ));
        return responseLiveData;
    }

    public LiveData<ApiResponse> getSortedData(List<News> inputList, final int filterType) {
        final MutableLiveData<ApiResponse> responseLiveData =  new MutableLiveData<>();
        responseLiveData.setValue(ApiResponse.loading());

        mDisposable.add(Single.just(inputList)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<News>, List<News>>() {
                    @Override
                    public List<News> apply(List<News> newsList) throws Exception {
                        if(filterType == FILTER_BY_RANK)
                            return AppUtils.getSortedListByPopular(newsList);
                        else
                            return AppUtils.getSortedListByDate(newsList);
                    }
                })
                .subscribeWith(new DisposableSingleObserver<List<News>>() {
                                   @Override
                                   public void onSuccess(List<News> value) {
                                       responseLiveData.setValue(ApiResponse.success(value));
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       responseLiveData.setValue(ApiResponse.error(ErrorHandler.parseException(e)));
                                   }
                               }
                ));
        return responseLiveData;
    }

    public void clear() {
        if (mDisposable != null) {
            mDisposable.clear();
            mDisposable = null;
        }
    }
}
