package dev.seariver.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    public String weekDay() {
        return switch (eventDate.getDayOfWeek().name()) {
            case "MONDAY" -> "SEGUNDA";
            case "TUESDAY" -> "TERÇA";
            case "WEDNESDAY" -> "QUARTA";
            case "THURSDAY" -> "QUINTA";
            case "FRIDAY" -> "SEXTA";
            case "SATURDAY" -> "SÁBADO";
            case "SUNDAY" -> "DOMINGO";
            default -> "";
        };
    }

    public String date() {
        return eventDate().format(DateTimeFormatter.ofPattern("dd/MM"));
    }

    public String start() {
        return startAt().format(DateTimeFormatter.ofPattern("HH'h'mm"));
    }

    public String end() {
        return endAt().format(DateTimeFormatter.ofPattern("HH'h'mm"));
    }
}
