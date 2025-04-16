package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MessageDto {
    Long id;
    Instant eventTime;
    Action action;
    String eventDetails;
}
