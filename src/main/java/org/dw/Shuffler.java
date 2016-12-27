package org.dw;

import static com.google.common.collect.Lists.newArrayList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

/**
 * Main class to shuffle the lines in a text file, and output the result.
 * 
 * @author David Whitmore
 */
public class Shuffler {
	public List<String> shuffle(int n, Reader in) throws IOException {
		try (BufferedReader buff = new BufferedReader(in)) {
			List<String> lines = buff.lines().collect(Collectors.toList());

			lines = discardHeader(lines);
			lines = discardBlankLines(lines);

			Random random = new Random(System.currentTimeMillis());
			List<String> choices = newArrayList();

			for (int i = 0; i < n; i++) {
				if (lines.isEmpty()) {
					break;
				}

				int index = random.nextInt(lines.size());

				choices.add(lines.get(index));
				lines.remove(index);
			}

			return choices;
		}
	}

	private List<String> discardBlankLines(List<String> lines) {
		return lines.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
	}

	private List<String> discardHeader(List<String> lines) {
		lines.remove(0);
		return lines;
	}

	public static void main(String[] args) throws IOException {
		Shuffler shuffler = new Shuffler();

		switch (args.length) {
		case 1:
			shuffleFromStandardInput(args, shuffler);
			break;
		case 2:
			shuffleFromFile(args, shuffler);
			break;
		default:
			throw new RuntimeException(
					"Invalid syntax.  Try: shuffle <numberOfItemsToChoose> <file> OR chooze <numberOfItemsToChoose> < fileStream.");
		}
	}

	private static void shuffleFromStandardInput(String[] args, Shuffler choozr) throws IOException {
		int n = Integer.parseInt(args[0].trim());

		try (InputStreamReader in = new InputStreamReader(System.in)) {
			choozr.shuffle(n, in);
		}
	}

	private static void shuffleFromFile(String[] args, Shuffler choozr) throws IOException, FileNotFoundException {
		int n = Integer.parseInt(args[0].trim());
		File file = new File(args[1].trim());

		try (Reader in = new FileReader(file)) {
			List<String> choices = choozr.shuffle(n, in);

			outputChoices(choices);
		}
	}

	private static void outputChoices(List<String> choices) {
		choices.stream().forEach(x -> System.out.println(x));
	}
}
