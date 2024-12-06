package com.lets.apis.api.caller.util;

import com.lets.apis.api.caller.model.ApiDetail;

public class ApiOperationUtil {

    public static ApiDetail getApiDetail() {
        ApiDetail apiDetail = new ApiDetail();
        ControllerReaderUtil.load(apiDetail); // controller classes load edilir.
        ControllerDetailUtil.load(apiDetail); // controller details load edilir
        ControllerModelUtil.load(apiDetail); //  model details load edilir.
        GradleDetailUtil.load(apiDetail);
        return apiDetail;
    }
}