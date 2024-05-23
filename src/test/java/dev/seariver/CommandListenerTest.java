package dev.seariver;

import dev.seariver.command.CommandManager;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.message.model.MessageContainerBuilder;
import it.auties.whatsapp.model.message.standard.TextMessageBuilder;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandListenerTest {

    Whatsapp whatsapp = mock(Whatsapp.class);
    MessageInfo messageInfo = mock(ChatMessageInfo.class);
    CommandManager commandManager = spy(CommandManager.instance());

    @Test
    void GIVEN_valid_message_WHE_new_message_arrives_MUST_call_command_manager() {

        // GIVEN
        var textMessage = new TextMessageBuilder()
            .text("/l")
            .build();
        var messageContainer = new MessageContainerBuilder()
            .textMessage(textMessage)
            .build();
        when(messageInfo.message()).thenReturn(messageContainer);

        // WHEN
        var listener = new CommandListener(commandManager);
        listener.onNewMessage(whatsapp, messageInfo);

        // THEN
        verify(commandManager).findCommand(anyString());
    }
}