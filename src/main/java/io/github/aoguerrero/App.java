package io.github.aoguerrero;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws URISyntaxException, IOException {
		DirectoryProcessor.generate("D:\\github\\newprj\\templates", "D:\\github\\newprj",
				"D:\\github\\newprj\\config.json");
	}
}
