package dev.seariver.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record Event(
    int id,
    String chatGroupJid,
    LocalDate eventDate,
    LocalTime startAt,
    LocalTime endAt,
    String template,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
