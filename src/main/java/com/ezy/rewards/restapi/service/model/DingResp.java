package com.ezy.rewards.restapi.service.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DingResp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer dingRespId;

    private String apiName;
    private String fullResp;
    private String respItem;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Timestamp recordEntryTimestamp;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Timestamp recordUpdateTimestamp;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private boolean activationStatus;

    public DingResp(String apiName, String fullResp, String respItem) {
        this.apiName = apiName;
        this.fullResp = fullResp;
        this.respItem = respItem;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getFullResp() {
        return fullResp;
    }

    public void setFullResp(String fullResp) {
        this.fullResp = fullResp;
    }

    public String getRespItem() {
        return respItem;
    }

    public void setRespItem(String respItem) {
        this.respItem = respItem;
    }

    public Timestamp getRecordEntryTimestamp() {
        return recordEntryTimestamp;
    }

    public Timestamp getRecordUpdateTimestamp() {
        return recordUpdateTimestamp;
    }

    public boolean isActivationStatus() {
        return activationStatus;
    }

}
