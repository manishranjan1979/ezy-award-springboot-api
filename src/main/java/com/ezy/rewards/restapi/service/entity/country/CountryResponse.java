package com.ezy.rewards.restapi.service.entity.country;

import java.util.List;

import com.ezy.rewards.restapi.service.entity.ErrorCodes;

public class CountryResponse {
    private Integer ResultCode;
    private List<ErrorCodes> ErrorCodes;
    private List<Country> Items;

    public CountryResponse() {
    }

    public CountryResponse(Integer resultCode, List<com.ezy.rewards.restapi.service.entity.ErrorCodes> errorCodes,
            List<Country> items) {
        ResultCode = resultCode;
        ErrorCodes = errorCodes;
        Items = items;
    }

    public Integer getResultCode() {
        return ResultCode;
    }

    public void setResultCode(Integer resultCode) {
        ResultCode = resultCode;
    }

    public List<ErrorCodes> getErrorCodes() {
        return ErrorCodes;
    }

    public void setErrorCodes(List<ErrorCodes> errorCodes) {
        ErrorCodes = errorCodes;
    }

    public List<Country> getItems() {
        return Items;
    }

    public void setItems(List<Country> items) {
        Items = items;
    }

}
