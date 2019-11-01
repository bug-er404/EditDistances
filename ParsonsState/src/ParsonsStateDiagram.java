import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsonsStateDiagram {
    // The starting of each section in the log (Also of the student assembled code)
    private static final String LOG_PROBLEM_SECTION_START = "The Problem:";
    // The ending of the student assembled code section and the beginning of sorted events.
    private static final String LOG_PROBLEM_SECTION_END = "Here is a summary of your actions sorted by line";
    // The ending of the sorted events section in the log.
    private static final String SORTED_EVENTS_END = "Your Grade:";
    // The ending of a solution with no student moves.
    private static final String NO_MOVE_TAKEN = "Total time taken for this session:";

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

    // Template of where template number can be found.
    private static final String TEMPLATE_NUMBER_PATTERN = "^Using Template # (.*)$";

    // Variable that checks the number of instances of LOG_PROBLEM_SECTION_START
    // to account for repetitions of different sections.
    // Used to get to the chronological student assembled code section and to ensure repetitions are skipped.
    private int SECTION_START_COUNT = 0;

    // Flag used to check whether both valid and invalid moves are counted in the program or not.
    // true, count all moves.
    // false, counts only valid moves.
    private static boolean countIgnoredSteps = false;

    // Flag to check for multiple reconstructed states and to check if it converges or not.
    private boolean stateConverges = true;

    // STUDENT ASSEMBLED SOLUTION.
    // PATTERN OF STUDENT ACTIONS
    // Move actions pattern
    private static final String MOVE_PROBLEM_SOLUTION = "(.*)Moved from problem to solution at line (.*)\\: (.*)$";
    private static final String MOVE_SOLUTION_PROBLEM = "(.*)Moved from solution to problem at line (.*)\\: (.*)$";
    private static final String MOVE_SOLUTION_TRASH = "(.*)Moved from solution to trash at line (.*)\\: (.*)$";
    private static final String MOVE_TRASH_SOLUTION = "(.*)Moved from trash to solution at line (.*)\\: (.*)$";
    private static final String MOVE_PROBLEM_TRASH = "(.*)Moved from problem to trash at line (.*)\\: (.*)$";
    private static final String MOVE_TRASH_PROBLEM = "(.*)Moved from trash to problem at line (.*)\\: (.*)$";
    // Reorder action pattern
    private static final String REORDER_ACTION = "(.*)Reordered from line (.*) to (.*)\\: (.*)$";


    // Vector of ParsonsState objects that has the student assembled code of each state
    // Index 0 has the initial solution presented to the student.
    private Vector<ParsonsState> stateVector = new Vector<>();
    // ParsonsState object that stores the correct solution.
    private ParsonsState correctSolution = new ParsonsState();

    // *********************************************************
    // ************** Default Constructor **********************
    // *********************************************************
    ParsonsStateDiagram() {

    }

    // *********************************************************
    // ************ Parameterized Constructor ******************
    // *********************************************************
    ParsonsStateDiagram(Vector<String> problemLog)
    {
        // Get the correct solution.
        // Method also updates the integer representation of code and checks for repeated sections.
        // Updates correctSolution
        extractCorrectCode(problemLog);
//        correctSolution.printState();

        // Get the student assembled code
        // Updates stateVector
        getStudentAssembledCode(problemLog);

    }


    /**
     Method to extract the correct code from log. Updates the object that calls this function with the correct solution.
     ALSO updates the hashmap codeIntegerRepresentation with integer representation for the code lines
     and checks for section repetitions.
     */
    private void extractCorrectCode(Vector<String> problemLog) {
        // Buffer to store only the code part from processing the sorted event log.
        String code;

        // Counter variable to keep track of line number.
        int count = 1;

        // Getting past the log sections till the beginning of the sorted events section.
        // Also count the instances of LOG_PROBLEM_SECTION_START to check for repetitions.
        for(int i = 0; i<problemLog.size(); i++)
        {
            // Getting count of sections to check for repetitions.
            if (problemLog.get(i).contains(LOG_PROBLEM_SECTION_START))
                // Update the variable that checks for repetitions.
                SECTION_START_COUNT++;

            if(problemLog.get(i).contains(LOG_PROBLEM_SECTION_END))
            {
                // Moving to the next line
                i++;

                // The pattern of the line where correct code can be found
                // Note that this is case-sensitive, so it strips away distracters pre-2016
                Pattern pattern = Pattern.compile( "^Line .* \\(.* moves\\):(.*)$" );
                Matcher matcher;

                // Reading till we reach the end of sorted events section
                while(problemLog.get(i).contains(SORTED_EVENTS_END) == false) {
                    // Extract the code from the event section if it contains a code line.
                    matcher = pattern.matcher(problemLog.get(i));
                    if( matcher.matches() )
                    {
                        // Get the code and add it to resultVector
                        code = matcher.group(1).trim();
                        correctSolution.addCode(code);

                        // Update codeIntegerRepresentation
                        // Sets the integer representation of the code.
                        correctSolution.addCodeRepresentation(code, count);
                        count++;

                        // If this is the last line in the program, break
                        // because what follows are distracters
                        if( code.equals( JAVA_PROGRAM_END ) // Java
                                || code.equals( CPP_PROGRAM_END ) // C++
                                || code.equals( CS_PROGRAM_END ) // C#
                                || code.equals( C_PROGRAM_END ) //C
                        )
                            break;
                    }
                    // Moving to next tile.
                    i++;
                }
                // Breaking for loop.
                break;
            }
        }

        // Checking for logs that do not have sorted events section. (Most incomplete solutions)
        // Correct solution is assembled by getIncompleteCorrectSolution function.
        if(correctSolution.getSize() == 0)
        {
            getIncompleteCorrectSolution(problemLog);
        }
        else {
            // Storing the integer representation of the code in the object.
            correctSolution.codeToint();
        }
    }

    /**
     Method to process the log file and assemble the correct solution for incomplete solutions that do not have sorted events section.
     Vector of String that hold the log file.
     */
    private void getIncompleteCorrectSolution(Vector<String> problemLog){
        // Variable to hold template number.
        String templateNumber="";
        // Variable to hold language.
        String probLanguage = "";

        // Flag for finding stats
        boolean found = false;

//        for(int i = 0; i<problemLog.size();i++)
//            System.out.println(problemLog.get(i));

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
            System.out.println("Cannot find language used. Check ParsonsStateDiagram class-getIncompleteCorrectSolution.");
        }

        // Get initial solution that is presented to the student.
        Vector<String> initialCode = getInitialCode(problemLog);

        // Get scrambled code.
        Vector<String> scrambledCode = extractScrambledCode(problemLog);

        correctSolution.getCorrectSolutionforTemplate(initialCode, scrambledCode, templateNumber, probLanguage);

        correctSolution.codeToint();

    }

    /**
     Method to process the log file and update the Vector of ParsonsState objects of student assembled solution.
     Vector of ParsonsState objects that holds the states of the student assembled solutions.
     */
    private void getStudentAssembledCode(Vector<String> problemLog) {
        // Get initial solution that is presented to the student.
        ParsonsState initialSolution = extractInitialCode(problemLog);

        // Counter variable
        int count = 0;
        // Counter for the number of states in the log.
        // 0 state is the initial solution presented to the student.
        int state = 0;

        // Buffer that holds line number of action taken.
        Integer line_number1 = 0;
        Integer line_number2 = 0;
        // Buffer that holds code of the action taken.
        String code = "" ;

        // Pattern matchers used for checking actions.
        Pattern pattern1, pattern2;
        Matcher matcher1, matcher2;

        //Appending the initial solution state to the vector and updating state count.
        stateVector.add(initialSolution);
        state++;

        // Log iterator.
        int i = 0;

        // Getting past the log sections till the beginning of the student assembled code is reached.
        // (Third section of the log when there are no section repetitions)
        while (count < SECTION_START_COUNT) {
            i++;

            if (problemLog.get(i).contains(LOG_PROBLEM_SECTION_START))
                count++;
            // Checking if no steps were made by the student. (Empty solution)
            if(problemLog.get(i).contains(NO_MOVE_TAKEN))
                return;
        }

        // Reading till we reach the end of student assembled code section
        while (problemLog.get(i).contains(LOG_PROBLEM_SECTION_END) == false
        && problemLog.get(i).contains(NO_MOVE_TAKEN) == false) {
            // Moving to next line.
            i++;

            // Checking for valid/invalid moves and checking the flag if it should be considered or not.
            if(problemLog.get(i).contains(INVALID_MOVE_INDICATOR) == true && countIgnoredSteps == false) {
                // To skip invalid moves, we read the next line which contains the beginning of the student code solution.
                // We continue with the loop as once we skipped the beginning, the rest won't be considered as a state in the program.
                i++;
                continue;
            }

            // CHECKING FOR MOVE ACTION FROM
            // PROBLEM TO TRASH OR
            // TRASH TO PROBLEM OR
            // ATTEMPTED MOVES (WHEN countIgnoredSteps is TRUE and we count all valid/invalid moves.
            // Otherwise it is removed in the previous if check. )
            // The pattern of the line where action log can be found.
            pattern1 = Pattern.compile(MOVE_PROBLEM_TRASH);
            matcher1 = pattern1.matcher(problemLog.get(i));
            pattern2 = Pattern.compile(MOVE_TRASH_PROBLEM);
            matcher2 = pattern2.matcher(problemLog.get(i));
            // If true copy the current state as the next state using the copy constructor.
            if (matcher1.matches() || matcher2.matches()) {
                // Copy the current state to new state and append to vector.
                ParsonsState temp_state = new ParsonsState(stateVector.get(state-1));
                stateVector.add(temp_state);
                state++;

                continue;
            }

            // CHECKING FOR MOVE ACTION FROM
            // PROBLEM TO SOLUTION OR
            // TRASH TO SOLUTION
            // The pattern of the line where action log can be found.
            pattern1 = Pattern.compile(MOVE_PROBLEM_SOLUTION);
            matcher1 = pattern1.matcher(problemLog.get(i));
            pattern2 = Pattern.compile(MOVE_TRASH_SOLUTION);
            matcher2 = pattern2.matcher(problemLog.get(i));
            // Extract the code from the event if true and
            // Add the code line in action to the next state.
            if (matcher1.matches() || matcher2.matches()) {
                // MOVE_PROBLEM_SOLUTION
                if(matcher1.matches()){
                    // Get the code and add it to resultVector
                    line_number1 = Integer.parseInt(matcher1.group(2).trim());
                    code = matcher1.group(3).trim();
                }
                // MOVE_TRASH_SOLUTION
                if(matcher2.matches()){
                    // Get the code and add it to resultVector
                    line_number1 = Integer.parseInt(matcher2.group(2).trim());
                    code = matcher2.group(3).trim();
                }

                // Add code to state at (line_number1-1) and append state to vector.
                ParsonsState temp_state = new ParsonsState(stateVector.get(state-1), code, "ADD", (line_number1-1), Integer.MAX_VALUE);
                stateVector.add(temp_state);
                state++;

                continue;
            }

            // CHECKING FOR MOVE ACTION FROM
            // SOLUTION TO PROBLEM OR
            // SOLUTION TO TRASH
            // The pattern of the line where action log can be found.
            pattern1 = Pattern.compile(MOVE_SOLUTION_PROBLEM);
            matcher1 = pattern1.matcher(problemLog.get(i));
            pattern2 = Pattern.compile(MOVE_SOLUTION_TRASH);
            matcher2 = pattern2.matcher(problemLog.get(i));
            // Extract the code from the event if true and
            // Remove the code line in action from the next state.
            if (matcher1.matches() || matcher2.matches()) {
                if (matcher1.matches()) {
                    // Get the code and add it to resultVector
                    line_number1 = Integer.parseInt(matcher1.group(2).trim());
                    code = matcher1.group(3).trim();
                }
                if (matcher2.matches()) {
                    // Get the code and add it to resultVector
                    line_number1 = Integer.parseInt(matcher2.group(2).trim());
                    // Log lines are not correct
                    line_number1 = Integer.MAX_VALUE;
                    code = matcher2.group(3).trim();
                }

                // Remove the code line from state and append to vector.
                ParsonsState temp_state;
                try {
                    temp_state = new ParsonsState(stateVector.get(state - 1), code, "REMOVE", (line_number1 - 1), Integer.MAX_VALUE);
                } catch (IllegalArgumentException e){
                    throw new IllegalArgumentException();
                }

                // FOR MULTIPLE RECONSTRUCTIONS
//                ParsonsState temp_state = new ParsonsState(stateVector.get(state - 1), code, "REMOVE", (line_number1 - 1), Integer.MAX_VALUE);
//                // Checking if we need to create multiple reconstructions:
//                if(temp_state==null)
//                {
//                    ArrayList<Integer> removeLineNums = new ArrayList<>();
//                    removeLineNums = temp_state.checkMultipleReconstructs(stateVector.get(state-1), code);
//                    Vector<ParsonsStateDiagram> multiStates = new Vector<>();
//                    for(int j=0; j<removeLineNums.size();j++)
//                    {
//                        temp_state = new ParsonsState(stateVector.get(state - 1), code, "REMOVE", removeLineNums.get(i), Integer.MAX_VALUE);
//                        // If the removing is done, we recursively check which state diagram converges properly.
//                        if(temp_state!=null)
//                            multiStates.add(j, new ParsonsStateDiagram(problemLog, problemLog.get(i), temp_state));
//                    }
//                    //
//                }
                stateVector.add(temp_state);
                state++;

                continue;

            }

            // CHECKING FOR REORDER ACTION
            // The pattern of the line where action log can be found.
            pattern1 = Pattern.compile(REORDER_ACTION);
            matcher1 = pattern1.matcher(problemLog.get(i));
            // Extract the code from the event if true and
            // Remove the code line in action from the next state.
            if( matcher1.matches() ) {
                // Get the code and add it to resultVector
                line_number1 = Integer.parseInt(matcher1.group(2).trim());
                line_number2 = Integer.parseInt(matcher1.group(3).trim());
                code = matcher1.group(4).trim();

                // Move the code line in state and append to vector.
                ParsonsState temp_state = new ParsonsState(stateVector.get(state-1), code, "MOVE", (line_number1-1), (line_number2-1));
                stateVector.add(temp_state);
                state++;

                continue;
            }
        }
    }

//
//    // *********************************************************
//    // Parameterized Constructor for recursion when multiple reconstructions are done.
//    // *********************************************************
//    ParsonsStateDiagram(Vector<String> problemLog, String logEntry, ParsonsState currentState)
//    {
//        // Buffer that holds code of the action taken.
//        String code = "" ;
//
//        // Buffer that holds line number of action taken.
//        Integer line_number1 = 0;
//        Integer line_number2 = 0;
//
//        // Pattern matchers used for checking actions.
//        Pattern pattern1, pattern2;
//        Matcher matcher1, matcher2;
//
//        // Variable that holds the line that has been reached.
//        int i = 0;
//
//        // Temp variable to keep track of how many states are created
//        int state = 1;
//
//        // 0 has the previous state that is already appended. When merging, we start at index 1 till the end.
//        stateVector.add(currentState);
//
//        // Getting past the log sections till the previous entry is reached and going to the next entry.
//        i = problemLog.indexOf(logEntry);
//
//        // Reading till we reach the end of student assembled code section
//        while (problemLog.get(i).contains(LOG_PROBLEM_SECTION_END) == false
//                && problemLog.get(i).contains(NO_MOVE_TAKEN) == false) {
//            // Moving to next line.
//            i++;
//
//            // Checking for valid/invalid moves and checking the flag if it should be considered or not.
//            if(problemLog.get(i).contains(INVALID_MOVE_INDICATOR) == true && countIgnoredSteps == false) {
//                // To skip invalid moves, we read the next line which contains the beginning of the student code solution.
//                // We continue with the loop as once we skipped the beginning, the rest won't be considered as a state in the program.
//                i++;
//                continue;
//            }
//
//            // CHECKING FOR MOVE ACTION FROM
//            // PROBLEM TO TRASH OR
//            // TRASH TO PROBLEM OR
//            // ATTEMPTED MOVES (WHEN countIgnoredSteps is TRUE and we count all valid/invalid moves.
//            // Otherwise it is removed in the previous if check. )
//            // The pattern of the line where action log can be found.
//            pattern1 = Pattern.compile(MOVE_PROBLEM_TRASH);
//            matcher1 = pattern1.matcher(problemLog.get(i));
//            pattern2 = Pattern.compile(MOVE_TRASH_PROBLEM);
//            matcher2 = pattern2.matcher(problemLog.get(i));
//            // If true copy the current state as the next state using the copy constructor.
//            if (matcher1.matches() || matcher2.matches()) {
//                // Copy the current state to new state and append to vector.
//                ParsonsState temp_state = new ParsonsState(stateVector.get(state-1));
//                stateVector.add(temp_state);
//                state++;
//
//                continue;
//            }
//
//            // CHECKING FOR MOVE ACTION FROM
//            // PROBLEM TO SOLUTION OR
//            // TRASH TO SOLUTION
//            // The pattern of the line where action log can be found.
//            pattern1 = Pattern.compile(MOVE_PROBLEM_SOLUTION);
//            matcher1 = pattern1.matcher(problemLog.get(i));
//            pattern2 = Pattern.compile(MOVE_TRASH_SOLUTION);
//            matcher2 = pattern2.matcher(problemLog.get(i));
//            // Extract the code from the event if true and
//            // Add the code line in action to the next state.
//            if (matcher1.matches() || matcher2.matches()) {
//                // MOVE_PROBLEM_SOLUTION
//                if(matcher1.matches()){
//                    // Get the code and add it to resultVector
//                    line_number1 = Integer.parseInt(matcher1.group(2).trim());
//                    code = matcher1.group(3).trim();
//                }
//                // MOVE_TRASH_SOLUTION
//                if(matcher2.matches()){
//                    // Get the code and add it to resultVector
//                    line_number1 = Integer.parseInt(matcher2.group(2).trim());
//                    code = matcher2.group(3).trim();
//                }
//                ParsonsState temp_state;
//
//                // Add code to state at (line_number1-1) and append state to vector.
//                try {
//                   temp_state = new ParsonsState(stateVector.get(state-1), code, "ADD", (line_number1-1), Integer.MAX_VALUE);
//                }catch (Exception e){
//                stateConverges=false;
//                return;
//                }
//                stateVector.add(temp_state);
//                state++;
//
//                continue;
//            }
//
//            // CHECKING FOR MOVE ACTION FROM
//            // SOLUTION TO PROBLEM OR
//            // SOLUTION TO TRASH
//            // The pattern of the line where action log can be found.
//            pattern1 = Pattern.compile(MOVE_SOLUTION_PROBLEM);
//            matcher1 = pattern1.matcher(problemLog.get(i));
//            pattern2 = Pattern.compile(MOVE_SOLUTION_TRASH);
//            matcher2 = pattern2.matcher(problemLog.get(i));
//            // Extract the code from the event if true and
//            // Remove the code line in action from the next state.
//            if (matcher1.matches() || matcher2.matches()) {
//                if (matcher1.matches()) {
//                    // Get the code and add it to resultVector
//                    line_number1 = Integer.parseInt(matcher1.group(2).trim());
//                    code = matcher1.group(3).trim();
//                }
//                if (matcher2.matches()) {
//                    // Get the code and add it to resultVector
//                    line_number1 = Integer.parseInt(matcher2.group(2).trim());
//                    // Log lines are not correct
//                    line_number1 = Integer.MAX_VALUE;
//                    code = matcher2.group(3).trim();
//                }
//
//                ParsonsState temp_state = new ParsonsState(stateVector.get(state - 1), code, "REMOVE", (line_number1 - 1), Integer.MAX_VALUE);
//                // Checking if we need to create multiple reconstructions:
//                if(temp_state==null)
//                {
//                    ArrayList<Integer> removeLineNums = new ArrayList<>();
//                    removeLineNums = temp_state.checkMultipleReconstructs(stateVector.get(state-1), code);
//                    Vector<ParsonsStateDiagram> multiStates = new Vector<>();
//                    for(int j=0; j<removeLineNums.size();j++)
//                    {
//                        multiStates.add(j, new ParsonsStateDiagram(problemLog, code, stateVector.get(state-1)));
//                    }
//                }
//                stateVector.add(temp_state);
//                state++;
//
//                continue;
//
//            }
//
//            // CHECKING FOR REORDER ACTION
//            // The pattern of the line where action log can be found.
//            pattern1 = Pattern.compile(REORDER_ACTION);
//            matcher1 = pattern1.matcher(problemLog.get(i));
//            // Extract the code from the event if true and
//            // Remove the code line in action from the next state.
//            if( matcher1.matches() ) {
//                // Get the code and add it to resultVector
//                line_number1 = Integer.parseInt(matcher1.group(2).trim());
//                line_number2 = Integer.parseInt(matcher1.group(3).trim());
//                code = matcher1.group(4).trim();
//
//                ParsonsState temp_state;
//                // Move the code line in state and append to vector.
//                try {
//                    temp_state = new ParsonsState(stateVector.get(state - 1), code, "MOVE", (line_number1 - 1), (line_number2 - 1));
//                }catch (Exception e){
//                    stateConverges=false;
//                    return;
//                }
//                stateVector.add(temp_state);
//                state++;
//
//                continue;
//            }
//        }
//    }

    /**
     Method that extracts the initial code that is presented to the student from the log.
     @return ParsonsState object that holds the initial code presented to the student.
     */
    private ParsonsState extractInitialCode(Vector<String> problemLog)
    {
        // Counter variable,
        int count = 0;
        // Log iterator buffer
        int i = 0;

        // Pre checking for problems that do not have language specified.
        // Usually the template 100 problems
        if(problemLog.contains("Using Template # 100"))
        {
            // Finding initial code
            while(problemLog.get(i).contains("Problem") || problemLog.get(i).contains("Using Template"))
                i++;


            if(problemLog.get(i).contains("Problem") == false &&
                problemLog.get(i).contains("Using Template") == false){
                // Create Parsons Object
                ParsonsState temp_state = new ParsonsState();

                // Append to the obj
                do {
                    temp_state.addCode(problemLog.get(i));
                    i++;
                } while (problemLog.get(i).contains("Using Template") == false
                        && problemLog.get(i).contains("The Problem")==false);

                // The last code line is not appended yet, so we append it.
                temp_state.addCode(problemLog.get(i));

                // Get the integer representation of the code and store it as an array.
                temp_state.codeToint();

                return temp_state;
            }
            else
            {
                System.out.println("Cannot get initial code for problem without language specificity. Check extractInitialCode function.");
                System.exit(1);
            }
        }

        // Getting past the log sections till we get to the second section that has the initial code
        // that is presented to the student.
        while (count < 2) {
            i++;
            if (problemLog.get(i).contains(LOG_PROBLEM_SECTION_START))
                count++;
        }

        // Getting the next line which should have the beginning of the initial solution.
        i++;

        // Getting the initial code and creating a ParsonsState object for the initial code.
        // Asserting the beginning of the student code solution.
        if ((problemLog.get(i).contains(JAVA_PROGRAM_START)) //JAVA
                || (problemLog.get(i).contains(CPP_PROGRAM_START)) //C++
                || (problemLog.get(i).contains(C_PROGRAM_START))   //C
                || (problemLog.get(i).contains(CS_PROGRAM_START))) //C#
        {
            // Create Parsons Object
            ParsonsState temp_state = new ParsonsState();

            // Append to the obj
            do {
                temp_state.addCode(problemLog.get(i));
                i++;
            } while (problemLog.get(i).contains(C_PROGRAM_END) == false
                    && problemLog.get(i).contains(CS_PROGRAM_END)==false
                    && problemLog.get(i).contains(JAVA_PROGRAM_END) == false
                    && problemLog.get(i).contains(CPP_PROGRAM_END) == false);

            // The last code line is not appended yet, so we append it.
            temp_state.addCode(problemLog.get(i));

            // Get the integer representation of the code and store it as an array.
            temp_state.codeToint();

            return temp_state;
        }

        System.out.println("HERE");
        for(i=0;i<=problemLog.size();i++)
            System.out.println(problemLog.get(i));
        // Returns NULL if extraction fails.
        return null;
    }

    /**
     Method that simply extracts the initial code that is presented to the student from the log. Only used by incomplete solutions.
     Difference between this and extractInitialCode is that this does not update the array representation.
     @return ParsonsState object that holds the initial code presented to the student.
     */
    private Vector<String> getInitialCode(Vector<String> problemLog) {
        // Vector to hold scrambled lines of code.
        Vector<String> initialCode = new Vector<>();
        int count = 0;
        int i = 0;

        while (count<2){
            if(problemLog.get(i).contains(LOG_PROBLEM_SECTION_START))
                count++;
            i++;
        }

        // Adding all the lines to the vector
        while(problemLog.get(i).contains("Using Template") == false){
            initialCode.add(problemLog.get(i));
            i++;
        }

        return initialCode;
    }

    /**
     Method that extracts the scrambled code that is presented to the student from the log.
     @return ParsonsState object that holds the initial code presented to the student.
     */
    private Vector<String> extractScrambledCode(Vector<String> problemLog) {
        // Vector to hold scrambled lines of code.
        Vector<String> scrambledCode = new Vector<>();
        int i = 0;

        while (problemLog.get(i).contains(LOG_PROBLEM_SECTION_START) == false)
            i++;

        // Going to the next line that contains the scrambled code.
        i++;

        // Adding all the lines to the vector
        while(problemLog.get(i).contains("Using Template") == false){
            scrambledCode.add(problemLog.get(i));
            i++;
        }

        return scrambledCode;
    }


    public Vector<Integer> getEditDistanceVector(){
        Vector<Integer> editDistanceVector = new Vector<>();

        if(!stateVector.isEmpty())
        {
            for(int i=0; i<stateVector.size(); i++)
            {
//                System.out.println("STATE: "+ i);
//                stateVector.get(i).printState();
                editDistanceVector.add(stateVector.get(i).getEditDistance(this.correctSolution));
            }
            return editDistanceVector;
        }
        else
            return null;
    }

}
