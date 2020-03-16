import java.util.Stack;
import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/* An AmoebaFamily is a tree, where nodes are Amoebas, each of which can have
   any number of children. */
public class AmoebaFamily implements Iterable<AmoebaFamily.Amoeba> {

    /* ROOT is the root amoeba of this AmoebaFamily */
    private Amoeba root = null;

    /* Creates an AmoebaFamily, where the first Amoeba's name is NAME. */
    public AmoebaFamily(String name) {
        root = new Amoeba(name, null);
    }

    /* Adds a new Amoeba with CHILDNAME to this AmoebaFamily as the youngest
       child of the Amoeba named PARENTNAME. This AmoebaFamily must contain an
       Amoeba named PARENTNAME. */
    public void addChild(String parentName, String childName) {
        if (root != null) {
            root.addChildHelper(parentName, childName);
        }
    }

    /* Prints the name of all Amoebas in this AmoebaFamily in preorder, with
       the ROOT Amoeba printed first. Each Amoeba should be indented four spaces
       more than its parent. */
    public void print() {
        if (root != null) {
            root.printHelper(0);
        }
    }

    /* Returns the length of the longest name in this AmoebaFamily. */
    public int longestNameLength() {
        if (root != null) {
            return root.longestNameLengthHelper();
        }
        return 0;
    }

    /* Returns the longest name in this AmoebaFamily. */
    public String longestName() {
        if (root != null) {
            return root.longestNameHelper();
        }
        return "";
    }

    /* Returns an Iterator for this AmoebaFamily. */
    public Iterator<Amoeba> iterator() {
        return new AmoebaBFSIterator(root);
    }

    /* Creates a new AmoebaFamily and prints it out. */
    public static void main(String[] args) {
        AmoebaFamily family = new AmoebaFamily("Amos McCoy");
        family.addChild("Amos McCoy", "mom/dad");
        family.addChild("Amos McCoy", "auntie");
        family.addChild("mom/dad", "me");
        family.addChild("mom/dad", "Fred");
        family.addChild("mom/dad", "Wilma");
        family.addChild("me", "Mike");
        family.addChild("me", "Homer");
        family.addChild("me", "Marge");
        family.addChild("Mike", "Bart");
        family.addChild("Mike", "Lisa");
        family.addChild("Marge", "Bill");
        family.addChild("Marge", "Hilary");
        System.out.println("Here's the family:");
        family.print();
        for (Amoeba a : family) {
            System.out.println(a);
        }
        Queue<Integer> shabi = new ArrayDeque<>();
        System.out.println(shabi.size());
    }

    /* An Amoeba is a node of an AmoebaFamily. */
    public static class Amoeba {

        private String name;
        private Amoeba parent;
        private ArrayList<Amoeba> children;

        public Amoeba(String name, Amoeba parent) {
            this.name = name;
            this.parent = parent;
            this.children = new ArrayList<Amoeba>();
        }

        public String toString() {
            return name;
        }

        public Amoeba getParent() {
            return parent;
        }

        public ArrayList<Amoeba> getChildren() {
            return children;
        }

        /* Adds child with name CHILDNAME to an Amoeba with name PARENTNAME. */
        public void addChildHelper(String parentName, String childName) {
            if (name.equals(parentName)) {
                Amoeba child = new Amoeba(childName, this);
                children.add(child);
            } else {
                for (Amoeba a : children) {
                    a.addChildHelper(parentName, childName);
                }
            }
        }

        public void printHelper(int n) {
            String blankSpace = "";
            for (int i = n; i > 0; i--) {
                blankSpace += "    ";
            }
            System.out.println(blankSpace + this.name);

            for (Amoeba a : children) {
                a.printHelper(n + 1);
            }
            return;
        }

        /* Returns the length of the longest name between this Amoeba and its
           children. */
        public int longestNameLengthHelper() {
            int maxLengthSeen = name.length();
            for (Amoeba a : children) {
                maxLengthSeen = Math.max(maxLengthSeen,
                                         a.longestNameLengthHelper());
            }
            return maxLengthSeen;
        }


        public String longestNameHelper() {
            String maxName = name;
            for (Amoeba a : children) {
                String tempString = a.longestNameHelper();
                if (tempString.length() > maxName.length()) {
                    maxName = tempString;
                }
            }
            return maxName;
        }

    }

    /* An Iterator class for the AmoebaFamily, running a DFS traversal on the
       AmoebaFamily. Complete enumeration of a family of N Amoebas should take
       O(N) operations. */
    public class AmoebaDFSIterator implements Iterator<Amoeba> {

        private Stack<Amoeba> fringe = new Stack<>();

        /* AmoebaDFSIterator constructor. Sets up all of the initial information
           for the AmoebaDFSIterator. */
        public AmoebaDFSIterator(Amoeba a) {
            if (a != null) {
                fringe.push(a);
            }
        }

        /* Returns true if there is a next element to return. */
        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        /* Returns the next element. */
        public Amoeba next() {
            if (!hasNext()) {
                throw new NoSuchElementException("AmoebaFamily ran out of members");
            }
            Amoeba nextAmoeba = fringe.pop();
            if (nextAmoeba.children != null) {
                for (int i = nextAmoeba.children.size() - 1; i >= 0; i--) {
                    fringe.add(nextAmoeba.children.get(i));
                }
            }
            return nextAmoeba;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* An Iterator class for the AmoebaFamily, running a BFS traversal on the
       AmoebaFamily. Complete enumeration of a family of N Amoebas should take
       O(N) operations. */
    public class AmoebaBFSIterator implements Iterator<Amoeba> {

        Queue<Amoeba> fringe = new ArrayDeque<>();

        /* AmoebaBFSIterator constructor. Sets up all of the initial information
           for the AmoebaBFSIterator. */
        public AmoebaBFSIterator(Amoeba a) {
            if (a != null) {
                fringe.add(a);
            }
        }

        /* Returns true if there is a next element to return. */
        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        /* Returns the next element. */
        public Amoeba next() {
            if (!hasNext()) {
                throw new NoSuchElementException("AmoebaFamily ran out of members");
            }
            Amoeba nextAmoeba = fringe.remove();
            if (nextAmoeba.children != null) {
                for (int i = 0; i <= nextAmoeba.children.size() - 1; i++) {
                    fringe.add(nextAmoeba.children.get(i));
                }
            }
            return nextAmoeba;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
