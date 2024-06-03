package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.helper.DatabaseHelper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


class ListCommandTest extends DatabaseHelper {

    @Test
    void GIVEN_valid_text_command_WHEN_check_its_mine_MUST_return_true() {

        // GIVEN
        var text = "/l";

        // WHEN
        var listCommand = new ListCommand(repository);
        var itsMine = listCommand.itsMine(text);

        // THEN
        assertThat(itsMine).isTrue();
    }

    @Test
    void GIVEN_new_message_WHEN_group_has_event_MUST_return_only_next_event() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "5511922222222:22@s.whatsapp.net",
            "Sicrana",
            "/l"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var listCommand = new ListCommand(repository);
        listCommand.run(newMessage);

        // THEN
        var expected = """
            ID: -201 | *GRUPO 2 ATUAL - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            01 - Fulana de Tal
            *02 - Sicrana*
            03 - Beltrana"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_new_message_WHEN_event_not_exist_MUST_return_no_event_message() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "000000000000000000@g.us",
            "5511912345678:12@s.whatsapp.net",
            "Someone",
            "/l"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var listCommand = new ListCommand(repository);
        listCommand.run(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo("Sem eventos programados");
    }

    @Test
    void GIVEN_new_message_WHEN_has_event_WITH_no_people_MUST_return_empty_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "5511912345678:12@s.whatsapp.net",
            "Someone",
            "/l"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var listCommand = new ListCommand(repository);
        listCommand.run(newMessage);

        // THEN
        var expected = """
            ID: -100 | *GRUPO 1 - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            """
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }
}
