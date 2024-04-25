package ex05;

public class UsersArrayList implements UsersList{

    private User[] dataUsers = new User[10];
    private int countUsers = 0;

    /*it is considered best practice,
    and it signifies that the method is intended to override or implement a method declared in a superclass or interface.*/
    @Override
    public void addUser(User newUser) {
        if (countUsers >= dataUsers.length){
            User[] tmp = new User[dataUsers.length + (dataUsers.length / 2)];
            for (int i = 0; i < dataUsers.length; i++){
                tmp[i] = dataUsers[i];
            }
            dataUsers = tmp;
        }
        dataUsers[countUsers++] = newUser;
    }

    @Override
    public User getUserById(int identifier) {
        for (int i = 0; i < countUsers; i++){
            if (identifier == dataUsers[i].getIdentifier())
                return dataUsers[i];
        }
        throw  new UserNotFoundException();
    }

    @Override
    public User getUserByIndex(int index) {
        if (index > countUsers)
            throw new UserNotFoundException();
        return dataUsers[index];
    }

    @Override
    public long getCountUsers() {
//        System.out.print("Users Count equal= ");
        return countUsers;
    }
}
