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

import io.github.aoguerrero.utils.MintAddonUtils;

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
		List<ReplacedVariables> targetDirs = replaceVariables(targetDirWithVariables);
		for (ReplacedVariables targetDir : targetDirs) {
			File targetFile = new File(targetDir.fileName);
			if (FilenameUtils.isExtension(file.getName(), "vm") || FilenameUtils.isExtension(file.getName(), "vm1")) {
				targetFile = new File(FilenameUtils.removeExtension(targetDir.fileName));
			}
			if (FilenameUtils.isExtension(file.getName(), "vm")) {
				FileUtils.writeStringToFile(targetFile, merge(file, targetDir.newContext), "utf-8");
			} else if (FilenameUtils.isExtension(file.getName(), "vm1")) {
				if (!targetFile.exists()) {
					FileUtils.writeStringToFile(targetFile, merge(file, targetDir.newContext), "utf-8");
				}
			} else {
				if (!file.isDirectory()) {
					FileUtils.copyFile(file, targetFile);
				} else {
					FileUtils.forceMkdirParent(targetFile);
				}
			}
		}
	}

	public String merge(File file, VelocityContext context)
			throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
		context.put("Utils", MintAddonUtils.class);
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "log tag name", FileUtils.readFileToString(file, "utf-8"));
		return writer.toString();
	}

	private String getRelativePath(String baseDir, String fullDir) {
		Path basePath = Paths.get(baseDir);
		return basePath.relativize(Paths.get(fullDir)).toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<ReplacedVariables> replaceVariables(String fileName) {
		List<ReplacedVariables> replaced = new ArrayList<>();
		Matcher matcher = this.pattern.matcher(fileName);
		while (matcher.find()) {
			String key = matcher.group(1);
			String strToReplace = "\\{" + key + "\\}";
			String[] keys = key.split("\\.");
			if (keys.length > 1) {
				if (this.context.get(keys[0]) instanceof Map) {
					String value = ((Map) this.context.get(keys[0])).get(keys[1]).toString();
					fileName = fileName.replaceAll(strToReplace, value);
				} else if (this.context.get(keys[0]) instanceof List) {
					List<Map> maps = (List<Map>) this.context.get(keys[0]);
					for (Map map : maps) {
						VelocityContext newContext = new VelocityContext(this.context);
						newContext.put(keys[0], map);
						String replacement = map.get(keys[1]).toString();
						replaced.add(new ReplacedVariables(fileName.replaceAll(strToReplace, replacement), newContext));
					}
					return replaced;
				}
			}
			fileName = fileName.replaceAll(strToReplace, this.context.get(key).toString());
		}
		replaced.add(new ReplacedVariables(fileName, context));
		return replaced;
	}

	private class ReplacedVariables {
		ReplacedVariables(String fileName, VelocityContext newContext) {
			this.fileName = fileName;
			this.newContext = newContext;
		}

		String fileName;
		VelocityContext newContext;
	}
}
