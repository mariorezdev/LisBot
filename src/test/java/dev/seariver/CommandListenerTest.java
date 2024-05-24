package dev.seariver;

import dev.seariver.command.CommandBus;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.message.model.MessageContainerBuilder;
import it.auties.whatsapp.model.message.standard.TextMessageBuilder;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandListenerTest {

    Whatsapp whatsapp = mock(Whatsapp.class);
    MessageInfo messageInfo = mock(ChatMessageInfo.class);
    CommandBus commandBus = spy(CommandBus.instance());

    @Test
    void GIVEN_valid_message_WHEN_new_message_arrives_MUST_call_command_bus() {

        // GIVEN
        var textMessage = new TextMessageBuilder()
            .text("/l")
            .build();
        var messageContainer = new MessageContainerBuilder()
            .textMessage(textMessage)
            .build();
        when(messageInfo.message()).thenReturn(messageContainer);

        // WHEN
        var listener = new CommandListener(commandBus);
        listener.onNewMessage(whatsapp, messageInfo);

        // THEN
        verify(commandBus).execute(any(Event.class));
    }
}