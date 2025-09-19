package com.bar.gestiondesfichier.location.service;

import com.bar.gestiondesfichier.common.service.BaseService;
import com.bar.gestiondesfichier.location.model.Country;
import com.bar.gestiondesfichier.location.projection.CountryProjection;

import java.util.List;
import java.util.Optional;

public interface CountryService extends BaseService<Country> {
    
    Optional<Country> findByIsoCode(String isoCode);
    
    Optional<Country> findByName(String name);
    
    List<Country> searchByName(String name);
    
    List<CountryProjection> findAllProjections();
    
    Optional<CountryProjection> findProjectionById(Long id);
    
    boolean existsByIsoCode(String isoCode);
    
    boolean existsByName(String name);
    
    Country createCountry(String name, String description, String isoCode, String phoneCode, String flagUrl);
    
    Country updateCountry(Long id, String name, String description, String isoCode, String phoneCode, String flagUrl);
}