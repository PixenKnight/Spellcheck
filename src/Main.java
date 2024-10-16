import java.io.IOException;
import java.nio.file.Path;

import com.paul.ArgumentParser;

public class Main {
	public static void main(String[] args) {
		ArgumentParser parser = new ArgumentParser(args);

		if (parser.hasFlag("-input")) {

		} else {
			throw new RuntimeException("No input filepath specified");
		}

		PrefixDictionary dict = new PrefixDictionary();

		System.out.println(dict);
		System.out.println(dict.checkWord("helios"));
		try {
			dict.toFile(Path.of("dict.txt"));
		} catch (IOException ignored) {}

		//System.out.println(Similarity.lcs(words[2], words[8]));
	}
}