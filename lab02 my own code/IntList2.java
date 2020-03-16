public class IntList2 {
	public int first;
	public IntList2 rest;

	public IntList2(int f, IntList2 r) {
		first = f;
		rest = r;
	}

	public int size() {
		if (this.rest == null) {
			return 1;
		}
		else {
			return this.rest.size() + 1;
		}
	}

	public int iterativeSize() {
		IntList current = this;
		int totalSize = 0;
		while (current != null) {
			totalSize += 1;
			current = current.rest
		}
		return totalSize;
	}

	public int get(int i) {
		if (i == 0) {
			return first;
		}
		return rest.get(i - 1);
	}

	public static void main(String[] args) {
		IntList2 L = new IntList2(15,null);
		L = new IntList(10,L);
		L = new IntList(5,L);
	}
}