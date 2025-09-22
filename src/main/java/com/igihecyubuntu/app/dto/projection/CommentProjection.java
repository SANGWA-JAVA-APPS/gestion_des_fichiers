package com.igihecyubuntu.app.dto.projection;

public interface CommentProjection {
    Long getId();
    String getTitle();
    String getContent();
    Long getDoneBy();
}