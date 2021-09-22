package io.github.aoguerrero;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.junit.jupiter.api.Test;

class FileProcessorTest {

	@Test
	void test() throws IOException, URISyntaxException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource1 = classLoader.getResource("test_config.json");
		String json = FileUtils.readFileToString(new File(resource1.toURI()), "utf-8");
		ContextLoader loader = new ContextLoader();
		VelocityContext context = loader.load(json);

		FileProcessor processor = new FileProcessor(null, null, context);
		URL resource2 = classLoader.getResource("test_template.vm");
		String merged = processor.merge(new File(resource2.toURI()), context);
		assertTrue(merged.equals("1\r\n2\r\n4\r\n\r\nx1\r\nx2\r\nx3\r\n"));
	}

}
