package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.helper.DatabaseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AddCommandSenderNameTest extends DatabaseHelper {

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
    void GIVEN_new_message_WHEN_add_sender_MUST_return_updated_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "5511912345678:12@s.whatsapp.net",
            "𝓓𝓸𝓶𝓲 🪷 𝓓𝓸𝓶𝓲",
            "/a"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.run(newMessage);

        // THEN
        var expected = """
            ID: -100 | *GRUPO 1 - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            *01 - Domi Domi*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_new_message_WHEN_add_existent_sender_MUST_return_event_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "5511922222222:22@s.whatsapp.net",
            "Sicrana",
            "/a"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.run(newMessage);

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

    @ParameterizedTest
    @MethodSource("eventProvider")
    void GIVEN_two_messages_WHEN_add_same_sender_in_different_events_MUST_return_success(String chatJid,
                                                                                         String expected) {
        // GIVEN
        var newMessage = new NewMessage(getChatMessageInfo(
            chatJid,
            "5511922222222:22@s.whatsapp.net",
            "Sicrana",
            "/a"));

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.run(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo(expected);
    }

    public static Stream<Arguments> eventProvider() {

        var expected1 = """
            ID: -100 | *GRUPO 1 - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            *01 - Sicrana*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        var expected2 = """
            ID: -201 | *GRUPO 2 ATUAL - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            01 - Fulana de Tal
            *02 - Sicrana*
            03 - Beltrana"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));


        return Stream.of(
            arguments("111111111111111111@g.us", expected1),
            arguments("222222222222222222@g.us", expected2)
        );
    }
}
