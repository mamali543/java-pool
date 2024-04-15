package ex04;

public interface UsersList {
    public void addUser(User user);
    public User getUserById(int identifier);
    public User getUserByIndex(int index);
    public long getCountUsers();
}
