package com.bar.gestiondesfichier.common.projection;

/**
 * Named projection interface for entities with name and description
 */
public interface NamedProjection extends BaseProjection {
    String getName();
    String getDescription();
}