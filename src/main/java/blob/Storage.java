package blob;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Responsible for interfacing with the database (.csv file).
 * Constructor is to be called with a string representation of the desired file path to store the database as its argument.
 */
public class Storage {
    private File file;
    private String filePath;
    private final String NA_DATA = "NA";

    public Storage(String filePath) {
        //ensures file is always created (if it didn't exist initially)
        try {
            this.filePath = filePath;
            this.file = new File(filePath);
            if (this.file.createNewFile()) {
                FileWriter fw = new FileWriter(filePath);
                fw.write("type,is_checked,task_name,time1,time2,tags\n");
                fw.close();
            } else {
                Scanner s = new Scanner(this.file);
                String headers = s.nextLine();
                assert headers.equals("type,is_checked,task_name,time1,time2,tags\n") : "File headers are wrong!";
            }
        } catch (IOException e) {
            throw new RuntimeException("Database file was unable to be generated!");
        }
    }

    /**
     * Retrieves tasks from the database, skipping the first line in the database (skips data headers).
     * @return ArrayList containing all existing tasks from the database.
     * @throws FileNotFoundException if file cannot be found.
     */
    public ArrayList<Task> getFileContents() throws FileNotFoundException {
        ArrayList<Task> tasks = new ArrayList<>();
        Scanner s = new Scanner(this.file);
        s.nextLine(); //skips headers

        while (s.hasNext()) {
            String[] array = s.nextLine().split(",");
            String taskType = array[0];
            int isChecked = Integer.parseInt(array[1]);
            String taskName = array[2];

            switch (taskType) {
            case "T" -> {
                ArrayList<String> tags = new ArrayList<>();
                for (int i = 5; i < array.length; i++) {
                    tags.add(array[i]);
                }

                Todo t = new Todo(taskName, isChecked == 1, tags);
                tasks.add(t);
            }
            case "D" -> {
                ArrayList<String> tags = new ArrayList<>();
                for (int i = 5; i < array.length; i++) {
                    tags.add(array[i]);
                }
                Deadline d = new Deadline(taskName, isChecked == 1, array[3], tags);
                tasks.add(d);
            }
            case "E" -> {
                ArrayList<String> tags = new ArrayList<>();
                for (int i = 5; i < array.length; i++) {
                    tags.add(array[i]);
                }
                Event e = new Event(taskName, isChecked == 1, array[3], array[4], tags);
                tasks.add(e);
            }
            }
        }
        return tasks;
    }

    /**
     * Rewrites the database file and replaces all data with tasks from its argument.
     * @param database ArrayList containing tasks.
     * @throws IOException if there exists an error related to writing to the database.
     */
    public void updateFileContents(ArrayList<Task> database) throws IOException {
        FileWriter fw = new FileWriter(this.filePath);
        StringBuilder allData = new StringBuilder("type,is_checked,task_name,time1,time2,tags\n");
        for (int i = 0; i < database.size(); i++) {
            Task t = database.get(i);
            String isChecked = String.valueOf(t.isDone ? 1 : 0);

            switch (t.type) {
            case "T" -> {
                StringBuilder data = new StringBuilder(String.format("%s,%s,%s,%s,%s,%s\n",
                        t.type, isChecked, t.name, NA_DATA, NA_DATA, t.displayTags()));
                allData.append(data);
            }
            case "D" -> {
                Deadline d = (Deadline) t;
                StringBuilder data = new StringBuilder(String.format("%s,%s,%s,%s,%s,%s\n",
                        t.type, isChecked, t.name, d.getDeadline(), NA_DATA, t.displayTags()));
                allData.append(data);
            }
            case "E" -> {
                Event e = (Event) t;
                StringBuilder data = new StringBuilder(String.format("%s,%s,%s,%s,%s,%s\n",
                        t.type, isChecked, t.name, e.getStart(), e.getEnd(), t.displayTags()));
                allData.append(data);
            }
            }
        }
        fw.write(allData.toString());
        fw.close();
    }
}
