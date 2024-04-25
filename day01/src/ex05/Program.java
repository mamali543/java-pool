package ex05;

public class Program {
    public static void main(String[] args) {
        Menu menu = new Menu();
        if (args.length == 0){
            System.err.println("no args!");
            System.exit(-1);
        }
        else if (args[0].equals("--profile=dev")){
            menu.setDevMenu(true);
        } else if (args[0].equals("--profile=production")) {
            menu.setDevMenu(false);
        }
        menu.startMenu();
    }
}
