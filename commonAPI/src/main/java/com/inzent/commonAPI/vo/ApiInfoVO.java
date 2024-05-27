package com.inzent.commonAPI.vo;

/**
 * API 정보
 * @author 황유진
 */

public class ApiInfoVO {
    private String apiId;
    private String apiKey;
    private  String apiName;
    private  String apiUrl;

    private String apiCode;

    public ApiInfoVO(String apiId, String apiKey, String apiName, String apiUrl, String apiCode) {
        this.apiId = apiId;
        this.apiKey = apiKey;
        this.apiName = apiName;
        this.apiUrl = apiUrl;
        this.apiCode = apiCode;
    }
    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }
}
