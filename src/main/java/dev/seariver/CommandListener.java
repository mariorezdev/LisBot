package dev.seariver;

import dev.seariver.command.Command;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.listener.Listener;
import it.auties.whatsapp.model.info.MessageInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.System.out;

public class CommandListener implements Listener {

    private final Set<Command> commands = new HashSet<>();
    private final Repository repository;

    public CommandListener(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void onNewMessage(Whatsapp whatsapp, MessageInfo info) {

        out.printf("New message: %s%n", info.toJson());

        var newMessage = new NewMessage(info);

        // todo: cache registered chats
        if (newMessage.text().isEmpty() ||
            !repository.isRegisteredChat(newMessage)) return;

        findCommand(newMessage.text())
            .ifPresent(command -> command.execute(newMessage));

        if (newMessage.response().isEmpty()) return;

        whatsapp.sendChatMessage(newMessage.chatJid(), newMessage.response());
    }

    public void addCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    private Optional<Command> findCommand(String text) {
        return commands.stream()
            .filter(command -> command.itsMine(text))
            .findAny();
    }
}

