package dev.seariver.model;

import java.text.Normalizer;
import java.time.LocalDateTime;

public record Person(
    int id,
    int eventId,
    String senderJid,
    String slug,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public Person {
        if (null != slug) {
            slug = slugify(slug);
        }
    }

    public static String slugify(String input) {
        String lowerCased = input.toLowerCase();
        // remove accents
        String normalized = Normalizer.normalize(lowerCased, Normalizer.Form.NFD);
        String noAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        // replace non alfa-numeric
        String slug = noAccents.replaceAll("[^\\p{Alnum}]+", "_");
        // remove start and end underscore
        slug = slug.replaceAll("^_+|_+$", "");

        return slug;
    }
}
