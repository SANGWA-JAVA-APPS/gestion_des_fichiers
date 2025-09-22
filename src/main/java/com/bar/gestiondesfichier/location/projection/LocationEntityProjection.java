package com.bar.gestiondesfichier.location.projection;

import com.bar.gestiondesfichier.common.projection.NamedProjection;
import com.bar.gestiondesfichier.location.model.LocationEntity.EntityType;

public interface LocationEntityProjection extends NamedProjection {
    EntityType getEntityType();
    String getCode();
    String getPostalCode();
    CountryProjection getCountry();
    long getModulesCount();
}