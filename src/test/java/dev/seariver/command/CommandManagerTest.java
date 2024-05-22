package dev.seariver.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommandManagerTest {

    @Test
    void GIVEN_valida_command_WHEN_find_command_MUST_return_command_instance() {

        // GIVEN
        var textCommand = "/l";
        CommandManager.instance().addCommands(new ListCommand());

        // WHEN
        var optionalCommand = CommandManager.instance().findCommand(textCommand);

        // THEN
        assertThat(optionalCommand).isPresent();
    }
}