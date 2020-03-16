package gitlet;

import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

public class Commit implements Serializable {

    static final String location = "./.gitlet/Commits/";

    String parentID;
    Calendar dateOfCommit;
    String message;
    Map<String, String> files;

    public Commit(String message) {
        this.message = message;
        this.dateOfCommit = Calendar.getInstance();
        this.files = new HashMap<>();
    }

    public static void setup() {
        File pointer = new File(location);
        pointer.mkdir();
    }

    public String getHashCode() {
        return Helper.getSha1(this.hashHelper());
    }

    public static void serialize(Commit cmt) throws IOException {
        ObjectOutputStream oout;

        oout = new ObjectOutputStream(new FileOutputStream(new File(location + cmt.getHashCode())));
        oout.writeObject(cmt);
    }

    public static Commit deserialize(String hashcode) {
        String hashCode = Helper.idMatcher(hashcode);
        File inputFile = new File(location + hashCode);
        try {
            ObjectInputStream inp = new ObjectInputStream(new FileInputStream(inputFile));
            Commit target = (Commit) inp.readObject();
            return target;
        } catch (IOException excp) {
            System.out.println("No commit with that id exists.");
        } catch (ClassNotFoundException excp) {
            System.out.println("No commit with that id exists");
        }
        return null;
    }

    public static String getParentID() {
        Pointer p = Pointer.deserialize();
        return p.pointers.get(p.activePointer);
    }

    public String toString() {
        String date = Integer.toString(dateOfCommit.get(Calendar.YEAR)) + "-"
                + Helper.modify(Integer.toString(dateOfCommit.get(Calendar.MONTH))) + "-"
                + Helper.modify(Integer.toString(dateOfCommit.get(Calendar.DAY_OF_MONTH))) + " "
                + Helper.modify(Integer.toString(dateOfCommit.get(Calendar.HOUR_OF_DAY))) + ":"
                + Helper.modify(Integer.toString(dateOfCommit.get(Calendar.MINUTE))) + ":"
                + Helper.modify(Integer.toString(dateOfCommit.get(Calendar.SECOND)));

        return "===\nCommit " + getHashCode() + "\n" + date + "\n" + message + "\n";
    }

    public String hashHelper() {
        String date = Integer.toString(dateOfCommit.get(Calendar.YEAR)) + "-"
                + Integer.toString(dateOfCommit.get(Calendar.MONTH)) + "-"
                + Integer.toString(dateOfCommit.get(Calendar.DAY_OF_MONTH)) + " "
                + Integer.toString(dateOfCommit.get(Calendar.HOUR_OF_DAY)) + ":"
                + Integer.toString(dateOfCommit.get(Calendar.MINUTE)) + ":"
                + Integer.toString(dateOfCommit.get(Calendar.SECOND));

        return date + message;
    }

    public static void print() {
        File newfile = new File(location);
        String[] fileList = newfile.list();
        for (String s : fileList) {
            System.out.println(deserialize(s));
        }
    }

    public static void find(String message) {
        Boolean fuck = false;
        File newfile = new File(location);
        String[] fileList = newfile.list();
        for (String s : fileList) {
            Commit cm = deserialize(s);
            if (cm.message.equals(message)) {
                fuck = true;
                System.out.println(s);
            }
        }
        if (!fuck) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void printHistory(String hashCode) {
        Commit current = deserialize(hashCode);

        while (!(current.parentID).equals("")) {
            System.out.println(current);
            current = deserialize(current.parentID);
        }

        System.out.println(current);
    }


    public static void main(String[] args) {

    }
}
