package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class RemoveArea implements Serializable {

    ArrayList<String> removedFiles;
    static String location = "./.gitlet/removeArea.ser";

    public static void setup() {
        RemoveArea ra = new RemoveArea();
        File newfile = new File(location);
        if (!newfile.exists()) {
            try {
                newfile.createNewFile();
            } catch (IOException e) {
                System.out.println("RemoveArea Setup is wrong");
            }
        }
        serialize(ra);
    }

    public RemoveArea() {
        this.removedFiles = new ArrayList<>();
    }

    public static void serialize(RemoveArea ra) {
        ObjectOutputStream ops;

        try {
            ops = new ObjectOutputStream(new FileOutputStream(new File(location)));
            ops.writeObject(ra);
            ops.close();
        } catch (IOException ioe) {
            System.out.println("removeArea serialize");
        }
    }

    public static RemoveArea deserialize() {
        ObjectInputStream ips;

        try {
            ips = new ObjectInputStream(new FileInputStream(new File(location)));
            RemoveArea ra = (RemoveArea) ips.readObject();
            ips.close();
            return ra;
        } catch (IOException ioe) {
            System.out.println("removeArea deserialize");
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("removeArea deserialize");
            return null;
        }
    }

    public void add(String filename) {
        this.removedFiles.add(filename);
    }


    public static void clear() {
        RemoveArea ra = deserialize();
        ra.removedFiles = new ArrayList<>();
        serialize(ra);
    }

}

