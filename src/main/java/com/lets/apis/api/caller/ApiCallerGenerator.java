package com.lets.apis.api.caller;

import com.lets.apis.api.caller.model.ApiDetail;
import com.lets.apis.api.caller.util.ApiOperationUtil;
import com.lets.apis.api.caller.writer.FileWriter;

public class ApiCallerGenerator {

    public static void main(String[] args) {
        ApiDetail apiDetail = ApiOperationUtil.getApiDetail();
        FileWriter.write(apiDetail);
    }
}