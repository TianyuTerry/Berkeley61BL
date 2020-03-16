public class IntList {
	public int first;
	public IntList rest;

	public static void main(String[] args) {
		InList L = new IntList();
		L.first = 5;
		L.rest = null;

		L.rest = new IntList();
		L.rest.first = 10;

		L.rest.rest = new IntList();
		L.rest.rest.first = 15
	}
}