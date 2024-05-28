package dev.seariver;

import dev.seariver.model.Event;
import dev.seariver.model.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Repository {

    private final DataSource datasource;

    public Repository(DataSource datasource) {
        this.datasource = datasource;
    }

    public void listNextEvent(NewMessage newMessage) {

        newMessage.response("Sem eventos programados");

        var nextEvent = findNextEvent(newMessage);

        if (nextEvent.isEmpty()) return;

        var event = nextEvent.get();

        var persons = findPersonList(event.id());

        var personList = IntStream
            .range(0, persons.size())
            .mapToObj(i -> {
                var row = newMessage.senderJid().toString().equals(persons.get(i).jid())
                    ? "*%02d - %s*"
                    : "%02d - %s";
                return row.formatted(i + 1, persons.get(i).name());
            })
            .collect(Collectors.joining("\n"));

        var response = event.template()
            .replace("#ID", String.valueOf(event.id()))
            .replace("#DATE", event.eventDate().format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#START_AT", event.startAt().format(DateTimeFormatter.ofPattern("HH'h'mm")))
            .replace("#END_AT", event.endAt().format(DateTimeFormatter.ofPattern("HH'h'mm")))
            .replace("#PERSON_LIST", personList);

        newMessage.response(response);
    }

    public void addPersonOnNextEvent(NewMessage newMessage) {

        newMessage.reply("Sem eventos programados");

        var nextEvent = findNextEvent(newMessage);

        if (nextEvent.isEmpty()) return;

        var event = nextEvent.get();

        var sql = """
            INSERT INTO person 
            (jid, event_id, name, created_at, updated_at) 
            values (?, ?, ?, ?, ?)""";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newMessage.senderJid().toString());
            stmt.setInt(2, event.id());
            stmt.setString(3, newMessage.senderName());
            stmt.setTimestamp(4, Timestamp.from(Instant.now()));
            stmt.setTimestamp(5, Timestamp.from(Instant.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listNextEvent(newMessage);
    }

    public boolean isRegisteredChat(NewMessage newMessage) {

        var result = false;

        String sql = "SELECT jid FROM chat_group WHERE jid = ?";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, newMessage.chatJid().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Optional<Event> findNextEvent(NewMessage newMessage) {

        Optional<Event> result = Optional.empty();

        String sql = """
            SELECT
                id,
                chat_group_jid,
                event_date,
                start_at,
                end_at,
                template,
                created_at,
                updated_at
            FROM event 
            WHERE chat_group_jid = ? 
            AND event_date >= CURRENT_DATE
            ORDER BY event_date DESC
            LIMIT 1""";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, newMessage.chatJid().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result = Optional.of(new Event(
                        rs.getInt("id"),
                        rs.getString("chat_group_jid"),
                        rs.getDate("event_date").toLocalDate(),
                        rs.getTime("start_at").toLocalTime(),
                        rs.getTime("end_at").toLocalTime(),
                        rs.getString("template"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Person> findPersonList(int eventId) {

        var result = new ArrayList<Person>();

        String sql = """
            SELECT
                jid,
                event_id,
                name,
                created_at,
                updated_at
            FROM person
            WHERE event_id = ?
            ORDER BY created_at ASC""";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new Person(
                        rs.getString("jid"),
                        rs.getInt("event_id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
