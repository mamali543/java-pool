package ex00;

public class Program {
    public static void main(String[] args){
        if (args.length != 1){
            System.err.println("Error: wrong args length");
            System.exit(-1);
        }
        String[] str = args[0].split("[=]");
        if (!str[0].equals("--count")){
            System.err.println("Error: wrong flag");
            System.exit(-1);
        }
        try {
            int count = Integer.parseInt(str[1]);
            Egg egg = new Egg(count);
            Hen hen = new Hen(count);

            egg.start();
            hen.start();
            hen.join();
            egg.join();

            for (int i = 0; i < 50; i++) {
                System.out.println("Human");
            }
        }
        catch(NumberFormatException e){
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
