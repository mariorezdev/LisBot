package dev.seariver;

import dev.seariver.helper.TestHelper;
import it.auties.whatsapp.model.jid.Jid;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NewMessageTest extends TestHelper {

    @ParameterizedTest
    @CsvSource({
        "Sicrana, Sicrana, sicrana",
        "Fulana de Tal, Fulana de Tal, fulana_de_tal",
        "𝓓𝓸𝓶𝓲 🪷 𝓓𝓸𝓶𝓲, Domi Domi, domi_domi",
        "~ 🪷 🩵 !, '~ !', ''",
    })
    void name_placeholder(String senderName,
                          String normalizedName,
                          String slug) {

        // GIVEN
        var chatJid = "222222222222222222@g.us";
        var senderJid = "5511922222222:22@s.whatsapp.net";
        var text = "/l";
        var chatMessageInfo = getChatMessageInfo(
            chatJid,
            senderJid,
            senderName,
            text);

        // WHEN
        var newMessage = new NewMessage(chatMessageInfo);

        // THEN
        assertThat(newMessage.chatJid()).isEqualTo(Jid.of(chatJid));
        assertThat(newMessage.senderJid()).isEqualTo(Jid.of(senderJid));
        assertThat(newMessage.senderName()).isEqualTo(senderName);
        assertThat(newMessage.senderNormalized()).isEqualTo(normalizedName);
        assertThat(newMessage.senderSlug()).isEqualTo(slug);
        assertThat(newMessage.text()).isEqualTo(text);
    }
}
