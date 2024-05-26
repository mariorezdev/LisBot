package dev.seariver;

import dev.seariver.model.Event;
import dev.seariver.model.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Repository {

    private final DataSource datasource;

    public Repository(DataSource datasource) {
        this.datasource = datasource;
    }

    public void create() {
        String sql = "INSERT INTO shortener (source_url, short_code) values (?, ?)";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "");
            stmt.setString(2, "");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegisteredChat(String chatJid) {

        var result = false;

        String sql = "SELECT jid FROM chat_group WHERE jid = ?";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, chatJid);
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

    public Optional<Event> findNextEvent(String chatJid) {

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
            FROM event WHERE chat_group_jid = ?""";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, chatJid);
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
                phone_number,
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
                        rs.getString("phone_number"),
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
