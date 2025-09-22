package com.bar.gestiondesfichier.document.projection;

/**
 * DocStatus projection for listing document statuses
 */
public interface DocStatusProjection {
    Long getId();
    String getName();
    String getDescription();
    boolean isActive();
}