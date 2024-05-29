package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.helper.TestHelper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class AddCommandTest extends TestHelper {

    @Test
    void GIVEN_valid_text_command_WHEN_check_its_mine_MUST_return_true() {

        // GIVEN
        var text = "/a";

        // WHEN
        var addCommand = new AddCommand(repository);
        var itsMine = addCommand.itsMine(text);

        // THEN
        assertThat(itsMine).isTrue();
    }

    @Test
    void GIVEN_chat_jid_WHEN_add_new_person_MUST_return_updated_event_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "5511912345678:12@s.whatsapp.net",
            "Novata",
            "/a"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.execute(newMessage);

        // THEN
        var expected = """
            ID: 2 | *PROXIMO EVENTO - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            01 - Fulana de Tal
            02 - Sicrana
            03 - Beltrana
            *04 - Novata*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_chat_jid_WHEN_add_existent_person_MUST_return_event_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "5511922222222:22@s.whatsapp.net",
            "Sicrana",
            "/a"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.execute(newMessage);

        // THEN
        var expected = """
            ID: 2 | *PROXIMO EVENTO - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            01 - Fulana de Tal
            *02 - Sicrana*
            03 - Beltrana"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_two_message_WHEN_add_same_person_in_different_events_MUST_return_success() {

        // GIVEN
        var newMessage1 = new NewMessage(getChatMessageInfo(
            "111111111111111111@g.us",
            "5511922222222:22@s.whatsapp.net",
            "Sicrana",
            "/a"));
        var newMessage2 = new NewMessage(getChatMessageInfo(
            "333333333333333333@g.us",
            "5511922222222:22@s.whatsapp.net",
            "Sicrana",
            "/a"));

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.execute(newMessage1);
        addCommand.execute(newMessage2);

        // THEN
        var expected1 = """
            ID: 2 | *PROXIMO EVENTO - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            01 - Fulana de Tal
            *02 - Sicrana*
            03 - Beltrana"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));
        assertThat(newMessage1.response()).isEqualTo(expected1);

        var expected2 = """
            ID: 4 | *EVENTO NOVO - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            *01 - Sicrana*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));
        assertThat(newMessage2.response()).isEqualTo(expected2);

    }
}
