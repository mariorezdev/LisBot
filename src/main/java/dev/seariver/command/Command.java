package dev.seariver.command;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;

public interface Command {

    void onCommand(Whatsapp api, MessageInfo info);

    String command();
}
