package dev.seariver;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public boolean isRegisteredChat(String jid) {

        var result = false;

        String sql = "SELECT jid FROM chat_group WHERE jid = ?";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, jid);
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
}
