public class Main {
	public static void main(String[] args) {
		String[] words = new String[]{"Hello", "helios", "world", "Wallaby", "worse", "worsen", "gaunt", "n00b", "order"};

		PrefixDictionary dict = new PrefixDictionary();
		dict.addWords(words);

		System.out.println(dict);
		System.out.println(dict.checkWord("helio"));

		System.out.println(Similarity.lcs(words[2], words[8]));
	}
}