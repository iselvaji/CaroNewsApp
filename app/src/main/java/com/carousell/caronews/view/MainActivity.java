package com.carousell.caronews.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.carousell.caronews.R;
import com.carousell.caronews.di.ViewModelFactory;
import com.carousell.caronews.model.pojo.APIError;
import com.carousell.caronews.model.pojo.ApiResponse;
import com.carousell.caronews.model.pojo.News;
import com.carousell.caronews.util.ConnectivityUtils;
import com.carousell.caronews.util.ErrorHandler;
import com.carousell.caronews.view.adapter.NewsListAdapter;
import com.carousell.caronews.viewmodel.MainViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

import static com.carousell.caronews.util.AppConstants.FILTER_BY_RANK;
import static com.carousell.caronews.util.AppConstants.FILTER_BY_TIME;

public class MainActivity extends DaggerAppCompatActivity {

    @BindView(R.id.recyclerviewItems)
    RecyclerView mRecyclerView;

    @BindView(R.id.textviewNoItems)
    View mTextViewNoItems;

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.container)
    View mContainer;

    @BindString(R.string.error_generic_message)
    String genericError;

    @BindString(R.string.err_no_connectivity)
    String connectionError;

    @BindString(R.string.msg_filtered_by_latest)
    String msgFiterbyLatest;

    @BindString(R.string.msg_filtered_by_rank)
    String msgFiterbyRank;

    @Inject
    ViewModelFactory mViewModelFactory;

    private NewsListAdapter mRecyclerViewAdapter;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(MainActivity.this, mViewModelFactory).get(MainViewModel.class);

        mRecyclerViewAdapter = new NewsListAdapter(this, new ArrayList<News>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        loadData();
    }

    private void loadData() {

        if (!ConnectivityUtils.isNetworkConnected(this)) {
            notifyUI(connectionError);
        }
        else {
            mViewModel.getNewsDetails().observe(this, new Observer<ApiResponse>() {
                @Override
                public void onChanged(@Nullable ApiResponse response) {
                    handleResponse(response);
                }
            });
        }
    }

    private void handleResponse(ApiResponse apiResponse) {

        if(apiResponse != null) {
            switch (apiResponse.status) {
                case LOADING:
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    mProgressBar.setVisibility(View.GONE);
                    populateSuccessResponse(apiResponse);
                    break;
                case ERROR:
                    mProgressBar.setVisibility(View.GONE);
                    populateErrorResponse(apiResponse);
                    break;
            }
        }
        else notifyUI(genericError);
    }

    private void populateSuccessResponse(ApiResponse response) {
        if (response.data != null & response.data.size() != 0) {
            mTextViewNoItems.setVisibility(View.GONE);
            mRecyclerViewAdapter.updateItems(response.data);
        }
        else {
            mTextViewNoItems.setVisibility(View.VISIBLE);
        }
    }

    private void populateErrorResponse(ApiResponse errResponse) {

        String errMsg = genericError;
        if(errResponse != null && errResponse.apiError != null) {
            if (errResponse.apiError.errorCode() == APIError.ERROR_TYPE_EXCEPTION) {
                int errResourceId = ErrorHandler.getErrorMsg(errResponse.apiError.throwable());
                errMsg = getString(errResourceId);
            }
        }

        notifyUI(errMsg);
    }

    private void notifyUI(String message) {
         Snackbar.make(mContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_time:
                sortNews(FILTER_BY_TIME);
                notifyUI(msgFiterbyLatest);
                break;
            case R.id.menu_sort_by_popular:
                sortNews(FILTER_BY_RANK);
                notifyUI(msgFiterbyRank);
                break;
        }
        return true;
    }

    private void sortNews(int filterType) {

        mViewModel.getSortedNews(mRecyclerViewAdapter.getDataList(), filterType).observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse response) {
                handleResponse(response);
            }
        });
    }
}
