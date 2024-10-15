import java.util.Arrays;
import java.util.Objects;

public class PrefixDictionary {
	private static class Node {
		Node[] children;
		Node parent;
		boolean leaf = false;
		String word;

		public Node(Node parent) {
			this.children = new Node[26];
			this.parent = parent;
		}

		public Node(Node parent, String word) {
			this(parent);
			this.leaf = true;
			this.word = word;
		}

		public void combine(String word) {
			int lcp = Similarity.firstDiff(this.word, word);
			this.leaf = false;
			int idx;
			if (lcp != this.word.length()) {
				idx = this.word.charAt(lcp) - 'a';
				if (this.children[idx] == null) {
					this.children[idx] = new Node(this, this.word.substring(lcp));
				} else {
					this.children[idx].combine(this.word.substring(lcp));
				}
			}
			if (lcp != word.length()) {
				idx = word.charAt(lcp) - 'a';
				if (this.children[idx] == null) {
					this.children[idx] = new Node(this, word.substring(lcp));
				} else {
					this.children[idx].combine(word.substring(lcp));
				}
			}
			this.word = this.word.substring(0, lcp);
		}
	}

	private final Node root;

	public PrefixDictionary() {
		root = new Node(null, "");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		pretty(sb, 0, root, false);
		return sb.toString();
	}

	private void pretty(StringBuilder sb, int indentation, Node curr, boolean end) {
		Node[] children = Arrays.stream(curr.children).filter(Objects::nonNull).toArray(Node[]::new);
		sb.append(curr.word);
		sb.append("\n");
		for (int i = 0; i < children.length; i++) {
			if (end) {
				sb.append("  ".repeat(indentation));
			} else {
				sb.append("│ ".repeat(indentation));
			}

			if (i == children.length - 1) {
				sb.append("└─");
				pretty(sb, indentation + 1, children[i], true);
			} else {
				sb.append("├─");
				pretty(sb, indentation + 1, children[i], false);
			}
		}
	}

	public void addWord(String word) {
		word = word.trim().toLowerCase();
		if (word.isEmpty() || !word.chars().allMatch(Character::isLetter)) return;
		root.combine(word);
	}

	public void addWords(String[] words) {
		for (String word : words) {
			addWord(word);
		}
	}

	public void addWords(Iterable<String> words) {
		words.forEach(this::addWord);
	}

	public boolean checkWord(String word) {
		word = word.trim().toLowerCase();
		Node end = getPrefixEnd(word);
		return end != null && end.leaf && word.endsWith(end.word);
	}

	public boolean checkPrefix(String word) {
		return getPrefixEnd(word) != null;
	}

	private Node getPrefixEnd(String word) {
		word = word.trim().toLowerCase();
		Node currNode = root;
		int lcp;
		while (currNode != null && !word.isEmpty()) {
			lcp = Similarity.firstDiff(currNode.word, word);
			if (lcp != currNode.word.length() && lcp != word.length()) {
				return null;
			}
			word = word.substring(lcp);
			if (!word.isEmpty()) {
				currNode = currNode.children[word.charAt(0) - 'a'];
			}
		}
		return currNode;
	}
}
