package gitlet;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class Pointer implements Serializable {

    Map<String, String> pointers;
    String activePointer = "";
    static String location = "./.gitlet/pointers.ser";

    public static void setup(String hashCode) {
        Pointer p = new Pointer();
        p.pointers.put("master", hashCode);
        p.activePointer = "master";
        File newfile = new File(location);
        if (!newfile.exists()) {
            try {
                newfile.createNewFile();
            } catch (IOException e) {
                System.out.println("Pointer Setup is wrong");
            }
        }
        serialize(p);
    }

    public Pointer() {
        this.pointers = new HashMap<>();
    }

    public static void serialize(Pointer p) {
        ObjectOutputStream ops;

        try {
            ops = new ObjectOutputStream(new FileOutputStream(new File(location)));
            ops.writeObject(p);
            ops.close();
        } catch (IOException ioe) {
            System.out.println("Pointer serialize");
        }
    }

    public static Pointer deserialize() {
        ObjectInputStream ips;

        try {
            ips = new ObjectInputStream(new FileInputStream(new File(location)));
            Pointer p = (Pointer) ips.readObject();
            ips.close();
            return p;
        } catch (IOException ioe) {
            System.out.println("Pointer deserialize");
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Pointer deserialize");
            return null;
        }
    }

    public static void createPointer(String branchName) {
        Pointer p = deserialize();
        if (p.pointers.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
        } else {
            p.pointers.put(branchName, Commit.getParentID());
            serialize(p);
        }
    }

    public static void updatePointer(String hashCode) {
        Pointer p = deserialize();
        p.pointers.remove(p.activePointer);
        p.pointers.put(p.activePointer, hashCode);
        serialize(p);
    }

    public static void switchPointer(String pointerName) {
        Pointer p = deserialize();
        p.activePointer = pointerName;
        serialize(p);
    }


    public static void removePointer(String pointerName) {
        Pointer p = deserialize();
        if (!p.pointers.containsKey(pointerName)) {
            System.out.println("A branch with that name does not exist.");
        } else {
            if (p.activePointer.equals(pointerName)) {
                System.out.println("Cannot remove the current branch.");
            } else {
                p.pointers.remove(pointerName);
            }
        }
        serialize(p);
    }

    public static Commit findSplitPoint(String givenBranch) {
        Pointer p = deserialize();
        ArrayList<String> currentList = new ArrayList<>();
        ArrayList<String> givenList = new ArrayList<>();
        String previousID;

        if (!p.pointers.containsKey(givenBranch)) {
            System.out.println("A branch with that name does not exist.");

        }
        if (p.activePointer.equals(givenBranch)) {
            System.out.println("Cannot merge a branch with itself.");

        }

        previousID = p.pointers.get(p.activePointer);
        while (!previousID.equals("")) {
            currentList.add(previousID);
            previousID = (Commit.deserialize(previousID)).parentID;
        }

        previousID = p.pointers.get(givenBranch);
        while (!previousID.equals("")) {
            if (currentList.contains(previousID)) {
                return Commit.deserialize(previousID);
            } else {
                previousID = (Commit.deserialize(previousID)).parentID;
            }
        }

        return null;
    }


}
