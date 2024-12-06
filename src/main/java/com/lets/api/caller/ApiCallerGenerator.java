package com.lets.api.caller;

import com.lets.api.caller.model.ApiDetail;
import com.lets.api.caller.util.ApiOperationUtil;
import com.lets.api.caller.writer.FileWriter;

public class ApiCallerGenerator {

    public static void main(String[] args) {
        ApiDetail apiDetail = ApiOperationUtil.getApiDetail();
        FileWriter.write(apiDetail);
    }
}