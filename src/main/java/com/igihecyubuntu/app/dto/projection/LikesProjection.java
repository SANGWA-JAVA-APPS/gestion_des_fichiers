package com.igihecyubuntu.app.dto.projection;

import java.time.LocalDateTime;

public interface LikesProjection {
    Long getId();
    Long getPostId();
    LocalDateTime getDateTime();
    Long getDoneBy();
}