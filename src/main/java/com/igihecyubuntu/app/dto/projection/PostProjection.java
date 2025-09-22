package com.igihecyubuntu.app.dto.projection;

import java.time.LocalDateTime;

public interface PostProjection {
    Long getId();
    LocalDateTime getDateTime();
    Long getBlogId();
    Long getDoneBy();
    String getBlogTitle();
}