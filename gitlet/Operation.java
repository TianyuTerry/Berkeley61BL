package gitlet;


import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.Map;

public class Operation {

    public static void init() {
        File newfile = new File("./.gitlet");
        if (newfile.exists()) {
            System.out.println("A gitlet version-control system already exists in the current directory.");
        } else {
            newfile.mkdir();

            StagingArea.setup();
            RemoveArea.setup();

            Commit.setup();


            Commit init = new Commit("initial commit");
            init.parentID = "";
            String text = init.getHashCode();

            try {
                Commit.serialize(init);
                Pointer.setup(text);
            } catch (IOException e) {
                System.out.println("caonima");
            }

        }
    }

    public static void add(String filename) {
        try {
            StagingArea.add(filename);
        } catch (IOException e) {
            System.out.println("File does not exist.");
        }
        RemoveArea ra = RemoveArea.deserialize();
        if (ra.removedFiles.contains(filename)) {
            ra.removedFiles.remove(filename);
            RemoveArea.serialize(ra);
        }
    }

    public static void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
        } else {
            Commit commit = new Commit(message);
            gitlet.StagingArea sa = gitlet.StagingArea.deserialize();

            RemoveArea ra = RemoveArea.deserialize();


            if (!sa.map.isEmpty() || !ra.removedFiles.isEmpty()) {

                try {

                    commit.files = sa.map;

                    commit.parentID = Commit.getParentID();

                    Commit parentCommit = Commit.deserialize(commit.parentID);

                    if (!(commit.parentID).equals("")) {
                        Map<String, String> parent = parentCommit.files;
                        for (String s : parent.keySet()) {
                            if (!commit.files.containsKey(s)) {
                                commit.files.put(s, parent.get(s));
                            }
                        }
                        for (String s : ra.removedFiles) {
                            if (commit.files.containsKey(s)) {
                                commit.files.remove(s);
                            }
                        }
                    }


                    String hashCode = commit.getHashCode();
                    Pointer.updatePointer(hashCode);
                    Commit.serialize(commit);

                    gitlet.StagingArea.clear();
                    RemoveArea.clear();
                } catch (IOException e) {
                    System.out.println("Operation.commit try-block error");
                }
            } else {
                System.out.println("No changes added to the commit.");
            }
        }
    }

    public static void remove(String filename) {

        String parentID = Commit.getParentID();
        StagingArea sa = gitlet.StagingArea.deserialize();

        Map<String, String> parent = Commit.deserialize(parentID).files;
        if (parent.containsKey(filename)) {
            File newfile = new File("./" + filename);
            newfile.delete();

            if (sa.map.containsKey(filename)) {
                sa.map.remove(filename);
            }

            RemoveArea ra = RemoveArea.deserialize();
            ra.add(filename);
            RemoveArea.serialize(ra);
        } else if (sa.map.containsKey(filename)) {
            sa.map.remove(filename);
            StagingArea.serialize(sa);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    public static void checkoutTwo(String hashCode, String fileName) {
        Commit commit = Commit.deserialize(hashCode);
        if (commit != null) {
            Map<String, String> files = commit.files;
            try {
                String fileHashCode = files.get(fileName);
                try {
                    Blob.deserialize(fileName, fileHashCode);
                } catch (IOException e) {
                    System.out.println("Commit checkout method is wrong. Actually using blob.deserialize method");
                }
            } catch (Exception e) {
                System.out.println("File does not exist in that commit.");
            }
        }
    }

    public static void checkoutOne(String fileName) {
        checkoutTwo(Commit.getParentID(), fileName);
    }

    public static void print() {
        Commit.print();
    }

    public static void printHistory() {
        String parentID = Commit.getParentID();
        Commit.printHistory(parentID);
    }

    public static void find(String message) {
        Commit.find(message);
    }

    public static void checkoutThree(String givenBranch) {
        Pointer p = Pointer.deserialize();
        if (p.pointers.containsKey(givenBranch)) {
            String currentBranch = p.activePointer;
            if (currentBranch.equals(givenBranch)) {
                System.out.println("No need to checkout the current branch");
            } else {
                String newhashcode = p.pointers.get(givenBranch);
                Commit newCommit = Commit.deserialize(newhashcode);
                Map<String, String> newmap = newCommit.files;

                Commit currentCommit = Commit.deserialize(Commit.getParentID());
                Map<String, String> currentmap = currentCommit.files;

                Map<String, String> stagedFiles = gitlet.StagingArea.deserialize().map;


                ArrayList<String> unTracked =
                        Helper.untrackedFilesCurrentBranch(Commit.getParentID());
                for (String s : newmap.keySet()) {
                    if (unTracked.contains(s)) {
                        System.out.println
                                ("There is an untracked file in the way; delete it or add it first");
                        return;
                    }
                }

                try {
                    for (String s : newmap.keySet()) {
                        Blob.deserialize(s, newmap.get(s));
                    }
                } catch (IOException e) {
                    System.out.println("deserialize blob error");
                }


                Pointer.switchPointer(givenBranch);

                for (String f : currentmap.keySet()) {
                    if (!newmap.containsKey(f)) {
                        File file = new File("./" + f);
                        file.delete();
                    }
                }

                for (String t : stagedFiles.keySet()) {
                    if (!newmap.containsKey(t)) {
                        File file = new File("./" + t);
                        file.delete();
                    }
                }

                gitlet.StagingArea.clear();
            }
        } else {
            System.out.println("No such branch exists");
        }
    }

    public static void branch(String branchName) {
        Pointer.createPointer(branchName);
    }

    public static void reset(String commitHashCode) {
        try {
            Commit resetCommit = Commit.deserialize(commitHashCode);
            if (resetCommit == null) {
                return;
            }

            Map<String, String> resetMap = resetCommit.files;
            Commit currentCommit = Commit.deserialize(Commit.getParentID());
            Map<String, String> currentmap = currentCommit.files;

            Map<String, String> stagedFiles = gitlet.StagingArea.deserialize().map;

            ArrayList<String> unTracked = Helper.untrackedFilesCurrentBranch(Commit.getParentID());

            for (String s : resetMap.keySet()) {
                if (unTracked.contains(s)) {
                    System.out.println
                            ("There is an untracked file in the way; delete it or add it first");
                    return;
                }
            }

            for (String s : resetMap.keySet()) {
                Blob.deserialize(s, resetMap.get(s));
            }

            Pointer.updatePointer(commitHashCode);

            for (String f : currentmap.keySet()) {
                if (!resetMap.containsKey(f)) {
                    File file = new File("./" + f);
                    file.delete();
                }
            }


            for (String t : stagedFiles.keySet()) {
                if (!resetMap.containsKey(t)) {
                    File file = new File("./" + t);
                    file.delete();
                }
            }

            gitlet.StagingArea.clear();

        } catch (IOException ioe) {
            System.out.println("untracked files method error");
        }
    }

    public static void status() {
        gitlet.StagingArea sa = gitlet.StagingArea.deserialize();
        RemoveArea ra = RemoveArea.deserialize();
        Pointer pt = Pointer.deserialize();

        Commit parentCommit = Commit.deserialize(Commit.getParentID());
        ArrayList<Object> list = new ArrayList<>();

        for (String s : sa.map.keySet()) {
            if (!parentCommit.files.containsKey(s)) {
                list.add(s);
            }
        }

        String[] saFile = Helper.cast(list.toArray());
        String[] raFile = Helper.cast(ra.removedFiles.toArray());
        String[] ptFile = Helper.cast(pt.pointers.keySet().toArray());
        String[] utFile = Helper.cast
                (Helper.untrackedFilesCurrentBranch(Commit.getParentID()).toArray());

        Arrays.sort(saFile);
        Arrays.sort(raFile);
        Arrays.sort(ptFile);
        Arrays.sort(utFile);


        System.out.println("=== Branches ===");
        for (int i = 0; i < ptFile.length; i++) {
            if (pt.activePointer == ptFile[i]) {
                System.out.println("*" + ptFile[i]);
            } else {
                System.out.println(ptFile[i]);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (int i = 0; i < saFile.length; i++) {
            System.out.println(saFile[i]);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (int i = 0; i < raFile.length; i++) {
            System.out.println(raFile[i]);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (int i = 0; i < utFile.length; i++) {
            System.out.println(utFile[i]);
        }
    }

    public static void rmBranch(String branchName) {
        Pointer.removePointer(branchName);
    }

    /*public static void merge(String givenBranch) {
        Pointer p = Pointer.deserialize();

        if (!p.pointers.containsKey(givenBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;

        }
        if (p.activePointer.equals(givenBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            return;

        }

        Commit splitPoint = Pointer.findSplitPoint(givenBranch);
        Commit current = Commit.deserialize(Commit.getParentID());
        Commit given = Commit.deserialize(p.pointers.get(givenBranch));

        if (splitPoint.equals(given)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }

        if (splitPoint.equals(current)) {

            System.out.println("Current branch fast-forwarded.");
            return;
        }

        ArrayList<String> modifiedGivenbranch = Helper.modified(given, splitPoint);
        ArrayList<String> unmodifiedGivenbranch = Helper.unmodified(given, splitPoint);
        ArrayList<String> modifiedCurrentbranch = Helper.modified(current, splitPoint);
        ArrayList<String> unmodifiedCurrentbranch = Helper.unmodified(current, splitPoint);


    }*/
}
