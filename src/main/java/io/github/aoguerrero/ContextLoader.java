package io.github.aoguerrero;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ContextLoader {

	public VelocityContext load(String json) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode mainNode = mapper.readTree(json);
		VelocityContext context = new VelocityContext();
		addFields(mainNode, context);
		return context;
	}

	private void addFields(JsonNode node, VelocityContext context) {
		addFields(node, null, context);
	}

	private void addFields(JsonNode node, String parentKey, VelocityContext context) {
		parentKey = parentKey == null ? "" : parentKey + "_";
		Iterator<Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> field = fields.next();
			String key = parentKey + field.getKey();
			JsonNode value = field.getValue();
			if (!value.isObject()) {
				context.put(key, value.asText());
			} else if(value.isArray()) {
				Iterator<JsonNode> elements = value.elements();
				List<String> listElements = new ArrayList<>(); 
				while(elements.hasNext()) {
					JsonNode element = elements.next();
					if(!element.isContainerNode()) {
						listElements.add(element.asText());
					} else {
						throw new IllegalArgumentException("Not supported in this version.");
					}
				}
				context.put(key, listElements);
			} else {
				addFields(value, key, context);
			}
		}
	}
}
