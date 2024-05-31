package dev.seariver.model;

public record Person(
    int id,
    int eventId,
    String senderJid,
    String slug,
    String name
) {
}
