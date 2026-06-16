 package com.example.CLI.commands;

import com.example.CLI.commands.helpers.DateAndTime;
import com.example.CLI.commands.helpers.Id;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

import com.example.CLI.commands.helpers.Id.*;

@ShellComponent
public class AddTask {

    final File file = new File("C:/Users/shrey/IdeaProjects/Spring-and-Sprinboot/CLI/src/main/resources/tasks.json");
    public Id helperId = new Id();
    public DateAndTime dnt = new DateAndTime();

    @ShellMethod(key = "task add", value = "Adds tasks to the list")
    public String addTask(@ShellOption String description, @ShellOption(defaultValue = "todo") String status) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (!file.exists()) {
                System.out.println("Input file not found. Creating new JSON structure.");
                ObjectNode root = mapper.createObjectNode();
                ArrayNode tasks = mapper.createArrayNode();
                root.set("tasks", tasks);

                mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
                helperId.setId(0);
            }
            JsonNode rootNode = mapper.readTree(file);
            JsonNode tasksNode = rootNode.get("tasks");

            if (tasksNode == null || !tasksNode.isArray()) {
                System.out.println("Invalid JSON format: 'tasks' array is missing. Creating it.");
                ObjectNode newRoot = mapper.createObjectNode();
                tasksNode = mapper.createArrayNode();
                newRoot.set("tasks", tasksNode);
                rootNode = newRoot;
            }

            int id = 0;

            if (tasksNode instanceof ArrayNode tasksArray) {
                int newId = helperId.getId(tasksNode) + 1;
                id = newId;
                ObjectNode newTask = mapper.createObjectNode();
                newTask.put("id", newId);
                newTask.put("description", description);
                newTask.put("status", status);
                newTask.put("created_at", dnt.current);
                newTask.put("updated_at", dnt.current);

                tasksArray.add(newTask);
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

            helperId.setId(id+1);

        } catch (IOException e) {
            System.err.println("Error reading or writing JSON: " + e.getMessage());
        }

        return "\"" + description + "\"" + " added to tasks list";
    }
}

