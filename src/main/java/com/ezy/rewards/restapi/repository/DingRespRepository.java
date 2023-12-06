package com.ezy.rewards.restapi.repository;

import org.springframework.data.repository.CrudRepository;
import com.ezy.rewards.restapi.service.model.DingResp;

public interface DingRespRepository extends CrudRepository<DingResp, Integer> {
    public Iterable<DingResp> findByApiName(String apiName);

    public Iterable<DingResp> deleteByApiName(String apiName);
}
