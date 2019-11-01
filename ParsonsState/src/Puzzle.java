import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

    // Template of where template number can be found.
    private static final String TEMPLATE_NUMBER_PATTERN = "^Using Template # (.*)$";
    // Template of where number of steps and lines of code can be found.
    String STEPS_LINES_PATTERN = "^You took (.*) steps to solve a Parsons puzzle containing (.*) lines of code.$";
    // Template of where total time spent on the problem can be found.
    String TIME_SPENT_PATTERN = "^Time spent on this problem: (.*) seconds$";
    // Template of where total time spent for incomplete solutions can be found.
    String INC_TIME_SPENT_PATTERN = "^Total time taken for this session: (.*)$";

    // To get the language of the problem:
    // The line that signifies the beginning of the program in Java
    private static final String JAVA_PROGRAM_START = "// The Java program";
    // The line that signifies the beginning of the program in C++
    private static final String C_PROGRAM_START = "// The C program";
    // The line that signifies the beginning of the program in C++
    private static final String CPP_PROGRAM_START = "// The C++ program";
    // The line that signifies the beginning of the program in C#
    private static final String CS_PROGRAM_START = "// The C# program";


    // Variable to store template number
    private String templateNumber;
    // Variable to store language
    private String probLanguage;
    // Variable to store the ParsonsStateDiagram
    private ParsonsStateDiagram stateDiagram;
    // Variable to store # of steps
    private Integer stepsUsed;
    // Variable to store # lines of code
    private Integer totalCodeLines;
    // Variable to store total time spent on the problem
    private String totalTimeSpent;
    // Variable to store student name.
    private String studentName;
    // Flag to check if the solution is incomplete.
    private boolean incompleteFlag = false;

    // *********************************************************
    // ************** Default Constructor **********************
    // *********************************************************
    Puzzle() {

    }

    // *********************************************************
    // *********** Parameterized Constructor *******************
    // *********************************************************
    Puzzle(Vector<String> problemLog, String studentName)
    {
        // Flag for finding stats
        boolean found = false;

        // Storing student name
        this.studentName = studentName;

        // Pattern matchers used for checking the template number.
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(TEMPLATE_NUMBER_PATTERN);

        // Getting template number if it matches and storing it.
        // Found in the first two lines.
        for(int i=1; i<3; i++)
        {
            matcher = pattern.matcher(problemLog.get(i));
            if (matcher.matches()) {
                templateNumber = matcher.group(1).trim();
                found = true;
            }
            // No solution case
            if(problemLog.get(i).contains("Detailed Statistics:"))
            {
                return;
            }
        }
        // If not found.
        if(!found) {
            System.out.println("Cannot find template number. Check Puzzle constructor.");
            System.exit(1);
        }

        // Getting the language used in the puzzle and storing it.
        if ((problemLog.contains(JAVA_PROGRAM_START)))
            probLanguage = "JAVA";
        else if (problemLog.contains(CPP_PROGRAM_START))
            probLanguage = "C++";
        else if (problemLog.contains(C_PROGRAM_START))
            probLanguage = "C";
        else if (problemLog.contains(CS_PROGRAM_START))
            probLanguage = "C#";
        else
        {
            System.out.println("Cannot find language used. Check Puzzle constructor.");
            // System.exit(1);
        }

        // Getting number of steps used and total code lines for the problem and storing it.
        pattern = Pattern.compile(STEPS_LINES_PATTERN);
        matcher = pattern.matcher(problemLog.get((problemLog.size()-3)));
        if (matcher.matches()) {
            stepsUsed = Integer.parseInt(matcher.group(1).trim());
            totalCodeLines = Integer.parseInt(matcher.group(2).trim());
        }
        else
        {
            // In this case the student bailed and did not complete the puzzle.
            // Initialized to 0.
            stepsUsed = 0;
            totalCodeLines = 0;
        }

        // Getting the time spent on the problem and recording it.
        pattern = Pattern.compile(TIME_SPENT_PATTERN);
        matcher = pattern.matcher(problemLog.get((problemLog.size()-1)));
        if (matcher.matches()) {
            totalTimeSpent = matcher.group(1).trim();
            // To get integer value:
            // totalTimeSpent = Integer.parseInt(matcher.group(1).trim());
        }
        else
        {
            // Checking if the solution is incomplete and getting the total time.
            pattern = Pattern.compile(INC_TIME_SPENT_PATTERN);
            matcher = pattern.matcher(problemLog.get((problemLog.size()-1)));
            if (matcher.matches()) {
                totalTimeSpent = matcher.group(1).trim();
                // Flagging incomplete solution.
                incompleteFlag = true;
            }
            else
            {
                System.out.println("Cannot find total time spent on the problem. Check Puzzle constructor.");
                System.exit(1);
            }
        }

        // Creating and recording the Parsons State diagram for the problem.
        try{
        stateDiagram = new ParsonsStateDiagram(problemLog);}
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException();
        }
    }

    public void printPuzzleEditDistance(String template, String language){
        // iterator
        int i = 0;

        // Checking for empty states.
        try{
            Vector<Integer> temp_editVec = this.stateDiagram.getEditDistanceVector();
        }catch (Exception e){
            // System.out.println("Empty state encountered.");
            return;
        }

        if(template.equals("*") && language.equals("*")){
            Vector<Integer> temp_editVec = this.stateDiagram.getEditDistanceVector();
            System.out.println(this.templateNumber);
            System.out.print(this.studentName +",");
//            System.out.print("[ ");
            for(i = 0; i<temp_editVec.size(); i++)
                System.out.print(temp_editVec.get(i)+",");
            System.out.println("");

        }
        else if(template.equals("*") && language.equals(this.probLanguage))
        {
            Vector<Integer> temp_editVec = this.stateDiagram.getEditDistanceVector();

            System.out.print("[ ");
            for(i = 0; i<temp_editVec.size(); i++)
                System.out.print(temp_editVec.get(i)+" ");
            System.out.println(" ]");

            System.out.println("Student Name: " + this.studentName);
        }
        else if (template.equals(this.templateNumber) && language.equals("*"))
        {
            Vector<Integer> temp_editVec = this.stateDiagram.getEditDistanceVector();

//            System.out.print("[ ");
            System.out.print(this.studentName +",");
            for(i = 0; i<temp_editVec.size(); i++)
                System.out.print(temp_editVec.get(i)+",");
            System.out.println("");
//            System.out.println(" ]");

//            System.out.println("Student Name: " + this.studentName);
        }
        else if (template.equals(this.templateNumber) && language.equals(this.probLanguage))
        {
            Vector<Integer> temp_editVec = this.stateDiagram.getEditDistanceVector();

            System.out.print("[ ");
            for(i = 0; i<temp_editVec.size(); i++)
                System.out.print(temp_editVec.get(i)+" ");
            System.out.println(" ]");

            System.out.println("Student Name: " + this.studentName);
        }
        else{}



    }
}
