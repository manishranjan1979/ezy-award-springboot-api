package com.ezy.rewards.restapi.service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer regionId;

    private String RegionCode;
    private String RegionName;
    private String CountryIso;


    

    public Region(String regionCode, String regionName, String countryIso) {
        RegionCode = regionCode;
        RegionName = regionName;
        CountryIso = countryIso;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(String regionCode) {
        RegionCode = regionCode;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    public String getCountryIso() {
        return CountryIso;
    }

    public void setCountryIso(String countryIso) {
        CountryIso = countryIso;
    }

}
