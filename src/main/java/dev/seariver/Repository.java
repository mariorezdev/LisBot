package dev.seariver;

import dev.seariver.model.Event;
import dev.seariver.model.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.out;

public class Repository {

    private final DataSource datasource;

    public Repository(DataSource datasource) {
        this.datasource = datasource;
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
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        return result;
    }

    public void listNextEvent(NewMessage newMessage) {

        newMessage.response("Sem eventos programados");

        var nextEvent = findNextEvent(newMessage);

        if (nextEvent.isEmpty()) return;

        var event = nextEvent.get();
        var persons = event.persons();
        var personList = IntStream
            .range(0, persons.size())
            .mapToObj(i -> {
                var row = newMessage.names().containsKey(persons.get(i).slug())
                    ? "*%02d - %s*"
                    : "%02d - %s";
                return row.formatted(i + 1, persons.get(i).name());
            })
            .collect(Collectors.joining("\n"));

        var response = event.template()
            .replace("#ID", String.valueOf(event.id()))
            .replace("#WEEK_DAY", event.weekDay())
            .replace("#DATE", event.date())
            .replace("#START_AT", event.start())
            .replace("#END_AT", event.end())
            .replace("#PERSON_LIST", personList);

        newMessage.response(response);
    }

    public Optional<Event> findNextEvent(NewMessage newMessage) {

        Optional<Event> result = Optional.empty();

        String sql = """
            select
                e.id as event_id,
                e.chat_group_jid,
                e.event_date,
                e.start_at,
                e.end_at,
                e.template,
                p.id as person_id,
                p.event_id as person_event_id,
                p.sender_phone,
                p.is_sender,
                p.slug,
                p.name
            from
               (select id, chat_group_jid, event_date, start_at, end_at, template
                from event
                where
                   chat_group_jid = ? and
                   event_date >= current_date
                order by event_date asc
                limit 1) e
            left join person p on e.id = p.event_id
            order by p.id asc""";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, newMessage.chatJid().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                var persons = new ArrayList<Person>();
                while (rs.next()) {
                    if (rs.getInt("person_id") != 0) {
                        persons.add(new Person(
                            rs.getInt("person_id"),
                            rs.getInt("person_event_id"),
                            rs.getString("sender_phone"),
                            rs.getBoolean("is_sender"),
                            rs.getString("slug"),
                            rs.getString("name")
                        ));
                    }
                    if (rs.isLast()) {
                        result = Optional.of(new Event(
                            rs.getInt("event_id"),
                            rs.getString("chat_group_jid"),
                            rs.getDate("event_date").toLocalDate(),
                            rs.getTime("start_at").toLocalTime(),
                            rs.getTime("end_at").toLocalTime(),
                            rs.getString("template"),
                            persons));
                    }
                }
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        return result;
    }

    public boolean addPersonOnNextEvent(NewMessage newMessage, String name) {

        newMessage.response("Sem eventos programados");

        var nextEvent = findNextEvent(newMessage);

        if (nextEvent.isEmpty()) return false;

        if (newMessage.slug(name).isEmpty()) {
            newMessage.response("""
                Desculpe 🥲 Nao consigo adicionar: %s

                Tente desta forma:
                `/a Nome` Troque a palavra `Nome` pelo nome que desejar.""".formatted(name));

            return false;
        }

        var event = nextEvent.get();

        var sql = """
            INSERT INTO person 
            (event_id, sender_phone, is_sender, slug, name) 
            values (?, ?, ?, ?, ?)""";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, event.id());
            stmt.setString(2, newMessage.senderPhone());
            stmt.setBoolean(3, newMessage.isSenderName());
            stmt.setString(4, newMessage.slug(name));
            stmt.setString(5, newMessage.normalize(name));
            stmt.executeUpdate();
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        return true;
    }

    public boolean removeSender(NewMessage newMessage) {

        newMessage.response("Sem eventos programados");

        var nextEvent = findNextEvent(newMessage);

        if (nextEvent.isEmpty()) return false;

        newMessage.response("Ops \uD83E\uDEE5 %s nao esta na lista"
            .formatted(newMessage.senderNormalized()));

        var event = nextEvent.get();

        var optionalPerson = event.persons().stream()
            .filter(p -> p.isSender() && p.senderPhone().equals(newMessage.senderPhone()))
            .findFirst();

        if (optionalPerson.isEmpty()) return false;

        var sql = """
            DELETE FROM person
            WHERE id = %d;"""
            .formatted(optionalPerson.get().id());

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        return true;
    }

    public boolean removeByName(NewMessage newMessage, String name) {

        newMessage.response("Sem eventos programados");

        var nextEvent = findNextEvent(newMessage);

        if (nextEvent.isEmpty()) return false;

        newMessage.response("Ops \uD83E\uDEE5 %s nao esta na lista"
            .formatted(newMessage.senderNormalized()));

        var event = nextEvent.get();

        var optionalPerson = event.persons().stream()
            .filter(p -> p.slug().equals(newMessage.slug(name)))
            .findFirst();

        if (optionalPerson.isEmpty()) return false;

        var sql = """
            DELETE FROM person
            WHERE
                event_id = %d AND
                slug = '%s';"""
            .formatted(event.id(), optionalPerson.get().slug());

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        return true;
    }
}
