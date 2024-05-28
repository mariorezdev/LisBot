package dev.seariver;

import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.standard.TextMessage;

public class NewMessage {

    private final MessageInfo info;
    private TextMessage textMessage;
    private String response = "";
    private boolean reply = false;

    public NewMessage(MessageInfo info) {
        this.info = info;
        if (info.message().content() instanceof TextMessage textMessage) {
            this.textMessage = textMessage;
        }
    }

    public String text() {

        if (null == textMessage) {
            return "";
        }

        return textMessage.text().trim();
    }

    public Jid chatJid() {
        return info.parentJid();
    }

    public Jid senderJid() {
        return info.senderJid();
    }

    public String senderName() {

        if (info instanceof ChatMessageInfo chatMessageInfo) {
            if (chatMessageInfo.pushName().isPresent()) {
                return chatMessageInfo.pushName().get();
            }
            return chatMessageInfo.senderName();
        }

        return info.senderJid().user();
    }

    public String response() {
        return response;
    }

    public void response(String text) {
        this.response = text;
    }

    public boolean reply() {
        return reply;
    }

    public void reply(String text) {
        this.reply = true;
        this.response = text;
    }
}
