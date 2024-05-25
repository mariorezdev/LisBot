package dev.seariver.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddCommandTest {

    @Test
    void GIVEN_valid_text_command_WHEN_check_its_mine_MUST_return_true() {

        // GIVEN
        var text = "/a";

        // WHEN
        var addCommand = new AddCommand();
        var itsMine = addCommand.itsMine(text);

        // THEN
        assertThat(itsMine).isTrue();
    }
}
