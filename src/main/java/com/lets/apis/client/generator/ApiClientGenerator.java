package com.lets.apis.client.generator;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.util.ApiOperationUtil;
import com.lets.apis.client.generator.writer.FileWriter;

public class ApiClientGenerator {

    public static void main(String[] args) {
        ApiDetail apiDetail = ApiOperationUtil.getApiDetail();
        FileWriter.write(apiDetail);
    }
}