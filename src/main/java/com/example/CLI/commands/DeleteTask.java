package com.example.CLI.commands;

import com.example.CLI.commands.helpers.DateAndTime;
import com.example.CLI.commands.helpers.Id;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@ShellComponent
public class DeleteTask {

    final File file = new File("tasks file location");
    public Id idHelper =  new Id();

    public JsonNode resetIds(JsonNode rootNode, JsonNode tasksNode){
        int i=1;
        for(JsonNode task : tasksNode){
            ObjectNode t = (ObjectNode) task;
            t.put("id",i);
            i++;
        }
        return tasksNode;
    }

    @ShellMethod(key = "task delete", value = "Delete task using the id")
    public String deleteTask(@ShellOption(help = "Task id to delete") Integer id) {
        try{
            if (!file.exists()) {
                return "Input file not found. No tasks available. To add new tasks please use `tasks-add taskName taskDescription`";
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(file);
            JsonNode tasksNode = rootNode.get("tasks");

            if (tasksNode == null || !tasksNode.isArray()) {
                return "No Tasks in the file to delete";
            }

            ArrayNode newTasksArray = mapper.createArrayNode();

            for(JsonNode task : tasksNode){
                if(!(task.get("id").asInt()==id)){
                    ObjectNode t = mapper.createObjectNode();
                    t.put("id", task.get("id").asText());
                    t.put("description", task.get("description").asText());
                    t.put("status", task.get("status").asText());
                    t.put("created_at", task.get("created_at").asText());
                    t.put("updated_at", task.get("updated_at").asText());

                    newTasksArray.add(t);
                }
            }

            if(newTasksArray.isEmpty()){
                idHelper.setId(0);
            }

            newTasksArray = (ArrayNode) resetIds(rootNode,newTasksArray);

            ((ObjectNode) rootNode).set("tasks", newTasksArray);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);

            System.out.println("Task deleted successfully.");
        }
        catch(IOException e){
            System.err.println("Error reading or writing JSON: " + e.getMessage());
        }

        return id + " deleted from tasks list";
    }

    @ShellMethod(key="tasks delete", value="Delete multiple tasks using ids")
    public String deleteTask(@ShellOption String[] ids) throws IOException {
        try{
            if (!file.exists()) {
                return "Input file not found. No tasks available. To add new tasks please use `tasks-add taskName taskDescription`";
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(file);
            JsonNode tasksNode = rootNode.get("tasks");

            if (tasksNode == null || !tasksNode.isArray()) {
                return "No Tasks in the file to delete";
            }

            Arrays.sort(ids);
            int i=0;
            ArrayNode newTasksArray = mapper.createArrayNode();

            for(JsonNode task : tasksNode){
                if(i>=ids.length || !(task.get("id").asInt()==Integer.parseInt(ids[i]))){
                    ObjectNode t = mapper.createObjectNode();
                    t.put("id", task.get("id").asText());
                    t.put("description", task.get("description").asText());
                    t.put("status", task.get("status").asText());
                    t.put("created_at", task.get("created_at").asText());
                    t.put("updated_at", task.get("updated_at").asText());

                    newTasksArray.add(t);
                }
                else if(task.get("id").asInt()==Integer.parseInt(ids[i])){
                    i++;
                }
            }

            if(newTasksArray.isEmpty()){
                idHelper.setId(0);
            }
            newTasksArray = (ArrayNode) resetIds(rootNode,newTasksArray);


            ((ObjectNode) rootNode).set("tasks", newTasksArray);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
        }
        catch(IOException e){
            System.err.println("Error reading or writing JSON: " + e.getMessage());
        }

        return "Tasks deleted successfully";
    }
}
