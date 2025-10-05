public class HelloGoodbye {
    public static void main(String[] args) {
        // initialise variables
        String helloStr = "Hello " + args[0] + " and " + args[1] + ".";
        String goodbyeStr = "Goodbye " + args[1] + " and " + args[0] + ".";

        // print output
        System.out.println(helloStr);
        System.out.println(goodbyeStr);
    }
}
