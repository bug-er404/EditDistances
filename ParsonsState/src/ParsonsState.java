/*******************************************************
 Class that holds the state of the solution in Parsons Puzzle Logs.
 @author Salil Maharjan
 @since 06/24/2019
  ******************************************************** */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsonsState {

    // Stores the integer representation of each code line.
    // Information stored by the correct solution ParsonsState object.
    // Static and uninitialized by the other states in the student solution.
    private static HashMap<String, Integer> codeIntegerRepresentation = new HashMap<>();

    // Stores the code from the log for different states.
    private Vector<String> codeState = new Vector<>();

    // Store the numeric array representation of the Parson state object.
    private ArrayList<Integer> arrayRepresentation = new ArrayList<>();


    // *********************************************************
    // ************** Default Constructor **********************
    // *********************************************************
    ParsonsState() {

    }


    /**
     Method to check for multiple reconstructions of the code to check which line of code should be removed.
     Returns an arraylist of indexes of where code should be removed.
     */
    public ArrayList<Integer> checkMultipleReconstructs(ParsonsState currentCode, String code){
        // List to store line number of the code instances
        ArrayList<Integer> removeLineNum = new ArrayList<>();

        // Count how many instances of the line are there:
        for(int i = 0;i<currentCode.codeState.size();i++){
            if(currentCode.codeState.get(i).equals(code)) {
                removeLineNum.add(i);
            }
        }

        return removeLineNum;
    }

    // *********************************************************
    // ************ Parameterized Constructor ******************
    // *********************************************************
    ParsonsState(ParsonsState currentCode, String code, String action, int lineNumber, int toLineNumber) //Vector<String> problemLog)
    {
        // Copying the code from the previous state
        for(int i = 0; i < currentCode.codeState.size(); i++){
            this.codeState.add(currentCode.codeState.get(i));
        }

        // Performing the action:
        switch (action)
        {
            case "ADD":
                if(lineNumber >= 0)
                    this.codeState.add(lineNumber, code);
                // Get the integer representation of the code and store it as an array.
                this.codeToint();
                break;

            case "REMOVE":
                //NEED TO DO SOMETHING ABOUT THE LINE NUMBERS THAT ARE NOT CONSISTENT.
                if(lineNumber == Integer.MAX_VALUE-1){
                    this.codeState.removeElement(code);}
                else if(lineNumber<codeState.size() && this.codeState.get(lineNumber) == code)
                    this.codeState.remove(lineNumber);
                else {
//                    for(int i = 0; i < currentCode.codeState.size(); i++){
//                        System.out.println(currentCode.codeState.get(i));
//                    }
                    System.out.println("---------------------");
                    System.out.println("Action: " + action);
                    System.out.println("Line: " + lineNumber);
                    throw new IllegalArgumentException("Error. Cannot find code: " + code);
                }
                // Get the integer representation of the code and store it as an array.
                this.codeToint();
                break;

                // FOR MULTIPLE RECONSTRUCTIONS
//                // If line number matches
//                if(lineNumber<codeState.size() && this.codeState.get(lineNumber) == code){
//                    this.codeState.remove(lineNumber);
//                    this.codeToint();
//                    break;
//                }
//                // Line number mismatch: multiple parallel reconstructions
//                else{
//                    // Check for void object to call multiple reconstructions
//                    this.codeState.clear();
//                    break;
//                }
            case "MOVE":
//                System.out.println("MOVE! Line number: "+ lineNumber);
//                System.out.println("MOVE! Line number2: "+ toLineNumber);
                if(lineNumber >= 0) {
                    try {
                        this.codeState.remove(lineNumber);
                    }catch (Exception e){
                        System.out.println("Invalid reorder operation encountered. Size of state is: " + codeState.size() +
                                " Tried to remove line: "+ lineNumber);
                    }
                    if(toLineNumber>codeState.size())
                        this.codeState.add(code);
                    else
                        this.codeState.add(toLineNumber, code);
                }
                else{
                    System.out.println("Error doing the reorder operation. Check ParsonState Constructor-MOVE." );
                    currentCode.printState();
                    System.exit(1);
                }
                // Get the integer representation of the code and store it as an array.
                this.codeToint();
                break;
                default:
                    System.out.println("Cannot determine the move action in Parameterized Constructor.");
                    System.exit(1);

        }

    }

    // *********************************************************
    // ******************* Copy Constructor ********************
    // *********************************************************
    ParsonsState(ParsonsState currentState)
    {
        // Copying over the same state to the next state.
        for(int i = 0; i < currentState.codeState.size(); i++)
            this.codeState.add(currentState.codeState.get(i));

        for(int i = 0; i < currentState.arrayRepresentation.size(); i++)
            this.arrayRepresentation.add(currentState.arrayRepresentation.get(i));
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
     Print method that prints the state of a ParsonsState object. Used for debugging purposes.
     */
    public void printState(){
        // Print the content of the vector of an ParsonsState object.
//        System.out.println("HERE: " + codeState.size());
        for(int i = 0; i<codeState.size();i++)
            System.out.println(codeState.get(i));
    }

    /**
     Getter method that gets the state of a ParsonsState object.
     */
    private Vector<String> getState(){
        return this.codeState;
    }

    /**
     Getter method that gets the size of a ParsonsState object.
     */
    public Integer getSize() {
        return this.codeState.size();
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

//        System.out.print("[");
//        for(int i = 0; i < arrayRepresentation.size(); i++) {
//            System.out.print(tempArrayRep[i] + ",");
//        }
//        System.out.println("]");


        return tempArrayRep;
    }


    /**
     Method to convert code to integer representation based on the codeIntegerRepresentation.
     */
    public void codeToint(){
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
     Mutator that adds code to codeState of a ParsonsState objcet.
     */
    public void addCode(String code)
    {
        this.codeState.add(code);
    }

    /**
     Mutator that adds integer representation of a code.
     */
    public void addCodeRepresentation(String code, Integer num)
    {
        this.codeIntegerRepresentation.put(code,num);
    }



    /**
     Mutator that adds integer representation of a code.
     */
    public Integer getEditDistance(ParsonsState correctCode)
    {
        Integer editDistance = ParsonDistance.parsonDistance(this.getArrayRepresentation(),correctCode.getArrayRepresentation());
        return editDistance;
    }

    /**
     Method to assemble correct solution for the given template number.
     */
    public void getCorrectSolutionforTemplate(Vector<String> initialCode, Vector<String> scrambledCode,
                                                      String templateNumber, String probLanguage){
        for(int i=0; i<initialCode.size();i++)
            this.codeState.add(initialCode.get(i));

        boolean flag = false;
//        System.out.println("HELLOOO!!!!!");
//        for (int i = 0; i<scrambledCode.size(); i++){
//            System.out.println(scrambledCode.get(i));
//        }

        // Pattern holders to find variables.
        String INC_TIME_SPENT_PATTERN = "^Total time taken for this session: (.*)$";
        String DECLARATION_PATTERN = "^// Declare (.*)$";
        String READ_PATTERN = "^// Read";

        // Variables to hold:
        // program variable names
        String firstVar = "";
        String secondVar = "";
        String firstVarDec = "";
        String secondVarDec = "";
        String firstInput = "";
        String secondInput = "";
        String firstOutput = "";
        String secondOutput = "";
        String ifStatement = "";
        // TEMPLATE 2105
        String ifStatement90 = "";
        String ifStatement80 = "";
        String ifStatement70 = "";
        String ifStatement55 = "";
        String output3 = "";
        String output6 = "";
        String output9 = "";
        String output12 = "";
        // TEMPLATE 2100
        String ifStatement3 = "";
        String ifStatement6 = "";
        String ifStatement9 = "";
        String assignA = "";
        String assignB = "";
        String assignC = "";
        String assignD = "";
        String assignF = "";
        // TEMPLATE 2101
        String ifStatement1 = "";
        String ifStatement2 = "";
        String ifStatement4 = "";
        String output1 = "";
        String output2 = "";
        String output4 = "";
        // GENERAL
        String firstPrompt = "";
        String secondPrompt = "";
        String openBraces = "";
        String closedBraces = "";
        String elseStat = "";
        String endIf = "";
        String endElse = "";

        // Pattern matchers used for checking declaration names.
        Pattern pattern;
        Matcher matcher;

        if(templateNumber.equals("2005"))
        {
            if(probLanguage.equals("C++")){

                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(5));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C++2005");

                // Getting second variable name.
                matcher = pattern.matcher(this.codeState.get(6));
                if (matcher.matches())
                    secondVar = matcher.group(1).trim();
                else
                    System.out.println("Second variable name not found. Check getCorrectSolutionforTemplate. C++2005");


                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("cin")==false
                                && scrambledCode.get(i).contains("cout")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                            ifStatement = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("cin") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("cout") && scrambledCode.get(i).contains(";"))
                            firstOutput = scrambledCode.get(i);
                    }

                    // If it has second variable name
                    if(scrambledCode.get(i).contains(secondVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("cin")==false
                                && scrambledCode.get(i).contains("cout")==false
                                && scrambledCode.get(i).contains(";"))
                            secondVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                            ifStatement = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("cin") && scrambledCode.get(i).contains(";"))
                            secondInput = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("cout") && scrambledCode.get(i).contains(";"))
                            secondOutput = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("cout << \"Enter the first value\"; "))
                        firstPrompt = scrambledCode.get(i);
                    // Second prompt
                    if(scrambledCode.get(i).contains("cout << \"Enter the second value\";"))
                        secondPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(6,firstVarDec);
                this.codeState.add(8,secondVarDec);
                this.codeState.add(10,firstPrompt);
                this.codeState.add(11,firstInput);
                this.codeState.add(13,secondPrompt);
                this.codeState.add(14,secondInput);
                this.codeState.add(16,ifStatement);
                this.codeState.add(17,openBraces);
                this.codeState.add(18,firstOutput);
                this.codeState.add(19,endIf);
                this.codeState.add(20,elseStat);
                this.codeState.add(21,openBraces);
                this.codeState.add(22,secondOutput);
                this.codeState.add(23,endElse);

                flag = true;
            }
            if(probLanguage.equals("JAVA"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(7));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. JAVA2005");

                // Getting second variable name.
                matcher = pattern.matcher(this.codeState.get(8));
                if (matcher.matches())
                    secondVar = matcher.group(1).trim();
                else
                    System.out.println("Second variable name not found. Check getCorrectSolutionforTemplate. JAVA2005");


                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("stdin")==false
                                && scrambledCode.get(i).contains("System.out.print")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                            ifStatement = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("stdin") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("System.out.print") && scrambledCode.get(i).contains(";"))
                            firstOutput = scrambledCode.get(i);
                    }

                    // If it has second variable name
                    if(scrambledCode.get(i).contains(secondVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("stdin")==false
                                && scrambledCode.get(i).contains("System.out.print")==false
                                && scrambledCode.get(i).contains(";"))
                            secondVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                            ifStatement = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("stdin") && scrambledCode.get(i).contains(";"))
                            secondInput = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("System.out.print") && scrambledCode.get(i).contains(";"))
                            secondOutput = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("System.out.print( \"Enter the first value\");"))
                        firstPrompt = scrambledCode.get(i);
                    // Second prompt
                    if(scrambledCode.get(i).contains("System.out.print( \"Enter the second value\");"))
                        secondPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(8,firstVarDec);
                this.codeState.add(10,secondVarDec);
                this.codeState.add(12,firstPrompt);
                this.codeState.add(13,firstInput);
                this.codeState.add(15,secondPrompt);
                this.codeState.add(16,secondInput);
                this.codeState.add(18,ifStatement);
                this.codeState.add(19,openBraces);
                this.codeState.add(20,firstOutput);
                this.codeState.add(21,endIf);
                this.codeState.add(22,elseStat);
                this.codeState.add(23,openBraces);
                this.codeState.add(24,secondOutput);
                this.codeState.add(25,endElse);

                flag = true;
            }
            if(probLanguage.equals("C"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(4));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. JAVA2005");

                // Getting second variable name.
                matcher = pattern.matcher(this.codeState.get(5));
                if (matcher.matches())
                    secondVar = matcher.group(1).trim();
                else
                    System.out.println("Second variable name not found. Check getCorrectSolutionforTemplate. JAVA2005");


                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("scanf(")==false
                                && scrambledCode.get(i).contains("printf(")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<"))
                            ifStatement = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("scanf(") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("printf(") && scrambledCode.get(i).contains(";"))
                            firstOutput = scrambledCode.get(i);
                    }

                    // If it has second variable name
                    if(scrambledCode.get(i).contains(secondVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("scanf(")==false
                                && scrambledCode.get(i).contains("printf(")==false
                                && scrambledCode.get(i).contains(";"))
                            secondVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<"))
                            ifStatement = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("scanf(") && scrambledCode.get(i).contains(";"))
                            secondInput = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("printf(") && scrambledCode.get(i).contains(";"))
                            secondOutput = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("printf( \"%d\", \"Enter the first value\" );"))
                        firstPrompt = scrambledCode.get(i);
                    // Second prompt
                    if(scrambledCode.get(i).contains("printf( \"%d\", \"Enter the second value\" );"))
                        secondPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(5,firstVarDec);
                this.codeState.add(7,secondVarDec);
                this.codeState.add(9,firstPrompt);
                this.codeState.add(10,firstInput);
                this.codeState.add(12,secondPrompt);
                this.codeState.add(13,secondInput);
                this.codeState.add(15,ifStatement);
                this.codeState.add(16,openBraces);
                this.codeState.add(17,firstOutput);
                this.codeState.add(18,endIf);
                this.codeState.add(19,elseStat);
                this.codeState.add(20,openBraces);
                this.codeState.add(21,secondOutput);
                this.codeState.add(22,endElse);

                flag = true;
            }
            if(probLanguage.equals("C#"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(8));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C#2005");

                // Getting second variable name.
                matcher = pattern.matcher(this.codeState.get(9));
                if (matcher.matches())
                    secondVar = matcher.group(1).trim();
                else
                    System.out.println("Second variable name not found. Check getCorrectSolutionforTemplate. C#2005");


                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("Console.ReadLine()")==false
                                && scrambledCode.get(i).contains("Console.Write(")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                            ifStatement = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("Console.ReadLine()") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("Console.Write(") && scrambledCode.get(i).contains(";"))
                            firstOutput = scrambledCode.get(i);
                    }

                    // If it has second variable name
                    if(scrambledCode.get(i).contains(secondVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("Console.ReadLine()")==false
                                && scrambledCode.get(i).contains("Console.Write(")==false
                                && scrambledCode.get(i).contains(";"))
                            secondVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                            ifStatement = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("Console.ReadLine()") && scrambledCode.get(i).contains(";"))
                            secondInput = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("Console.Write(") && scrambledCode.get(i).contains(";"))
                            secondOutput = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("Console.Write( \"Enter the first value\" );"))
                        firstPrompt = scrambledCode.get(i);
                    // Second prompt
                    if(scrambledCode.get(i).contains("Console.Write( \"Enter the second value\" );"))
                        secondPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(9,firstVarDec);
                this.codeState.add(11,secondVarDec);
                this.codeState.add(13,firstPrompt);
                this.codeState.add(14,firstInput);
                this.codeState.add(16,secondPrompt);
                this.codeState.add(17,secondInput);
                this.codeState.add(19,ifStatement);
                this.codeState.add(20,openBraces);
                this.codeState.add(21,firstOutput);
                this.codeState.add(22,endIf);
                this.codeState.add(23,elseStat);
                this.codeState.add(24,openBraces);
                this.codeState.add(25,secondOutput);
                this.codeState.add(26,endElse);

                flag = true;
            }
        }
        if(templateNumber.equals("2105"))
        {
            if(probLanguage.equals("C++")){

                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(5));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C++2005");

                // Getting second variable name.
                matcher = pattern.matcher(this.codeState.get(6));
                if (matcher.matches())
                    secondVar = matcher.group(1).trim();
                else
                    System.out.println("Second variable name not found. Check getCorrectSolutionforTemplate. C++2005");


                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("cin")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                        {
                            if(scrambledCode.get(i).contains(">= 90"))
                                ifStatement90 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains(">= 80"))
                                ifStatement80 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains(">= 70"))
                                ifStatement70 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains(">= 55"))
                                ifStatement55 = scrambledCode.get(i);

                        }
                        else if(scrambledCode.get(i).contains("cin") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // If it has second variable name
                    if(scrambledCode.get(i).contains(secondVar)){

                        // If it is the declaration of second variable, append it:
                        if(scrambledCode.get(i).contains("=")==false
                                && scrambledCode.get(i).contains("cout")==false
                                && scrambledCode.get(i).contains(";"))
                            secondVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("=") && scrambledCode.get(i).contains(";"))
                        {
                            if(scrambledCode.get(i).contains("= 'A';"))
                                assignA = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("= 'B';"))
                                assignB = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("= 'C';"))
                                assignC = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("= 'D';"))
                                assignD = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("= 'F';"))
                                assignF = scrambledCode.get(i);
                        }
                        else if(scrambledCode.get(i).contains("cout") && scrambledCode.get(i).contains(";"))
                            secondOutput = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("cout << \"Enter the numerical grade\";"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(6,firstVarDec);
                this.codeState.add(8,secondVarDec);
                this.codeState.add(10,firstPrompt);
                this.codeState.add(11,firstInput);
                this.codeState.add(13,ifStatement90);
                this.codeState.add(14,openBraces);
                this.codeState.add(15,assignA);
                this.codeState.add(16,endIf);
                this.codeState.add(17,elseStat);
                this.codeState.add(18,ifStatement80);
                this.codeState.add(19,openBraces);
                this.codeState.add(20,assignB);
                this.codeState.add(21,endIf);
                this.codeState.add(22,elseStat);
                this.codeState.add(23,ifStatement70);
                this.codeState.add(24,openBraces);
                this.codeState.add(25,assignC);
                this.codeState.add(26,endIf);
                this.codeState.add(27,elseStat);
                this.codeState.add(28,ifStatement55);
                this.codeState.add(29,openBraces);
                this.codeState.add(30,assignD);
                this.codeState.add(31,endIf);
                this.codeState.add(32,elseStat);
                this.codeState.add(33,openBraces);
                this.codeState.add(34,assignF);
                this.codeState.add(35,endElse);
                this.codeState.add(37,secondOutput);

                flag = true;
            }
            if(probLanguage.equals("JAVA"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(7));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C#2105");

                // Getting second variable name.
                matcher = pattern.matcher(this.codeState.get(8));
                if (matcher.matches())
                    secondVar = matcher.group(1).trim();
                else
                    System.out.println("Second variable name not found. Check getCorrectSolutionforTemplate. C#2105");


                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("stdin")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                        {
                            if(scrambledCode.get(i).contains("90"))
                                ifStatement90 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("80"))
                                ifStatement80 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("70"))
                                ifStatement70 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("55"))
                                ifStatement55 = scrambledCode.get(i);
                        }
                        else if(scrambledCode.get(i).contains("stdin") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // If it has second variable name
                    if(scrambledCode.get(i).contains(secondVar)){

                        // If it is the declaration of second variable, append it:
                        if(scrambledCode.get(i).contains("=")==false
                                && scrambledCode.get(i).contains("System.out.print")==false
                                && scrambledCode.get(i).contains(";"))
                            secondVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("=") && scrambledCode.get(i).contains(";"))
                        {
                            if(scrambledCode.get(i).contains("A"))
                                assignA = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("B"))
                                assignB = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("C"))
                                assignC = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("D"))
                                assignD = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("F"))
                                assignF = scrambledCode.get(i);
                        }
                        else if(scrambledCode.get(i).contains("System.out.print") && scrambledCode.get(i).contains(";"))
                            secondOutput = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("System.out.print( \"Enter the numerical grade\");"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(8,firstVarDec);
                this.codeState.add(10,secondVarDec);
                this.codeState.add(12,firstPrompt);
                this.codeState.add(13,firstInput);
                this.codeState.add(15,ifStatement90);
                this.codeState.add(16,openBraces);
                this.codeState.add(17,assignA);
                this.codeState.add(18,endIf);
                this.codeState.add(19,elseStat);
                this.codeState.add(20,ifStatement80);
                this.codeState.add(21,openBraces);
                this.codeState.add(22,assignB);
                this.codeState.add(23,endIf);
                this.codeState.add(24,elseStat);
                this.codeState.add(25,ifStatement70);
                this.codeState.add(26,openBraces);
                this.codeState.add(27,assignC);
                this.codeState.add(28,endIf);
                this.codeState.add(29,elseStat);
                this.codeState.add(30,ifStatement55);
                this.codeState.add(31,openBraces);
                this.codeState.add(32,assignD);
                this.codeState.add(33,endIf);
                this.codeState.add(34,elseStat);
                this.codeState.add(35,openBraces);
                this.codeState.add(36,assignF);
                this.codeState.add(37,endElse);
                this.codeState.add(39,secondOutput);

                flag = true;
            }
            if(probLanguage.equals("C"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(4));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C#2105");

                // Getting second variable name.
                matcher = pattern.matcher(this.codeState.get(5));
                if (matcher.matches())
                    secondVar = matcher.group(1).trim();
                else
                    System.out.println("Second variable name not found. Check getCorrectSolutionforTemplate. C#2105");


                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("scanf(")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                        {
                            if(scrambledCode.get(i).contains("90"))
                                ifStatement90 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("80"))
                                ifStatement80 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("70"))
                                ifStatement70 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("55"))
                                ifStatement55 = scrambledCode.get(i);
                        }
                        else if(scrambledCode.get(i).contains("scanf(") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // If it has second variable name
                    if(scrambledCode.get(i).contains(secondVar)){

                        // If it is the declaration of second variable, append it:
                        if(scrambledCode.get(i).contains("=")==false
                                && scrambledCode.get(i).contains("printf(")==false
                                && scrambledCode.get(i).contains(";"))
                            secondVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("=") && scrambledCode.get(i).contains(";"))
                        {
                            if(scrambledCode.get(i).contains("A"))
                                assignA = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("B"))
                                assignB = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("C"))
                                assignC = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("D"))
                                assignD = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("F"))
                                assignF = scrambledCode.get(i);
                        }
                        else if(scrambledCode.get(i).contains("printf(") && scrambledCode.get(i).contains(";"))
                            secondOutput = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("printf( \"%d\", \"Enter the numerical grade\" );"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(5,firstVarDec);
                this.codeState.add(7,secondVarDec);
                this.codeState.add(9,firstPrompt);
                this.codeState.add(10,firstInput);
                this.codeState.add(12,ifStatement90);
                this.codeState.add(13,openBraces);
                this.codeState.add(14,assignA);
                this.codeState.add(15,endIf);
                this.codeState.add(16,elseStat);
                this.codeState.add(17,ifStatement80);
                this.codeState.add(18,openBraces);
                this.codeState.add(19,assignB);
                this.codeState.add(20,endIf);
                this.codeState.add(21,elseStat);
                this.codeState.add(22,ifStatement70);
                this.codeState.add(23,openBraces);
                this.codeState.add(24,assignC);
                this.codeState.add(25,endIf);
                this.codeState.add(26,elseStat);
                this.codeState.add(27,ifStatement55);
                this.codeState.add(28,openBraces);
                this.codeState.add(29,assignD);
                this.codeState.add(30,endIf);
                this.codeState.add(31,elseStat);
                this.codeState.add(32,openBraces);
                this.codeState.add(33,assignF);
                this.codeState.add(34,endElse);
                this.codeState.add(36,secondOutput);

                flag = true;
            }
            if(probLanguage.equals("C#"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(8));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C#2105");

                // Getting second variable name.
                matcher = pattern.matcher(this.codeState.get(9));
                if (matcher.matches())
                    secondVar = matcher.group(1).trim();
                else
                    System.out.println("Second variable name not found. Check getCorrectSolutionforTemplate. C#2105");


                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){

                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("Console.ReadLine()")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if"))
                        {
                            if(scrambledCode.get(i).contains("90"))
                                ifStatement90 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("80"))
                                ifStatement80 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("70"))
                                ifStatement70 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("55"))
                                ifStatement55 = scrambledCode.get(i);
                        }
                        else if(scrambledCode.get(i).contains("Console.ReadLine()") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // If it has second variable name
                    if(scrambledCode.get(i).contains(secondVar)){

                        // If it is the declaration of second variable, append it:
                        if(scrambledCode.get(i).contains("=")==false
                                && scrambledCode.get(i).contains("Console.Write(")==false
                                && scrambledCode.get(i).contains(";"))
                            secondVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("=") && scrambledCode.get(i).contains(";"))
                        {
                            if(scrambledCode.get(i).contains("A"))
                                assignA = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("B"))
                                assignB = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("C"))
                                assignC = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("D"))
                                assignD = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("F"))
                                assignF = scrambledCode.get(i);
                        }
                        else if(scrambledCode.get(i).contains("Console.Write(") && scrambledCode.get(i).contains(";"))
                            secondOutput = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("Console.Write( \"Enter the numerical grade\" );"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(9,firstVarDec);
                this.codeState.add(11,secondVarDec);
                this.codeState.add(13,firstPrompt);
                this.codeState.add(14,firstInput);
                this.codeState.add(16,ifStatement90);
                this.codeState.add(17,openBraces);
                this.codeState.add(18,assignA);
                this.codeState.add(19,endIf);
                this.codeState.add(20,elseStat);
                this.codeState.add(21,ifStatement80);
                this.codeState.add(22,openBraces);
                this.codeState.add(23,assignB);
                this.codeState.add(24,endIf);
                this.codeState.add(25,elseStat);
                this.codeState.add(26,ifStatement70);
                this.codeState.add(27,openBraces);
                this.codeState.add(28,assignC);
                this.codeState.add(29,endIf);
                this.codeState.add(30,elseStat);
                this.codeState.add(31,ifStatement55);
                this.codeState.add(32,openBraces);
                this.codeState.add(33,assignD);
                this.codeState.add(34,endIf);
                this.codeState.add(35,elseStat);
                this.codeState.add(36,openBraces);
                this.codeState.add(37,assignF);
                this.codeState.add(38,endElse);
                this.codeState.add(40,secondOutput);

                flag = true;
            }

        }
        if(templateNumber.equals("2100"))
        {
            if(probLanguage.equals("C++")){
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(5));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C++2100");

                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("cin")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<="))
                        {
                            if(scrambledCode.get(i).contains("3"))
                                ifStatement3 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("6"))
                                ifStatement6 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("9"))
                                ifStatement9 = scrambledCode.get(i);

                        }
                        else if(scrambledCode.get(i).contains("cin") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // Month output
                    if(scrambledCode.get(i).contains("cout")
                            && scrambledCode.get(i).contains(";")
                            && scrambledCode.get(i).contains("<<"))
                    {
                        if(scrambledCode.get(i).contains("January") || scrambledCode.get(i).contains("March"))
                            output3 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("April") || scrambledCode.get(i).contains("June"))
                            output6 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("July") || scrambledCode.get(i).contains("September"))
                            output9 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("October") || scrambledCode.get(i).contains("December"))
                            output12 = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("cout << \"Enter the number of the month (1-12)\";"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(6,firstVarDec);
                this.codeState.add(8,firstPrompt);
                this.codeState.add(9,firstInput);
                this.codeState.add(11,ifStatement3);
                this.codeState.add(12,openBraces);
                this.codeState.add(13,output3);
                this.codeState.add(14,endIf);
                this.codeState.add(15,elseStat);
                this.codeState.add(16,ifStatement6);
                this.codeState.add(17,openBraces);
                this.codeState.add(18,output6);
                this.codeState.add(19,endIf);
                this.codeState.add(20,elseStat);
                this.codeState.add(21,ifStatement9);
                this.codeState.add(22,openBraces);
                this.codeState.add(23,output9);
                this.codeState.add(24,endIf);
                this.codeState.add(25,elseStat);
                this.codeState.add(26,openBraces);
                this.codeState.add(27,output12);
                this.codeState.add(28,endElse);

                flag = true;
            }
            if(probLanguage.equals("JAVA"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(7));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C++2100");

                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("stdin.")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<="))
                        {
                            if(scrambledCode.get(i).contains("3"))
                                ifStatement3 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("6"))
                                ifStatement6 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("9"))
                                ifStatement9 = scrambledCode.get(i);

                        }
                        else if(scrambledCode.get(i).contains("stdin.") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // Month output
                    if(scrambledCode.get(i).contains("System.out.print(")
                            && scrambledCode.get(i).contains(";"))
                    {
                        if(scrambledCode.get(i).contains("January") || scrambledCode.get(i).contains("March"))
                            output3 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("April") || scrambledCode.get(i).contains("June"))
                            output6 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("July") || scrambledCode.get(i).contains("September"))
                            output9 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("October") || scrambledCode.get(i).contains("December"))
                            output12 = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("System.out.print( \"Enter the number of the month (1-12)\");"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(8,firstVarDec);
                this.codeState.add(10,firstPrompt);
                this.codeState.add(11,firstInput);
                this.codeState.add(13,ifStatement3);
                this.codeState.add(14,openBraces);
                this.codeState.add(15,output3);
                this.codeState.add(16,endIf);
                this.codeState.add(17,elseStat);
                this.codeState.add(18,ifStatement6);
                this.codeState.add(19,openBraces);
                this.codeState.add(20,output6);
                this.codeState.add(21,endIf);
                this.codeState.add(22,elseStat);
                this.codeState.add(23,ifStatement9);
                this.codeState.add(24,openBraces);
                this.codeState.add(25,output9);
                this.codeState.add(26,endIf);
                this.codeState.add(27,elseStat);
                this.codeState.add(28,openBraces);
                this.codeState.add(29,output12);
                this.codeState.add(30,endElse);

                flag = true;
            }
            if(probLanguage.equals("C"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(4));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C2100");

                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("scanf(")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<="))
                        {
                            if(scrambledCode.get(i).contains("3"))
                                ifStatement3 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("6"))
                                ifStatement6 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("9"))
                                ifStatement9 = scrambledCode.get(i);

                        }
                        else if(scrambledCode.get(i).contains("scanf(") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // Month output
                    if(scrambledCode.get(i).contains("printf")
                    && scrambledCode.get(i).contains(";")
                    && scrambledCode.get(i).contains("%"))
                    {
                        if(scrambledCode.get(i).contains("January") || scrambledCode.get(i).contains("March"))
                            output3 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("April") || scrambledCode.get(i).contains("June"))
                            output6 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("July") || scrambledCode.get(i).contains("September"))
                            output9 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("October") || scrambledCode.get(i).contains("December"))
                            output12 = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("printf( \"%d\", \"Enter the number of the month (1-12)\" );"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(5,firstVarDec);
                this.codeState.add(7,firstPrompt);
                this.codeState.add(8,firstInput);
                this.codeState.add(10,ifStatement3);
                this.codeState.add(11,openBraces);
                this.codeState.add(12,output3);
                this.codeState.add(13,endIf);
                this.codeState.add(14,elseStat);
                this.codeState.add(15,ifStatement6);
                this.codeState.add(16,openBraces);
                this.codeState.add(17,output6);
                this.codeState.add(18,endIf);
                this.codeState.add(19,elseStat);
                this.codeState.add(20,ifStatement9);
                this.codeState.add(21,openBraces);
                this.codeState.add(22,output9);
                this.codeState.add(23,endIf);
                this.codeState.add(24,elseStat);
                this.codeState.add(25,openBraces);
                this.codeState.add(26,output12);
                this.codeState.add(27,endElse);

                flag = true;
            }
            if(probLanguage.equals("C#"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(8));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C++2100");

                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("Console.ReadLine()")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<="))
                        {
                            if(scrambledCode.get(i).contains("3"))
                                ifStatement3 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("6"))
                                ifStatement6 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("9"))
                                ifStatement9 = scrambledCode.get(i);

                        }
                        else if(scrambledCode.get(i).contains("Console.ReadLine()") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // Month output
                    if(scrambledCode.get(i).contains("Console.Write(")
                            && scrambledCode.get(i).contains(";"))
                    {
                        if(scrambledCode.get(i).contains("January") || scrambledCode.get(i).contains("March"))
                            output3 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("April") || scrambledCode.get(i).contains("June"))
                            output6 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("July") || scrambledCode.get(i).contains("September"))
                            output9 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("October") || scrambledCode.get(i).contains("December"))
                            output12 = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("Console.Write( \"Enter the number of the month (1-12)\" );"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(9,firstVarDec);
                this.codeState.add(11,firstPrompt);
                this.codeState.add(12,firstInput);
                this.codeState.add(14,ifStatement3);
                this.codeState.add(15,openBraces);
                this.codeState.add(16,output3);
                this.codeState.add(17,endIf);
                this.codeState.add(18,elseStat);
                this.codeState.add(19,ifStatement6);
                this.codeState.add(20,openBraces);
                this.codeState.add(21,output6);
                this.codeState.add(22,endIf);
                this.codeState.add(23,elseStat);
                this.codeState.add(24,ifStatement9);
                this.codeState.add(25,openBraces);
                this.codeState.add(26,output9);
                this.codeState.add(27,endIf);
                this.codeState.add(28,elseStat);
                this.codeState.add(29,openBraces);
                this.codeState.add(30,output12);
                this.codeState.add(31,endElse);

                flag = true;
            }

        }
        if(templateNumber.equals("2101"))
        {
            if(probLanguage.equals("C++")){
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(5));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C++2100");

                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("cin")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<="))
                        {
                            if(scrambledCode.get(i).contains("90"))
                                ifStatement1 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("180"))
                                ifStatement2 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("270"))
                                ifStatement3 = scrambledCode.get(i);

                        }
                        else if(scrambledCode.get(i).contains("cin") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // Month output
                    if(scrambledCode.get(i).contains("cout")
                            && scrambledCode.get(i).contains(";")
                            && scrambledCode.get(i).contains("<<"))
                    {
                        if(scrambledCode.get(i).contains("First quadrant"))
                            output1 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Second quadrant"))
                            output2 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Third quadrant"))
                            output3 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Fourth quadrant"))
                            output4 = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("cout << \"Enter the location in degrees (1-360)\";"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(6,firstVarDec);
                this.codeState.add(8,firstPrompt);
                this.codeState.add(9,firstInput);
                this.codeState.add(11,ifStatement1);
                this.codeState.add(12,openBraces);
                this.codeState.add(13,output1);
                this.codeState.add(14,endIf);
                this.codeState.add(15,elseStat);
                this.codeState.add(16,ifStatement2);
                this.codeState.add(17,openBraces);
                this.codeState.add(18,output2);
                this.codeState.add(19,endIf);
                this.codeState.add(20,elseStat);
                this.codeState.add(21,ifStatement3);
                this.codeState.add(22,openBraces);
                this.codeState.add(23,output3);
                this.codeState.add(24,endIf);
                this.codeState.add(25,elseStat);
                this.codeState.add(26,openBraces);
                this.codeState.add(27,output4);
                this.codeState.add(28,endElse);

                flag = true;
            }
            if(probLanguage.equals("JAVA"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(7));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C++2100");

                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("stdin.")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<="))
                        {
                            if(scrambledCode.get(i).contains("90"))
                                ifStatement1 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("180"))
                                ifStatement2 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("270"))
                                ifStatement3 = scrambledCode.get(i);

                        }
                        else if(scrambledCode.get(i).contains("stdin.") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // Quadrant output
                    if(scrambledCode.get(i).contains("System.out.print(")
                            && scrambledCode.get(i).contains(";"))
                    {
                        if(scrambledCode.get(i).contains("First quadrant"))
                            output1 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Second quadrant"))
                            output2 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Third quadrant"))
                            output3 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Fourth quadrant"))
                            output4 = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("System.out.print( \"Enter the location in degrees (1-360)\");"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(8,firstVarDec);
                this.codeState.add(10,firstPrompt);
                this.codeState.add(11,firstInput);
                this.codeState.add(13,ifStatement1);
                this.codeState.add(14,openBraces);
                this.codeState.add(15,output1);
                this.codeState.add(16,endIf);
                this.codeState.add(17,elseStat);
                this.codeState.add(18,ifStatement2);
                this.codeState.add(19,openBraces);
                this.codeState.add(20,output2);
                this.codeState.add(21,endIf);
                this.codeState.add(22,elseStat);
                this.codeState.add(23,ifStatement3);
                this.codeState.add(24,openBraces);
                this.codeState.add(25,output3);
                this.codeState.add(26,endIf);
                this.codeState.add(27,elseStat);
                this.codeState.add(28,openBraces);
                this.codeState.add(29,output4);
                this.codeState.add(30,endElse);

                flag = true;
            }
            if(probLanguage.equals("C"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(4));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C2100");

                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("scanf(")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<="))
                        {
                            if(scrambledCode.get(i).contains("90"))
                                ifStatement1 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("180"))
                                ifStatement2 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("270"))
                                ifStatement3 = scrambledCode.get(i);

                        }
                        else if(scrambledCode.get(i).contains("scanf(") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // Month output
                    if(scrambledCode.get(i).contains("printf")
                            && scrambledCode.get(i).contains(";")
                            && scrambledCode.get(i).contains("%"))
                    {
                        if(scrambledCode.get(i).contains("First quadrant"))
                            output1 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Second quadrant"))
                            output2 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Third quadrant"))
                            output3 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Fourth quadrant"))
                            output4 = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("printf( \"%d\", \"Enter the location in degrees (1-360)\" );"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(5,firstVarDec);
                this.codeState.add(7,firstPrompt);
                this.codeState.add(8,firstInput);
                this.codeState.add(10,ifStatement1);
                this.codeState.add(11,openBraces);
                this.codeState.add(12,output1);
                this.codeState.add(13,endIf);
                this.codeState.add(14,elseStat);
                this.codeState.add(15,ifStatement2);
                this.codeState.add(16,openBraces);
                this.codeState.add(17,output2);
                this.codeState.add(18,endIf);
                this.codeState.add(19,elseStat);
                this.codeState.add(20,ifStatement3);
                this.codeState.add(21,openBraces);
                this.codeState.add(22,output3);
                this.codeState.add(23,endIf);
                this.codeState.add(24,elseStat);
                this.codeState.add(25,openBraces);
                this.codeState.add(26,output4);
                this.codeState.add(27,endElse);

                flag = true;
            }
            if(probLanguage.equals("C#"))
            {
                // Getting first variable name.
                pattern = Pattern.compile(DECLARATION_PATTERN);
                matcher = pattern.matcher(this.codeState.get(8));
                if (matcher.matches())
                    firstVar = matcher.group(1).trim();
                else
                    System.out.println("First variable name not found. Check getCorrectSolutionforTemplate. C++2100");

                // Get statements from scrambled code.
                for(int i = 0; i<scrambledCode.size(); i++)
                {
                    // If it has first variable name
                    if(scrambledCode.get(i).contains(firstVar)){
                        // If it is the declaration of first variable, append it:
                        if(scrambledCode.get(i).contains("if")==false
                                && scrambledCode.get(i).contains("Console.ReadLine()")==false
                                && scrambledCode.get(i).contains(";"))
                            firstVarDec = scrambledCode.get(i);
                        else if(scrambledCode.get(i).contains("if") && scrambledCode.get(i).contains("<="))
                        {
                            if(scrambledCode.get(i).contains("90"))
                                ifStatement1 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("180"))
                                ifStatement2 = scrambledCode.get(i);
                            if(scrambledCode.get(i).contains("270"))
                                ifStatement3 = scrambledCode.get(i);

                        }
                        else if(scrambledCode.get(i).contains("Console.ReadLine()") && scrambledCode.get(i).contains(";"))
                            firstInput = scrambledCode.get(i);
                    }

                    // Quadrant ouput
                    if(scrambledCode.get(i).contains("Console.Write(")
                            && scrambledCode.get(i).contains(";"))
                    {
                        if(scrambledCode.get(i).contains("First quadrant"))
                            output1 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Second quadrant"))
                            output2 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Third quadrant"))
                            output3 = scrambledCode.get(i);
                        if(scrambledCode.get(i).contains("Fourth quadrant"))
                            output4 = scrambledCode.get(i);
                    }

                    // First prompt
                    if(scrambledCode.get(i).contains("Console.Write( \"Enter the location in degrees (1-360)\" );"))
                        firstPrompt = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("{") && scrambledCode.get(i).length()==1)
                        openBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).contains("}") && scrambledCode.get(i).length()==1)
                        closedBraces = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("else"))
                        elseStat = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of if-clause"))
                        endIf = scrambledCode.get(i);
                    if(scrambledCode.get(i).equals("} // End of else-clause"))
                        endElse = scrambledCode.get(i);
                }

                // Appending scrambled lines to form correct solution:
                this.codeState.add(9,firstVarDec);
                this.codeState.add(11,firstPrompt);
                this.codeState.add(12,firstInput);
                this.codeState.add(14,ifStatement1);
                this.codeState.add(15,openBraces);
                this.codeState.add(16,output1);
                this.codeState.add(17,endIf);
                this.codeState.add(18,elseStat);
                this.codeState.add(19,ifStatement2);
                this.codeState.add(20,openBraces);
                this.codeState.add(21,output2);
                this.codeState.add(22,endIf);
                this.codeState.add(23,elseStat);
                this.codeState.add(24,ifStatement3);
                this.codeState.add(25,openBraces);
                this.codeState.add(26,output3);
                this.codeState.add(27,endIf);
                this.codeState.add(28,elseStat);
                this.codeState.add(29,openBraces);
                this.codeState.add(30,output4);
                this.codeState.add(31,endElse);

                flag = true;
            }
        }


        if(flag){
            // Update codeIntegerRepresentation
            // Sets the integer representation of the code.
            for(int i=0; i<this.codeState.size(); i++){
                addCodeRepresentation(this.codeState.get(i), i);
            }
        }
        else{

            // If template number is printed and it is one of the following, a complete solution has been misclassified as incomplete.
            if(templateNumber.equals("2005") == false
            && templateNumber.equals("2105") == false
            && templateNumber.equals("2100") == false
            && templateNumber.equals("2101") == false){
                System.out.println(templateNumber);
                return;

            }

            System.out.println("Error generating correct solution for an incomplete solution without Sorted Events.");
            System.exit(1);
        }
    }

}


//    // The log file name
//    // private static final String sourceFileName = "/Users/salilmaharjan/Desktop/kattis/kattis/PartialDemo5.txt";
//    private static final String sourceFileName = "/Users/salilmaharjan/Desktop/kattis/kattis/Raw_Test1.txt";
//
//    // Temporary flag for end of state in student assembled code that was manually constructed.
//    private static final String CODE_STATE_END = "-------END OF CURRENT STATE-------";

//    public static void main(String[] args)
//    {
//        // Correct solution section.
//        ParsonsState correctSolution = new ParsonsState();
//        correctSolution.extractCorrectCode();
//
//        System.out.println("Correct Solution: ");
//        System.out.println("------------------------------");
//        correctSolution.printState();
//        System.out.println("Code to integer representation: ");
//        System.out.println("------------------------------");
//        correctSolution.getCodeIntegerRepresentation();
//        System.out.println("Integer Array representation: ");
//        System.out.println("------------------------------");
//        correctSolution.getArrayRepresentation();
//
//        // Initial solution.
//        ParsonsState initialSolution = new ParsonsState();
//        initialSolution = initialSolution.extractInitialCode();
//
//        System.out.println("Initial Solution: ");
//        System.out.println("------------------------------");
//        initialSolution.printState();
//        System.out.println("Integer Array representation: ");
//        System.out.println("------------------------------");
//        initialSolution.getArrayRepresentation();
//
//
//        // Student Assembled Code section.
//        ParsonsState ps = new ParsonsState();
//        Vector<ParsonsState> vec;
//
//        vec =  ps.getStudentAssembledCode(initialSolution);
//        System.out.println(vec.size());
//        for(int i = 0; i < vec.size(); i++)
//        {
//            System.out.println("State: " + (i+1));
//            System.out.println("------------------------------");
//            vec.get(i).printState();
//            System.out.println("Integer Array representation: ");
//            vec.get(i).getArrayRepresentation();
//        }
//
////        // Testing copy constructor
////        ParsonsState copySolution = new ParsonsState(initialSolution);
////        System.out.println("Copied Solution: ");
////        System.out.println("------------------------------");
////        copySolution.printState();
////        System.out.println("Integer Array representation: ");
////        System.out.println("------------------------------");
////        copySolution.getArrayRepresentation();
//
//        System.out.println("------------------------------");
//        System.out.println("------------------------------");
//        System.out.println("EDIT DISTANCES:");
//
//        int[] editDistance = getEditDistance(vec, correctSolution);
//
//        // Debugging purpose:
//        System.out.println("EDIT DISTANCES IN MOVES:");
//        for(int j = 0; j<=vec.size()-1; j++)
//            System.out.println(
//                    "Move " + j + ":  " +
//                            editDistance[j]);
//
//    }
//    /**
//     Method to process the log file and create a Vector of ParsonsState objects of student assembled solution.
//     @return Vector of ParsonsState objects that holds the states of the student assembled solutions.
//     */
//    private Vector<ParsonsState> getStudentAssembledCode(ParsonsState initialSolution) {
//        // Vector of ParsonsState objects that has the student assembled code of each state
//        // Index 0 has the initial solution presented to the student.
//        Vector<ParsonsState> stateVector = new Vector<>();
//
//        // Reader to read log file
//        BufferedReader reader = null;
//        // buffer to read the statement from the log file
//        String buffer = null;
//
//        // Counter variable
//        int count = 0;
//        // Counter for the number of states in the log.
//        // 0 state is the initial solution presented to the student.
//        int state = 0;
//
//        // Buffer that holds line number of action taken.
//        Integer line_number1 = 0;
//        Integer line_number2 = 0;
//        // Buffer that holds code of the action taken.
//        String code = "" ;
//
//        // Pattern matchers used for checking actions.
//        Pattern pattern1, pattern2;
//        Matcher matcher1, matcher2;
//
//        //Appending the initial solution state to the vector and updating state count.
//        stateVector.add(initialSolution);
//        state++;
//
//        // Open log file to read and check if it opened correctly.
//        File file = new File(sourceFileName);
//        try {
//            reader = new BufferedReader(new FileReader(file));
//
//            // Getting past the log sections till the beginning of the student assembled code is reached.
//            // (Third section of the log)
//            while (count < SECTION_START_COUNT) {
//                buffer = reader.readLine();
//                if (buffer.contains(LOG_PROBLEM_SECTION_START))
//                    count++;
//            }
//        } catch (Exception e) {
//            System.err.println("The following error was caught: " + e.getMessage());
//            System.out.println("Could not open the file: " + sourceFileName);
//            System.exit(1);
//        }
//
//        try {
//            // Reading till we reach the end of student assembled code section
//            while (buffer.contains(LOG_PROBLEM_SECTION_END) == false) {
//                buffer = reader.readLine();
//
//                // Checking for valid/invalid moves and checking the flag if it should be considered or not.
//                if(buffer.contains(INVALID_MOVE_INDICATOR) == true && countIgnoredSteps == false) {
//                    // To skip invalid moves, we read the next line which contains the beginning of the student code solution.
//                    // We continue with the loop as once we skipped the beginning, the rest won't be considered as a state in the program.
//                    buffer = reader.readLine();
//                    continue;
//                }
//
//                // CHECKING FOR MOVE ACTION FROM
//                // PROBLEM TO TRASH OR
//                // TRASH TO PROBLEM OR
//                // ATTEMPTED MOVES (WHEN countIgnoredSteps is TRUE and we count all valid/invalid moves.
//                // Otherwise it is removed in the previous if check. )
//                // The pattern of the line where action log can be found.
//                pattern1 = Pattern.compile(MOVE_PROBLEM_TRASH);
//                matcher1 = pattern1.matcher(buffer);
//                pattern2 = Pattern.compile(MOVE_TRASH_PROBLEM);
//                matcher2 = pattern2.matcher(buffer);
//                // If true copy the current state as the next state using the copy constructor.
//                if (matcher1.matches() || matcher2.matches()) {
//                    // Parsing out line number and code.
////                    if(matcher1.matches()){
////                        // Get the code and add it to resultVector
////                        line_number1 = Integer.parseInt(matcher1.group(1).trim());
////                        code = matcher1.group(2).trim();
////                    }
////                    if (matcher2.matches()) {
////                        // Get the code and add it to resultVector
////                        line_number1 = Integer.parseInt(matcher2.group(1).trim());
////                        code = matcher2.group(2).trim();
////                    }
//
//                    // Copy the current state to new state and append to vector.
//                    ParsonsState temp_state = new ParsonsState(stateVector.get(state-1));
//                    stateVector.add(temp_state);
//                    state++;
//
//                    continue;
//                }
//
//                // CHECKING FOR MOVE ACTION FROM
//                // PROBLEM TO SOLUTION OR
//                // TRASH TO SOLUTION
//                // The pattern of the line where action log can be found.
//                pattern1 = Pattern.compile(MOVE_PROBLEM_SOLUTION);
//                matcher1 = pattern1.matcher(buffer);
//                pattern2 = Pattern.compile(MOVE_TRASH_SOLUTION);
//                matcher2 = pattern2.matcher(buffer);
//                // Extract the code from the event if true and
//                // Add the code line in action to the next state.
//                if (matcher1.matches() || matcher2.matches()) {
//                    // MOVE_PROBLEM_SOLUTION
//                    if(matcher1.matches()){
//                        // Get the code and add it to resultVector
//                        line_number1 = Integer.parseInt(matcher1.group(1).trim());
//                        code = matcher1.group(2).trim();
//                    }
//                    // MOVE_TRASH_SOLUTION
//                    if(matcher2.matches()){
//                        // Get the code and add it to resultVector
//                        line_number1 = Integer.parseInt(matcher2.group(1).trim());
//                        code = matcher2.group(2).trim();
//                    }
//
//                    // Add code to state at (line_number1-1) and append state to vector.
//                    ParsonsState temp_state = new ParsonsState(stateVector.get(state-1), code, "ADD", (line_number1-1), Integer.MAX_VALUE);
//                    stateVector.add(temp_state);
//                    state++;
//
//                    continue;
//                }
//
//                // CHECKING FOR MOVE ACTION FROM
//                // SOLUTION TO PROBLEM OR
//                // SOLUTION TO TRASH
//                // The pattern of the line where action log can be found.
//                pattern1 = Pattern.compile(MOVE_SOLUTION_PROBLEM);
//                matcher1 = pattern1.matcher(buffer);
//                pattern2 = Pattern.compile(MOVE_SOLUTION_TRASH);
//                matcher2 = pattern2.matcher(buffer);
//                // Extract the code from the event if true and
//                // Remove the code line in action from the next state.
//                if (matcher1.matches() || matcher2.matches()) {
//                    if (matcher1.matches()) {
//                        // Get the code and add it to resultVector
//                        line_number1 = Integer.parseInt(matcher1.group(1).trim());
//                        code = matcher1.group(2).trim();
//                    }
//                    if (matcher2.matches()) {
//                        // Get the code and add it to resultVector
//                        line_number1 = Integer.parseInt(matcher2.group(1).trim());
//                        code = matcher2.group(2).trim();
//                    }
//
//                    // Remove the code line from state and append to vector.
//                    ParsonsState temp_state = new ParsonsState(stateVector.get(state-1), code, "REMOVE", (line_number1-1), Integer.MAX_VALUE);
//                    stateVector.add(temp_state);
//                    state++;
//
//                    continue;
//
//                }
//
//                // CHECKING FOR REORDER ACTION
//                // The pattern of the line where action log can be found.
//                pattern1 = Pattern.compile(REORDER_ACTION);
//                matcher1 = pattern1.matcher(buffer);
//                // Extract the code from the event if true and
//                // Remove the code line in action from the next state.
//                if( matcher1.matches() ) {
//                    // Get the code and add it to resultVector
//                    line_number1 = Integer.parseInt(matcher1.group(1).trim());
//                    line_number2 = Integer.parseInt(matcher1.group(2).trim());
//                    code = matcher1.group(3).trim();
//
//                    // Move the code line in state and append to vector.
//                    ParsonsState temp_state = new ParsonsState(stateVector.get(state-1), code, "MOVE", (line_number1-1), (line_number2-1));
//                    stateVector.add(temp_state);
//                    state++;
//
//                    continue;
//                }
//
//            }
//        } catch (Exception e) {
//            System.err.println("The following error was caught: " + e.getMessage());
//            System.out.println("Could not read line at state: " + state + " and line: " + count);
//            System.exit(1);
//        }
//
//        return stateVector;
//    }


//    /**
//     Method to extract the correct code from log. Updates the object that calls this function with the correct solution.
//     ALSO updates the hashmap codeIntegerRepresentation with integer representation for the code lines.
//     */
//    private void extractCorrectCode() {
//        // Reader to read log file
//        BufferedReader reader = null;
//
//        // Buffer to read the statement from the log file
//        String buffer = null;
//
//        // Buffer to store only the code part from processing the sorted event log.
//        String code;
//
//        // Counter variable to keep track of line number.
//        int count = 1;
//
//        // Open log file to read and check if it opened correctly.
//        File file = new File(sourceFileName);
//        try {
//            reader = new BufferedReader(new FileReader(file));
//
//            // Initializing buffer
//            buffer = reader.readLine();
//
//            // Getting past the log sections till the beginning of the sorted events section.
//            // Also count the instances of LOG_PROBLEM_SECTION_START to check for repetitions.
//            // Update the variable SECTION_START_COUNT that checks for repetitions later.
//            while (buffer.contains(LOG_PROBLEM_SECTION_END) == false) {
//                buffer = reader.readLine();
//                if (buffer.contains(LOG_PROBLEM_SECTION_START))
//                    SECTION_START_COUNT++;
//            }
//
//        } catch (Exception e) {
//            System.err.println("The following error was caught: " + e.getMessage());
//            System.out.println("Could not open the file: " + sourceFileName);
//            System.exit(1);
//        }
//
//        // The pattern of the line where correct code can be found
//        // Note that this is case-sensitive, so it strips away distracters pre-2016
//        Pattern pattern = Pattern.compile( "^Line .* \\(.* moves\\):(.*)$" );
//        Matcher matcher;
//
//        // Reading the sorted event log and updating the resultVector when correct code is found.
//        try {
//            // Reading till we reach the end of sorted events section
//            while (buffer.contains(SORTED_EVENTS_END) == false) {
//
//                // Extract the code from the event section if it contains a code line.
//                buffer = reader.readLine();
//                matcher = pattern.matcher(buffer);
//                if( matcher.matches() )
//                {
//                    // Get the code and add it to resultVector
//                    code = matcher.group( 1 ).trim();
//                    this.codeState.add(code);
//
//                    // Update codeIntegerRepresentation
//                    codeIntegerRepresentation.put(code,count);
//                    count++;
//
//                    // If this is the last line in the program, break
//                    // because what follows are distracters
//                    if( code.equals( JAVA_PROGRAM_END ) // Java
//                            || code.equals( CPP_PROGRAM_END ) // C++
//                            || code.equals( CS_PROGRAM_END ) // C#
//                    )
//                        break;
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("The following error was caught: " + e.getMessage());
//            System.out.println("Could not read line: " + count);
//            System.exit(1);
//        }
//
//        // Store the integer representation of the code in the object.
//        this.codeToint();
//    }


///**
// //     Method that extracts the initial code that is presented to the student from the log.
// //     @return ParsonsState object that holds the initial code presented to the student.
// //     */
//    private ParsonsState extractInitialCode()
//    {
//        // Reader to read log file
//        BufferedReader reader = null;
//
//        // Buffer to read the statement from the log file
//        String buffer = null;
//
//        // Buffer to store only the code part from processing the sorted event log.
//        String code;
//
//        // Counter variable,
//        int count = 0;
//
//        // Open log file to read and check if it opened correctly.
//        File file = new File(sourceFileName);
//        try {
//            // Initializing buffer
//            reader = new BufferedReader(new FileReader(file));
//            buffer = reader.readLine();
//
//            // Getting past the log sections till we get to the second section that has the initial code
//            // that is presented to the student.
//            while (count < 2) {
//                buffer = reader.readLine();
//                if (buffer.contains(LOG_PROBLEM_SECTION_START))
//                    count++;
//            }
//
//            // Getting the next line which should have the beginning of the initial solution.
//            buffer = reader.readLine();
//
//            // Getting the initial code and creating a ParsonsState object for the initial code.
//            // Asserting the beginning of the student code solution.
//            if ((buffer.contains(JAVA_PROGRAM_START)) //JAVA
//                    || (buffer.contains(CPP_PROGRAM_START)) //C++
//                    || (buffer.contains(C_PROGRAM_START))   //C
//                    || (buffer.contains(CS_PROGRAM_START))) //C#
//            {
//                // Create Parsons Object
//                ParsonsState temp_state = new ParsonsState();
//
//                // Append to the obj
//                do {
//                    temp_state.codeState.add(buffer);
//                    count++;
//                    buffer = reader.readLine();
//                } while (buffer.contains(C_PROGRAM_END) == false
//                        && buffer.contains(CS_PROGRAM_END)==false
//                        && buffer.contains(JAVA_PROGRAM_END) == false
//                        && buffer.contains(CPP_PROGRAM_END) == false);
//
//                // The last code line is not appended yet, so we append it.
//                temp_state.codeState.add(buffer);
//                count++;
//
//                // Get the integer representation of the code and store it as an array.
//                temp_state.codeToint();
//
//                return temp_state;
//            }
//
//        } catch (Exception e) {
//            System.err.println("The following error was caught: " + e.getMessage());
//            System.out.println("Could not open the file: " + sourceFileName);
//            System.exit(1);
//        }
//
//        // Returns NULL if extraction fails.
//        return null;
//    }