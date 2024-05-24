package dev.seariver.command;

import dev.seariver.Event;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.message.model.MessageContainerBuilder;
import it.auties.whatsapp.model.message.standard.TextMessageBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandBusTest {

    Whatsapp whatsapp = mock(Whatsapp.class);
    MessageInfo messageInfo = mock(ChatMessageInfo.class);
    static ListCommand listCommand = spy(new ListCommand());
    static CommandBus commandBus = CommandBus.instance();

    @BeforeAll
    static void setup() {
        doNothing().when(listCommand).execute(any(Event.class));
        commandBus.addCommands(listCommand);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/l",
        "/L",
        "/l ",
        " /l",
        " /l  ",
        " /L  ",
    })
    void GIVEN_valid_text_command_WHEN_bus_execute_event_MUST_call_correspondent_command(String textCommand) {

        // GIVEN
        var textMessage = new TextMessageBuilder()
            .text(textCommand)
            .build();
        var messageContainer = new MessageContainerBuilder()
            .textMessage(textMessage)
            .build();
        when(messageInfo.message()).thenReturn(messageContainer);
        var event = new Event(whatsapp, messageInfo);

        // WHEN
        commandBus.execute(event);

        // THEN
        verify(listCommand).execute(event);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {
        "   ",
        "l",
        "L",
        "//l",
        "/x",
    })
    void GIVEN_invalid_text_command_WHEN_bus_execute_event_CANT_call_any_command(String textCommand) {

        // GIVEN
        var textMessage = new TextMessageBuilder()
            .text(textCommand)
            .build();
        var messageContainer = new MessageContainerBuilder()
            .textMessage(textMessage)
            .build();
        when(messageInfo.message()).thenReturn(messageContainer);
        var event = new Event(whatsapp, messageInfo);

        // WHEN
        commandBus.execute(event);

        // THEN
        verify(listCommand, never()).execute(event);
    }
}