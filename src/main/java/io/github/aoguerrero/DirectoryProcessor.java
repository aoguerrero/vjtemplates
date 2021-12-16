package io.github.aoguerrero;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;

public class DirectoryProcessor {

	public static void generate(String inputDir, String outputDir, String jsonPath) throws IOException {

		File jsonFile = new File(jsonPath);
		String json = FileUtils.readFileToString(jsonFile, "utf-8");
		
		VelocityContext context = JsonContextLoader.load(json);

		FileProcessor processor = new FileProcessor(inputDir, outputDir, context);

		Iterator<File> files = FileUtils.iterateFiles(new File(inputDir), null, true);
		while (files.hasNext()) {
			File file = files.next();
			processor.process(file);
		}
	}
	
}
