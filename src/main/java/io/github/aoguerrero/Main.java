package io.github.aoguerrero;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws URISyntaxException, IOException {
		if (args.length < 3) {
			DirectoryProcessor.generate(args[0], args[1], args[2]);
		} else {
			logger.error("Invalid parameter count, use: \njava -jar vjtemplates-{version}.jar [Input Directory] [Output Directory] [JSSON File]");
		}
	}
}
