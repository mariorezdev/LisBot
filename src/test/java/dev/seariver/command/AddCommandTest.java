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
        var chatMessageInfo = new ChatMessageInfoBuilder()
            .key(new ChatMessageKeyBuilder()
                .chatJid(Jid.of("111111111111111111@g.us"))
                .build())
            .senderJid(Jid.of("5511912345678:12@s.whatsapp.net"))
            .message(getMessageContainer("/a"))
            .build();
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var addCommand = new AddCommand(repository);
        addCommand.execute(newMessage);

        // THEN
        assertThat(newMessage.reply()).isTrue();
        var expected = """
            ID: 3
            *PRÓXIMA JOGATINA - SÁBADO - %s*
            Local: Shopping Jardim Pamplona - Rua Pamplona, 1704 (Próximo ao metrô Trianon-Masp - são uns 15/20min a pé, não é colado) - 3° Andar, na frente do cinema
            Horário: 14h00

            PESSOAS
            01 - Fulana de Tal
            02 - Sicrana
            03 - Beltrana
            *04 - 5511912345678*

            - Compre jogos na BoardGamePlay Store! Cupom de *5%* em todo o site: *JOGATINA*!
            - Leve casaco! Às vezes o shopping fica *muito* gelado.
            - Lembre-se de que o shopping fecha às 22h00!"""
            .replace("%s", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM")));

        assertThat(newMessage.response()).isEqualTo(expected);
    }
}
