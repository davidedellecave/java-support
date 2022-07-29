package ddc.support.jack;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JackUtil {
	private static ObjectMapper mapper = new ObjectMapper();
	// {
	// mapper.setVisibility(mapper.getSerializationConfig()
	//
	// .getDefaultVisibilityChecker()
	//
	// .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
	//
	// .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
	//
	// .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
	//
	// .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
	// }

	public static String toPrettifiedString(Object obj) throws JsonProcessingException  {
		return (mapper.writerWithDefaultPrettyPrinter()).writeValueAsString(obj);
	}

	public static String toString(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		return mapper.writeValueAsString(obj);
	}

	public static JsonNode toJson(Object json) {
		return mapper.valueToTree(json);
	}

	public static JsonNode parse(String json) throws IOException {
		return mapper.readTree(json);
	}

	public static JsonNode parseOptional(String json) {
		try {
			return mapper.readTree(json);
		} catch (IOException e) {
			return null;
		}
	}

	public static <T> T parse(String json, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, valueType);
	}

	public List<? extends Object> parseList(String json, Class<? extends Object> elementClass) throws JsonParseException, JsonMappingException, IOException {
		List<? extends Object> list = mapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, elementClass));
		return list;
	}

	public static String asString(final JsonNode rootNode, String... names) {
		JsonNode node = rootNode;
		for (String name : names) {
			node = node.get(name);
			if (node == null)
				return null;
		}
		return node.asText().trim();
	}

	public static Long asLong(final JsonNode rootNode, String... names) {
		JsonNode node = rootNode;
		for (String name : names) {
			node = node.get(name);
			if (node == null)
				return null;
		}
		return node.asLong();
	}

	public static Integer asInt(final JsonNode rootNode, String... names) {
		JsonNode node = rootNode;
		for (String name : names) {
			node = node.get(name);
			if (node == null)
				return null;
		}
		return node.asInt();
	}

	public static Boolean asBoolean(final JsonNode rootNode, String... names) {
		JsonNode node = rootNode;
		for (String name : names) {
			node = node.get(name);
			if (node == null)
				return null;
		}
		return node.asBoolean();
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object obj)  {
		Map<String,Object> map = mapper.convertValue(obj, Map.class);
		return map;

	}
	

	public static void merge_to_be_tested(JsonNode toBeMerged, JsonNode mergedInTo) {
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

	private static void updateArray(JsonNode valueToBePlaced, Map.Entry<String, JsonNode> toBeMerged) {
		toBeMerged.setValue(valueToBePlaced);
	}

	private static void updateObject(JsonNode mergeInTo, ValueNode valueToBePlaced, Map.Entry<String, JsonNode> toBeMerged) {
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
