package gitlet;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        //Operation.add("test1.txt");
        Operation.init();
        Operation.add("test1.txt");
        Operation.add("test2.txt");
        Operation.commit("01");
        Operation.add("test3.txt");
        Operation.commit("02");
        Operation.remove("test1.txt");
        Operation.checkoutOne("test1.txt");
        Operation.checkoutOne("test2.txt");
        Operation.find("01");
        Operation.branch("cool-beans");
        Operation.checkoutThree("cool-beans");
        Operation.add("test4.txt");
        Operation.commit("03");
        Operation.printHistory();
        Operation.checkoutThree("master");
        Operation.status();
    }
}
