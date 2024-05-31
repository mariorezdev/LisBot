package dev.seariver;

import dev.seariver.command.ListCommand;
import dev.seariver.helper.DatabaseHelper;
import it.auties.whatsapp.api.Whatsapp;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandListenerTest extends DatabaseHelper {

    Whatsapp whatsapp = mock(Whatsapp.class);
    ListCommand listCommand = mock(ListCommand.class);

    @Test
    void GIVEN_text_command_FROM_registered_chat_WHEN_new_message_arrives_MUST_execute_related_command() {

        // GIVEN
        var messageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "551295656565656@s.whatsapp.net",
            "Anyone",
            "/l");
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
        var messageInfo = getChatMessageInfo(
            "121212121212121212@g.us",
            "551295656565656@s.whatsapp.net",
            "Anyone",
            "/l");
        when(listCommand.itsMine(anyString())).thenReturn(true);

        var commandListener = new CommandListener(repository);
        commandListener.addCommands(listCommand);

        // WHEN
        commandListener.onNewMessage(whatsapp, messageInfo);

        // THEN
        verify(listCommand, never()).execute(any(NewMessage.class));
    }
}
