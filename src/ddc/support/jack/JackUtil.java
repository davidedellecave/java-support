package ddc.support.jack;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	public static String toPrettified(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
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

}
