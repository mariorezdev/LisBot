package dev.seariver;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.listener.Listener;
import it.auties.whatsapp.model.info.MessageInfo;

import static java.lang.System.out;

public record OneForAllListener() implements Listener {

    @Override
    public void onNewMessage(Whatsapp whatsapp, MessageInfo info) {
        out.printf("New message: %s%n", info.toJson());
    }
}
