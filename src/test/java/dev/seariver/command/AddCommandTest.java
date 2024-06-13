package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.helper.DatabaseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AddCommandTest extends DatabaseHelper {

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

    @ParameterizedTest
    @ValueSource(strings = {"/a", "/a Clarice Lispector"})
    void GIVEN_valid_text_command_WHEN_check_its_mine_MUST_return_true(String text) {

        // WHEN
        var addCommand = new AddCommand(repository);
        var itsMine = addCommand.itsMine(text);

        // THEN
        assertThat(itsMine).isTrue();
    }

    @Test
    void GIVEN_add_sender_WHEN_valid_name_MUST_add_to_list() {

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
    void GIVEN_add_sender_WHEN_sender_already_in_list_CANNOT_add_to_list() {

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
    void GIVEN_add_sender_WHEN_in_different_events_MUST_add_to_list(String chatJid,
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

    @Test
    void GIVEN_add_sender_WHEN_sender_name_added_by_other_CANNOT_add_to_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "5511933333333:22@s.whatsapp.net",
            "Beltrana",
            "/a"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.run(newMessage);

        // THEN
        var expected = """
            Alguém ja adicionou o nome *Beltrana* 🤭

            Use: `/a Nome Alternativo` para adicionar o nome que desejar.

            ID: -201 | *GRUPO 2 ATUAL - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            01 - Fulana de Tal
            02 - Sicrana
            *03 - Beltrana*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @Test
    void GIVEN_add_name_WHEN_valid_name_MUST_add_to_list() {

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
        addCommand.run(newMessage);

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
    void GIVEN_add_names_WHEN_all_valid_names_MUST_add_all_to_list() {

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
        addCommand.run(newMessage);

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
    void GIVEN_add_names_WHEN_more_than_10_CANNOT_add_to_list() {

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
        addCommand.run(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo("Perai ✋ Adicione 10 pessoas por vez.");
    }

    @Test
    void GIVEN_add_name_WHEN_not_valid_name_CANNOT_add_to_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "55229112233445@s.whatsapp.net",
            "Someone",
            "/a 06 123 _ 24"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.run(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo("""
            Desculpe 🥲 Nao consigo adicionar: 06 123 _ 24

            Use: `/a Nome Alternativo` para adicionar o nome que desejar.""");
    }

    @Test
    void GIVEN_add_name_WHEN_invalid_format_CANNOT_add_to_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "111111111111111111@g.us",
            "55229112233445@s.whatsapp.net",
            "Someone",
            """
                /a Nome
                Sobrenome"""
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.run(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo("Assim nao, ne \uD83E\uDD28");
    }

    @Test
    void GIVEN_add_name_WHEN_name_already_in_list_CANNOT_add_to_list() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "55229112233445@s.whatsapp.net",
            "Someone",
            "/a Beltrana"
        );
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.run(newMessage);

        // THEN
        var expected = """
            Alguém ja adicionou o nome *Beltrana* 🤭

            Use: `/a Nome Alternativo` para adicionar o nome que desejar.

            ID: -201 | *GRUPO 2 ATUAL - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            01 - Fulana de Tal
            02 - Sicrana
            *03 - Beltrana*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "/a (Catan), Clarice (Catan)",
        "/a (Azul, Terra Nova), Clarice (Azul, Terra Nova)",
    })
    void GIVEN_add_sender_games_WHEN_valid_command_format_MUST_add_to_list(String text,
                                                                           String result) {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "55229112233445@s.whatsapp.net",
            "Clarice",
            text
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
            02 - Sicrana
            03 - Beltrana
            *04 - #RESULT*"""
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()))
            .replace("#RESULT", result);

        assertThat(newMessage.response()).isEqualTo(expected);
    }

    //TODO: Add Game when Sender already in list - added by himself
    //TODO: Add Game when Sender name alread in list - add by order

}
