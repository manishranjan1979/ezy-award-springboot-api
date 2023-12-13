package com.ezy.rewards.restapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ezy.rewards.restapi.service.entity.country.InternationalDialingInformation;

@Repository
public interface InternationalDialingInformationRepository
        extends CrudRepository<InternationalDialingInformation, Integer> {

}
