package com.ezy.rewards.restapi.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ezy.rewards.restapi.dao.DingRespRepository;
import com.ezy.rewards.restapi.dao.RegionRepository;
import com.ezy.rewards.restapi.service.model.Region;
import com.ezy.rewards.restapi.utils.Utils;

@PropertySource("classpath:restapi.properties")
@RestController
public class MainController {

    final static Logger LOG = Logger.getLogger(MainController.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private DingRespRepository dingRespRepository;
    @Value("${grant.type}")
    private String grantType;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${token.uri}")
    private String tokenUri;

    // * GetRegions
    // localhost:8084/GetRegions?countryIsos&countryIsos
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetRegions?countryIsos&countryIsos
    @GetMapping(value = "/GetRegions1")
    public String GetRegions(@RequestParam(name = "countryIsos", required = false) String[] countryIsos) {
        LOG.info("Inside GetRegions..");
        String uri = "https://api.dingconnect.com/api/V1/GetRegions";
        String param = "";
        Utils utils = new Utils();
        param = utils.getParamFromArray(param, "countryIsos=", countryIsos);
        String token = getTokenNew();
        String jsonResponse = utils.getAPIData(utils.getFinalURIWithParam(uri, param), token);
        saveRegions(jsonResponse);
        LOG.info("Leaving GetRegions..");
        return jsonResponse;
    }

    public void saveRegions(String jsonResponse) {
        LOG.info("Inside saveRegions..");
        // Saving all the data from Items Array in the response
        List<Region> regionEntities = new ArrayList<>();
        JSONObject newObj = new JSONObject(jsonResponse);
        JSONArray items = newObj.getJSONArray("Items");

        for (int it = 0; it < items.length(); it++) {
            JSONObject regionItem = items.getJSONObject(it);
            regionEntities.add(new Region(regionItem.getString("RegionCode"), regionItem.getString("RegionName"),
                    regionItem.getString("CountryIso")));
        }
        regionRepository.saveAll(regionEntities);

        // Saving the whole jsonResponse along with the Items value ,
        // recordEntryTimestamp/recordUpdateTimestamp (Automatic generated),
        // activationStatus and apiName

        LOG.info("Leaving saveRegions..");
    }

    @GetMapping(value = "/getTokenNew")
    private String getTokenNew() {
        LOG.info("Inside getTokenNew..");
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
        LOG.info("Leaving getTokenNew..");
        return strToken;
    }
}
