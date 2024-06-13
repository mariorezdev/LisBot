package dev.seariver.command;

import dev.seariver.NewMessage;

public class WelcomeCommand implements Command {

    @Override
    public void execute(NewMessage newMessage) {

        newMessage.response("""
            Boas vindas 🤗 !
            As JogaTinas ocorrem toda *QUARTA* e *QUINTA* as 18h00 e *SABADO* as 14h00.

            Mais informacoes utilize os comandos:
            `/l` *L* ista da próxima jogatina.
            `/a` *A* diciona *seu nome* na próxima jogatina.
            `/h` *H* elp : exibe todos comandos disponíveis.""");
    }

    @Override
    public boolean itsMine(String text) {
        return text.equals("GROUP_PARTICIPANT_ADD");
    }
}
