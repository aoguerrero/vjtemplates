package io.github.aoguerrero;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;

public class Generator {

	public void generate(String inputDir, String outputDir, String jsonPath) throws IOException {

		File jsonFile = new File(jsonPath);
		String json = FileUtils.readFileToString(jsonFile, "utf-8");
		
		ContextLoader jsonLoader = new ContextLoader();
		VelocityContext context = jsonLoader.load(json);

		TemplateProcessor processor = new TemplateProcessor(inputDir, outputDir, context);

		Iterator<File> files = FileUtils.iterateFiles(new File(inputDir), null, true);
		while (files.hasNext()) {
			File file = files.next();
			processor.process(file);
		}
	}
	
}
