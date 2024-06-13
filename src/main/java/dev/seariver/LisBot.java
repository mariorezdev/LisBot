package dev.seariver;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.seariver.command.AddCommand;
import dev.seariver.command.HelpCommand;
import dev.seariver.command.ListCommand;
import dev.seariver.command.RemoveCommand;
import dev.seariver.command.WelcomeCommand;
import dev.seariver.lib.PropertyLoader;
import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.WebHistoryLength;
import it.auties.whatsapp.api.Whatsapp;
import org.flywaydb.core.Flyway;

import static java.lang.System.out;

public class LisBot {

    public static void main(String[] args) {

        PropertyLoader.load();

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(System.getProperty("jdbc.url"));
        hikariConfig.setUsername(System.getProperty("jdbc.username"));
        hikariConfig.setPassword(System.getProperty("jdbc.password"));
        hikariConfig.setDriverClassName(System.getProperty("jdbc.driverClassName"));
        var dataSource = new HikariDataSource(hikariConfig);

        Flyway.configure().dataSource(dataSource).load().migrate();

        var repository = new Repository(dataSource);

        var commandListener = new CommandListener(repository);
        commandListener.addCommands(
            new ListCommand(repository),
            new AddCommand(repository),
            new HelpCommand(repository),
            new RemoveCommand(repository),
            new WelcomeCommand()
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
