package com.pivotallabs.api;

import android.os.AsyncTask;
import android.util.Log;

public class ApiGateway {

    private final Http http = new Http();

    public void makeRequest(ApiRequest apiRequest, final ApiResponseCallbacks responseCallbacks) {
        new RemoteCallTask(responseCallbacks).execute(apiRequest);
    }

    protected void dispatch(ApiResponse apiResponse, ApiResponseCallbacks responseCallbacks) {
        if (apiResponse.isSuccess()) {
            try {
                responseCallbacks.onSuccess(apiResponse);
            } catch (Exception e) {
                Log.e(ApiGateway.class.getName(), "Error proccessing response", e);
                responseCallbacks.onFailure(apiResponse);
            }
        } else {
            responseCallbacks.onFailure(apiResponse);
        }
        responseCallbacks.onComplete();
    }

    private class RemoteCallTask extends AsyncTask<ApiRequest, Void, ApiResponse> {
        private final ApiResponseCallbacks responseCallbacks;

        public RemoteCallTask(ApiResponseCallbacks responseCallbacks) {
            this.responseCallbacks = responseCallbacks;
        }

        @Override
        protected ApiResponse doInBackground(ApiRequest... apiRequests) {
            ApiRequest apiRequest = apiRequests[0];
            try {
                Http.Response response = http.get(apiRequest.getUrlString(), apiRequest.getHeaders(), apiRequest.getUsername(), apiRequest.getPassword());
                return new ApiResponse(response.getStatusCode(), response.getResponseBody());
            } catch (Exception e) {
                throw new RuntimeException("error making request", e);
            }
        }

        @Override
        protected void onPostExecute(ApiResponse apiResponse) {
            dispatch(apiResponse, responseCallbacks);
        }
    }
}
