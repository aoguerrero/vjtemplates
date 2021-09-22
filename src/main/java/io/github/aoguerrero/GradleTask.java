package io.github.aoguerrero;

import java.io.File;
import java.io.IOException;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public class GradleTask extends DefaultTask {

	String currentPath = System.getProperty("user.dir");
	
	@Input
	String inputDir = currentPath + File.separator + "templates";

	@Input
	String outputDir = currentPath;

	@Input
	String json = currentPath + File.separator + "config.json";

	@TaskAction
	public void projectSkeleton() throws IOException {
		DirectoryProcessor.generate(getInputDir(), getOutputDir(), getJson());
	}

	public String getInputDir() {
		return inputDir;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public String getJson() {
		return json;
	}

}
