package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.Repository;

public class HelpCommand implements Command {

    private final Repository repository;

    public HelpCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(NewMessage newMessage) {
        var response = """
            Ola 🩵 Eu organizo sua Jogatina 🎲

            Digite os comandos abaixo para:
            > `/l` → *Lista* da próxima jogatina agendada.
            > `/a` → *Adiciona* seu nome na próxima jogatina.
            > `/h` → *Help* : exibe todos comandos disponíveis.""";

        newMessage.response(response);
    }

    @Override
    public boolean itsMine(String text) {
        return text.equalsIgnoreCase("/h")
            || text.equals("@5511973778233");
    }
}
