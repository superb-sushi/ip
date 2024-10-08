package blob;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Represents the Event task (subtype of Task)
 * Contains LocalDateTime fields 'start' and 'end' that represent the duration of the task
 * Requires a name, boolean value to check if its done, and string representations of its start and end timings for its constructor
 */
public class Event extends Task {
    private LocalDateTime start;
    private LocalDateTime end;

    public Event(String name, boolean isDone, String start, String end, ArrayList<String> tags) {
        super(name, isDone, tags);
        this.start = LocalDateTime.parse(start);
        assert this.start.isAfter(LocalDateTime.now()) : "This event's start time is already in the past!";
        this.end = LocalDateTime.parse(end);
        assert this.end.isAfter(this.start) : "This event ends earlier than it begins? How can that be?";
        super.type = "E";
    }

    /**
     * @return String representation of the Event task in the form "[E] ['completion status'] 'task name' (from: 'MM d HH:mm' to: 'MM d HH:mm')"
     */
    @Override
    public String toString() {
        if (this.tags.isEmpty()) {
            return "[E]" + "[" + this.check() + "] " + this.name +
                    " (from: " + start.format(DateTimeFormatter.ofPattern("MMM d HH:mm")) +
                    " to: " + end.format(DateTimeFormatter.ofPattern("MMM d HH:mm")) + ")";
        }
        StringBuilder tags = new StringBuilder("");
        for (int i = 0; i < this.tags.size(); i++) {
            String tag = this.tags.get(i);
            tags.append("#" + tag + " ");
        }
        return "[E]" + "[" + this.check() + "] " + this.name +
                " (from: " + start.format(DateTimeFormatter.ofPattern("MMM d HH:mm")) +
                " to: " + end.format(DateTimeFormatter.ofPattern("MMM d HH:mm")) + ") - " +
                tags.toString();
    }

    /**
     * @return LocalDateTime 'start' of the task
     */
    public LocalDateTime getStart() {
        return this.start;
    }

    /**
     * @return LocalDateTime 'end' of the task
     */
    public LocalDateTime getEnd() {
        return this.end;
    }
}
