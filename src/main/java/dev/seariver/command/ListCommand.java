package dev.seariver.command;

import dev.seariver.NewMessage;
import dev.seariver.Repository;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListCommand implements Command {

    private final Repository repository;

    public ListCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(NewMessage newMessage) { // todo: resolves template on repository

        newMessage.response("Sem eventos programados");

        var nextEvent = repository.findNextEvent(newMessage);

        nextEvent.ifPresent(event -> {
            var persons = repository.findPersonList(event.id());
            var personList = persons.isEmpty()
                ? "01 -"
                : IntStream.range(0, persons.size())
                .mapToObj(i -> "%02d - %s".formatted(i + 1, persons.get(i).name()))
                .collect(Collectors.joining("\n"));

            var response = event.template()
                .replace("#ID", String.valueOf(event.id()))
                .replace("#DATE", event.eventDate().format(DateTimeFormatter.ofPattern("dd/MM")))
                .replace("#START_AT", event.startAt().format(DateTimeFormatter.ofPattern("HH'h'mm")))
                .replace("#END_AT", event.endAt().format(DateTimeFormatter.ofPattern("HH'h'mm")))
                .replace("#PERSON_LIST", personList);

            newMessage.response(response);
        });
    }

    @Override
    public boolean itsMine(String text) {
        return text.equalsIgnoreCase("/l");
    }
}
