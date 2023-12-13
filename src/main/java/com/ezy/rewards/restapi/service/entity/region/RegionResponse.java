package com.ezy.rewards.restapi.service.entity.region;

import java.util.List;

import com.ezy.rewards.restapi.service.entity.ErrorCodes;

public class RegionResponse {
    private int ResultCode;
    private ErrorCodes ErrorCodes[];
    private List<Region> Items;

    public RegionResponse() {
    }

    public RegionResponse(int resultCode, ErrorCodes[] errorCodes,
            List<Region> Items) {
        this.ResultCode = resultCode;
        this.ErrorCodes = errorCodes;
        this.Items = Items;
    }

    public int getResultCode() {
        return this.ResultCode;
    }

    public void setResultCode(int resultCode) {
        this.ResultCode = resultCode;
    }

    public List<Region> getItens() {
        return this.Items;
    }

    public void setItens(List<Region> Items) {
        this.Items = Items;
    }

    public ErrorCodes[] getErrorCodes() {
        return this.ErrorCodes;
    }

    public void setErrorCodes(ErrorCodes[] errorCodes) {
        this.ErrorCodes = errorCodes;
    }

}
