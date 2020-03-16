package gitlet;


import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Helper {

    public static String getSha1(String str) {
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readText(String path) throws IOException {
        FileInputStream fin;
        String text = "";
        int i;
        fin = new FileInputStream(path);


        do {
            i = fin.read();
            if (i >= 0) {
                text += String.valueOf((char) i);
            }
        } while (i >= 0);

        return text;
    }

    public static ArrayList<String> untrackedFilesCurrentBranch(String commitHashcode) {
        ArrayList<String> list = new ArrayList<>();
        Commit commit = Commit.deserialize(commitHashcode);
        StagingArea sa = StagingArea.deserialize();
        File pwd = new File("./");
        for (String filename : pwd.list()) {
            File f = new File("./" + filename);
            if (f.isFile()) {
                if (!commit.files.containsKey(filename) && !sa.map.containsKey(filename)) {
                    list.add(filename);
                }
            }
        }
        return list;
    }

    public static String[] cast(Object[] o) {
        String[] answer = new String[o.length];
        for (int i = 0; i < o.length; i++) {
            answer[i] = (String) o[i];
        }
        return answer;
    }

    public static String modify(String s) {
        if (s.length() == 1) {
            String answer = "0" + s;
            return answer;
        }
        return s;
    }

    public static String idMatcher(String commitId) {
        String path = "./.gitlet/Commits";
        File commitFolder = new File(path);
        String[] fileArray = commitFolder.list();

        for (String s : fileArray) {
            if (match(s, commitId)) {
                return s;
            }
        }

        return null;
    }

    public static boolean match(String s1, String s2) {

        for (int i = 0; i < s2.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<String> modified(Commit givenBranch, Commit splitPoint) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : splitPoint.files.keySet()) {
            if (givenBranch.files.containsKey(s)) {
                if (!splitPoint.files.get(s).equals(givenBranch.files.get(s))) {
                    list.add(s);
                }
            }
        }
        return list;
    }

    public static ArrayList<String> unmodified(Commit givenBranch, Commit splitPoint) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : splitPoint.files.keySet()) {
            if (givenBranch.files.containsKey(s)) {
                if (splitPoint.files.get(s).equals(givenBranch.files.get(s))) {
                    list.add(s);
                }
            }
        }
        return list;
    }


}
