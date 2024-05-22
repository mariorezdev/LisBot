package dev.seariver.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommandManagerTest {

    @Test
    void GIVEN_valid_command_WHEN_find_command_MUST_return_present() {

        // GIVEN
        var textCommand = "/l";
        CommandManager.instance().addCommands(new ListCommand());

        // WHEN
        var optionalCommand = CommandManager.instance().findCommand(textCommand);

        // THEN
        assertThat(optionalCommand).isPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
        "  ",
        "l",
        "//l",
        "/x",
    })
    void GIVEN_invalid_or_nonexistent_command_WHEN_find_command_MUST_return_empty(String textCommand) {

        // GIVEN
        CommandManager.instance().addCommands(new ListCommand());

        // WHEN
        var optionalCommand = CommandManager.instance().findCommand(textCommand);

        // THEN
        assertThat(optionalCommand).isEmpty();
    }
}