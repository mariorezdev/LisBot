package dev.seariver.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.seariver.Repository;
import it.auties.whatsapp.model.contact.ContactBuilder;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.ChatMessageInfoBuilder;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.model.ChatMessageKeyBuilder;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.model.MessageContainerBuilder;
import it.auties.whatsapp.model.message.standard.TextMessageBuilder;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

public class DatabaseHelper extends TestHelper {

    protected static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16");
    protected static Repository repository;
    protected static DataSource dataSource;
    protected static boolean useContainer = true;

    @BeforeAll
    static void setup() {
        if (useContainer) {
            container.start();
            var hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(container.getJdbcUrl());
            hikariConfig.setUsername(container.getUsername());
            hikariConfig.setPassword(container.getPassword());
            hikariConfig.setDriverClassName(container.getDriverClassName());
            dataSource = new HikariDataSource(hikariConfig);
        } else {
            var url = "jdbc:h2:mem:TEST;" +
                "MODE=PostgreSQL;";
            dataSource = JdbcConnectionPool.create(url, "sa", "sa");
        }

        repository = new Repository(dataSource);
    }

    @BeforeEach
    void cleanup() {
        var flyway = Flyway.configure()
            .cleanDisabled(false)
            .dataSource(dataSource)
            .load();
        flyway.clean();
        flyway.migrate();
    }

    public ChatMessageInfo getChatMessageInfo(String chatJid,
                                              String senderJid,
                                              String senderName,
                                              String text) {

        var chatMessageInfo = new ChatMessageInfoBuilder()
            .key(new ChatMessageKeyBuilder()
                .chatJid(Jid.of(chatJid))
                .build())
            .senderJid(Jid.of(senderJid))
            .message(getMessageContainer(text))
            .build();
        chatMessageInfo.setSender(new ContactBuilder()
            .shortName(senderName)
            .jid(Jid.of(senderJid))
            .build());

        return chatMessageInfo;
    }

    public MessageContainer getMessageContainer(String text) {
        var textMessage = new TextMessageBuilder()
            .text(text)
            .build();
        return new MessageContainerBuilder()
            .textMessage(textMessage)
            .build();
    }

    public static String translateWeekDay(String weekDay) {
        return switch (weekDay) {
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
}
