package dev.seariver.model;

public record Person(
    int id,
    int eventId,
    String senderPhone,
    boolean isSender,
    String slug,
    String name
) {
}
