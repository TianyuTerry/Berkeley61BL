package gitlet;

import java.io.File;
/* Driver class for Gitlet, the tiny stupid version-control system.
   @author
*/



public class Main {
    /* Usage: java Main ARGS, where ARGS contains
           <COMMAND> <OpeRAND> .... */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a command.");
        }
        if (args[0].equals("init")) {
            if (args.length == 1) {
                Operation.init();
            } else {
                System.out.println("Invalid syntax for gitlet init.");
            }
        } else {
            if (!new File("./.gitlet").exists()) {
                System.out.println("Not in an initialized gitlet directory.");
            } else {
                if (args[0].equals("add")) {
                    if (args.length == 1) {
                        System.out.println
                                ("You did not put the file name that you want to add on.");
                    } else {
                        for (int i = 1; i < args.length; i++) {
                            Operation.add(args[i]);
                        }
                    }
                } else if (args[0].equals("commit")) {
                    if (args.length == 1) {
                        Operation.commit("");
                    } else if (args.length > 2) {
                        System.out.println("The only arguments for gitlet commit is the message.");
                    } else {
                        Operation.commit(args[1]);
                    }
                } else if (args[0].equals("rm")) {
                    if (args.length == 1) {
                        System.out.println
                                ("You have not type in the file name that you want to remove.");
                    } else {
                        for (int i = 1; i < args.length; i++) {
                            Operation.remove(args[i]);
                        }
                    }
                } else if (args[0].equals("log")) {
                    if (args.length > 1) {
                        System.out.println("Invalid syntax for gitlet log.");
                    } else {
                        Operation.printHistory();
                    }
                } else if (args[0].equals("global-log")) {
                    if (args.length > 1) {
                        System.out.println("Invalid syntax for gitlet global-log.");
                    } else {
                        Operation.print();
                    }
                } else if (args[0].equals("find")) {
                    if (args.length == 1) {
                        System.out.println
                                ("You miss the message that is used to find the commit Id.");
                    } else if (args.length > 2) {
                        System.out.println("Invalid syntax for gitlet find.");
                    } else {
                        Operation.find(args[1]);
                    }
                } else if (args[0].equals("status")) {
                    if (args.length > 1) {
                        System.out.println("Invalid syntax for gitlet status.");
                    } else {
                        Operation.status();
                    }
                } else if (args[0].equals("checkout")) {
                    if (args[1].equals("--")) {
                        if (args.length == 2) {
                            System.out.println
                                    ("Invalid Syntax to checkout files using checkoutOne method.");
                        } else {
                            for (int i = 2; i < args.length; i++) {
                                Operation.checkoutOne(args[i]);
                            }
                        }
                    } else if (args.length > 3 && args[2].equals("--")) {
                        String commitID = args[1];
                        for (int i = 3; i < args.length; i++) {
                            Operation.checkoutTwo(commitID, args[i]);
                        }
                    } else if (args.length == 2) {
                        Operation.checkoutThree(args[1]);
                    } else {
                        System.out.println("Incorrect operands.");
                    }
                } else if (args[0].equals("branch")) {
                    if (args.length == 1) {
                        System.out.println("Invalid syntax for gitlet branch.");
                    } else {
                        for (int i = 1; i < args.length; i++) {
                            Operation.branch(args[i]);
                        }
                    }
                } else if (args[0].equals("rm-branch")) {
                    if (args.length == 1) {
                        System.out.println("Invalid syntax for gitlet rm-branch.");
                    } else {
                        for (int i = 1; i < args.length; i++) {
                            Operation.rmBranch(args[i]);
                        }
                    }
                } else if (args[0].equals("reset")) {
                    if (args.length == 1) {
                        System.out.println("Invalid");
                    } else if (args.length > 2) {
                        System.out.println("You can not type in something after commit id.");
                    } else {
                        Operation.reset(args[1]);
                    }
                } else {
                    System.out.println("No command with that name exists.");
                }
            }
        }
    }
}













































