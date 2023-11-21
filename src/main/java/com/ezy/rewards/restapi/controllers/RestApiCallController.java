/**
 * @author Manish Ranjan
 * 
 */
package com.ezy.rewards.restapi.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@PropertySource("classpath:restapi.properties")
@RestController
public class RestApiCallController {
    static Logger LOG = Logger.getLogger(RestApiCallController.class);

    @Value("${grant.type}")
    private String grantType;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${token.uri}")
    private String tokenUri;

    @GetMapping(value = "/getToken")
    private String getToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", grantType);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);
        RestTemplate restTemplateToken = new RestTemplate();
        ResponseEntity<String> responseData = null;

        try {
            responseData = restTemplateToken.exchange(tokenUri,
                    HttpMethod.POST,
                    entity,
                    String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            LOG.debug("RestClientException occured...." + e.getMessage());

        }

        String responseJson = responseData.getBody();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseJson);
        } catch (JSONException err) {
            LOG.debug("Could not create JSON Object");
        }
        String strToken = jsonObject.getString("access_token");
        return strToken;
    }

    // @GetMapping(value = "/getProductDescriptions")
    // public String getProductDescriptions() {
    // String uri = "https://api.dingconnect.com/api/V1/GetProductDescriptions";
    // String param = "";
    // return getAPIData(getFinalURIWithParam(uri, param));
    // }

    private String getFinalURIWithParam(String uri, String param) {
        String uriWithParam = uri + "?" + param;
        return uriWithParam;
    }

    private String getAPIData(String uriWithParam) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", " Bearer " + getToken());
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
        return responseJson;
    }

    private String getSingleParamAddedInURI(String param, String paramFieldName, String paramFieldValue) {
        if (paramFieldValue != null && !"".equals(paramFieldValue)) {
            if (param != null && !"".equals(param))
                param = param + "&" + paramFieldName + paramFieldValue;
            else
                param = paramFieldName + paramFieldValue;
        }
        return param;
    }

    /*
     * CancelTransfers
     * EstimatePrices
     * ListTransferRecords
     * LookupBills
     * SendTransfer
     */

    // * GetAccountLookup --
    // localhost:8084/GetAccountLookup?accountNumber
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetAccountLookup?accountNumber
    @GetMapping(value = "/GetAccountLookup")
    public String GetAccountLookup(@RequestParam(name = "accountNumber", required = false) String accountNumber) {
        String uri = "https://api.dingconnect.com/api/V1/GetAccountLookup";
        String param = "";
        param = getSingleParamAddedInURI(param, "accountNumber=", accountNumber);
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetBalance No-Param - Tested
    // localhost:8084/GetBalance
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetBalance
    @GetMapping(value = "/GetBalance")
    public String GetBalance() {
        String uri = "https://api.dingconnect.com/api/V1/GetBalance";
        String param = "";
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetCountries No-Param - Tested
    // localhost:8084/GetCountries
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetCountries
    @GetMapping(value = "/GetCountries")
    public String GetCountries() {
        String uri = "https://api.dingconnect.com/api/V1/GetCountries";
        String param = "";
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetCurrencies No-Param - Tested
    // localhost:8084/GetCurrencies
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetCurrencies
    @GetMapping(value = "/GetCurrencies")
    public String GetCurrencies() {
        String uri = "https://api.dingconnect.com/api/V1/GetCurrencies";
        String param = "";
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetErrorCodeDescriptions No-Param - Tested
    // localhost:8084/GetErrorCodeDescriptions
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetErrorCodeDescriptions
    @GetMapping(value = "/GetErrorCodeDescriptions")
    public String GetErrorCodeDescriptions() {
        String uri = "https://api.dingconnect.com/api/V1/GetErrorCodeDescriptions";
        String param = "";
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetProductDescriptions - Tested Partially
    // localhost:8084/GetProductDescriptions?languageCodes&languageCodes&skuCodes&skuCodes
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetProductDescriptions?languageCodes&languageCodes&skuCodes&skuCodes
    @GetMapping(value = "/GetProductDescriptions")
    public String GetProductDescriptions(@RequestParam(name = "languageCodes", required = false) String[] languageCodes,
            @RequestParam(name = "skuCodes", required = false) String[] skuCodes) {
        String uri = "https://api.dingconnect.com/api/V1/GetProductDescriptions";
        String param = "";
        param = getParamFromArray(param, "languageCodes=", languageCodes);
        param = getParamFromArray(param, "skuCodes=", skuCodes);
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetProducts
    // localhost:8084/GetProducts?countryIsos&countryIsos&providerCodes&providerCodes&skuCodes&skuCodes&benefits&benefits&regionCodes&regionCodes&accountNumber
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetProducts?countryIsos&countryIsos&providerCodes&providerCodes&skuCodes&skuCodes&benefits&benefits&regionCodes&regionCodes&accountNumber
    @GetMapping(value = "/GetProducts")
    public String GetProducts(@RequestParam(name = "countryIsos", required = false) String[] countryIsos,
            @RequestParam(name = "providerCodes", required = false) String[] providerCodes,
            @RequestParam(name = "skuCodes", required = false) String[] skuCodes,
            @RequestParam(name = "benefits", required = false) String[] benefits,
            @RequestParam(name = "regionCodes", required = false) String[] regionCodes,
            @RequestParam(name = "accountNumber", required = false) String accountNumber) {
        String uri = "https://api.dingconnect.com/api/V1/GetProducts";
        String param = "";
        param = getParamFromArray(param, "countryIsos=", countryIsos);
        param = getParamFromArray(param, "providerCodes=", providerCodes);
        param = getParamFromArray(param, "skuCodes=", skuCodes);
        param = getParamFromArray(param, "benefits=", benefits);
        param = getParamFromArray(param, "regionCodes=", regionCodes);
        param = getSingleParamAddedInURI(param, "accountNumber=", accountNumber);
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetPromotionDescriptions
    // localhost:8084/GetPromotionDescriptions?languageCodes&languageCodes
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetPromotionDescriptions?languageCodes&languageCodes
    @GetMapping(value = "/GetPromotionDescriptions")
    public String GetPromotionDescriptions(
            @RequestParam(name = "languageCodes", required = false) String[] languageCodes) {
        String uri = "https://api.dingconnect.com/api/V1/GetPromotionDescriptions";
        String param = "";
        param = getParamFromArray(param, "languageCodes=", languageCodes);
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetPromotions
    // localhost:8084/GetPromotions?countryIsos&countryIsos&providerCodes&providerCodes&accountNumber
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetPromotions?countryIsos&countryIsos&providerCodes&providerCodes&accountNumber
    @GetMapping(value = "/GetPromotions")
    public String GetPromotions(@RequestParam(name = "countryIsos", required = false) String[] countryIsos,
            @RequestParam(name = "providerCodes", required = false) String[] providerCodes,
            @RequestParam(name = "accountNumber", required = false) String accountNumber) {
        String uri = "https://api.dingconnect.com/api/V1/GetPromotions";
        String param = "";
        param = getParamFromArray(param, "countryIsos=", countryIsos);
        param = getParamFromArray(param, "providerCodes=", providerCodes);
        param = getSingleParamAddedInURI(param, "accountNumber=", accountNumber);
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetProviders
    // localhost:8084/GetPromotions?providerCodes&providerCodes&countryIsos&countryIsos&regionCodes&regionCodes&accountNumber
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetPromotions?providerCodes&providerCodes&countryIsos&countryIsos&regionCodes&regionCodes&accountNumber
    @GetMapping(value = "/GetProviders")
    public String GetProviders(@RequestParam(name = "providerCodes", required = false) String[] providerCodes,
            @RequestParam(name = "countryIsos", required = false) String[] countryIsos,
            @RequestParam(name = "regionCodes", required = false) String[] regionCodes,
            @RequestParam(name = "accountNumber", required = false) String accountNumber) {
        String uri = "https://api.dingconnect.com/api/V1/GetProviders";
        String param = "";
        param = getParamFromArray(param, "providerCodes=", providerCodes);
        param = getParamFromArray(param, "countryIsos=", countryIsos);
        param = getParamFromArray(param, "regionCodes=", regionCodes);
        param = getSingleParamAddedInURI(param, "accountNumber=", accountNumber);
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetProviderStatus
    // localhost:8084/GetProviderStatus?providerCodes&providerCodes
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetProviderStatus?providerCodes&providerCodes
    @GetMapping(value = "/GetProviderStatus")
    public String GetProviderStatus(@RequestParam(name = "providerCodes", required = false) String[] providerCodes) {
        String uri = "https://api.dingconnect.com/api/V1/GetProviderStatus";
        String param = "";
        param = getParamFromArray(param, "providerCodes=", providerCodes);
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    // * GetRegions
    // localhost:8084/GetRegions?countryIsos&countryIsos
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetRegions?countryIsos&countryIsos
    @GetMapping(value = "/GetRegions")
    public String GetRegions(@RequestParam(name = "countryIsos", required = false) String[] countryIsos) {
        String uri = "https://api.dingconnect.com/api/V1/GetRegions";
        String param = "";
        param = getParamFromArray(param, "countryIsos=", countryIsos);
        return getAPIData(getFinalURIWithParam(uri, param));
    }

    private String getParamFromArray(String param, String paramField, String[] paramArray) {
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
        return param;
    }

}
