package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.helper.TestHelper;
import it.auties.whatsapp.model.info.ChatMessageInfoBuilder;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.model.ChatMessageKeyBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


class ListCommandTest extends TestHelper {

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
    void GIVEN_list_command_WHEN_group_has_event_MUST_return_only_next_event() {

        // GIVEN
        var chatMessageInfo = new ChatMessageInfoBuilder()
            .key(new ChatMessageKeyBuilder()
                .chatJid(Jid.of("111111111111111111@g.us"))
                .build())
            .senderJid(Jid.of("5511922222222:22@s.whatsapp.net"))
            .message(getMessageContainer("/l"))
            .build();
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var listCommand = new ListCommand(repository);
        listCommand.execute(newMessage);

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
    void GIVEN_chat_jid_WHEN_event_not_exist_MUST_return_no_event_message() {

        // GIVEN
        var chatMessageInfo = new ChatMessageInfoBuilder()
            .key(new ChatMessageKeyBuilder()
                .chatJid(Jid.of("222222222222222222@g.us"))
                .build())
            .senderJid(Jid.of("5511912345678:12@s.whatsapp.net"))
            .message(getMessageContainer("/l"))
            .build();
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var listCommand = new ListCommand(repository);
        listCommand.execute(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo("Sem eventos programados");
    }

    @Test
    void GIVEN_chat_jid_WHEN_has_event_WITH_no_people_MUST_return_empty_list() {

        // GIVEN
        var chatMessageInfo = new ChatMessageInfoBuilder()
            .key(new ChatMessageKeyBuilder()
                .chatJid(Jid.of("333333333333333333@g.us"))
                .build())
            .senderJid(Jid.of("5511912345678:12@s.whatsapp.net"))
            .message(getMessageContainer("/l"))
            .build();
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var listCommand = new ListCommand(repository);
        listCommand.execute(newMessage);

        // THEN
        var expected = """
            ID: 4 | *EVENTO SEM PESSOAS - #WEEK_DAY - #DATE* | Local: Na Rua | Horário: das 14h00 as 22h00
            PESSOAS
            """
            .replace("#DATE", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")))
            .replace("#WEEK_DAY", translateWeekDay(LocalDate.now().plusDays(2).getDayOfWeek().name()));

        assertThat(newMessage.response()).isEqualTo(expected);
    }
}
