package com.igihecyubuntu.app.dto.projection;

import java.time.LocalDateTime;

public interface PictureProjection {
    Long getId();
    String getType();
    String getPath();
    String getAltText();
    LocalDateTime getDateTime();
    Long getDoneBy();
}