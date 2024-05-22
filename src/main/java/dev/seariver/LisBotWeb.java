package dev.seariver;

import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.WebHistoryLength;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.chat.Chat;

import static java.lang.System.out;

public class LisBotWeb {

    public static void main(String[] args) {

        var api = Whatsapp.webBuilder()                                                          // Use the Web api
            .lastConnection()                                                                         // Deserialize the last connection, or create a new one if it doesn't exist
            .historyLength(WebHistoryLength.zero())
            .unregistered(QrHandler.toTerminal())                                                     // Print the QR to the terminal
            .addLoggedInListener(wa -> out.printf("Connected: %s%n", wa.store().privacySettings())) // Print a message when connected
            .addDisconnectedListener(reason -> out.printf("Disconnected: %s%n", reason))              // Print a message when disconnected
            .addListener(new OneForAllListener())
            .connect()                                                                                // Connect to Whatsapp asynchronously
            .join();                                                                                  // Await the result

        var chatNotListed = true;

        do try {
            Thread.sleep(1_000L);
            if (api.isConnected() && chatNotListed) {
                api.store().chats().stream()
                    .filter(Chat::isGroup)
                    .forEach(chat -> out.printf("Name: %s, Jid: %s%n", chat.name(), chat.jid()));
                chatNotListed = false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true);
    }
}
