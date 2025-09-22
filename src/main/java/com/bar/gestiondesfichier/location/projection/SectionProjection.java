package com.bar.gestiondesfichier.location.projection;

import com.bar.gestiondesfichier.common.projection.NamedProjection;
import com.bar.gestiondesfichier.location.model.Section.SectionType;
import com.bar.gestiondesfichier.location.model.Section.AccessLevel;

public interface SectionProjection extends NamedProjection {
    String getSectionCode();
    SectionType getSectionType();
    Integer getFloorNumber();
    String getRoomNumber();
    Integer getCapacity();
    String getCoordinates();
    AccessLevel getAccessLevel();
    ModuleProjection getModule();
}