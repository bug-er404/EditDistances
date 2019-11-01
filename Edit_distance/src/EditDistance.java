/*******************************************************
 Class that calculates the edit distance (Levenshtein && Damerau-Lev)
 @author Salil Maharjan
 @since 04/03/2019
  ******************************************************** */

public class EditDistance {

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    /** Matrix used for dynamic programming (DP). */
    private int [][] V = new int[128][128];
    /** Variables to hold string length. */
    private int m, n;
    /** Variables that hold parameter value of the states. */
    private int dp1, dp2, dp3;
    /** Variable for matrix iteration. */
    private int i, j;

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************
    EditDistance()
    {
        //stub
    }

    /**
     Method that calculates the Levenshtein Distance for strings.
     @param str1 String that holds the first string.
     @param str2 String that holds the second string to calculate the edit distance with.
     @return V[m][n] int that holds the edit distance between str1 and str2.
     */
    int levenshtein(String str1, String str2) {

        m = str1.length();
        n = str2.length();

        // ************************************************************
        // Initializing DP matrix and calculating the state parameters.
        // ************************************************************
        // Source prefixes initialization
        for (i = 1; i <= m; i++)
            V[i][0] = i;
        // Target prefixes initialization
        for (j = 1; j <= n; j++)
            V[0][j] = j;

        // *********************************************************
        // ******Calculating the state for the DP algorithm.********
        // *********************************************************
        for (i = 1; i <= m; i++) {
            for (j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1))
                    //same character.
                    V[i][j] = V[i - 1][j - 1];
                else {
                    //deletion state.
                    dp1 = V[i - 1][j] + 1;
                    //insertion state.
                    dp2 = V[i][j - 1] + 1;
                    //substitution state.
                    dp3 = V[i - 1][j - 1] + 1;
                    V[i][j] = Math.min(dp1, Math.min(dp2, dp3));
                }
            }
        }
        return V[m][n];
    }

    /**
     Overloaded method that calculates the Levenshtein Distance for numeric arrays.
     @param arr1 int[] that holds the first numeric array.
     @param arr2 int[] that holds the second numeric array to calculate the edit distance with.
     @return V[m][n] int that holds the edit distance between str1 and str2.
     */
    int levenshtein(int[] arr1, int[] arr2) {

        m = arr1.length;
        n = arr2.length;

        // ************************************************************
        // Initializing DP matrix and calculating the state parameters.
        // ************************************************************
        // Source prefixes initialization
        for (i = 1; i <= m; i++)
            V[i][0] = i;
        // Target prefixes initialization
        for (j = 1; j <= n; j++)
            V[0][j] = j;

        // *********************************************************
        // ******Calculating the state for the DP algorithm.********
        // *********************************************************
        for (i = 1; i <= m; i++) {
            for (j = 1; j <= n; j++) {
                if (arr1[i-1] == arr2[j - 1])
                    //same character.
                    V[i][j] = V[i - 1][j - 1];
                else {
                    //deletion state.
                    dp1 = V[i - 1][j] + 1;
                    //insertion state.
                    dp2 = V[i][j - 1] + 1;
                    //substitution state.
                    dp3 = V[i - 1][j - 1] + 1;
                    V[i][j] = Math.min(dp1, Math.min(dp2, dp3));
                }
            }
        }
        return V[m][n];
    }

    /**
     Method that calculates the Damerau-Levenshtein Distance for strings.
     @param str1 String that holds the first string.
     @param str2 String that holds the second string to calculate the edit distance with.
     @return V[m][n] int that holds the edit distance between str1 and str2.
     */
    int damerauLev(String str1, String str2) {
        /** Cost parameter used for transposition in Damerau's algorithm. */
        int cost;

        m = str1.length();
        n = str2.length();

        // ************************************************************
        // Initializing DP matrix and calculating the state parameters.
        // ************************************************************
        // Source prefixes initialization
        for (i = 1; i <= m; i++)
            V[i][0] = i;
        // Target prefixes initialization
        for (j = 1; j <= n; j++)
            V[0][j] = j;

        // *********************************************************
        // ******Calculating the state for the DP algorithm.********
        // *********************************************************
        for (i = 1; i <= m; i++) {
            for (j = 1; j <= n; j++) {
                //Checking transposition and updating cost.
                cost = (str1.charAt(i - 1) == str2.charAt(j - 1))? 0:1;

                //deletion state.
                dp1 = V[i - 1][j] + 1;
                //insertion state.
                dp2 = V[i][j - 1] + 1;
                //substitution state.
                dp3 = V[i - 1][j - 1] + cost;

                V[i][j] = Math.min(dp1, Math.min(dp2, dp3));

                // *********************************************************
                // ******Checking for transposable adjacent character.******
                // *********************************************************
                if ((i > 1) && (j > 1) && (str1.charAt(i - 1) == str2.charAt(j - 2)) && (str1.charAt(i - 2) == str2.charAt(j - 1))) {
                    //transposition state.
                    V[i][j] = Math.min((V[i][j]), (V[i - 2][j - 2] + cost));
                }
            }
        }
        return V[m][n];
    }

    /**
     Overloaded method that calculates the Damerau-Levenshtein Distance for numeric arrays.
     @param arr1 int[] that holds the first numeric array.
     @param arr2 int[] that holds the second numeric array to calculate the edit distance with.
     @return V[m][n] int that holds the edit distance between str1 and str2.
     */
    int damerauLev(int[] arr1, int[] arr2) {
        /** Cost parameter used for transposition in Damerau's algorithm. */
        int cost;

        m = arr1.length;
        n = arr2.length;

        // ************************************************************
        // Initializing DP matrix and calculating the state parameters.
        // ************************************************************
        // Source prefixes initialization
        for (i = 1; i <= m; i++)
            V[i][0] = i;
        // Target prefixes initialization
        for (j = 1; j <= n; j++)
            V[0][j] = j;

        // *********************************************************
        // ******Calculating the state for the DP algorithm.********
        // *********************************************************
        for (i = 1; i <= m; i++) {
            for (j = 1; j <= n; j++) {
                //Checking transposition and updating cost.
                cost = (arr1[i-1] == arr2[j - 1])? 0:1;

                //deletion state.
                dp1 = V[i - 1][j] + 1;
                //insertion state.
                dp2 = V[i][j - 1] + 1;
                //substitution state.
                dp3 = V[i - 1][j - 1] + cost;

                V[i][j] = Math.min(dp1, Math.min(dp2, dp3));

                // *********************************************************
                // ******Checking for transposable adjacent character.******
                // *********************************************************
                if ((i > 1) && (j > 1) && (arr1[i - 1] == arr2[j - 2]) && (arr1[i - 2] == arr2[j - 1])) {
                    //transposition state.
                    V[i][j] = Math.min((V[i][j]), (V[i - 2][j - 2] + cost));
                }
            }
        }
        return V[m][n];
    }

    public static void main(String[] args) {
        //iterator variable
        int i;

        EditDistance dist= new EditDistance();

        // ************************************************************
        // *************** STRING TEST CASES **************************
        // ************************************************************
        String testCases[][]=
                {
                        // **************************************************************************
                        // COMMENT FORMAT:::(Action performed. Levenshtein distance//Damerau distance)
                        // **************************************************************************
                        //Different edit distance cases:
                        //Single adjacent transposition. 2//1.
                        {"RCIK", "RICK"},
                        //Two adjacent transposition. 3//2.
                        //Two numbers should be moved within.
                        {"132546","123456"},


                        //Same edit distance cases:
                        //Moving withing C by two locations. 2//2
                        {"ABDEC","ABCDE"},
                        //Non-Adjacent character transposition. 2//2
                        {"ADCBE","ABCDE"},
                        //Two non adjacent transpositions. 4//4
                        {"145236","123456"},
                        //Single insertion. 1//1
                        {"GTGAC", "GTTGAC"},
                        //Single deletion. 1//1
                        {"PARSONPUZZZLE", "PARSONPUZZLE"},
                        //Single substitution. 1//1
                        {"abcdd","abcde"},
                        //Two insertions together. 2//2
                        {"A", "ABC"},
                        //Two insertions apart. 2//2
                        {"AC","ABCD"},
                        //Two deletions together. 2//2
                        {"ABCDE","ABC"},
                        //Two deletions apart. 2//2
                        {"ACE","ABCDE"},
                        //Two substitutions together. 2//2
                        {"AFWDE","ABCDE"},
                        //Two substitutions apart. 2//2
                        {"AgCD3","ABCDE"},

                        //Boundary cases:
                        //Insertion in first and last character. 2//2
                        {"234","12345"},
                        //Deletion in first and last character. 2//2
                        {"0123456","12345"},
                        //Substitution in first and last character. 2//2
                        {"92341","12345"},
                        //Swapping first and last elements. 2//2
                        {"52341","12345"}

                };

        // ************************************************************
        // *********** NUMERIC ARRAY TEST CASES ***********************
        // ************************************************************
        int[][] num1 =
                {
                        {4,5,6,7},
                        {1,2,3,4,5}
                };
        int[][] num2 =
                {
                        {4,6,5,7},
                        {4,3,2,1,5}
                };

        System.out.println("FOR STRINGS: ");
        for(i=0 ; i<testCases.length ; i++)
        {
            System.out.println("For string: " + testCases[i][0] + " and " + testCases[i][1]);
            System.out.print("The Lev. edit distance is: ");
            System.out.println(dist.levenshtein(testCases[i][0],testCases[i][1]));
            System.out.print("The Damerau edit distance is: ");
            System.out.println(dist.damerauLev(testCases[i][0],testCases[i][1]));
            System.out.println("---------------");

        }
        System.out.println("---------------");

        System.out.println("FOR NUMERIC ARRAYS");
        for(i=0 ; i<num1.length ; i++)
        {
            //System.out.println("For arrays" + Arrays.deepToString(num1)+ " and " + Arrays.deepToString(num2));
            System.out.print("The Lev. edit distance is: ");
            System.out.println(dist.levenshtein(num1[i], num2[i]));
            System.out.print("The Damerau edit distance is: ");
            System.out.println(dist.damerauLev(num1[i],num2[i]));
            System.out.println("---------------");
        }

    }
};