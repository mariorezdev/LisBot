package dev.seariver;

import dev.seariver.command.AddCommand;
import dev.seariver.command.ListCommand;
import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.WebHistoryLength;
import it.auties.whatsapp.api.Whatsapp;

import static java.lang.System.out;

public class LisBot {

    public static void main(String[] args) {

        var commandListener = new CommandListener();
        commandListener.addCommands(
            new ListCommand(),
            new AddCommand()
        );

        Whatsapp.webBuilder() // Use the Web api
            .lastConnection() // Deserialize the last connection, or create a new one if it doesn't exist
            .historyLength(WebHistoryLength.zero())
            .unregistered(QrHandler.toTerminal()) // Print the QR to the terminal
            .addLoggedInListener(whatsapp -> out.printf("Connected: %s%n", whatsapp.store().privacySettings())) // Print a message when connected
            .addDisconnectedListener(reason -> out.printf("Disconnected: %s%n", reason)) // Print a message when disconnected
            .addListener(commandListener)
            .connect() // Connect to Whatsapp asynchronously
            .join() // Await the result
            .awaitDisconnection();
    }
}
