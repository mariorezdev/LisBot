package dev.seariver;

import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.WebHistoryLength;
import it.auties.whatsapp.api.Whatsapp;

import static java.lang.System.out;

public class LisBotWeb {

    public static void main(String[] args) {

        var whatsapp = Whatsapp.webBuilder()                                                          // Use the Web api
            .lastConnection()                                                                         // Deserialize the last connection, or create a new one if it doesn't exist
            .historyLength(WebHistoryLength.zero())
            .unregistered(QrHandler.toTerminal())                                                     // Print the QR to the terminal
            .addLoggedInListener(api -> out.printf("Connected: %s%n", api.store().privacySettings())) // Print a message when connected
            .addDisconnectedListener(reason -> out.printf("Disconnected: %s%n", reason))              // Print a message when disconnected
            .connect()                                                                                // Connect to Whatsapp asynchronously
            .join();                                                                                  // Await the result

        whatsapp.addListener(new WhatsappListener());

        while (true) {
            try {
                Thread.sleep(1_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
