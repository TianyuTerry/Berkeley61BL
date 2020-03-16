package gitlet;

import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.util.HashMap;


public class StagingArea implements Serializable {

    HashMap<String, String> map;

    static String location = "./.gitlet/stagingArea.ser";

    public static void setup() {
        StagingArea sa = new StagingArea();
        File f = new File(location);
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("Staging area setup error");
        }
        serialize(sa);
    }


    public static void serialize(StagingArea sa) {
        ObjectOutputStream oout;

        try {
            oout = new ObjectOutputStream(new FileOutputStream(new File(location)));
            oout.writeObject(sa);
            oout.close();
        } catch (IOException ioe) {
            System.out.println("Staging area serialize error");
        }
    }

    public static StagingArea deserialize() {
        ObjectInputStream oin;

        try {
            oin = new ObjectInputStream(new FileInputStream(new File(location)));
            StagingArea sa = (StagingArea) oin.readObject();
            oin.close();
            return sa;
        } catch (IOException ioe) {
            System.out.println("No gitlet has been created yet.");
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("No gitlet has been created yet.");
            return null;
        }
    }


    public StagingArea() {
        map = new HashMap<>();
    }

    public static void add(String... paths) throws IOException {
        int i;
        String hashcode;


        StagingArea sa = StagingArea.deserialize();
        for (i = 0; i < paths.length; i++) {
            Blob b = new Blob(paths[i]);

            b.setContent();
            b.setHashCode();

            hashcode = b.getHashCode();

            try {
                b.serialize();
            } catch (IOException e) {
                System.out.println("Staging area add method error");
            }

            if (sa.map.containsKey(paths[i])) {
                if (!hashcode.equals(sa.map.get(paths[i]))) {
                    sa.map.remove(paths[i]);
                    sa.map.put(paths[i], hashcode);
                }
            } else {
                sa.map.put(paths[i], hashcode);
            }

        }
        StagingArea.serialize(sa);
    }


    public static void clear() {
        StagingArea sa = deserialize();
        sa.map = new HashMap<>();
        serialize(sa);
    }

}





























