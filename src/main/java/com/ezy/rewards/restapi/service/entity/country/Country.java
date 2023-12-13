package com.ezy.rewards.restapi.service.entity.country;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Expose
    @JsonProperty("CountryIso")
    private String countryIso;
    @Expose
    @JsonProperty("CountryName")
    private String countryName;
    @Expose
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("InternationalDialingInformation")
    private List<InternationalDialingInformation> internationalDialingInformation = new ArrayList<>();
    @Expose
    @JsonProperty("RegionCodes")
    private List<String> regionCodes = new ArrayList<>();

    public Country() {
    }

    public Country(String countryIso, String countryName,
            List<InternationalDialingInformation> internationalDialingInformation, List<String> regionCodes) {
        this.countryIso = countryIso;
        this.countryName = countryName;
        this.internationalDialingInformation = internationalDialingInformation;
        this.regionCodes = regionCodes;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<InternationalDialingInformation> getInternationalDialingInformation() {
        return internationalDialingInformation;
    }

    public void setInternationalDialingInformation(
            List<InternationalDialingInformation> internationalDialingInformation) {
        this.internationalDialingInformation = internationalDialingInformation;
    }

    public List<String> getRegionCodes() {
        return regionCodes;
    }

    public void setRegionCodes(List<String> regionCodes) {
        this.regionCodes = regionCodes;
    }

}