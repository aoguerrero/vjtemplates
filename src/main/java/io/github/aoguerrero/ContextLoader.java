package io.github.aoguerrero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		Iterator<Entry<String, JsonNode>> fields = mainNode.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> field = fields.next();
			JsonNode subNode = field.getValue();
			Object value = null;
			if(subNode.isObject()) {
				value = getMap(subNode);
			} else if(subNode.isArray()) {
				value = getList(subNode);
			} else {
				value = subNode.asText();	
			}
			context.put(field.getKey(), value);
		}
		return context;
	}

	private Map<String, Object> getMap(JsonNode node) {
		Map<String, Object> map = new HashMap<>();
		Iterator<Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> field = fields.next();
			JsonNode subNode = field.getValue();
			Object value = null;
			if(subNode.isObject()) {
				value = getMap(subNode);
			} else if(subNode.isArray()) {
				value = getList(subNode);
			} else {
				value = subNode.asText();	
			}
			map.put(field.getKey(), value);
		}
		return map;
	}
	
	private List<Object> getList(JsonNode node) {
		List<Object> list = new ArrayList<>();
		Iterator<JsonNode> subNodes = node.elements();
		while (subNodes.hasNext()) {
			JsonNode subNode = subNodes.next();
			Object value = null;
			if(subNode.isObject()) {
				value = getMap(subNode);
			} else if(subNode.isArray()) {
				value = getList(subNode);
			} else {
				value = subNode.asText();	
			}
			list.add(value);
		}
		return list;
	}
	
}
