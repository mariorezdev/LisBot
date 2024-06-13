package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.helper.DatabaseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class RemoveCommandTest extends DatabaseHelper {

    @ParameterizedTest
    @ValueSource(strings = {"/r", "/r Fulana"})
    void GIVEN_valid_text_command_WHEN_check_its_mine_MUST_return_true(String text) {

        // WHEN
        var removeCommand = new RemoveCommand(repository);
        var itsMine = removeCommand.itsMine(text);

        // THEN
        assertThat(itsMine).isTrue();
    }

    @Test
    void GIVEN_new_message_WHEN_remove_sender_MUST_return_updated_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "5511922222222:12@s.whatsapp.net",
            "Sicrana",
            "/r"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var removeCommand = new RemoveCommand(repository);
        removeCommand.run(newMessage);

        // THEN
        var expected = """
            ID: -201 | *GRUPO 2 ATUAL - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            01 - Fulana de Tal
            02 - Beltrana"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_new_message_WHEN_remove_not_existent_sender_MUST_return_warning_and_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "5511922222222:12@s.whatsapp.net",
            "Sicrana",
            "/r"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var removeCommand = new RemoveCommand(repository);
        removeCommand.run(newMessage);

        // THEN
        var expected = """
            Ops \uD83E\uDEE5 Sicrana nao esta na lista

            ID: -100 | *GRUPO 1 - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            """
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_new_message_WHEN_remove_by_name_MUST_return_updated_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "5511922222222:12@s.whatsapp.net",
            "Sicrana",
            "/r Fulana de Tal"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var removeCommand = new RemoveCommand(repository);
        removeCommand.run(newMessage);

        // THEN
        var expected = """
            ID: -201 | *GRUPO 2 ATUAL - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            *01 - Sicrana*
            02 - Beltrana"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_new_message_WHEN_sender_name_add_by_other_MUST_return_warning() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "5511933333333:12@s.whatsapp.net",
            "Beltrana",
            "/r"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var removeCommand = new RemoveCommand(repository);
        removeCommand.run(newMessage);

        // THEN
        var expected = """
            Não posso remover *Beltrana*, pois não foi adicionado por voce.

            Use: `/r Beltrana` se ainda deseja remover este nome.

            ID: -201 | *GRUPO 2 ATUAL - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            01 - Fulana de Tal
            02 - Sicrana
            *03 - Beltrana*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }
}
