package com.ezy.rewards.restapi.controllers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
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

import com.ezy.rewards.restapi.repository.CountryRepository;
import com.ezy.rewards.restapi.repository.DingRespRepository;
import com.ezy.rewards.restapi.repository.RegionRepository;
import com.ezy.rewards.restapi.service.entity.DingAPI;
import com.ezy.rewards.restapi.service.entity.country.Country;
import com.ezy.rewards.restapi.service.entity.country.InternationalDialingInformation;
import com.ezy.rewards.restapi.service.entity.region.Region;
import com.ezy.rewards.restapi.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@PropertySource("classpath:restapi.properties")
@RestController
public class MainController {

    final static Logger LOG = Logger.getLogger(MainController.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private DingRespRepository dingRespRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Value("${grant.type}")
    private String grantType;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${token.uri}")
    private String tokenUri;

    @GetMapping(value = "/fetchAndRefreshApiJSONs")
    public void fetchAndRefreshApiJSONs() {
        LOG.info("Inside fetchAndRefreshApiJSONs..");
        saveRegionsResponseJSON();
        saveCountriesResponseJSON();
        saveCurrenciesResponseJSON();
        LOG.info("Leaving fetchAndRefreshApiJSONs..");
    }

    public void saveRegionsResponseJSON() {
        LOG.info("Inside saveRegionsResponseJSON..");
        String uri = "https://api.dingconnect.com/api/V1/GetRegions";
        String param = "";
        Utils utils = new Utils();
        String token = getTokenNew();
        String jsonResponse = utils.getAPIData(utils.getFinalURIWithParam(uri, param), token);

        JSONObject jsonRespoObj = new JSONObject(jsonResponse);
        JSONArray jsonResponseItemsArr = jsonRespoObj.getJSONArray("Items");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        DingAPI dingResp = new DingAPI("https://api.dingconnect.com/api/V1/GetRegions", jsonResponse,
                jsonResponseItemsArr.toString(), timestamp, timestamp, true);
        dingRespRepository.save(dingResp); // Saving response JSON for countries in DB
        LOG.info("Leaving saveRegionsResponseJSON..");

    }

    public void saveCountriesResponseJSON() {
        LOG.info("Inside saveCountriesResponseJSON..");
        String uri = "https://api.dingconnect.com/api/V1/GetCountries";
        String param = "";
        Utils utils = new Utils();
        String token = getTokenNew();
        String jsonResponse = utils.getAPIData(utils.getFinalURIWithParam(uri, param), token);

        JSONObject jsonRespoObj = new JSONObject(jsonResponse);
        JSONArray jsonResponseItemsArr = jsonRespoObj.getJSONArray("Items");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        DingAPI dingResp = new DingAPI("https://api.dingconnect.com/api/V1/GetCountries", jsonResponse,
                jsonResponseItemsArr.toString(), timestamp, timestamp, true);
        dingRespRepository.save(dingResp); // Saving response JSON for countries in DB
        LOG.info("Leaving saveCountriesResponseJSON..");

    }

    public void saveCurrenciesResponseJSON() {
        LOG.info("Inside saveCurrenciesResponseJSON..");
        String uri = "https://api.dingconnect.com/api/V1/GetCurrencies";
        String param = "";
        Utils utils = new Utils();
        // param = utils.getParamFromArray(param, "countryIsos=", countryIsos);
        String token = getTokenNew();
        String jsonResponse = utils.getAPIData(utils.getFinalURIWithParam(uri, param), token);
        JSONObject jsonRespoObj = new JSONObject(jsonResponse);
        JSONArray jsonResponseItemsArr = jsonRespoObj.getJSONArray("Items");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        DingAPI dingResp = new DingAPI("https://api.dingconnect.com/api/V1/GetCurrencies", jsonResponse,
                jsonResponseItemsArr.toString(), timestamp, timestamp, true);
        dingRespRepository.save(dingResp); // Saving response JSON for currencies in DB
        LOG.info("Leaving saveCurrenciesResponseJSON..");

    }

    /**
     * This method checks in the ding_resp table for the response of apiName.
     * If the response is 3 hrs older, then null is returned else data json is
     * returned
     */
    public boolean checkResponseIsMoreThan3HrsOld(String apiName) {
        LOG.info("Inside checkResponseIsLessThan3HrsOld..");
        boolean responseIsMoreThan3HrsOld = false;
        // Iterable<DingResp> itr = dingRespRepository.find
        // Iterable<DingResp> iterableDingResp = dingRespRepository
        // .findByApiName("https://api.dingconnect.com/api/V1/GetRegions");
        Iterable<DingAPI> iterableDingResp = dingRespRepository
                .findByApiName(apiName);
        Iterator<DingAPI> iteratorDingResp = iterableDingResp.iterator();
        DingAPI dingRespVal = null;
        while (iteratorDingResp.hasNext()) {
            dingRespVal = iteratorDingResp.next();
            Date date = new Date();
            Timestamp currentTimestamp = new Timestamp(date.getTime());

            // java.text.ParseException: Unparseable date: "2023-11-29 09:13:30.443"
            SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy-mm-dd");
            Date firstParsedDate = null;
            Date secondParsedDate = null;
            double dateDiff = 0;

            SimpleDateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.S");
            Date firstParsedDateTime = null;
            Date secondParsedDateTime = null;
            double dateTimeDiff = 0;
            double hourDiff = 0;

            try {
                firstParsedDate = dateFormatDate.parse(dingRespVal.getRecordEntryTimestamp().toString());
                LOG.info("Info-TryCatch: firstParsedDate=" + firstParsedDate);
                secondParsedDate = dateFormatDate.parse(currentTimestamp.toString());
                LOG.info("Info-TryCatch: secondParsedDate=" + secondParsedDate);
                dateDiff = secondParsedDate.compareTo(firstParsedDate);
                LOG.info("Info-TryCatch: dateDiff=" + dateDiff);

                firstParsedDateTime = dateFormatDateTime.parse(dingRespVal.getRecordEntryTimestamp().toString());
                LOG.info("Info-TryCatch: firstParsedDateTime=" + firstParsedDateTime);
                secondParsedDateTime = dateFormatDateTime.parse(currentTimestamp.toString());
                LOG.info("Info-TryCatch: secondParsedDateTime=" + secondParsedDateTime);
                dateTimeDiff = secondParsedDateTime.getTime() - firstParsedDateTime.getTime();
                LOG.info("Info-TryCatch: dateTimeDiff=" + dateTimeDiff);
                hourDiff = (dateTimeDiff / (1000 * 60 * 60)) % 24;
                LOG.info("Info-TryCatch: hourDiff=" + hourDiff);
            } catch (ParseException e) {
                LOG.error("Error: Date parsing gives error...");
                e.printStackTrace();
            }
            if (dateDiff > 0) {
                responseIsMoreThan3HrsOld = true;
            } else if (hourDiff >= 3.0) {
                responseIsMoreThan3HrsOld = true;
                LOG.info(
                        "As saved response is more than 3 hours old, hence returning NULL to initiate new response fetch!");
            }
            break;
        }
        if (dingRespVal == null) {
            responseIsMoreThan3HrsOld = true;
        }
        LOG.info("Leaving checkResponseIsLessThan3HrsOld..");
        return responseIsMoreThan3HrsOld;
    }

    /**
     * This method fetches the data from Region table
     * 
     * @param countryIsos
     * @return
     */
    public String fetchRegionsDataFromDB(String[] countryIsos) {
        LOG.info("Inside fetchRegionsDataFromDB..");
        // Iterable<DingResp> itr = dingRespRepository.find
        // Iterable<DingResp> iterableDingResp = dingRespRepository
        // .findByApiName("https://api.dingconnect.com/api/V1/GetRegions");
        //
        // String countryIsosStr = StringUtils.arrayToCommaDelimitedString(countryIsos);
        List<String> countryIsosList = countryIsos != null ? Arrays.asList(countryIsos) : null;
        Iterable<Region> iterableRegion = (countryIsosList != null && countryIsosList.size() > 0)
                ? regionRepository.findByCountryIsoIn(countryIsosList)
                : regionRepository.findAll();
        Iterator<Region> iteratorRegion = iterableRegion != null ? iterableRegion.iterator() : null;
        List<Region> regionList = new ArrayList<>();
        if (iteratorRegion != null) {
            iteratorRegion.forEachRemaining(regionList::add);
        }
        LOG.info("regionList----- : " + regionList);
        LOG.info("Leaving fetchRegionsDataFromDB..");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        // String regionListJson = regionList != null && regionList.size() > 0 ?
        // gson.toJson(regionList) : null;
        // List<RegionResponse> rr = new ArrayList<>();
        // ErrorCodes errorCodes[] = { new ErrorCodes("", ""), new ErrorCodes("", "") };
        // rr.add(new RegionResponse(1, errorCodes, regionList));
        // String regionListJson = rr != null && rr.size() > 0 ? gson.toJson(rr) : null;
        String regionListJson = regionList != null && regionList.size() > 0 ? gson.toJson(regionList) : null;
        return regionListJson;
    }

    /**
     * This method fetches the data from Country table
     * 
     * @return
     */
    public String fetchCountriesDataFromDB() {
        LOG.info("Inside fetchCountriesDataFromDB..");

        Iterable<Country> iterableCountry = countryRepository.findAll();
        Iterator<Country> iteratorCountry = iterableCountry != null ? iterableCountry.iterator() : null;
        List<Country> countryList = new ArrayList<>();
        while (iteratorCountry != null && iteratorCountry.hasNext()) {
            Country c1 = (Country) iteratorCountry.next();
            countryList.add(c1);
        }
        LOG.info("countryList----- : " + countryList);
        LOG.info("Leaving fetchCountriesDataFromDB..");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String countryListJson = countryList != null && countryList.size() > 0
                ? gson.toJson(countryList)
                : null;
        return countryListJson;
    }

    /**
     * GetCountries
     * 
     * @param countryIsos
     * @return
     */
    @GetMapping(value = "/GetCountries")
    public String GetCountries() {
        LOG.info("Inside GetCountries..");
        String uri = "https://api.dingconnect.com/api/V1/GetCountries";
        boolean responseIsMoreThan3HrsOld = checkResponseIsMoreThan3HrsOld(uri);
        String jsonResponse = null;

        if (responseIsMoreThan3HrsOld) {
            LOG.info(
                    "Firstly fetch the data from the API. Save the data in table. Further fetch the data from table as the current data is less than 3 hrs old. ");
            String param = "";
            Utils utils = new Utils();
            String token = getTokenNew();
            jsonResponse = utils.getAPIData(utils.getFinalURIWithParam(uri, param), token);
            countryRepository.deleteAll();// delete all countries before saving the new record
            Iterable<DingAPI> dingAPIIterable = dingRespRepository.findByApiName(uri);
            Iterator<DingAPI> dingAPIIterator = dingAPIIterable != null ? dingAPIIterable.iterator() : null;
            List<DingAPI> dingAPIList = new ArrayList<>();
            if (dingAPIIterator != null) {
                dingAPIIterator.forEachRemaining(dingAPIList::add);
            }
            dingRespRepository.deleteAll(dingAPIList);
            saveCountryData(jsonResponse);
            LOG.info("Leaving GetCountries..");
        } else {
            LOG.info("Fetch the data from table as the current data is less than 3 hrs old. Don't hit the API.");
        }
        jsonResponse = fetchCountriesDataFromDB();
        return jsonResponse;
    }

    // * GetRegions
    // localhost:8084/GetRegions?countryIsos&countryIsos
    // https://ezy-rewards-spring-boot-rest-api.azurewebsites.net/GetRegions?countryIsos&countryIsos
    @GetMapping(value = "/GetRegions")
    public String GetRegions1(@RequestParam(name = "countryIsos", required = false) String[] countryIsos) {
        LOG.info("Inside GetRegions1..");
        String uri = "https://api.dingconnect.com/api/V1/GetRegions";
        boolean responseIsMoreThan3HrsOld = checkResponseIsMoreThan3HrsOld(uri);
        String jsonResponse = null;

        if (responseIsMoreThan3HrsOld) {
            String param = "";
            Utils utils = new Utils();
            /**
             * As this is to be saved from original API to the DB, param will not be used as
             * filter in original API call
             */
            // param = utils.getParamFromArray(param, "countryIsos=", countryIsos);
            String token = getTokenNew();
            jsonResponse = utils.getAPIData(utils.getFinalURIWithParam(uri, param), token);
            regionRepository.deleteAll();// delete all regions before saving the new record
            Iterable<DingAPI> dingRespIterable = dingRespRepository.findByApiName(uri);
            Iterator<DingAPI> dingRespIterator = dingRespIterable != null ? dingRespIterable.iterator() : null;
            List<DingAPI> dingRespList = new ArrayList<>();
            if (dingRespIterator != null) {
                dingRespIterator.forEachRemaining(dingRespList::add);
            }
            dingRespRepository.deleteAll(dingRespList);
            saveRegionsData(countryIsos);
            jsonResponse = fetchRegionsDataFromDB(countryIsos);
            LOG.info("Leaving GetRegions1..");
        } else {
            LOG.info("Fetch the data based on the countryIsos from table as the current data is less than 3 hrs old.");
            jsonResponse = fetchRegionsDataFromDB(countryIsos);
        }
        return jsonResponse;
    }

    public void saveRegionsData(String[] countryIsos) {
        LOG.info("Inside saveRegionsData..");
        String uri = "https://api.dingconnect.com/api/V1/GetRegions";
        String param = "";
        Utils utils = new Utils();
        /**
         * As this is to be saved from original API to the DB, param will not be used as
         * filter in original API call
         */
        // param = utils.getParamFromArray(param, "countryIsos=", countryIsos);
        String token = getTokenNew();
        String jsonResponse = utils.getAPIData(utils.getFinalURIWithParam(uri, param), token);

        List<Region> regionEntities = new ArrayList<>();
        JSONObject jsonRespoObj = new JSONObject(jsonResponse);
        JSONArray jsonResponseItemsArr = jsonRespoObj.getJSONArray("Items");

        for (int it = 0; it < jsonResponseItemsArr.length(); it++) {
            JSONObject regionItem = jsonResponseItemsArr.getJSONObject(it);
            regionEntities.add(new Region(regionItem.getString("RegionCode"), regionItem.getString("RegionName"),
                    regionItem.getString("CountryIso")));
        }
        regionRepository.saveAll(regionEntities);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        DingAPI dingResp = new DingAPI("https://api.dingconnect.com/api/V1/GetRegions", jsonResponse,
                jsonResponseItemsArr.toString(), timestamp, timestamp, true);
        dingRespRepository.save(dingResp); // Saving response JSON for regions in DB
        LOG.info("Leaving saveRegionsData..");
    }

    public void saveCountryData(String jsonResponse) {
        LOG.info("Inside saveCountryData..");
        List<Country> countryEntities = new ArrayList<>();
        JSONObject jsonRespoObj = new JSONObject(jsonResponse);
        JSONArray jsonResponseItemsArr = jsonRespoObj.getJSONArray("Items");

        for (int it = 0; it < jsonResponseItemsArr.length(); it++) {
            Country country = new Country();
            JSONObject countryItem = jsonResponseItemsArr.getJSONObject(it);
            List<InternationalDialingInformation> intDialInfoList = new ArrayList<>();
            JSONArray countryItemItemsArr = countryItem.getJSONArray("InternationalDialingInformation");
            for (int it1 = 0; it1 < countryItemItemsArr.length(); it1++) {
                JSONObject obj1 = countryItemItemsArr.getJSONObject(it1);
                InternationalDialingInformation internationalDialingInformation = new InternationalDialingInformation(
                        obj1.getString("Prefix"),
                        obj1.getInt("MinimumLength"), obj1.getInt("MaximumLength"), country);
                intDialInfoList.add(internationalDialingInformation);
            }
            List<String> regionCodesList = new ArrayList<>();
            JSONArray regionCodesArr = countryItem.getJSONArray("RegionCodes");
            for (int it2 = 0; it2 < regionCodesArr.length(); it2++) {
                String regionCode = regionCodesArr.getString(it2);
                regionCodesList.add(regionCode);
            }

            country.setCountryIso(countryItem.getString("CountryIso"));
            country.setCountryName(countryItem.getString("CountryName"));
            country.setInternationalDialingInformation(intDialInfoList);
            country.setRegionCodes(regionCodesList);
            countryEntities.add(country);
        }
        countryRepository.saveAll(countryEntities);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        DingAPI dingResp = new DingAPI("https://api.dingconnect.com/api/V1/GetCountries", jsonResponse,
                jsonResponseItemsArr.toString(), timestamp, timestamp, true);
        dingRespRepository.save(dingResp); // Saving response JSON for regions in DB
        LOG.info("Leaving saveCountryData..");
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

        String responseJson = responseData != null ? responseData.getBody() : null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseJson);
        } catch (JSONException err) {
            LOG.debug("Could not create JSON Object");
        }
        String strToken = jsonObject != null ? jsonObject.getString("access_token") : null;
        LOG.info("Leaving getTokenNew..");
        return strToken;
    }
}
