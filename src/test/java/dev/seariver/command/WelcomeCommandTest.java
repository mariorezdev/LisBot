package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.helper.TestHelper;
import org.junit.jupiter.api.Test;

import static it.auties.whatsapp.model.info.ChatMessageInfo.StubType.GROUP_PARTICIPANT_ADD;
import static org.assertj.core.api.Assertions.assertThat;


public class WelcomeCommandTest extends TestHelper {

    @Test
    void GIVEN_new_message_WHEN_person_added_to_group_MUST_return_welcome_message() {

        // GIVEN
        var chatMessageInfo = getChatMessageInfo(
            "222222222222222222@g.us",
            "5511922222222:22@s.whatsapp.net",
            "Sicrana",
            "",
            GROUP_PARTICIPANT_ADD);
        var newMessage = new NewMessage(chatMessageInfo);

        // WHEN
        var welcomeCommand = new WelcomeCommand();
        welcomeCommand.run(newMessage);

        // THEN
        var expected = """
            Boas vindas 🤗 !
            As JogaTinas ocorrem toda *QUARTA* e *QUINTA* as 18h00 e *SABADO* as 14h00.

            Mais informacoes utilize os comandos:
            `/l` *L* ista da próxima jogatina.
            `/a` *A* diciona *seu nome* na próxima jogatina.
            `/h` *H* elp : exibe todos comandos disponíveis.""";

        assertThat(newMessage.response()).isEqualTo(expected);
    }
}
