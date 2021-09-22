package io.github.aoguerrero;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class FileProcessor {

	private String inputDir;
	private String outputDir;
	private VelocityContext context;
	private Pattern pattern;

	public FileProcessor(String inputDir, String outputDir, VelocityContext context) {
		this.inputDir = inputDir;
		this.outputDir = outputDir;
		this.context = context;
		this.pattern = Pattern.compile("\\{(.*?)\\}");
	}

	public void process(File file) throws IOException {
		String targetDirWithVariables = outputDir + File.separator + getRelativePath(inputDir, file.getAbsolutePath());
		List<String> targetDirs = replaceVariables(targetDirWithVariables);
		for(String targetDir : targetDirs) {
			if (FilenameUtils.isExtension(file.getName(), "vm")) {
				targetDir = FilenameUtils.removeExtension(targetDir);
				FileUtils.writeStringToFile(new File(targetDir), merge(file, context), "utf-8");
			} else {
				if (!file.isDirectory()) {
					FileUtils.copyFile(file, new File(targetDir));
				} else {
					FileUtils.forceMkdirParent(new File(targetDir));
				}
			}
		}
	}

	public String merge(File file, VelocityContext context)
			throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "log tag name", FileUtils.readFileToString(file, "utf-8"));
		return writer.toString();
	}

	private String getRelativePath(String baseDir, String fullDir) {
		Path basePath = Paths.get(baseDir);
		return basePath.relativize(Paths.get(fullDir)).toString();
	}

	private List<String> replaceVariables(String fileName) {
		List<String> fileNames = new ArrayList<>();
		Matcher matcher = this.pattern.matcher(fileName);
		while (matcher.find()) {
			String key = matcher.group(1);
			String[] keys = key.split("\\.");
			if(keys.length > 1) {
				if(this.context.get(keys[0]) instanceof Map) {
					String value = ((Map)this.context.get(keys[0])).get(keys[1]).toString();
					fileName = fileName.replaceAll("\\{" + key + "\\}", value);
				} else if(this.context.get(keys[0]) instanceof List) {
					List<Map> maps = (List<Map>)this.context.get(keys[0]);
					for(Map map : maps) {
						fileName = fileName.replaceAll("\\{" + key + "\\}", map.get(keys[1]).toString());
						fileNames.add(fileName);
					}
					return fileNames;
				}
			}
			fileName = fileName.replaceAll("\\{" + key + "\\}", this.context.get(key).toString());
		}
		fileNames.add(fileName);
		return fileNames;
	}
}
