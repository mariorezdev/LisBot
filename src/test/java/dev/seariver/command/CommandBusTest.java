package dev.seariver.command;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommandBusTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "/l",
        "/L",
        "/l ",
        " /l",
        " /l  ",
        " /L  ",
    })
    void GIVEN_valid_command_WHEN_find_command_MUST_return_present(String validAlias) {

        // GIVEN
        CommandBus.instance().addCommands(new ListCommand());

        // WHEN
        var optionalCommand = CommandBus.instance().findCommand(validAlias);

        // THEN
        assertThat(optionalCommand)
            .isPresent()
            .get()
            .isInstanceOf(ListCommand.class);
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
    void GIVEN_invalid_or_nonexistent_command_WHEN_find_command_MUST_return_empty(String textCommand) {

        // GIVEN
        CommandBus.instance().addCommands(new ListCommand());

        // WHEN
        var optionalCommand = CommandBus.instance().findCommand(textCommand);

        // THEN
        assertThat(optionalCommand).isEmpty();
    }
}