package com.ezy.rewards.restapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ezy.rewards.restapi.service.entity.region.Region;

@Repository
public interface RegionRepository extends CrudRepository<Region, Integer> {
    public Iterable<Region> findByCountryIsoIn(List<String> CountryIso);
}
