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

class OneForAllListenerTest {

    Whatsapp api = mock(Whatsapp.class);
    MessageInfo messageInfo = mock(ChatMessageInfo.class);
    CommandManager commandManager = spy(CommandManager.instance());

    @Test
    void name_placeholder() {

        // GIVEN
        var textMessage = new TextMessageBuilder()
            .text("/l")
            .build();
        var messageContainer = new MessageContainerBuilder()
            .textMessage(textMessage)
            .build();
        when(messageInfo.message()).thenReturn(messageContainer);

        // WHEN
        var listener = new OneForAllListener(commandManager);
        listener.onNewMessage(api, messageInfo);

        // THEN
        verify(commandManager).findCommand(anyString());
    }
}