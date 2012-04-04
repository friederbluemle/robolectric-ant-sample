package com.pivotallabs.api;

import java.io.IOException;

public interface ApiResponseCallbacks {
    public void onSuccess(ApiResponse response) throws IOException;
    public void onFailure(ApiResponse response);
    public void onComplete();
}
