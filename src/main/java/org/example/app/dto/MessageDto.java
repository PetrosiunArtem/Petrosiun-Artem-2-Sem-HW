package org.example.app.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class MessageDto {
    Long id;
    Instant eventTime;
    Action action;
    String eventDetails;

    @JsonCreator
    public MessageDto(
            @JsonProperty("id") Long id,
            @JsonProperty("eventTime") Instant eventTime,
            @JsonProperty("action") Action action,
            @JsonProperty("eventDetails") String eventDetails) {
        this.id = id;
        this.eventTime = eventTime;
        this.action = action;
        this.eventDetails = eventDetails;
    }
}
