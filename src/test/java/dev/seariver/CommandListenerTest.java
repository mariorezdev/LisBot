package dev.seariver;

import dev.seariver.command.ListCommand;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.model.MessageContainerBuilder;
import it.auties.whatsapp.model.message.standard.TextMessageBuilder;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandListenerTest {

    Whatsapp whatsapp = mock(Whatsapp.class);
    MessageInfo messageInfo = mock(ChatMessageInfo.class);
    ListCommand listCommand = mock(ListCommand.class);
    Repository repository;

    @BeforeEach
    void setup() {
        var url = "jdbc:h2:mem:TEST;" +
            "MODE=PostgreSQL;" +
            "INIT=RUNSCRIPT FROM 'src/main/resources/01_initial_setup.sql'\\;" +
            "RUNSCRIPT FROM 'classpath:dataset.sql'\\;";
        var dataSource = JdbcConnectionPool.create(url, "sa", "sa");
        repository = new Repository(dataSource);
    }

    @Test
    void GIVEN_text_command_FROM_registered_chat_WHEN_new_message_arrives_MUST_execute_related_command() {

        // GIVEN
        when(messageInfo.message()).thenReturn(getMessageContainer("/l"));
        when(messageInfo.parentJid()).thenReturn(Jid.of("121212121212121212@g.us"));
        when(listCommand.itsMine(anyString())).thenReturn(true);

        var commandListener = new CommandListener(repository);
        commandListener.addCommands(listCommand);

        // WHEN
        commandListener.onNewMessage(whatsapp, messageInfo);

        // THEN
        verify(listCommand).execute(any(NewMessage.class));
    }

    @Test
    void GIVEN_chat_not_registered_WHEN_new_message_arrives_MUST_do_nothing() {

        // GIVEN
        when(messageInfo.message()).thenReturn(getMessageContainer("/l"));
        when(messageInfo.parentJid()).thenReturn(Jid.of("xxxxxxxxxxxxxxxxx@g.us"));
        when(listCommand.itsMine(anyString())).thenReturn(true);

        var commandListener = new CommandListener(repository);
        commandListener.addCommands(listCommand);

        // WHEN
        commandListener.onNewMessage(whatsapp, messageInfo);

        // THEN
        verify(listCommand, never()).execute(any(NewMessage.class));
    }

    private MessageContainer getMessageContainer(String text) {
        var textMessage = new TextMessageBuilder()
            .text(text)
            .build();
        return new MessageContainerBuilder()
            .textMessage(textMessage)
            .build();
    }
}
