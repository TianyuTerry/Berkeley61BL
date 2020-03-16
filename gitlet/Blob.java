package gitlet;

import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class Blob implements Serializable {
    String path;
    String content;
    String hashCode;


    public Blob(String path) {
        this.path = path;
    }


    public void setContent() throws IOException {
        this.content = Helper.readText(this.path);
    }


    public void setHashCode() {
        hashCode = Helper.getSha1(this.content);
    }

    public String getHashCode() {
        return hashCode;
    }


    public void serialize() throws IOException {
        String filename = "";
        String dirpath;
        File dir;
        ObjectOutputStream oout;

        dirpath = "./.gitlet/" + hashCode.charAt(0) + hashCode.charAt(1);
        dir = new File(dirpath);
        if (!dir.exists() && (new File("./.gitlet")).exists()) {
            dir.mkdirs();
        }
        for (int i = 2; i < hashCode.length(); i++) {
            filename += hashCode.charAt(i);
        }
        filename += ".ser";

        oout = new ObjectOutputStream(new FileOutputStream(new File(dirpath + "/" + filename)));
        oout.writeObject(this.content);
        oout.close();
    }

    public static void deserialize(String filename, String hash) throws IOException {
        String hashPath;
        File file;
        File source;
        FileWriter fout;
        ObjectInputStream oin;

        hashPath = "./.gitlet/" + hash.charAt(0) + hash.charAt(1) + "/";
        for (int i = 2; i < hash.length(); i++) {
            hashPath += hash.charAt(i);
        }
        hashPath += ".ser";


        file = new File(filename);


        source = new File(hashPath);


        if (!file.exists()) {
            file.createNewFile();
        }

        try {
            fout = new FileWriter(file);
            oin = new ObjectInputStream(new FileInputStream(source));
            fout.write((String) oin.readObject());
            fout.flush();
            fout.close();
            oin.close();
        } catch (IOException ioe) {
            System.out.println("File open exception.");
            throw ioe;
        } catch (ClassNotFoundException ce) {
            System.out.println("No such class.");
        }

        return;
    }
}


















