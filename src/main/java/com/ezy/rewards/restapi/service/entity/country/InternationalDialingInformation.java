package com.ezy.rewards.restapi.service.entity.country;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class InternationalDialingInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Expose
    @JsonProperty("Prefix")
    private String prefix;
    @Expose
    @JsonProperty("MinimumLength")
    private Integer minimumLength;
    @JsonProperty("MaximumLength")
    @Expose
    private Integer maximumLength;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    public InternationalDialingInformation() {
    }

    public InternationalDialingInformation(String prefix, Integer minimumLength, Integer maximumLength,
            Country country) {
        this.prefix = prefix;
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
        this.country = country;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }

    public Integer getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }

}
