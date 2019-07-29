package com.carousell.caronews.util;

import com.carousell.caronews.R;
import com.carousell.caronews.model.pojo.APIError;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.HttpException;
import retrofit2.Response;

import static com.carousell.caronews.model.pojo.APIError.ERROR_TYPE_EXCEPTION;

public class ErrorHandler {

    private static int getLocalisedHttpErrorString(Throwable throwable) {

        int errMsgStringId;

        switch (((HttpException) throwable).code()) {

            case HttpsURLConnection.HTTP_BAD_REQUEST:
            case HttpsURLConnection.HTTP_UNAUTHORIZED:
            case HttpsURLConnection.HTTP_FORBIDDEN:
            case HttpsURLConnection.HTTP_BAD_METHOD:
            case HttpsURLConnection.HTTP_NOT_ACCEPTABLE:
                errMsgStringId = R.string.error_connection;
                break;
            case HttpsURLConnection.HTTP_NOT_FOUND:
                errMsgStringId = R.string.error_server_unreachable;
                break;
            case HttpsURLConnection.HTTP_CLIENT_TIMEOUT:
                errMsgStringId = R.string.error_timeout;
                break;
            case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                errMsgStringId = R.string.error_server;
                break;
            default:
                errMsgStringId = R.string.error_generic_message;
        }
        return errMsgStringId;
    }

    public static int getErrorMsg(Throwable throwable) {

        int errorMsg =  R.string.error_generic_message;

        if(throwable != null) {
            if (throwable instanceof HttpException) {
                errorMsg = getLocalisedHttpErrorString(throwable);
            } else if (throwable instanceof java.net.ConnectException || throwable instanceof java.io.IOException) {
                errorMsg = R.string.error_connection;
            }
        }
        return errorMsg;
    }

    public static APIError parseError(Response<?> response) {
        return new APIError(response.code(), response.message(), null);
    }

    public static APIError parseException(Throwable throwable) {
        return new APIError(ERROR_TYPE_EXCEPTION, throwable.getMessage(), throwable);
    }
}

