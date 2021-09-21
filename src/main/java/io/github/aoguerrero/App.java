package io.github.aoguerrero;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws URISyntaxException, IOException {

		Generator generator = new Generator();
		generator.generate("D:\\github\\vjgenerator\\input\\templates", "D:\\github\\vjgenerator\\output",
				"D:\\github\\vjgenerator\\input\\input.json");
	}
}
