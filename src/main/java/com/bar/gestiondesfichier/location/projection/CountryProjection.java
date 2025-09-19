package com.bar.gestiondesfichier.location.projection;

import com.bar.gestiondesfichier.common.projection.NamedProjection;

public interface CountryProjection extends NamedProjection {
    String getIsoCode();
    String getPhoneCode();
    String getFlagUrl();
    long getEntitiesCount();
}