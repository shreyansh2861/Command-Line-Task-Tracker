package com.example.CLI.commands;

import com.example.CLI.commands.helpers.DateAndTime;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import reactor.core.Exceptions;

import java.io.File;
import java.io.IOException;

@ShellComponent
public class UpdateTaskStatus {

    final File file = new File("C:/Users/shrey/IdeaProjects/Spring-and-Sprinboot/CLI/src/main/resources/tasks.json");
    public DateAndTime dnt = new DateAndTime();

//    @ShellMethod(key="task status-update-next",value="Update task status automatically")
//    public String updateTask(@ShellOption Integer id) throws IOException {
//        try{
//            if (!file.exists()) {
//                return "Input file not found. No tasks available. To add new tasks please use `tasks-add taskName taskDescription`";
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.readTree(file);
//            JsonNode tasksNode = rootNode.get("tasks");
//
//            if (tasksNode == null || !tasksNode.isArray()) {
//                return "No Tasks in the file to delete";
//            }
//
//            ArrayNode newTasksArray = new  ObjectMapper().createArrayNode();
//
//            for(JsonNode task : tasksNode) {
//                ObjectNode t = mapper.createObjectNode();
//                boolean updated = false;
//                t.put("id", task.get("id").asText());
//                t.put("description", task.get("description").asText());
//                if(task.get("id").asInt()==id){
//                    if(task.get("status").asText().equals("todo")){
//                        t.put("status", "in-progress");
//                    } else if (task.get("status").asText().equals("in-progress")) {
//                        t.put("status", "done");
//                    }
//                    updated = true;
//                }
//                else{
//                    t.put("status", task.get("status").asText());
//                }
//                t.put("created_at", task.get("created_at").asText());
//                if(updated){
//                    t.put("updated_at", dnt.current);
//                }
//                else{
//                    t.put("updated_at", task.get("updated_at").asText());
//                }
//                newTasksArray.add(t);
//            }
//
//            ((ObjectNode) rootNode).set("tasks", newTasksArray);
//            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
//        }
//        catch(IOException e){
//            System.err.println("Error reading or writing JSON: " + e.getMessage());
//        }
//        return "Task status updated successfully";
//    }

    @ShellMethod(key="task update-status", value="update the status of a task")
    public String updateTask(@ShellOption(help="task id to update status") Integer id, @ShellOption(help="task status",defaultValue = "Null") String status) throws IOException {
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

            ArrayNode newTasksArray = new  ObjectMapper().createArrayNode();

            if(status.equals("Null")){
                for(JsonNode task : tasksNode) {
                    ObjectNode t = mapper.createObjectNode();
                    boolean updated = false;
                    t.put("id", task.get("id").asText());
                    t.put("description", task.get("description").asText());
                    if(task.get("id").asInt()==id){
                        if(task.get("status").asText().equals("todo")){
                            t.put("status", "in-progress");
                        } else if (task.get("status").asText().equals("in-progress")) {
                            t.put("status", "done");
                        }
                        updated = true;
                    }
                    else{
                        t.put("status", task.get("status").asText());
                    }
                    t.put("created_at", task.get("created_at").asText());
                    if(updated){
                        t.put("updated_at", dnt.current);
                    }
                    else{
                        t.put("updated_at", task.get("updated_at").asText());
                    }
                    newTasksArray.add(t);
                }
            }
            else{
                if(!status.equalsIgnoreCase("todo") && !status.equalsIgnoreCase("in-progress") && !status.equalsIgnoreCase("done")){
                    throw Exceptions.failWithRejected("Status should be `todo`, `in-progress`, `done`");
                }
                for(JsonNode task : tasksNode) {
                    ObjectNode t = mapper.createObjectNode();
                    t.put("id", task.get("id").asText());
                    t.put("description", task.get("description").asText());
                    if(task.get("id").asInt()==id){
                        t.put("status", status);
                    }
                    else {
                        t.put("status", task.get("status").asText());
                    }
                    t.put("created_at", task.get("created_at").asText());
                    if(task.get("id").asInt()==id){
                        t.put("updated_at", dnt.current);
                    }
                    else {
                        t.put("updated_at", task.get("updated_at").asText());
                    }
                    newTasksArray.add(t);
                }
            }

            ((ObjectNode) rootNode).set("tasks", newTasksArray);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
        }
        catch(IOException e){
            System.err.println("Error reading or writing JSON: " + e.getMessage());
        }

        return "Task updated successfully";
    }
}
