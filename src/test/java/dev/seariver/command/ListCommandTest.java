package dev.seariver.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ListCommandTest {

    @Test
    void GIVEN_valid_text_command_WHEN_check_its_mine_MUST_return_true() {

        // GIVEN
        var text = "/l";

        // WHEN
        var listCommand = new ListCommand();
        var itsMine = listCommand.itsMine(text);

        // THEN
        assertThat(itsMine).isTrue();
    }
}
