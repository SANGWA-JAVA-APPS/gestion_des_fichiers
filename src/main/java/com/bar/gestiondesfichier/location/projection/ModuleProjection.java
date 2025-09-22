package com.bar.gestiondesfichier.location.projection;

import com.bar.gestiondesfichier.common.projection.NamedProjection;
import com.bar.gestiondesfichier.location.model.Module.ModuleType;

public interface ModuleProjection extends NamedProjection {
    String getModuleCode();
    ModuleType getModuleType();
    String getCoordinates();
    Double getAreaSize();
    String getAreaUnit();
    LocationEntityProjection getLocationEntity();
    long getSectionsCount();
}