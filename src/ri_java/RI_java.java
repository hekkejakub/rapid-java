package ri_java;

import sun.security.util.Length;

/**
 *
 * @author james
 */
public class RI_java {

    boolean respond;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String input = "";
        for (String s : args) {
            input +=" "+ s;
        }
        try {
            PathChecker path = new PathChecker(input);
            path.checkpath();
            System.out.println("Success");

        } catch (Exception e) {
            System.out.println("expected minimum 4 parameters; path owner group permissions (optional) flag");
        }

    }

}
