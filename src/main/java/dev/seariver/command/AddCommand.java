package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.Repository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommand implements Command {

    private final Repository repository;

    public AddCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(NewMessage newMessage) {

        var commandParams = newMessage
            .text()
            .substring(2)
            .trim();

        if (commandParams.isEmpty()) {
            addSender(newMessage, "");
        } else if (!extractGames(commandParams).isEmpty()) {
            addSender(newMessage, extractGames(commandParams));
        } else {
            addByName(newMessage, commandParams);
        }
    }

    private void addSender(NewMessage newMessage, String games) {

        newMessage.isSenderName(true);

        if (notValidName(newMessage, newMessage.senderName())) return;

        var success = repository.addPersonOnNextEvent(newMessage, newMessage.senderName());


        var gameList = games.split(",");

        for (String game : gameList) {

        }


        if (!success) return;

        if (!games.isEmpty()) {
            newMessage.normalize(games, true);
        }

        repository.listNextEvent(newMessage);
    }

    private void addByName(NewMessage newMessage, String commandParams) {

        var nameList = commandParams.split(",");

        if (nameList.length > 10) {
            newMessage.response("Perai ✋ Adicione 10 pessoas por vez.");
            return;
        }

        for (String name : nameList) {
            if (notValidName(newMessage, name)) return;
        }

        for (String name : nameList) {
            newMessage.names(name.trim());
            var success = repository.addPersonOnNextEvent(newMessage, name.trim());
            if (!success) return;
        }

        repository.listNextEvent(newMessage);
    }

    private boolean notValidName(NewMessage newMessage, String name) {
        if (newMessage.slug(name).isEmpty()) {
            newMessage.response("""
                Desculpe 🥲 Nao consigo adicionar: %s

                Use: `/a Nome Alternativo` para adicionar o nome que desejar."""
                .formatted(name));

            return true;
        }
        return false;
    }

    private String extractGames(String text) {
        String regex = "^\\(([^,()]+)(, [^,()]+)*\\)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            return text
                .replace("(", "")
                .replace(")", "");
        }
        return "";
    }

    @Override
    public boolean itsMine(String text) {

        if (text.length() < 2) return false;

        return text
            .substring(0, 2)
            .equalsIgnoreCase("/a");
    }
}

