package io.github.aoguerrero;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws URISyntaxException, IOException {

		if (args.length < 2) {
			logger.error("Input and output directory are required.");
			return;
		}

		String inputDir = args[0];
		String outputDir = args[1];

		ObjectMapper mapper = new ObjectMapper();
		File jsonFile = new File(inputDir + "/input.json");
		String json = FileUtils.readFileToString(jsonFile, "utf-8");
		JsonNode mainNode = mapper.readTree(json);

		VelocityContext context = new VelocityContext();

		Iterator<Entry<String, JsonNode>> fields = mainNode.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> field = fields.next();
			String key = field.getKey();
			JsonNode value = field.getValue();
			if (value.isObject()) {
				Iterator<Entry<String, JsonNode>> childFields = value.fields();
				while (childFields.hasNext()) {
					Entry<String, JsonNode> childField = childFields.next();
					context.put(key + "_" + childField.getKey(), childField.getValue().asText());
				}
			} else {
				context.put(key, value.asText());
			}
		}
		VelocityEngine engine = new VelocityEngine();

		engine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "/");
		engine.init();

		Iterator<File> files = FileUtils.iterateFiles(new File(inputDir + "/templates"), null, true);

		Path inputPath = Paths.get(inputDir + "/templates");
		Pattern pattern = Pattern.compile("\\{(.*)\\}");

		while (files.hasNext()) {

			File file = files.next();

			String templatePath = file.getAbsolutePath();
			Template template = engine.getTemplate(templatePath);
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			String merged = writer.toString();

			Path relativePath = inputPath.relativize(file.toPath());

			String mergedPath = FilenameUtils.removeExtension(outputDir + "/" + relativePath);
			Matcher matcher = pattern.matcher(mergedPath);
			while (matcher.find()) {
				String key = matcher.group(1);
				Object value = context.get(key);
				if (value != null) {
					mergedPath = mergedPath.replaceAll("\\{" + key + "\\}", value.toString());
				}
			}
			FileUtils.write(new File(mergedPath), merged, Charset.forName("utf-8"));
		}
	}
}
