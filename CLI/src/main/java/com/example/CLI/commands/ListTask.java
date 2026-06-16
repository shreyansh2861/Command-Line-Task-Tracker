package com.example.CLI.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import reactor.core.Exceptions;

import java.io.File;
import java.io.IOException;

@ShellComponent
public class ListTask {
    final File file = new File("C:/Users/shrey/IdeaProjects/Spring-and-Sprinboot/CLI/src/main/resources/tasks.json");

    public void printTask(JsonNode task){
        System.out.println("Id          : "+task.get("id").asText());
        System.out.println("Description : "+task.get("description").asText());
        System.out.println("Status      : "+task.get("status").asText());
        System.out.println("Created_At  : "+task.get("created_at").asText());
        System.out.println("Updated_At  : "+task.get("updated_at").asText());
        System.out.println("\n*********************************************************************************\n");
    }

    @ShellMethod(key="tasks list")
    public String listTask(@ShellOption(defaultValue = "all") String status) throws IOException{
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(file);
            JsonNode tasksNode = rootNode.get("tasks");

            if(tasksNode==null || !tasksNode.isArray()){
                return "No Tasks in the file to delete";
            }

            ArrayNode newTasksArray = mapper.createArrayNode();

            System.out.println("\n*********************************************************************************\n");
            if(status.equals("all")){
                for(JsonNode task : tasksNode){
                    printTask(task);
                }
            }
            else if(status.equals("todo")){
                for(JsonNode task : tasksNode){
                    if(task.get("status").asText().equals("todo")){
                        printTask(task);
                    }
                }
            }
            else if(status.equals("in-progress")){
                for(JsonNode task : tasksNode){
                    if(task.get("status").asText().equals("in-progress")){
                        printTask(task);
                    }
                }
            }
            else if(status.equals("done")){
                for(JsonNode task : tasksNode){
                    if(task.get("status").asText().equals("done")){
                        printTask(task);
                    }
                }
            }
            else if(status.equals("not done")){
                for(JsonNode task : tasksNode){
                    if(!task.get("status").asText().equals("done")){
                        printTask(task);
                    }
                }
            }
            else{
                throw Exceptions.failWithRejected("Status should be `todo`, `in-progress`, `done`");
            }
        }
        catch(IOException e){
            System.err.println("Error reading or writing JSON: " + e.getMessage());
        }

        return "These are your tasks";
    }
}
