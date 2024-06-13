package dev.seariver.helper;

import it.auties.whatsapp.model.contact.ContactBuilder;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.ChatMessageInfo.StubType;
import it.auties.whatsapp.model.info.ChatMessageInfoBuilder;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.model.ChatMessageKeyBuilder;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.model.MessageContainerBuilder;
import it.auties.whatsapp.model.message.standard.TextMessageBuilder;

public class TestHelper {

    public static String translateWeekDay(String weekDay) {
        return switch (weekDay) {
            case "MONDAY" -> "SEGUNDA";
            case "TUESDAY" -> "TERÇA";
            case "WEDNESDAY" -> "QUARTA";
            case "THURSDAY" -> "QUINTA";
            case "FRIDAY" -> "SEXTA";
            case "SATURDAY" -> "SÁBADO";
            case "SUNDAY" -> "DOMINGO";
            default -> "";
        };
    }

    public ChatMessageInfo getChatMessageInfo(String chatJid,
                                              String senderJid,
                                              String senderName,
                                              String text) {
        return getChatMessageInfo(chatJid,
            senderJid,
            senderName,
            text,
            null);
    }

    public ChatMessageInfo getChatMessageInfo(String chatJid,
                                              String senderJid,
                                              String senderName,
                                              String text,
                                              StubType stubType) {

        var chatMessageInfo = new ChatMessageInfoBuilder()
            .key(new ChatMessageKeyBuilder()
                .chatJid(Jid.of(chatJid))
                .build())
            .senderJid(Jid.of(senderJid))
            .message(getMessageContainer(text))
            .stubType(stubType)
            .build();
        chatMessageInfo.setSender(new ContactBuilder()
            .shortName(senderName)
            .jid(Jid.of(senderJid))
            .build());

        return chatMessageInfo;
    }

    public MessageContainer getMessageContainer(String text) {
        var textMessage = new TextMessageBuilder()
            .text(text)
            .build();
        return new MessageContainerBuilder()
            .textMessage(textMessage)
            .build();
    }
}
