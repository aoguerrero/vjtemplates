package io.github.aoguerrero;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GradlePlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getTasks().create("projectSkeleton", GradleTask.class);
	}
}
