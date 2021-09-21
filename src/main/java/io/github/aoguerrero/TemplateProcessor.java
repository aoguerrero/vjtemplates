package io.github.aoguerrero;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class TemplateProcessor {

	private String inputDir;
	private String outputDir;
	private VelocityContext context;
	private Pattern pattern;

	public TemplateProcessor(String inputDir, String outputDir, VelocityContext context) {
		this.inputDir = inputDir;
		this.outputDir = outputDir;
		this.context = context;
		this.pattern = Pattern.compile("\\{(.*)\\}");
	}

	public void process(File file) throws IOException {
		String targetDir = outputDir + File.separator + getRelativePath(inputDir, file.getAbsolutePath());
		targetDir = replaceVariables(targetDir);
		if (FilenameUtils.isExtension(file.getName(), "vm")) {
			StringWriter writer = new StringWriter();
			Velocity.evaluate(context, writer, "log tag name", FileUtils.readFileToString(file, "utf-8"));
			String merged = writer.toString();
			targetDir = FilenameUtils.removeExtension(targetDir);
			FileUtils.writeStringToFile(new File(targetDir), merged, "utf-8");
		} else {
			if (!file.isDirectory()) {
				FileUtils.copyFile(file, new File(targetDir));
			} else {
				FileUtils.forceMkdirParent(new File(targetDir));
			}
		}
	}

	private String getRelativePath(String baseDir, String fullDir) {
		Path basePath = Paths.get(baseDir);
		return basePath.relativize(Paths.get(fullDir)).toString();
	}

	private String replaceVariables(String text) {
		Matcher matcher = this.pattern.matcher(text);
		while (matcher.find()) {
			String variable = matcher.group(1);
			text = text.replaceAll("\\{" + variable + "\\}", this.context.get(variable).toString());
		}
		return text;
	}

}
