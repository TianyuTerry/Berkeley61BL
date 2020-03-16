import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.Objects;
import java.util.stream.Collectors;

public class DBTable<T> {
    private List<T> entries;

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
     * Returns a list of entries sorted based on the natural ordering of the
     * results of the getter. Non-destructive.
     */
    public <R extends Comparable<R>> List<T> getOrderedBy(Function<T, R> getter) {
        return this.entries.stream()
                .map(u -> (getter.apply(u)))
                .sorted()
                .map(u -> {
                    for (T t : this.entries) {
                        if (getter.apply(t) == u) {
                            return t;
                        }
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of entries whose value returned from the getter is found
     * in the whitelist. Non-destructive.
     */
    public <R> List<T> getWhitelisted(Function<T, R> getter, Collection<R> whitelist) {
        return this.entries.stream()
                .map(u -> (getter.apply(u)))
                .filter(r -> whitelist.contains(r))
                .map(u -> {
                    for (T t : this.entries) {
                        if (getter.apply(t) == u) {
                            return t;
                        }
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns a new DBTable that contains the elements as obtained by the
     * getter. For example, getting a DBTable of usernames would look like:
     * DBTable<String> names = table.getSubtableOf(User::getUsername);
     */
    public <R> DBTable<R> getSubtableOf(Function<T, R> getter) {
        return new DBTable<>(
                this.entries.stream()
                .map(u -> (getter.apply(u)))
                .collect(Collectors.toList())
        );
    }

    public String toString() {
        return entries.toString();
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
        List<String> userName = new ArrayList<>();
        userName.add("Alex");
        userName.add("Lauren");
        List<User> l2 = t.getWhitelisted(User::getName, userName);
        l2.forEach(System.out::println);
        DBTable<String> t2 = t.getSubtableOf(User::getName);
        System.out.println(t2);
    }
}
