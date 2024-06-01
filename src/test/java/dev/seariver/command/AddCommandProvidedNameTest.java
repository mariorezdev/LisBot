package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.helper.DatabaseHelper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class AddCommandProvidedNameTest extends DatabaseHelper {

    @Test
    void GIVEN_valid_text_command_WHEN_check_its_mine_MUST_return_true() {

        // GIVEN
        var text = "/a Clarice Lispector";

        // WHEN
        var addCommand = new AddCommand(repository);
        var itsMine = addCommand.itsMine(text);

        // THEN
        assertThat(itsMine).isTrue();
    }

    @Test
    void GIVEN_new_message_WHEN_add_provided_person_MUST_return_updated_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "5511911111111@s.whatsapp.net",
            "Fulana de Tal",
            "/a 𝓓𝓸𝓶𝓲 🪷 𝓓𝓸𝓶𝓲"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.execute(newMessage);

        // THEN
        var expected = """
            ID: -201 | *GRUPO 2 ATUAL - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            *01 - Fulana de Tal*
            02 - Sicrana
            03 - Beltrana
            *04 - Domi Domi*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_new_message_WHEN_add_multi_provided_person_MUST_return_updated_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "55229112233445@s.whatsapp.net",
            "Someone",
            "/a Ana Maria, Clarice"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.execute(newMessage);

        // THEN
        var expected = """
            ID: -100 | *GRUPO 1 - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            *01 - Ana Maria*
            *02 - Clarice*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_new_message_WHEN_add_more_than_10_person_MUST_respond_warning() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "55229112233445@s.whatsapp.net",
            "Someone",
            "/a Ana, Clarice, Bla, Cla, Dla, Fla, Ja, Ju, Cia, Deza, Onza"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.execute(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo("Perai 🖐️ Adicione 10 pessoas por vez.");
    }
}
