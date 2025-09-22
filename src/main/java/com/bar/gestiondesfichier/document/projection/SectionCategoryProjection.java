package com.bar.gestiondesfichier.document.projection;

/**
 * SectionCategory projection for listing section categories
 */
public interface SectionCategoryProjection {
    Long getId();
    String getName();
    String getDescription();
    boolean isActive();
}