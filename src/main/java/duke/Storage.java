package duke;
import tasks.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Storage {
    protected String directoryPath;
    protected String filePath;
    private static final ArrayList<Task> tasks = new ArrayList<>();

    public Storage(String directoryPath, String filePath) {
        this.directoryPath = directoryPath;
        this.filePath = filePath;
    }

    public Storage() {}


    public static void writeToDukeFile() throws DukeException {
        try{
            FileWriter fw = new FileWriter("./data/duke.txt");
            for (Task task: tasks) {
                fw.write(task.toFileFormat() + "\n");
            }
            fw.close();
        } catch (IOException exception){
            throw new DukeException("Duke file cannot be found!! Make sure it exists in the data folder.");
        }
    }


    public ArrayList<Task> readFromDukeFile() throws DukeException {
        boolean directoryExists = new File(this.directoryPath).exists();
        boolean fileExists = new File(this.filePath).exists();
        try {
            if (!directoryExists) {
                Files.createDirectories(Path.of(this.directoryPath));
                File dukeFile = new File(this.filePath);
                if (!dukeFile.createNewFile()) {
                    throw new IOException("Unable to create file at specified path. It already exists");
                }
            } else {
                if (!fileExists) {
                    File dukeFile = new File(this.filePath);
                    if (!dukeFile.createNewFile()) {
                        throw new IOException("Unable to create file at specified path. It already exists");
                    }
                }
            }
        } catch (Exception e) {
            throw new DukeException("Error with file/directory initialization!");
        }


        try {
            Scanner readFile = new Scanner(new File(this.filePath));
            while (readFile.hasNextLine()) {
                Task taskToAdd;
                String taskData = readFile.nextLine();
                String[] taskArray = taskData.split(",");
                String taskInput = taskArray[0].toUpperCase(Locale.ROOT);
                switch (taskInput) {
                    case "E":
                        if (taskArray.length == 4) {
                            taskToAdd = new Event(taskArray[2], taskArray[3]);
                            if (taskArray[1].equals("1")) {
                                taskToAdd.markIsDone();
                            }
                            tasks.add(taskToAdd);
                        } else {
                            throw new DukeException("Unable to read file format!");
                        }
                        break;
                    case "T":
                        if (taskArray.length == 3) {
                            taskToAdd = new Todo(taskArray[2]);
                            if (taskArray[1].equals("1")) {
                                taskToAdd.markIsDone();
                            }
                            tasks.add(taskToAdd);
                        } else {
                            throw new DukeException("Unable to read file format!");
                        }
                        break;
                    case "D":
                        if (taskArray.length == 4) {
                            taskToAdd = new Deadline(taskArray[2], taskArray[3]);
                            if (taskArray[1].equals("1")) {
                                taskToAdd.markIsDone();
                            }
                            tasks.add(taskToAdd);
                        } else {
                            throw new DukeException("Unable to read file format!");
                        }
                        break;
                    default:
                        System.out.println("No tasks in file!");
                }
            }

            System.out.println("Here is the list of tasks we have saved!");
            TaskList.listTasks(tasks);
        } catch (FileNotFoundException e) {
            throw new DukeException("File cannot be found!");
        }

        return tasks;
    }
}