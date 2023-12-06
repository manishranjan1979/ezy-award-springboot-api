package com.ezy.rewards.restapi.service.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DingResp {
    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // private Integer dingRespId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer apiId;
    private String apiName;
    private String fullResp;
    private String respItem;

    private Timestamp recordEntryTimestamp;

    private Timestamp recordUpdateTimestamp;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private boolean activationStatus;

    public DingResp() {
        System.out.println("Inside DingResp Default Controller..");
        System.out.println("Leaving DingResp Default Controller..");
    }

    public DingResp(String apiName, String fullResp, String respItem, Timestamp recordEntryTimestamp,
            Timestamp recordUpdateTimestamp, boolean activationStatus) {
        this.apiName = apiName;
        this.fullResp = fullResp;
        this.respItem = respItem;
        this.recordEntryTimestamp = recordEntryTimestamp;
        this.recordUpdateTimestamp = recordUpdateTimestamp;
        this.activationStatus = activationStatus;
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

    public void setRecordEntryTimestamp(Timestamp recordEntryTimestamp) {
        this.recordEntryTimestamp = recordEntryTimestamp;
    }

    public Timestamp getRecordEntryTimestamp() {
        return recordEntryTimestamp;
    }

    public void setRecordUpdateTimestamp(Timestamp recordUpdateTimestamp) {
        this.recordUpdateTimestamp = recordUpdateTimestamp;
    }

    public Timestamp getRecordUpdateTimestamp() {
        return recordUpdateTimestamp;
    }

    public void isActivationStatus(boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public boolean isActivationStatus() {
        return activationStatus;
    }

}
