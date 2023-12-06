package com.ezy.rewards.restapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ezy.rewards.restapi.service.model.Region;

public interface RegionRepository extends CrudRepository<Region, Integer> {
    public Iterable<Region> findByCountryIsoIn(List<String> CountryIso);
}
