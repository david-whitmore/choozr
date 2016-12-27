package org.dw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

/**
 * Main class to shuffle the lines in a text file, and output the result.
 * 
 * @author David Whitmore
 */
public class Shuffler {
	/** Shuffles the file, and returns all of the lines. */
	public List<String> shuffle(Reader in) throws IOException {
		try (BufferedReader buff = new BufferedReader(in)) {
			List<String> lines = buff.lines().collect(Collectors.toList());

			lines = discardBlankLines(lines);

			Collections.shuffle(lines);

			return lines;
		}
	}

	/** Shuffles the file, and returns the first <em>n</em> lines. */
	public List<String> shuffle(int n, Reader in) throws IOException {
		try (BufferedReader buff = new BufferedReader(in)) {
			List<String> lines = buff.lines().collect(Collectors.toList());

			lines = discardBlankLines(lines);

			Collections.shuffle(lines);

			return lines.subList(0, n);
		}
	}

	private List<String> discardBlankLines(List<String> lines) {
		return lines.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
	}

	public static void main(String[] args) throws IOException {
		Shuffler shuffler = new Shuffler();

		if (args.length == 2) {
			shuffle(args, shuffler);
			return;
		}

		throw new RuntimeException(
				"Invalid syntax.  Try: shuffle <numberOfItemsToChoose> <file> OR chooze <numberOfItemsToChoose> < fileStream.");
	}

	private static void shuffle(String[] args, Shuffler shuffler) throws IOException, FileNotFoundException {
		int n = Integer.parseInt(args[0].trim());
		File file = new File(args[1].trim());

		try (Reader in = new FileReader(file)) {
			List<String> shuffledLines = shuffler.shuffle(n, in);

			outputLines(shuffledLines);
		}
	}

	private static void outputLines(List<String> choices) {
		choices.stream().forEach(x -> System.out.println(x));
	}
}
