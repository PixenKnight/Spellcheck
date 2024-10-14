public class Main {
	public static void main(String[] args) {
		String[] words = new String[]{"Hello", "helios", "world", "Wallaby", "worse", "worsen", "gaunt", "n00b"};

		PrefixDictionary dict = new PrefixDictionary();
		for (String word : words) {
			dict.addWord(word);
		}

		System.out.println(dict);
		System.out.println(dict.checkWord("helio"));
	}
}