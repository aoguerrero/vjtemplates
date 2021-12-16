package io.github.aoguerrero;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.junit.Test;

public class FileProcessorTest {

	@Test
	public void test() throws IOException, URISyntaxException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource1 = classLoader.getResource("test_config.json");
		String json = FileUtils.readFileToString(new File(resource1.toURI()), "utf-8");
		JsonContextLoader loader = new JsonContextLoader();
		VelocityContext context = loader.load(json);

		FileProcessor processor = new FileProcessor(null, null, context);
		URL resource2 = classLoader.getResource("test_template.vm");
		String merged = processor.merge(new File(resource2.toURI()), context);
		assertTrue(merged.equals("1\n2\n4\n\nx1\nx2\nx3\n"));
	}

}
