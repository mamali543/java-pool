import java.util.ArrayDeque;
import java.util.Deque;

public class Program {

    public static void main(String[] args) {
        String[] testCases = {
                "<div><i></i><p>",  // Should return "div"
                "<div><b><p>hello world</p></b></div>",  // Should return "true"
                "<div><i>hello</i>world</b>",  // Should return "div"
                "<div><div><b></b></div></p>"  // Should return "div"
        };

        for (String testCase : testCases) {
            System.out.println(HTMLElements(testCase));
        }
    }

    public static String HTMLElements(String str) {
        Deque<String> stack = new ArrayDeque<>();
        String firstMismatchedOpeningTag = null;

        String[] tags = str.split("(?=<)|(?<=>)"); // Split keeping delimiters (tags)
        for (String tag : tags) {
            if (tag.matches("<[a-z]+>")) { // Opening tag
                stack.push(tag);
            } else if (tag.matches("</[a-z]+>")) { // Closing tag
                if (stack.isEmpty() || !stack.peek().equals(tag.replace("/", ""))) {
                    if (firstMismatchedOpeningTag == null && !stack.isEmpty()) {
                        firstMismatchedOpeningTag = stack.peek();
                    }
                    stack.pop(); // Assume mismatch and remove the top element to check further
                } else {
                    stack.pop();
                }
            }
        }

        if (!stack.isEmpty()) {
            if (stack.size() == 1 && firstMismatchedOpeningTag != null) {
                return extractTagName(firstMismatchedOpeningTag);
            }
            return "false";
        }
        return "true"; // Correctly nested
    }

    private static String extractTagName(String tag) {
        return tag == null ? "false" : tag.replaceAll("<|>", "");
    }
}
