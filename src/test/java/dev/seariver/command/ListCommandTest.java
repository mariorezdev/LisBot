package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.helper.TestHelper;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ListCommandTest extends TestHelper {

    MessageInfo messageInfo = mock(ChatMessageInfo.class);

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
    void GIVEN_chat_jid_WHEN_has_event_MUST_return_event_list() {

        // GIVEN
        when(messageInfo.message()).thenReturn(getMessageContainer("/l"));
        when(messageInfo.parentJid()).thenReturn(Jid.of("111111111111111111@g.us"));
        var newMessage = new NewMessage(messageInfo);

        // WHEN
        var listCommand = new ListCommand(repository);
        listCommand.execute(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo(
            """
                ID: 1
                **PRÓXIMA JOGATINA - SÁBADO - 01/06**
                Local: Shopping Jardim Pamplona - Rua Pamplona, 1704 (Próximo ao metrô Trianon-Masp - são uns 15/20min a pé, não é colado) - 3° Andar, na frente do cinema
                Horário: 14h00

                PESSOAS
                01 - Fulana de Tal
                02 - Sicrana
                03 - Beltrana

                - Compre jogos na BoardGamePlay Store! Cupom de **5%** em todo o site: **JOGATINA**!
                - Leve casaco! Às vezes o shopping fica **muito** gelado.
                - Lembre-se de que o shopping fecha às 22h00!"""
        );
    }

    @Test
    void GIVEN_chat_jid_WHEN_event_not_exist_MUST_return_no_event_message() {

        // GIVEN
        when(messageInfo.message()).thenReturn(getMessageContainer("/l"));
        when(messageInfo.parentJid()).thenReturn(Jid.of("222222222222222222@g.us"));
        var newMessage = new NewMessage(messageInfo);

        // WHEN
        var listCommand = new ListCommand(repository);
        listCommand.execute(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo("Sem eventos programados");
    }

    @Test
    void GIVEN_chat_jid_WHEN_has_event_WITH_no_people_MUST_return_empty_list() {

        // GIVEN
        when(messageInfo.message()).thenReturn(getMessageContainer("/l"));
        when(messageInfo.parentJid()).thenReturn(Jid.of("333333333333333333@g.us"));
        var newMessage = new NewMessage(messageInfo);

        // WHEN
        var listCommand = new ListCommand(repository);
        listCommand.execute(newMessage);

        // THEN
        assertThat(newMessage.response()).isEqualTo(
            """
                ID: 2
                **PRÓXIMA JOGATINA - SÁBADO - 01/06**
                Local: Shopping Jardim Pamplona - Rua Pamplona, 1704 (Próximo ao metrô Trianon-Masp - são uns 15/20min a pé, não é colado) - 3° Andar, na frente do cinema
                Horário: 14h00

                PESSOAS
                01 -

                - Compre jogos na BoardGamePlay Store! Cupom de **5%** em todo o site: **JOGATINA**!
                - Leve casaco! Às vezes o shopping fica **muito** gelado.
                - Lembre-se de que o shopping fecha às 22h00!"""
        );
    }
}
