package dev.seariver.command;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;

public class ListCommand implements Command {
    @Override
    public void onCommand(Whatsapp api, MessageInfo info) {

    }

    @Override
    public String command() {
        return "/l";
    }
}
