package com.bar.gestiondesfichier.common.projection;

import java.time.LocalDateTime;

/**
 * Base projection interface for all entities
 */
public interface BaseProjection {
    Long getId();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    boolean isActive();
}