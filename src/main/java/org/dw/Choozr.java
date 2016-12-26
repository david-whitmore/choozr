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
 * Main class to choose <em>n</em> lines from a text file.
 * 
 * @author David Whitmore
 */
public class Choozr {
	public List<String> choose(int n, Reader in) throws IOException {
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
		Choozr choozr = new Choozr();

		switch (args.length) {
		case 1:
			choozeFromStandardInput(args, choozr);
			break;
		case 2:
			choozeFromFile(args, choozr);
			break;
		default:
			throw new RuntimeException(
					"Invalid syntax.  Try: chooze <numberOfItemsToChoose> <file> OR chooze <numberOfItemsToChoose> < fileStream.");
		}
	}

	private static void choozeFromStandardInput(String[] args, Choozr choozr) throws IOException {
		int n = Integer.parseInt(args[0].trim());

		try (InputStreamReader in = new InputStreamReader(System.in)) {
			choozr.choose(n, in);
		}
	}

	private static void choozeFromFile(String[] args, Choozr choozr) throws IOException, FileNotFoundException {
		int n = Integer.parseInt(args[0].trim());
		File file = new File(args[1].trim());

		try (Reader in = new FileReader(file)) {
			List<String> choices = choozr.choose(n, in);

			outputChoices(choices);
		}
	}

	private static void outputChoices(List<String> choices) {
		choices.stream().forEach(x -> System.out.println(x));
	}
}
