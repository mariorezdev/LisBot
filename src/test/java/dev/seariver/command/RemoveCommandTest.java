package dev.seariver.command;

import dev.seariver.helper.DatabaseHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RemoveCommandTest extends DatabaseHelper {

    @Test
    void GIVEN_valid_text_command_WHEN_check_its_mine_MUST_return_true() {

        // GIVEN
        var text = "/r";

        // WHEN
        var addCommand = new RemoveCommand(repository);
        var itsMine = addCommand.itsMine(text);

        // THEN
        assertThat(itsMine).isTrue();
    }
}
