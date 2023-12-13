package com.ezy.rewards.restapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ezy.rewards.restapi.service.entity.DingAPI;
@Repository
public interface DingRespRepository extends CrudRepository<DingAPI, Integer> {
    public Iterable<DingAPI> findByApiName(String apiName);

    public Iterable<DingAPI> deleteByApiName(String apiName);
}