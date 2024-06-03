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
            Oi 🩵 Esses são os comandos mais usados:

            `/l` *L* ista da próxima jogatina.
            `/a` *A* diciona *seu nome* na próxima jogatina.
            `/h` *H* elp : exibe todos comandos disponíveis.
            
            *ADICIONAR PESSOA*
            `/a` *A* diciona *seu nome* na próxima jogatina.
            `/a Ana Maria` *A* diciona *um nome* na próxima jogatina.
            `/a Ana Maria, Clarice` *A* diciona *vários nomes* ao mesmo tempo.
            
            *REMOVER PESSOA*
            `/r` *R* emove *seu* nome da próxima jogatina.
            `/r Clarice` *R* emove *o* nome da próxima jogatina.
            """;

        newMessage.response(response);
    }

    @Override
    public boolean itsMine(String text) {
        return text.equalsIgnoreCase("/h")
            || text.equals("@5511973778233");
    }
}
