package blob;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Handles the manipulation of input from the user.
 */
public class Parser {

    public String executeList(TaskList tasklist, String[] arr) {
        StringBuilder s = new StringBuilder("Here are the tasks in your list:\n");
        for (int j = 0; j < tasklist.getSize(); j++) {
            Task t = tasklist.getTask(j);
            s.append(String.format("%d. %s\n", j + 1, t));
        }
        return s.toString();
    }

    public String executeMark(TaskList tasklist, String[] arr) {
        StringBuilder s = new StringBuilder("");
        try {
            int index = Integer.parseInt(arr[1]) - 1;
            tasklist.markTask(index);
            s.append("Nice! I've marked this task as done:\n");
            s.append(tasklist.getTask(index));
        } catch (NumberFormatException e) {
            s.append("Invalid Command!");
        } catch (IndexOutOfBoundsException e) {
            int taskNum = Integer.parseInt(arr[1]);
            s.append(String.format("There is no task at task number '%d'", taskNum));
        }
        return s.toString();
    }

    public String executeUnmark(TaskList tasklist, String[] arr) {
        StringBuilder s = new StringBuilder("");
        try {
            int index = Integer.parseInt(arr[1]) - 1;
            tasklist.unmarkTask(index);
            s.append("OK, I've marked this task as not done yet:\n");
            s.append(tasklist.getTask(index));
        } catch (NumberFormatException e) {
            s.append("Invalid Command!");
        } catch (IndexOutOfBoundsException e) {
            int taskNum = Integer.parseInt(arr[1]);
            s.append(String.format("There is no task at task number '%d'", taskNum));
        }
        return s.toString();
    }

    public String executeDelete(TaskList tasklist, String[] arr) {
        StringBuilder s = new StringBuilder();
        try {
            int index = Integer.parseInt(arr[1]) - 1;
            Task t = tasklist.deleteTask(index);
            s.append("Noted, I've removed this task:\n");
            s.append(t + "\n");
            s.append("Now you have " + tasklist.getSize() + " tasks in the list.");
        } catch (NumberFormatException e) {
            s.append("Invalid Command!");
        } catch (IndexOutOfBoundsException e) {
            int taskNum = Integer.parseInt(arr[1]);
            s.append(String.format("There is no task at task number '%d'", taskNum));
        }
        return s.toString();
    }

    public String executeTodo(TaskList tasklist, String[] arr) {
        // task name error handling
        int i = 0;
        if (arr.length == 1) {
            return "Sorry, I am unable to generate an empty 'todo' task!";
        }

        StringBuilder msg = new StringBuilder("");

        // for task name string
        StringBuilder a = new StringBuilder(arr[i + 1]);
        for (int j = i + 2; j < arr.length; j++) {
            StringBuilder str = new StringBuilder(" " + arr[j]);
            a = a.append(str);
        }

        Todo t = new Todo(a.toString(), false, new ArrayList<String>());
        tasklist.addTask(t);
        msg.append("Got it. I've added this task:\n");
        msg.append(t + "\n");
        msg.append("Now you have " + tasklist.getSize() + " tasks in the list.");
        return msg.toString();
    }

    public String executeDeadline(TaskList tasklist, String[] arr) {
        int i = 0;
        int by = 0;
        for (int j = 0; j < arr.length; j++) {
            if (arr[j].equals("/by")) {
                by = j;
            }
        }
        // task name error handling
        if (by <= 1) {
            return "I require a description for your 'deadline' task 😅!";
        }
        // task deadline error handling
        if (by == arr.length - 1) {
            return "Your deadline task can't not have a deadline! Please enter your task again!";
        }
        //task deadline only having date or time
        if (by == arr.length - 2) {
            return "You need to add both DATE and TIME to your deadline \uD83D\uDD27! (Format: yyyy-mm-dd HH:mm)";
        }
        StringBuilder msg = new StringBuilder("");
        // for task name string
        StringBuilder a = new StringBuilder(arr[i + 1]);
        for (int l = i + 2; l < by; l++) {
            System.out.println("word:-" + arr[l] + "-");
            StringBuilder str = new StringBuilder(" " + arr[l]);
            a = a.append(str);
        }
        // for task deadline string
        StringBuilder stringDate = new StringBuilder(arr[by + 1]);
        StringBuilder stringTime = new StringBuilder(arr[by + 2]);
        String Iso8601Format = stringDate.toString() + "T" + stringTime.toString() + ":00";
        try {
            Deadline d = new Deadline(a.toString(), false, Iso8601Format, new ArrayList<String>());
            tasklist.addTask(d);
            msg.append("Got it. I've added this task:\n");
            msg.append(d + "\n");
            msg.append("Now you have " + tasklist.getSize() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            msg.append("Input dates and times not in the format 'yyyy-mm-dd HH:mm'!");
        }
        return msg.toString();
    }

    public String executeEvent(TaskList tasklist, String[] arr) {
        int i = 0;
        int start = 0;
        int end = 0;
        for (int j = 0; j < arr.length; j++) {
            if (arr[j].equals("/from")) {
                start = j;
            } else if (arr[j].equals("/to")) {
                end = j;
            }
        }
        // task name error handling
        if (start <= 1) {
            return "What's the name of your event? Please add it 😅!";
        }
        // start string error handling
        if (end - start <= 1) {
            return "When does your event begin? Please add it 😅!";
        }
        // end string error handling
        if (end == arr.length - 1) {
            return "What time does your event end? Please let me know 😅!";
        }
        //'/from' only having date or time
        if (end - start == 2) {
            return "You need to add both DATE and TIME to your event's start \uD83D\uDD27! (Format: yyyy-mm-dd HH:mm)";
        }
        //'/to' only having date or time
        if (end == arr.length - 2) {
            return "You need to add both DATE and TIME to your event's end \uD83D\uDD27! (Format: yyyy-mm-dd HH:mm)";
        }

        StringBuilder msg = new StringBuilder("");

        //for task name string
        StringBuilder a = new StringBuilder(arr[i + 1]);
        for (int l = i + 2; l < start; l++) {
            StringBuilder str = new StringBuilder(" " + arr[l]);
            a = a.append(str);
        }

        //for start string
        StringBuilder startDate = new StringBuilder(arr[start + 1]);
        StringBuilder startTime = new StringBuilder(arr[start + 2]);
        String startIso8601Format = startDate.toString() + "T" + startTime.toString() + ":00";

        //for end string
        StringBuilder endDate = new StringBuilder(arr[end + 1]);
        StringBuilder endTime = new StringBuilder(arr[end + 2]);
        String endIso8601Format = endDate.toString() + "T" + endTime.toString() + ":00";

        try {
            Event e = new Event(a.toString(), false, startIso8601Format, endIso8601Format, new ArrayList<String>());
            tasklist.addTask(e);
            msg.append("Got it. I've added this task:\n");
            msg.append(e + "\n");
            msg.append("Now you have " + tasklist.getSize() + " tasks in the list.");
        } catch (DateTimeParseException e) {
            msg.append("Input dates and times not in the format 'yyyy-mm-dd HH:mm'!");
        }
        return msg.toString();
    }

    public String executeFind(TaskList tasklist, String[] arr) {
        //if 'find' entered without keyword
        StringBuilder msg = new StringBuilder("");
        if (arr.length == 1) {
            msg.append("You need to enter a keyword to find your tasks!");
            return msg.toString();
            //if 'find' entered with more than 1 keyword
        } else if (arr.length > 2) {
            msg.append("You can only enter one keyword!");
            return msg.toString();
        } else {
            String keyword = arr[1];
            msg.append("Here are the matching tasks in your list:\n");
            int num = 1;
            boolean isMatch = false;
            for (int j = 0; j < tasklist.getSize(); j++) {
                Task t = tasklist.getTask(j);
                String[] taskName = t.name.split(" ");
                for (int k = 0; k < taskName.length; k++) {
                    if (taskName[k].equalsIgnoreCase(keyword)) {
                        isMatch = true;
                    }
                }
                if (isMatch) {
                    msg.append(String.format("%d. %s\n", num, t));
                    num++;
                    isMatch = false;
                }
            }
            return msg.toString();
        }
    }

    public String executeTag(TaskList tasklist, String[] arr) {
        int i = 0;
        StringBuilder s = new StringBuilder("");

        //if 'tag' doesn't come with the proper amount of parameters
        if (arr.length < 3) {
            s.append("'tag' requires the next two parameters 'task number' and 'tag name'!");
            return s.toString();
        }

        try {
            int index = Integer.parseInt(arr[i + 1]) - 1;
            String tag = arr[i + 2];
            tasklist.tagTask(index, tag);
            s.append("OK, I've tagged this task as such:\n");
            s.append(tasklist.getTask(index));
        } catch (NumberFormatException e) {
            s.append("Invalid Command!");
        } catch (IndexOutOfBoundsException e) {
            int taskNum = Integer.parseInt(arr[i + 1]);
            s.append(String.format("There is no task at task number '%d'", taskNum));
        }
        return s.toString();
    }

    public String executeUntag(TaskList tasklist, String[] arr) {
        int i = 0;
        StringBuilder s = new StringBuilder("");

        //if 'tag' doesn't come with the proper amount of parameters
        if (arr.length < 2) {
            s.append("'untag' requires the parameter 'task number'!");
            return s.toString();
        }

        try {
            int index = Integer.parseInt(arr[i + 1]) - 1;
            tasklist.untagTask(index);
            s.append("OK, I've untagged this task as such:\n");
            s.append(tasklist.getTask(index));
        } catch (NumberFormatException e) {
            s.append("Invalid Command!");
        } catch (IndexOutOfBoundsException e) {
            int taskNum = Integer.parseInt(arr[i + 1]);
            s.append(String.format("There is no task at task number '%d'", taskNum));
        }
        return s.toString();
    }
    /**
     * @param ui Ui used by the chatbot.
     * @param tasklist Tasklist containing all exisitng tasks.
     * @param action String representation of the input from the user.
     * Existing accepted commands now include:
     *               'bye' - exits the conversation with the bot
     *               'list' - lists all existing tasks
     *               'mark X' - marks X'th task as done
     *               'unmark X' - marks X'th task as not done
     *               'delete X' - removes X'th task from the database
     *               'todo task' - adds a to-do task to database
     *               'deadline task /by YYYY-MM-DD HH:mm' - adds a deadline task to the database
     *               'event task /from YYYY-MM-DD HH:mm /to YYYY-MM-DD HH:mm' - adds a event task to the database
     */
    public String evaluateAction(Ui ui, TaskList tasklist, String action) {
        // '\\s+' refers to 'one or more whitespaces!'
        String[] arr = action.trim().split("\\s+");
        // gets rid of any ',' that will mess with csv database
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].replace(",", "\0");
        }

        for (int i = 0; i < arr.length; i++) {
            String act = arr[i].toLowerCase();
            switch (act) {
            case "list" -> {
                    return executeList(tasklist, arr);
                }
            case "mark" -> {
                    return executeMark(tasklist, arr);
                }
            case "unmark" -> {
                    return executeUnmark(tasklist, arr);
                }
            case "delete" -> {
                    return executeDelete(tasklist, arr);
                }
            case "todo" -> {
                    return executeTodo(tasklist, arr);
                }
            case "deadline" -> {
                    return executeDeadline(tasklist, arr);
                }
            case "event" -> {
                    return executeEvent(tasklist, arr);
                }
            case "find" -> {
                    return executeFind(tasklist, arr);
                }
            case "tag" -> {
                    return executeTag(tasklist, arr);
                }
            case "untag" -> {
                    return executeUntag(tasklist, arr);
                }
            default -> {
                    return "ERROR! Unknown Command!";
                }
            }
        }
        return "";
    }
}

