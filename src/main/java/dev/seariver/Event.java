package dev.seariver;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.standard.TextMessage;

public class Event {

    private final Whatsapp whatsapp;
    private final MessageInfo messageInfo;
    private TextMessage textMessage;

    public Event(Whatsapp whatsapp, MessageInfo messageInfo) {
        this.whatsapp = whatsapp;
        this.messageInfo = messageInfo;
        if (messageInfo.message().content() instanceof TextMessage textMessage) {
            this.textMessage = textMessage;
        }
    }

    public String text() {

        if (null == textMessage) {
            return "";
        }

        return textMessage.text().trim();
    }

    public Jid senderJid() {
        return messageInfo.senderJid();
    }

    public Jid chatJid() {
        return messageInfo.parentJid();
    }
}
