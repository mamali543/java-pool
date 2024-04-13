package ex01;

public class UserIdsGenerator {
    /*it belongs to the class itself, and it's shared among all the instances*/
    private static int id = 0;
    private static UserIdsGenerator instance = null;

    public static UserIdsGenerator getInstance(){
        /*to reference a field 'instance' from a static context, it has to be static*/
        if (instance == null){
            instance = new UserIdsGenerator();
        }
        return instance;
    }
    public int generateId(){
        return this.id++;
    }
}
