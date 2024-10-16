import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class PrefixDictionary {
	private static class Node {
		Node[] children;
		Node parent;
		boolean isLeaf = true;
		boolean isWord = false;
		String value = "";
		int count;

		public Node(Node parent) {
			this.children = new Node[26];
			this.parent = parent;
		}

		public Node(Node parent, String value) {
			this(parent);
			this.value = value;
			this.count = 1;
		}

		public Node(Node parent, String value, boolean isWord) {
			this(parent, value);
			this.isWord = isWord;
		}

		@Override
		public String toString() {
			return value + ": " + count + ", " + isWord;
		}

		public void combine(String word) {
			int lcp = Similarity.firstDiff(this.value, word);
			this.count++;
			int idx;
			if (lcp == this.value.length() && lcp == word.length()) {
				this.isWord = true;
			} else {
				this.isLeaf = false;
				if (lcp != this.value.length() && lcp != word.length()) {
					this.isWord = false;
				}
				if (lcp != this.value.length()) {
					idx = this.value.charAt(lcp) - 'a';
					if (this.children[idx] == null) {
						this.children[idx] = new Node(this, this.value.substring(lcp), true);
					} else {
						this.children[idx].isWord = true;
						this.children[idx].combine(this.value.substring(lcp));
					}

				}
				if (lcp != word.length()) {
					idx = word.charAt(lcp) - 'a';
					if (this.children[idx] == null) {
						this.children[idx] = new Node(this, word.substring(lcp), true);
					} else {
						this.children[idx].combine(word.substring(lcp));
					}
				}
			}
			this.value = this.value.substring(0, lcp);
		}
	}

	private final Node root;

	public PrefixDictionary() {
		root = new Node(null);
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			toTreeString(sw, "", root);
		} catch (IOException e) {
			System.err.println("Error writing to string: " + e);
		}
		return sw.toString();
	}

	public void toFile(Path path) throws IOException {
		try (BufferedWriter br = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			toJSON(br, 0, root);
		}
	}

	public String toJSON() {
		StringWriter sw = new StringWriter();
		try {
			toJSON(sw, 0, root);
		} catch (IOException e) {
			System.err.println("Error writing to string: " + e);
		}
		return sw.toString();
	}

	private void toJSON(Writer writer, int indent, Node curr) throws IOException {
		Node[] children = Arrays.stream(curr.children).filter(Objects::nonNull).toArray(Node[]::new);
		writer.write("{\"");
		writer.write(curr.value);
		writer.write("\": [");
		if (children.length > 0) {
			writer.write("\n");
			writer.write("  ".repeat(indent + 1));
			toJSON(writer, indent + 1, children[0]);
			for (int i = 1; i < children.length; i++) {
				writer.write(",\n");
				writer.write("  ".repeat(indent + 1));
				toJSON(writer, indent + 1, children[i]);
			}
			writer.write("\n");
			writer.write("  ".repeat(indent));
		}
		writer.write("]}");
	}

	private void toTreeString(Writer writer, String prefix, Node curr) throws IOException {
		Node[] children = Arrays.stream(curr.children).filter(Objects::nonNull).toArray(Node[]::new);
		if (curr.value.isEmpty()) {
			writer.write("\"\"");
		}
		writer.write(curr.toString());
		writer.write("\n");
		for (int i = 0; i < children.length; i++) {
			writer.write(prefix);
			if (i == children.length - 1) {
				writer.write("└─");
				toTreeString(writer, prefix + "  ", children[i]);
			} else {
				writer.write("├─");
				toTreeString(writer, prefix + "│ ", children[i]);
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
		return end != null && end.isLeaf && word.endsWith(end.value);
	}

	public boolean checkPrefix(String word) {
		return getPrefixEnd(word) != null;
	}

	private Node getPrefixEnd(String word) {
		word = word.trim().toLowerCase();
		Node currNode = root;
		int lcp;
		while (currNode != null && !word.isEmpty()) {
			lcp = Similarity.firstDiff(currNode.value, word);
			if (lcp != currNode.value.length() && lcp != word.length()) {
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
