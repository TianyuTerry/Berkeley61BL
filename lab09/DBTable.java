import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.Objects;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class DBTable<T> {
    protected List<T> entries;

    public DBTable() {
        this.entries = new ArrayList<>();
    }

    public DBTable(Collection<T> lst) {
        entries = new ArrayList<>(lst);
    }

    public void add(T t) {
        entries.add(t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DBTable<?> other = (DBTable<?>) o;
        return Objects.equals(entries, other.entries);
    }

    /** Add all items from a collection to the table. */
    public void add(Collection<T> col) {
        col.forEach(this::add);
    }

    /** Returns a copy of the entries in this table. */
    List<T> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * getOrderedBy should create a new list ordered on the results of the
     * getter, without modifying the entries.
     */
    public <R extends Comparable<R>> List<T> getOrderedBy(Function<T, R> getter) {

        Map<R, T> map = new HashMap<>();
        List<R> nameList = new ArrayList<>();
        List<T> returnList = new ArrayList<>();

        for (T t : this.entries) {
            map.put(getter.apply(t), t);
            nameList.add(getter.apply(t));
        }

        /*Collections.sort(nameList, R :: compareTo);*/

        Collections.sort(nameList, (R a, R b) -> (a.compareTo(b)));

        for (R r : nameList) {
            returnList.add(map.get(r));
        }
        return returnList;
    }

    public static void main(String[] args) {
        List<User> users = Arrays.asList(
                new User(2, "Christine", ""),
                new User(4, "Kevin", ""),
                new User(5, "Alex", ""),
                new User(1, "Lauren", ""),
                new User(1, "Catherine", "")
                );
        DBTable<User> t = new DBTable<>(users);
        List<User> l = t.getOrderedBy(User::getName);
        l.forEach(System.out::println);
    }
}
