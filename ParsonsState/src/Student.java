import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student {
    // Pattern of where student name can be found
    private static final String STUDENT_NAME_PATTERN_1 = "^SUBJECT: Selection/Analysis/(.*)/Practice$";
    private static final String STUDENT_NAME_PATTERN_2 = "^SUBJECT: Selection/Parsons/(.*)/Practice$";

    // The beginning of each new puzzle
    private static final String NEW_PUZZLE_BEGINNING = "Problem No.";

    // The end of each student problem
    private static final String NEW_PUZZLE_END = "Time spent on this problem:";
    // The end of each student problem if it is incomplete
    private static final String INC_PUZZEL_END = "Total time taken for this session:";

    private Vector<Puzzle> puzzleVec = new Vector<>();


    // *********************************************************
    // ************** Default Constructor **********************
    // *********************************************************
    Student() {

    }

    // *********************************************************
    // *********** Parameterized Constructor *******************
    // *********************************************************
    Student(Vector<String> studentLog) {

        // Variable to hold student name.
        String studentName="";

        // Get student name from the log (which is in the second line)
        // Pattern matchers used for checking the template number.
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(STUDENT_NAME_PATTERN_1);

        // Getting student name if it matches and storing it.
        matcher = pattern.matcher(studentLog.get(1));
        if (matcher.matches()) {
            studentName = matcher.group(1).trim();
        }
        else
        {
            pattern = Pattern.compile(STUDENT_NAME_PATTERN_2);
            matcher = pattern.matcher(studentLog.get(1));
            if (matcher.matches()) {
                studentName = matcher.group(1).trim();
            }
            else
            {
                System.out.println("Student name not found. Check Student constructor.");
                System.exit(1);
            }
        }


        // Creating a vector of student problems.
        for(int i = 0; i<studentLog.size(); i++)
        {
            // Checking for the beginning of each new problem.
            if(studentLog.get(i).contains(NEW_PUZZLE_BEGINNING))
            {
                // Temp vector of string to store the log of one problem from the student logs.
                Vector<String> temp_problem= new Vector<>();

                // Filling the vector with the the problem log
                do {
                    temp_problem.add(studentLog.get(i));
                    i++;
                } while ( i < studentLog.size()
                        && studentLog.get(i).contains(NEW_PUZZLE_END) == false
                        && studentLog.get(i).contains(INC_PUZZEL_END) == false);

                // Appending the last line.
                // Checking if the log is incomplete.
                if(i == studentLog.size())
                {
                    System.out.println(studentLog.get(i-1));
                    System.out.println("Incomplete log entry found. Check Student constructor.");
                    System.exit(1);
                }
                else
                    temp_problem.add(studentLog.get(i));

                // Creating temp Puzzle Object.
                Puzzle temp_puzzle = new Puzzle();
                try{
                temp_puzzle = new Puzzle(temp_problem, studentName);}
                catch(IllegalArgumentException e){}
                puzzleVec.add(temp_puzzle);
            }
        }
    }

    public void printStudentEditDistance(String template, String language){
        for(int i = 0; i<this.puzzleVec.size(); i++)
        {
//            System.out.println("PUZZLE SIZE: " + this.puzzleVec.size());
            puzzleVec.get(i).printPuzzleEditDistance(template, language);
        }
    }
}
