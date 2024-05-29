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
            Oi 🩵 Digite os comandos abaixo para:

            `/l` *L* ista da próxima jogatina.
            `/a` *A* diciona seu nome na próxima jogatina.
            `/h` *H* elp : exibe todos comandos disponíveis.""";

        newMessage.response(response);
    }

    @Override
    public boolean itsMine(String text) {
        return text.equalsIgnoreCase("/h")
            || text.equals("@5511973778233");
    }
}
