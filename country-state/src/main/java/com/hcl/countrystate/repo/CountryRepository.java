package com.hcl.countrystate.repo;

import com.hcl.countrystate.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "countries", path = "countries")
public interface CountryRepository extends JpaRepository<Country, Integer> {
//    List<State> findByCountryCode(@Param("code") String code);
}

