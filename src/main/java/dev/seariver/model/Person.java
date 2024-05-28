package dev.seariver.model;

import java.time.LocalDateTime;

public record Person(
    String jid,
    int eventId,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
