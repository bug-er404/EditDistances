import java.io.*;
import java.util.Vector;

// Code to group solutions into complete/incomplete.
public class test {
    // private static final String SOURCEFILE = "/Users/salilmaharjan/Desktop/kattis/kattis/csvResult/templated/2005.txt";
    private static final String SOURCEFILE = "/Users/salilmaharjan/Desktop/kumar/work/new_selection_groupdata/2001";

    // Record of complete solutions
    private static Vector<String> student_complete= new Vector<>();
    // Record of incomplete solutions
    private static Vector<String> student_incomplete = new Vector<>();

    public static void main(String[] args) {
        // Reader to read log file
        BufferedReader reader = null;
        // Buffer to read the statement from the log file
        String buffer = null;

        // Open log file to read and check if it opened correctly.
        File file = new File(SOURCEFILE);
        try {
            // Initializing buffer
            reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();

//            // Get past initial result section
//            //while(buffer.contains("# of Students:") == false)
//            while(buffer.contains("# of Trails:") == false)
//                buffer = reader.readLine();
//            buffer = reader.readLine();

//            // Reading till the end of the file.
//            while(buffer!=null) {
//                // Last line
//                if(buffer.contains("Process finished with exit code 0"))
//                    break;
//
//                // Empty states are not considered.
//                if(buffer.contains("Empty state encountered.")){
//                    buffer = reader.readLine();
//                    continue;
//                }
//
//                // Checking for complete solutions:
////                if(buffer.lastIndexOf("0") == (buffer.lastIndexOf("]")-3))
//                {
//                    student_complete.add(buffer);
//                    buffer = reader.readLine();
//                    student_complete.add(buffer);
//
//                    // Moving to next line:
//                    buffer= reader.readLine();
//                }
//                // Incomplete
//                else{
//                    student_incomplete.add(buffer);
//                    buffer = reader.readLine();
//                    student_incomplete.add(buffer);
//
//                    // Moving to next line:
//                    buffer= reader.readLine();
//                }
//            }

            // Reading till the end of the file.
            while(buffer!=null) {
                // Last line
                if(buffer.contains("Process finished with exit code 0"))
                    break;

                // Empty states are not considered.
                if(buffer.contains("Empty state encountered.")){
                    buffer = reader.readLine();
                    continue;
                }

                // Checking for complete solutions:
                if(buffer.length()>=2 && buffer.charAt(buffer.length()-2) == '0'
                        && buffer.charAt(buffer.length()-3) == ',')
                {
                    student_complete.add(buffer);

                    // Moving to next line:
                    buffer= reader.readLine();
                }
                // Incomplete
                else{
                    student_incomplete.add(buffer);
                    // Moving to next line:
                    buffer= reader.readLine();
                }
            }


        }catch(Exception e) {
            System.err.println("The following error was caught: " + e.getMessage());
            System.out.println("Could not open the file: " + SOURCEFILE);
            System.exit(1);
        }

        System.out.println("# of complete solutions: " + (student_complete.size()));
        System.out.println("# of incomplete solutions: " + (student_incomplete.size()));

        System.out.println("-------COMPLETE SOLUTIONS-------");
        for(int i=0; i<student_complete.size();i++){
            System.out.println(student_complete.get(i));}
        System.out.println("-------INCOMPLETE SOLUTIONS-------");
        for(int i=0; i<student_incomplete.size();i++){
            System.out.println(student_incomplete.get(i));}


    }

}

// TEST FOR CATEGORIZING ACTIONS

//    // STUDENT ASSEMBLED SOLUTION.
//    // PATTERN OF STUDENT ACTIONS
//    // Move actions pattern
//    String MOVE_PROBLEM_SOLUTION = "^Moved from problem to solution at line (.*)\\: (.*)$";
//    String MOVE_SOLUTION_PROBLEM = "^Moved from solution to problem at line (.*)\\: (.*)$";
//    String MOVE_SOLUTION_TRASH = "^Moved from solution to trash at line (.*)\\: (.*)$";
//    String MOVE_TRASH_SOLUTION = "^Moved from trash to solution at line (.*)\\: (.*)$";
//    String MOVE_PROBLEM_TRASH = "^Moved from problem to trash at line (.*)\\: (.*)$";
//    String MOVE_TRASH_PROBLEM = "^Moved from trash to problem at line (.*)\\: (.*)$";
//
//    // Reorder action pattern
//    String REORDER_ACTION = "^Reordered from line (.*) to (.*)\\: (.*)$";
//
//    // Buffer that holds line number of action taken.
//    Integer line_number1;
//    Integer line_number2;
//    // Buffer that holds code of the action taken.
//    String code;
//    // Pattern matchers used for checking actions.
//    Pattern pattern1, pattern2;
//    Matcher matcher1, matcher2;
//
//    // Test strings
//    String m1 = "Moved from problem to solution at line 9: unsigned secondNum;";
//    String m2 = "Moved from solution to problem at line 16: unsigned firstNum;";
//    String m3 = "Moved from solution to trash at line 0: unsigned firstNum";
//    String m4 = "Moved from problem to trash at line 1: cin << firstNum;";
//    String m5 = "Reordered from line 18 to 19: cout << firstNum;";
//    String m6 = "Moved from trash to solution at line 14: firstNum = stdin.nextShort();";
//    String m7 = "Moved from trash to problem at line 5: System.out.print( secondNum);";
//
//// CHECKING FOR MOVE ACTION FROM
//// PROBLEM TO SOLUTION OR
//// TRASH TO SOLUTION
//// The pattern of the line where action log can be found.
//        pattern1 = Pattern.compile(MOVE_PROBLEM_SOLUTION);
//                matcher1 = pattern1.matcher(m1);
//                pattern2 = Pattern.compile(MOVE_TRASH_SOLUTION);
//                matcher2 = pattern2.matcher(m6);
//                // Extract the code from the event if true and
//                // Add the code line in action to the next state.
//                if (matcher1.matches()) {
//                // Get the code and add it to resultVector
//                line_number1 = Integer.parseInt(matcher1.group(1).trim());
//                System.out.println("Line number1: " + line_number1);
//                code = matcher1.group(2).trim();
//                System.out.println("Code: " + code);
//
//                // Add code to vector at (line_number1-1)-> if positive
//                }
//                if (matcher2.matches()) {
//                // Get the code and add it to resultVector
//                line_number1 = Integer.parseInt(matcher2.group(1).trim());
//                System.out.println("Line number2: " + line_number1);
//                code = matcher2.group(2).trim();
//                System.out.println("Code: " + code);
//                }
//
//                // CHECKING FOR MOVE ACTION FROM
//                // SOLUTION TO PROBLEM OR
//                // SOLUTION TO TRASH
//                // The pattern of the line where action log can be found.
//                pattern1 = Pattern.compile(MOVE_SOLUTION_PROBLEM);
//                matcher1 = pattern1.matcher(m2);
//                pattern2 = Pattern.compile(MOVE_SOLUTION_TRASH);
//                matcher2 = pattern2.matcher(m3);
//                // Extract the code from the event if true and
//                // Remove the code line in action from the next state.
//                if (matcher1.matches()) {
//                // Get the code and add it to resultVector
//                line_number1 = Integer.parseInt(matcher1.group(1).trim());
//                System.out.println("Line number1: " + line_number1);
//                code = matcher1.group(2).trim();
//                System.out.println("Code: " + code);
//                }
//                if (matcher2.matches()) {
//                // Get the code and add it to resultVector
//                line_number1 = Integer.parseInt(matcher2.group(1).trim());
//                System.out.println("Line number2: " + line_number1);
//                code = matcher2.group(2).trim();
//                System.out.println("Code: " + code);
//                }
//
//                // CHECKING FOR MOVE ACTION FROM
//                // PROBLEM TO TRASH OR
//                // TRASH TO PROBLEM OR
//                // ATTEMPTED MOVES (WHEN countIgnoredSteps is TRUE and we count all valid/invalid moves.
//                // Otherwise it is removed in the starting if-statement check. )
//                // The pattern of the line where action log can be found.
//                pattern1 = Pattern.compile(MOVE_PROBLEM_TRASH);
//                matcher1 = pattern1.matcher(m4);
//                pattern2 = Pattern.compile(MOVE_TRASH_PROBLEM);
//                matcher2 = pattern2.matcher(m7);
//                // If true copy the current state as the next state.
//                // Can consolidate into one to call copy constructor.
//                if (matcher1.matches()) {
//                // Get the code and add it to resultVector
//                line_number1 = Integer.parseInt(matcher1.group(1).trim());
//                System.out.println("Line number1: " + line_number1);
//                code = matcher1.group(2).trim();
//                System.out.println("Code: " + code);
//                }
//                if (matcher2.matches()) {
//                // Get the code and add it to resultVector
//                line_number1 = Integer.parseInt(matcher2.group(1).trim());
//                System.out.println("Line number2: " + line_number1);
//                code = matcher2.group(2).trim();
//                System.out.println("Code: " + code);
//                }
//
//                // CHECKING FOR REORDER ACTION
//                // The pattern of the line where action log can be found.
//                pattern1 = Pattern.compile(REORDER_ACTION);
//                matcher1 = pattern1.matcher(m5);
//                // Extract the code from the event if true and
//                // Remove the code line in action from the next state.
//                if( matcher1.matches() )
//                {
//                // Get the code and add it to resultVector
//                line_number1 = Integer.parseInt(matcher1.group(1).trim());
//                System.out.println("Line number: " + line_number1);
//                line_number2 = Integer.parseInt(matcher1.group(2).trim());
//                System.out.println("Line number: " + line_number2);
//                code = matcher1.group(3).trim();
//                System.out.println("Code: " + code);
//                }
//                }
