/**
 * Utility class that holds methods to determine various similarities between Strings.
 *
 * @author Paul Maresquier
 */
public class Similarity {
	/**
	 * Finds whichever of the two passed {@link String} objects is longest.
	 * Defaults to {@code word1} if equal in length.
	 *
	 * @param word1 The first {@code String} to compare
	 * @param word2 The second {@code String} to compare
	 * @return {@code word1} if the length of {@code word1} is greater than or equal to the length of {@code word2},
	 * 		   {@code word2} otherwise
	 */
	public static String longer(String word1, String word2) {
		return word1.length() >= word2.length() ? word1 : word2;
	}

	/**
	 * Recursively calculates the Longest Common Subsequence (LCS) of two {@link String} objects.
	 * </br></br>
	 * The LCS of two strings is the longest sequence of characters shared by both strings.
	 * </br></br>
	 * Example:</br>
	 * The strings 'ABCBA' and 'CBBDA' both contain the subsequence 'BBA', but they also contain the subsequences 'BB',
	 * 'BA', and 'CB'.
	 * The longest of these is 'BBA' and so it is the LCS of 'ABCBA' and 'CBBDA'
	 * </br></br>
	 * The Longest Common Subsequence differs from the Longest Common Substring in that each character
	 * does not need to be in consecutive positions to count as a subsequence.
	 *
	 * @param word1 The first {@code String}
	 * @param word2 The second {@code String}
	 * @param idx1 The index to check in {@code word1}
	 * @param idx2 The index to check in {@code word2}
	 * @return A {@link String} containing the Longest Common Subsequence of the two provided strings
	 */
	private static String lcs(String word1, String word2, int idx1, int idx2) {
		if (idx1 == -1 || idx2 == -1) {
			return "";
		}

		if (word1.charAt(idx1) == word2.charAt(idx2)) {
			return lcs(word1, word2, idx1 - 1, idx2 - 1) + word1.charAt(idx1);
		} else {
			return longer(lcs(word1, word2, idx1 - 1, idx2),
						  lcs(word1, word2, idx1, idx2 - 1));
		}
	}

	/**
	 * Initial method to calculate the Longest Common Subsequence (LCS) of two {@link String} objects.
	 * Calls the recursive variant of the method with {@code idx1} and {@code idx2} set to the length of their
	 * respective strings - 1.
	 * @see Similarity#lcs(String, String, int, int)
	 *
	 * @param word1 The first {@code String} to compare
	 * @param word2 The second {@code String} to compare
	 * @return The LCS of the two strings
	 */
	public static String lcs(String word1, String word2) {
		return lcs(word1, word2, word1.length() - 1, word2.length() - 1);
	}

	/**
	 * Finds the index of the first different {@code char} between two {@link String} objects.
	 * 
	 * @param word1 The first {@code String} to compare
	 * @param word2 The second {@code String} to compare
	 * @return The index of the first char that differs the two words, or the length of the shorter {@code String} if
	 * 		   the longer {@code String} starts with it.
	 */
	public static int firstDiff(String word1, String word2) {
		if (word2.startsWith(word1)) { // Fastpath if word2 starts with word1
			return word1.length();
		} else if (word1.startsWith(word2)) { // Ditto for the opposite
			return word2.length();
		} else {
			for (int i = 0; i < Math.min(word1.length(), word2.length()); i++) {
				if (word1.charAt(i) != word2.charAt(i)) {
					return i;
				}
			}
		}
		return 0; // Words have no common prefix, should never be reached since any case would be caught above
	}

	/**
	 * Finds the Longest Common Prefix (LCP) shared by both {@link String} objects.
	 *
	 * @param word1 The first {@code String} to compare
	 * @param word2 The second {@code String} to compare
	 * @return The LCP of both strings
	 *
	 * @implNote Calculates the first different index of the strings and then creates a substring of word1 up to that
	 * 			 index.
	 */
	public static String lcp(String word1, String word2) {
		return word1.substring(0, firstDiff(word1, word2));
	}
}
