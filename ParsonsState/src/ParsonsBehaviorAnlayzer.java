import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

public class ParsonsBehaviorAnlayzer {

    // The beginning of each student solution.
    private static final String NEW_STUDENT_BEGINNING = "From problets@bit.ramapo.edu";
    // The end of each student solution.
    private static final String NEW_STUDENT_END = "<HR>";
    // New problem indicator.
    private static final String NEW_PROBLEM_INDICATOR = "Problem No.";

    // The end of each student problem
    private static final String NEW_PUZZLE_END = "Time spent on this problem:";
    // The end of each student problem if it is incomplete
    private static final String INC_PUZZEL_END = "Total time taken for this session:";


    private Vector<Student> studentsVec = new Vector<>();

    // *********************************************************
    // ************** Default Constructor **********************
    // *********************************************************
    ParsonsBehaviorAnlayzer() {

    }

    // *********************************************************
    // *********** Parameterized Constructor *******************
    // *********************************************************
    ParsonsBehaviorAnlayzer(String logSourceFile) {
        // Reader to read log file
        BufferedReader reader = null;
        // Buffer to read the statement from the log file
        String buffer = null;

        // Open log file to read and check if it opened correctly.
        File file = new File(logSourceFile);
        try {
            // Initializing buffer
            reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();

            // Reading till the end of the file.
            while(buffer!=null)
            {
                // Assert that it is the beginning of a new student code
                if(buffer.contains(NEW_STUDENT_BEGINNING))
                {
                    // Temp vector of string to store and pass log of one student
                    // to Student class constructor.
                    Vector<String> temp_log = new Vector<>();

                    // Creating a vector of string for the log of one student.
                    do {
                        temp_log.add(buffer);
                        buffer = reader.readLine();

                        // Checking for multiple problem solutions.
                        if( buffer!= null && buffer.equals(NEW_STUDENT_END)){
                            // Reading the next line
                            buffer = reader.readLine();
                            if(buffer.contains(NEW_PROBLEM_INDICATOR)){
                                temp_log.add(buffer);
                                continue;
                            }
                            else {
                                break;
                            }
                        }
                    } while (buffer != null
                            && !buffer.contains(NEW_STUDENT_BEGINNING)
                            && !buffer.equals(""));

                    // Creating temp Student Object.
                    Student temp_student = new Student(temp_log);
                    studentsVec.add(temp_student);
                    // Moving to next line
                    buffer = reader.readLine();
                }
                else{
                    // Move on to the next line.
                    buffer = reader.readLine();
                }
            }
        } catch (Exception e) {
            System.err.println("The following error was caught: " + e.getMessage());
            System.out.println("Could not open the file: " + logSourceFile);
            System.exit(1);
        }
        System.out.println("# of Students: "+studentsVec.size());
    }

    private Vector<Student> getStudentsVec(){
        return studentsVec;
    }


    public void PrintEditDistance(String template, String language){
        for(int i =0; i<studentsVec.size(); i++)
        {
//            System.out.println("STUDENT SIZE: " + studentsVec.size());
            studentsVec.get(i).printStudentEditDistance(template, language);
        }
    }

    public static void main(String[] args){
//        String sourceFileName = "/Users/salilmaharjan/Desktop/kattis/kattis/Raw_Test1.txt";
        String sourceFileName = "/Users/salilmaharjan/Desktop/kumar/work/selection_sep_data/selection_combined.txt";
//        String sourceFileName = "/Users/salilmaharjan/Desktop/anon8";

        Vector<Student> studentVector = new Vector<>();

        // Getting user input
        String tempNumber;
        String language;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Template Number: ");
        tempNumber = in.nextLine();
        System.out.println("Enter language: ");
        language = in.nextLine();

        ParsonsBehaviorAnlayzer parsonsLog = new ParsonsBehaviorAnlayzer(sourceFileName);

        parsonsLog.PrintEditDistance(tempNumber, language);

    }
}
