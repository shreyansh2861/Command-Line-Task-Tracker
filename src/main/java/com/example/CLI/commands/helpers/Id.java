package com.example.CLI.commands.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class Id {
    final File latestIdFile = new File("src/main/resources/latestId.json");

    public Integer getId(JsonNode tasksNode) throws IOException {
        if (tasksNode == null || !tasksNode.isArray() || tasksNode.isEmpty()) {
            return 0;
        }
        JsonNode lastTask = tasksNode.get(tasksNode.size() - 1);
        return lastTask.get("id").asInt();
    }

    public void setId(Integer id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(latestIdFile);
        ObjectNode obj = (ObjectNode) rootNode;
        obj.put("id", id);
        mapper.writerWithDefaultPrettyPrinter().writeValue(latestIdFile, obj);
    }
}
