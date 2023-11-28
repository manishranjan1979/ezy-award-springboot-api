package com.ezy.rewards.restapi.utils;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Utils {

    final static Logger LOG = Logger.getLogger(Utils.class);

    // @GetMapping(value = "/getProductDescriptions")
    // public String getProductDescriptions() {
    // String uri = "https://api.dingconnect.com/api/V1/GetProductDescriptions";
    // String param = "";
    // return getAPIData(getFinalURIWithParam(uri, param));
    // }

    public String getFinalURIWithParam(String uri, String param) {
        LOG.info("Inside getFinalURIWithParam..");
        String uriWithParam = uri + "?" + param;
        LOG.info("Leaving getFinalURIWithParam..");
        return uriWithParam;
    }

    public String getAPIData(String uriWithParam, String token) {
        LOG.info("Inside getAPIData..");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", " Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseData = null;
        try {
            responseData = restTemplate.exchange(uriWithParam, HttpMethod.GET, entity, String.class);
            LOG.info("Data received from API");
        } catch (Exception e) {
            LOG.info("Error occured when calling API " + e.getLocalizedMessage());
            return e.getMessage();
        }
        String responseJson = responseData.getBody();
        LOG.info("Leaving getAPIData..");
        return responseJson;
    }

    public String getAPIDataItems(String uriWithParam, String token) {
        LOG.info("Inside getAPIDataItems..");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", " Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseData = null;
        try {
            responseData = restTemplate.exchange(uriWithParam, HttpMethod.GET, entity, String.class);
            LOG.info("Data received from API");
        } catch (Exception e) {
            LOG.info("Error occured when calling API " + e.getLocalizedMessage());
            return e.getMessage();
        }
        String responseJson = responseData.getBody();
        LOG.info("Leaving getAPIDataItems..");
        return responseJson;
    }

    public String getSingleParamAddedInURI(String param, String paramFieldName, String paramFieldValue) {
        LOG.info("Inside getSingleParamAddedInURI..");
        if (paramFieldValue != null && !"".equals(paramFieldValue)) {
            if (param != null && !"".equals(param))
                param = param + "&" + paramFieldName + paramFieldValue;
            else
                param = paramFieldName + paramFieldValue;
        }
        LOG.info("Leaving getSingleParamAddedInURI..");
        return param;
    }

    public String getParamFromArray(String param, String paramField, String[] paramArray) {
        LOG.info("Inside getParamFromArray..");
        if (paramArray != null && paramArray.length > 0) {
            if (paramArray.length == 1) {
                param = paramField + paramArray[0];
            } else {
                int i = 0;
                do {
                    if (i == 0) {
                        param = paramField + paramArray[i];
                    } else {
                        param = param + "&" + paramField + paramArray[i];
                    }
                } while (paramArray.length > ++i);
            }
        }
        LOG.info("Leaving getParamFromArray..");
        return param;
    }
}
