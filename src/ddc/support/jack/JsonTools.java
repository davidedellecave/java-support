package ddc.support.jack;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonTools {
    private ObjectMapper mapper;

    public JsonTools() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

    }

    public JsonTools(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public static ObjectMapper buildMapperByPolymorphicTypes(String[] types) {
        var builder = BasicPolymorphicTypeValidator.builder();
        for (String type : types) {
            builder.allowIfSubType(type);
        }
        PolymorphicTypeValidator polymorphicTypeValidator = builder.build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
        return mapper;
    }

    public String toPrettifiedString(Object obj) throws JsonProcessingException {
        return (mapper.writerWithDefaultPrettyPrinter()).writeValueAsString(obj);
    }

    public String toPrettifiedString(String json) throws IOException {
        return parse(json).toPrettyString();
    }

    public String toString(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    public JsonNode toJson(Object json) {
        return mapper.valueToTree(json);
    }

    public JsonNode parse(String json) throws IOException {
        return mapper.readTree(json);
    }

    public JsonNode parseOptional(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            return null;
        }
    }

    public <T> T parse(String json, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(json, valueType);
    }

    public List<? extends Object> parseList(String json, Class<? extends Object> elementClass) throws JsonParseException, JsonMappingException, IOException {
        List<? extends Object> list = mapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, elementClass));
        return list;
    }

    public String asString(final JsonNode rootNode, String... names) {
        JsonNode node = rootNode;
        for (String name : names) {
            node = node.get(name);
            if (node == null)
                return null;
        }
        return node.asText().trim();
    }

    public Long asLong(final JsonNode rootNode, String... names) {
        JsonNode node = rootNode;
        for (String name : names) {
            node = node.get(name);
            if (node == null)
                return null;
        }
        return node.asLong();
    }

    public Integer asInt(final JsonNode rootNode, String... names) {
        JsonNode node = rootNode;
        for (String name : names) {
            node = node.get(name);
            if (node == null)
                return null;
        }
        return node.asInt();
    }

    public Boolean asBoolean(final JsonNode rootNode, String... names) {
        JsonNode node = rootNode;
        for (String name : names) {
            node = node.get(name);
            if (node == null)
                return null;
        }
        return node.asBoolean();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = mapper.convertValue(obj, Map.class);
        return map;

    }


    public void merge_to_be_tested(JsonNode toBeMerged, JsonNode mergedInTo) {
        Iterator<Map.Entry<String, JsonNode>> incomingFieldsIterator = toBeMerged.fields();
        Iterator<Map.Entry<String, JsonNode>> mergedIterator = mergedInTo.fields();

        while (incomingFieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> incomingEntry = incomingFieldsIterator.next();

            JsonNode subNode = incomingEntry.getValue();

            if (subNode.getNodeType().equals(JsonNodeType.OBJECT)) {
                boolean isNewBlock = true;
                mergedIterator = mergedInTo.fields();
                while (mergedIterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = mergedIterator.next();
                    if (entry.getKey().equals(incomingEntry.getKey())) {
                        merge_to_be_tested(incomingEntry.getValue(), entry.getValue());
                        isNewBlock = false;
                    }
                }
                if (isNewBlock) {
                    ((ObjectNode) mergedInTo).replace(incomingEntry.getKey(), incomingEntry.getValue());
                }
            } else if (subNode.getNodeType().equals(JsonNodeType.ARRAY)) {
                boolean newEntry = true;
                mergedIterator = mergedInTo.fields();
                while (mergedIterator.hasNext()) {
                    Map.Entry<String, JsonNode> entry = mergedIterator.next();
                    if (entry.getKey().equals(incomingEntry.getKey())) {
                        updateArray(incomingEntry.getValue(), entry);
                        newEntry = false;
                    }
                }
                if (newEntry) {
                    ((ObjectNode) mergedInTo).replace(incomingEntry.getKey(), incomingEntry.getValue());
                }
            }
            ValueNode valueNode = null;
            JsonNode incomingValueNode = incomingEntry.getValue();
            switch (subNode.getNodeType()) {
                case STRING:
                    valueNode = new TextNode(incomingValueNode.textValue());
                    break;
                case NUMBER:
                    valueNode = new IntNode(incomingValueNode.intValue());
                    break;
                case BOOLEAN:
                    valueNode = BooleanNode.valueOf(incomingValueNode.booleanValue());
                default:
                    break;
            }
            if (valueNode != null) {
                updateObject(mergedInTo, valueNode, incomingEntry);
            }
        }
    }

    private void updateArray(JsonNode valueToBePlaced, Map.Entry<String, JsonNode> toBeMerged) {
        toBeMerged.setValue(valueToBePlaced);
    }

    private void updateObject(JsonNode mergeInTo, ValueNode valueToBePlaced, Map.Entry<String, JsonNode> toBeMerged) {
        boolean newEntry = true;
        Iterator<Map.Entry<String, JsonNode>> mergedIterator = mergeInTo.fields();
        while (mergedIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = mergedIterator.next();
            if (entry.getKey().equals(toBeMerged.getKey())) {
                newEntry = false;
                entry.setValue(valueToBePlaced);
            }
        }
        if (newEntry) {
            ((ObjectNode) mergeInTo).replace(toBeMerged.getKey(), toBeMerged.getValue());
        }
    }
}
