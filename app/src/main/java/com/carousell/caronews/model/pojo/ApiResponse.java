package com.carousell.caronews.model.pojo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;


public class ApiResponse<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final List<News> data;

    @Nullable private final String message;

    @Nullable public final APIError apiError;

    private ApiResponse(@NonNull Status status, @Nullable List<News> data,
                     @Nullable String message, @Nullable APIError error) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.apiError = error;
    }

    public static <T> ApiResponse<News> success(@NonNull List<News> data) {
        return new ApiResponse<>(Status.SUCCESS, data, null, null);
    }

    public static <T> ApiResponse<T> error(APIError error) {
        return new ApiResponse<>(Status.ERROR, null, null, error);
    }

    public static <T> ApiResponse<T> loading() {
        return new ApiResponse<>(Status.LOADING, null, null, null);
    }

    public enum Status { SUCCESS, ERROR, LOADING }

}
