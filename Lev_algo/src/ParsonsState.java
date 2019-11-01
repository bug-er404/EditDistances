/*******************************************************
 Class that holds the state of the solution in Parsons Puzzle Logs.
 @author Salil Maharjan
 @since 06/24/2019
  ******************************************************** */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsonsState {
    // LOG SECTION INDICATORS
    // The log file name
    private static final String sourceFileName = "/Users/salilmaharjan/Desktop/kattis/kattis/PartialDemo5.txt";

    // The starting of each section in the log (Also of the student assembled code)
    private static final String LOG_PROBLEM_SECTION_START = "The Problem:";

    // The ending of the student assembled code section and the beginning of sorted events.
    private static final String LOG_PROBLEM_SECTION_END = "Here is a summary of your actions sorted by line";

    // The ending of the sorted events section in the log.
    private static final String SORTED_EVENTS_END = "Your Grade:";

    // The line that signifies the beginning of the program in Java
    private static final String JAVA_PROGRAM_START = "// The Java program";
    // The line that signifies the beginning of the program in C++
    private static final String C_PROGRAM_START = "// The C program";
    // The line that signifies the beginning of the program in C++
    private static final String CPP_PROGRAM_START = "// The C++ program";
    // The line that signifies the beginning of the program in C#
    private static final String CS_PROGRAM_START = "// The C# program";
    // The line that signifies the end of the program in Java
    private static final String JAVA_PROGRAM_END = "} // End of class Problem";
    // The line that signifies the end of the program in C++
    private static final String C_PROGRAM_END = "}  // End of function main";
    // The line that signifies the end of the program in C++
    private static final String CPP_PROGRAM_END = "}  // End of function main";
    // The line that signifies the end of the program in C#
    private static final String CS_PROGRAM_END = "}  // End of namespace MyCode";
    // Invalid move indicator
    private static final String INVALID_MOVE_INDICATOR = "Attempted to";
    // Temporary flag for end of state in student assembled code that was manually constructed.
    private static final String CODE_STATE_END = "-------END OF CURRENT STATE-------";

    // Stores the integer representation of each code line.
    // Information stored by the correct solution ParsonsState object only.
    // Uninitialized for the other states in the student solution.
    private static HashMap<String, Integer> codeIntegerRepresentation = new HashMap<>();

    // Variable initialized by extractCorrectCode
    // Checks the number of instances of LOG_PROBLEM_SECTION_START to account for repetitions of different sections.
    // Used to get to the chronological student assembled code section and to ensure repetitions are skipped.
    private static int SECTION_START_COUNT = 0;

    // Stores the code from the log for each different state.
    private Vector<String> codeState = new Vector<>();

    // Store the numeric array representation of the Parson state object.
    private ArrayList<Integer> arrayRepresentation = new ArrayList<>();

    // Flag used to check whether both valid and invalid moves are counted in the program or not.
    // true, count all moves.
    // false, counts only valid moves.
    private static boolean countIgnoredSteps = false;

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************
    ParsonsState() {
        //stub
    }

    /**
     Getter method that gets the state of a ParsonsState object.
     */
    private void getState(){
        // Print the content of the vector of an ParsonsState object.
        for(int i = 0; i<codeState.size();i++)
            System.out.println(codeState.get(i));
    }

    /**
     Getter method that gets the array representation of the code.
     */
    Integer[] getArrayRepresentation(){
        Integer [] tempArrayRep = new Integer[arrayRepresentation.size()];
        tempArrayRep = arrayRepresentation.toArray(tempArrayRep);

//        // Debugging purpose:
//        System.out.print("[");
//        for(int i = 0; i < arrayRepresentation.size(); i++) {
//            System.out.print(arrayRepresentation.get(i) + ",");
//        }
//        System.out.println("]");

        System.out.print("[");
        for(int i = 0; i < arrayRepresentation.size(); i++) {
            System.out.print(tempArrayRep[i] + ",");
        }
        System.out.println("]");


        return tempArrayRep;
    }

    /**
     Getter method to get code integer representation. Can
     */
    private void getCodeIntegerRepresentation(){
        // Print the content of the hashMap of the integer representation of code.
        Set<Entry<String,Integer>> hashSet=codeIntegerRepresentation.entrySet();
        for(Entry entry:hashSet ) {
            System.out.println(entry.getKey()+", = "+entry.getValue());
        }
    }

    /**
     Method to convert code to integer representation based on the codeIntegerRepresentation.
     */
    private void codeToint(){
        // Temp variable to hold integer representation of the code.
        // Used to check for NULL values of distractors.
        Integer temp;
        for(int i = 0; i<codeState.size();i++)
        {
            temp = codeIntegerRepresentation.get(codeState.get(i));
            // Checking for distractors.
            if(temp == null)
                temp = 0;

            this.arrayRepresentation.add(temp);
        }
    }

    /**
     Method to process the log file and create a Vector of ParsonsState objects of student assembled solution.
     @return Vector of ParsonsState objects that holds the states of the student assembled solutions.
     */
    private Vector<ParsonsState> getStudentAssembledCode() {
        // Vector of ParsonsState objects that has the student assembled code of each state
        Vector<ParsonsState> stateVector = new Vector<>();

        // Reader to read log file
        BufferedReader reader = null;

        // buffer to read the statement from the log file
        String buffer = null;

        // Counter variable
        int count = 0;

        // Open log file to read and check if it opened correctly.
        File file = new File(sourceFileName);
        try {
            reader = new BufferedReader(new FileReader(file));

            // Getting past the log sections till the beginning of the student assembled code is reached.
            // (Third section of the log)
            while (count < SECTION_START_COUNT) {
                buffer = reader.readLine();
                if (buffer.contains(LOG_PROBLEM_SECTION_START))
                    count++;
            }
        } catch (Exception e) {
            System.err.println("The following error was caught: " + e.getMessage());
            System.out.println("Could not open the file: " + sourceFileName);
            System.exit(1);
        }

        // Getting the student assembled solution and creating ParsonsState objects for each state.
        // Count keeps track of line numbers of each state.
        // Line number and number of states count starts from 1.
        count = 1;
        // Counter for the number of states in the log.
        int state = 1;

        try {
            // Reading till we reach the end of student assembled code section
            while (buffer.contains(LOG_PROBLEM_SECTION_END) == false) {
                buffer = reader.readLine();

                // Checking for valid/invalid moves and checking the flag if it should be considered or not.
                if(buffer.contains(INVALID_MOVE_INDICATOR) == true && countIgnoredSteps == false)
                {
                    // To skip invalid moves, we read the next line which contains the beginning of the student code solution.
                    // We continue with the loop as once we skipped the beginning, the rest won't be considered as a state in the program.
                    buffer = reader.readLine();
                    continue;
                }
                // Asserting the beginning of the student code solution.
                if ((buffer.contains(JAVA_PROGRAM_START)) //JAVA
                        || (buffer.contains(CPP_PROGRAM_START)) //C++
                        || (buffer.contains(C_PROGRAM_START))   //C
                        || (buffer.contains(CS_PROGRAM_START))) //C#
                {
                    // Create Parsons Object
                    ParsonsState temp_state = new ParsonsState();

                    // Append to the obj
                    do {
                        temp_state.codeState.add(buffer);
                        count++;
                        buffer = reader.readLine();
                    }
                    while(buffer.contains(CODE_STATE_END) == false);
                    // Previous version that did not account for code after the end statement.
//                    while (buffer.contains(C_PROGRAM_END) == false
//                            && buffer.contains(CS_PROGRAM_END)==false
//                            && buffer.contains(JAVA_PROGRAM_END) == false
//                            && buffer.contains(CPP_PROGRAM_END) == false);

                    // The last code line is not appended yet, so we append it.
//                    temp_state.codeState.add(buffer);
//                    count++;


                    // Get the integer representation of the code and store it as an array.
                    temp_state.codeToint();

                    //Appending the state to the vector and updating state count.
                    stateVector.add(temp_state);
                    state++;

                    // Finished reading the current state. Update count for the next state.
                    count = 1;
                }
                // Else we continue (if there are blanks or system reports) until we find the beginning of the code solution.
                else
                    continue;


            }
        } catch (Exception e) {
            System.err.println("The following error was caught: " + e.getMessage());
            System.out.println("Could not read line at state: " + state + " and line: " + count);
            System.exit(1);
        }

        return stateVector;
    }

    /**
     Method to extract the correct code from log. Updates the object that calls this function with the correct solution.
     ALSO updates the hashmap codeIntegerRepresentation with integer representation for the code lines.
     */
    private void extractCorrectCode() {
        // Reader to read log file
        BufferedReader reader = null;

        // Buffer to read the statement from the log file
        String buffer = null;

        // Buffer to store only the code part from processing the sorted event log.
        String code;

        // Counter variable to keep track of line number.
        int count = 1;

        // Open log file to read and check if it opened correctly.
        File file = new File(sourceFileName);
        try {
            reader = new BufferedReader(new FileReader(file));

            // Initializing buffer
            buffer = reader.readLine();

            // Getting past the log sections till the beginning of the sorted events section.
            // Also count the instances of LOG_PROBLEM_SECTION_START to check for repetitions.
            // Update the variable SECTION_START_COUNT that checks for repetitions later.
            while (buffer.contains(LOG_PROBLEM_SECTION_END) == false) {
                buffer = reader.readLine();
                if (buffer.contains(LOG_PROBLEM_SECTION_START))
                    SECTION_START_COUNT++;
            }

        } catch (Exception e) {
            System.err.println("The following error was caught: " + e.getMessage());
            System.out.println("Could not open the file: " + sourceFileName);
            System.exit(1);
        }

        // The pattern of the line where correct code can be found
        // Note that this is case-sensitive, so it strips away distracters pre-2016
        Pattern pattern = Pattern.compile( "^Line .* \\(.* moves\\):(.*)$" );
        Matcher matcher;

        // Reading the sorted event log and updating the resultVector when correct code is found.
        try {
            // Reading till we reach the end of sorted events section
            while (buffer.contains(SORTED_EVENTS_END) == false) {

                // Extract the code from the event section if it contains a code line.
                buffer = reader.readLine();
                matcher = pattern.matcher(buffer);
                if( matcher.matches() )
                {
                    // Get the code and add it to resultVector
                    code = matcher.group( 1 ).trim();
                    this.codeState.add(code);

                    // Update codeIntegerRepresentation
                    codeIntegerRepresentation.put(code,count);
                    count++;

                    // If this is the last line in the program, break
                    // because what follows are distracters
                    if( code.equals( JAVA_PROGRAM_END ) // Java
                            || code.equals( CPP_PROGRAM_END ) // C++
                            || code.equals( CS_PROGRAM_END ) // C#
                    )
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("The following error was caught: " + e.getMessage());
            System.out.println("Could not read line: " + count);
            System.exit(1);
        }

        // Store the integer representation of the code in the object.
        this.codeToint();
    }

    static int[] generateEditDistance(Vector<ParsonsState> studentCode, ParsonsState correctCode)
    {
        int[] editDistances = new int[studentCode.size()];

        for(int i = 0; i<= studentCode.size()-1; i++)
        {
            editDistances[i] = ParsonDistance.parsonDistance(studentCode.get(i).getArrayRepresentation(),correctCode.getArrayRepresentation());
            System.out.println(editDistances[i]);
        }

        return editDistances;

    }

    public static void main(String[] args)
    {
        // Correct solution section.
        ParsonsState correctSolution = new ParsonsState();
        correctSolution.extractCorrectCode();

        System.out.println("Correct Solution: ");
        System.out.println("------------------------------");
        correctSolution.getState();
        System.out.println("Code to integer representation: ");
        System.out.println("------------------------------");
        correctSolution.getCodeIntegerRepresentation();
        System.out.println("Integer Array representation: ");
        System.out.println("------------------------------");
        correctSolution.getArrayRepresentation();

        // Student Assembled Code section.

        ParsonsState ps = new ParsonsState();
        Vector<ParsonsState> vec;


        vec =  ps.getStudentAssembledCode();
        for(int i = 0; i < vec.size(); i++)
        {
            System.out.println("State: " + (i+1));
            System.out.println("------------------------------");
            vec.get(i).getState();
            System.out.println("Integer Array representation: ");
            vec.get(i).getArrayRepresentation();
        }

        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("EDIT DISTANCES:");

        int[] editDistance = generateEditDistance(vec, correctSolution);

        // Debugging purpose:
        System.out.println("EDIT DISTANCES IN MOVES:");
        for(int j = 0; j<=vec.size()-1; j++)
            System.out.println(
                    "Move " + j + ":  " +
                            editDistance[j]);


    }
}

//TRASH METHODS

// Checking for code after the end of the function.
//                    buffer = reader.readLine();
//                            while(codeIntegerRepresentation.containsKey(buffer) == true)
//                            {
//                            temp_state.codeState.add(buffer);
//                            count++;
//                            buffer = reader.readLine();
//                            }

