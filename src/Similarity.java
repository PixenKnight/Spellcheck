public class Similarity {
	public static String longer(String word1, String word2) {
		return word1.length() >= word2.length() ? word1 : word2;
	}

	public static String editSimilarity(String word1, String word2, int idx1, int idx2) {
		if (idx1 == -1 || idx2 == -1) {
			return "";
		}

		if (word1.charAt(idx1) == word2.charAt(idx2)) {
			return editSimilarity(word1, word2, idx1 - 1, idx2 - 1) + word1.charAt(idx1);
		} else {
			return longer(editSimilarity(word1, word2, idx1 - 1, idx2), editSimilarity(word1, word2, idx1, idx2 - 1));
		}
	}

	public static String editSimilarity(String word1, String word2) {
		return editSimilarity(word1, word2, word1.length() - 1, word2.length() - 1);
	}
}
