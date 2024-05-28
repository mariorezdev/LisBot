package dev.seariver.helper;

import dev.seariver.Repository;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.model.MessageContainerBuilder;
import it.auties.whatsapp.model.message.standard.TextMessageBuilder;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;

public class TestHelper {

    protected Repository repository;

    @BeforeEach
    void setup() {
        var url = "jdbc:h2:mem:TEST_" + new Random().nextInt() + ";" +
            "MODE=PostgreSQL;" +
            "INIT=RUNSCRIPT FROM 'src/main/resources/01_initial_setup.sql'\\;" +
            "RUNSCRIPT FROM 'classpath:dataset.sql'\\;";
        var dataSource = JdbcConnectionPool.create(url, "sa", "sa");
        repository = new Repository(dataSource);
    }

    protected MessageContainer getMessageContainer(String text) {
        var textMessage = new TextMessageBuilder()
            .text(text)
            .build();
        return new MessageContainerBuilder()
            .textMessage(textMessage)
            .build();
    }
}
