package com.ezy.rewards.restapi.service.model;

import com.google.gson.annotations.Expose;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer regionId;

    @Expose
    private String RegionCode;
    @Expose
    private String RegionName;
    @Expose
    private String countryIso;

    public Region(String RegionCode, String RegionName, String countryIso) {
        this.RegionCode = RegionCode;
        this.RegionName = RegionName;
        this.countryIso = countryIso;
    }

    public Region() {
    }

    public String getRegionCode() {
        return this.RegionCode;
    }

    public void setRegionCode(String RegionCode) {
        this.RegionCode = RegionCode;
    }

    public String getRegionName() {
        return this.RegionName;
    }

    public void setRegionName(String RegionName) {
        this.RegionName = RegionName;
    }

    public String getCountryIso() {
        return this.countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

}
