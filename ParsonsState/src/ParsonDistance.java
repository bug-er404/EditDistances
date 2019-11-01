/*******************************************************
 Class that calculates Parson edit distance
 @author Salil Maharjan
 @since 04/18/2019
  ******************************************************** */

import java.util.HashMap;

public class ParsonDistance {

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************
    ParsonDistance()
    {
        //stub
    }

    /**
     Method that calculates the Parson Distance for strings.
     @param str1 String that holds the first string that is incorrect.
     @param str2 String that holds the correct string to calculate the edit distance with.
     @return cost int that holds the parson distance between str1 and str2.
     */
    static int parsonDistance(String str1, String str2)
    {
        // *********************************************************
        // ******************** Local Variables ********************
        // *********************************************************

        /** Map used for traceback to find moving operations */
        /** Character holds the character in consideration for the operation (Insertion or Deletion)
         * Integer is computed based on the operations performed on the Character.
         * Insertions are +1 and Deletions are -1.
         * Zero state is when Integer == 0. This occurs when we find the first instance of
         * an operation on the character and is modified after each operation.
         * Also occurs when insertion and deletion operations happen for the same character.
         * When the Integer value for a Character reaches the zero state after initializing,
         * we consider it as a moving operation and update the cost accordingly. */
        HashMap<Character,Integer> trace_record = new HashMap<>();

        /** Matrix used for dynamic programming (DP). */
        int V[][] = new int[128][128];
        /** Integer that holds the parson distance. */
        int cost;
        /** Variables to hold string length. */
        int m, n;
        /** Variables that hold parameter value of the states. */
        /** dp1 holds deletion, dp2 holds insertion state.  */
        int dp1, dp2;
        /** Variables for matrix iteration. */
        int i,j;

        m = str1.length();
        n = str2.length();

        // ************************************************************
        // Initializing DP matrix and calculating the state parameters.
        // ************************************************************
        // Source prefixes initialization
        for (i=1;i<=m;i++)
            V [i][0] = i;
        // Target prefixes initialization
        for (j=1;j<=n;j++)
            V [0][j]=j;

        // *********************************************************
        // ****Calculating the state by Levenshtein's algorithm*****
        // *********************************************************
        for(i=1;i<=m;i++)
        {
            for(j=1;j<=n;j++)
            {
                if(str1.charAt(i-1)==str2.charAt(j-1))
                    // Same character state
                    V[i][j]=V[i-1][j-1];                            //same character
                else{
                    // Deletion state.
                    dp1= V[i-1][j]+1;
                    // Insertion state.
                    dp2= V[i][j-1]+1;
                    // Filling DP matrix
                    V[i][j]= Math.min(dp1,dp2);
                }
            }
        }

        // ***********************************************************************
        // ****Levenshtein edit distance cost before checking move opeartions*****
        // ***********************************************************************
        cost = V[i-1][j-1];

        //Updating string lengths
        i = m;
        j = n;

        // ***************************************************************************
        // ***Traceback from [m,n] to [0,0] in the DP matrix to check for moving******
        // ***operations. Operations performed on characters are recorded in the Map**
        // ***************************************************************************
        while(i != 0 || j != 0)
        {

            // *********************************************************
            // ********** Matrix boundary condition check **************
            // *********************************************************
            if(i == 0 || j ==0 )
            {
                // Staying in boundary to avoid character checking.
                do {
                    // Insertion boundary condition.
                    if (i == 0) {
                        //Character in map.
                        if (trace_record.containsKey(str2.charAt(j - 1)) &&
                                trace_record.get(str2.charAt(j - 1)) < 0)
                        {
                            cost--;
                            j--;
                        }
                        else
                        {
                            //Different character insertion case
                            trace_record.put(str2.charAt(j-1), 1);
                            j--;
                        }

                        if (i == 0 && j == 0)
                            return cost;
                    }
                    // Deletion boundary condition.
                    else {
                        if (trace_record.containsKey(str1.charAt(i - 1)) &&
                                trace_record.get(str1.charAt(i - 1)) > 0)
                        {
                            cost--;
                            i--;
                        }
                        else
                        {
                            //Different character deletion case
                            trace_record.put(str1.charAt(i-1), -1);
                            i--;
                        }
                        if (i == 0 && j == 0)
                            return cost;
                    }
                } while (i>0 || j>0);
            }

            // *************************************************************
            // ** Recording operation performed on different characters ****
            // *************************************************************
            if(str1.charAt(i-1) != str2.charAt(j-1))
            {
                // *********************************************************
                // *** Insertion Case. Horizontal move in DP matrix ********
                // *********************************************************
                if (Math.min(V[i][j-1],V[i-1][j])==V[i][j-1])
                {
                    // The character to be inserted
                    char c_insert = str2.charAt(j-1);

                    // For first element in the Hashmap
                    if(trace_record.isEmpty()) trace_record.put(c_insert, 1);

                    // For existing key in the map
                    else if (trace_record.containsKey(c_insert))
                    {
                        // Checking for zero state and moving operation.
                        // If deletion of the same key exists, moving operation found.
                        // Update cost.
                        if(trace_record.get(c_insert) != 0 && trace_record.get(c_insert)<0) cost--;

                        // Update map value for the character key.
                        trace_record.put(c_insert, (trace_record.get(c_insert))+1);
                    }
                    // New new key in the map.
                    else
                        trace_record.put(c_insert,1);

                    //Moving to the next cell
                    j--;
                }
                // *********************************************************
                // ****** Deletion Case. Vertical move in DP matrix ********
                // *********************************************************
                else
                {
                    // The character to be deleted
                    char c_delete = str1.charAt(i-1);

                    // For first element in the Hashmap
                    if(trace_record.isEmpty()) trace_record.put(c_delete, -1);

                    // For existing key in the map
                    else if (trace_record.containsKey(c_delete))
                    {
                        // Checking for zero state and moving operation.
                        // If insertion of the same key exists, moving operation found.
                        // Update cost.
                        if(trace_record.get(c_delete) != 0 && trace_record.get(c_delete)>0) cost--;

                        // Update map value for the character key.
                        trace_record.put(c_delete, (trace_record.get(c_delete))-1);
                    }
                    // New new key in the map.
                    else
                        trace_record.put(c_delete,-1);

                    //Moving to the next cell
                    i--;
                }
            }
            // *********************************************************
            // *** Same Character Case. Diagonal move in DP matrix *****
            // *********************************************************
            else
            {
                i--;
                j--;
            }
            //System.out.println(trace_record);
            //System.out.println("Cost"+cost);
        }

        // Returns Parson Distance.
        return cost;
    }

    /**
     Method that calculates the Parson Distance for integer arrays.
     @param str1 int[] that holds the first string that is incorrect.
     @param str2 int[] that holds the correct string to calculate the edit distance with.
     @return cost int that holds the parson distance between str1 and str2.
     */
    static int parsonDistance(Integer[] str1, Integer[] str2)
    {
        // *********************************************************
        // ******************** Local Variables ********************
        // *********************************************************

        /** Map used for traceback to find moving operations */
        /** Character holds the character in consideration for the operation (Insertion or Deletion)
         * Integer is computed based on the operations performed on the Character.
         * Insertions are +1 and Deletions are -1.
         * Zero state is when Integer == 0. This occurs when we find the first instance of
         * an operation on the character and is modified after each operation.
         * Also occurs when insertion and deletion operations happen for the same character.
         * When the Integer value for a Character reaches the zero state after initializing,
         * we consider it as a moving operation and update the cost accordingly. */
        HashMap<Integer,Integer> trace_record = new HashMap<>();

        /** Matrix used for dynamic programming (DP). */
        int V[][] = new int[128][128];
        /** Integer that holds the parson distance. */
        int cost;
        /** Variables to hold string length. */
        int m, n;
        /** Variables that hold parameter value of the states. */
        /** dp1 holds deletion, dp2 holds insertion state.  */
        int dp1, dp2;
        /** Variables for matrix iteration. */
        int i,j;

        m = str1.length;
        n = str2.length;

        // ************************************************************
        // Initializing DP matrix and calculating the state parameters.
        // ************************************************************
        // Source prefixes initialization
        for (i=1;i<=m;i++)
            V [i][0] = i;
        // Target prefixes initialization
        for (j=1;j<=n;j++)
            V [0][j]=j;

        // *********************************************************
        // ****Calculating the state by Levenshtein's algorithm*****
        // *********************************************************
        for(i=1;i<=m;i++)
        {
            for(j=1;j<=n;j++)
            {
                if(str1[i-1]==str2[j-1])
                    // Same character state
                    V[i][j]=V[i-1][j-1];                            //same character
                else{
                    // Deletion state.
                    dp1= V[i-1][j]+1;
                    // Insertion state.
                    dp2= V[i][j-1]+1;
                    // Filling DP matrix
                    V[i][j]= Math.min(dp1,dp2);
                }
            }
        }

        // ***********************************************************************
        // ****Levenshtein edit distance cost before checking move opeartions*****
        // ***********************************************************************
        cost = V[i-1][j-1];

        //Updating string lengths
        i = m;
        j = n;

        // ***************************************************************************
        // ***Traceback from [m,n] to [0,0] in the DP matrix to check for moving******
        // ***operations. Operations performed on characters are recorded in the Map**
        // ***************************************************************************
        while(i != 0 || j != 0)
        {

            // *********************************************************
            // ********** Matrix boundary condition check **************
            // *********************************************************
            if(i == 0 || j ==0 )
            {
                // Staying in boundary to avoid character checking.
                do {
                    // Insertion boundary condition.
                    if (i == 0) {
                        //Character in map.
                        if (trace_record.containsKey(str2[j - 1]) &&
                                trace_record.get(str2[j - 1]) < 0)
                        {
                            cost--;
                            j--;
                        }
                        else
                        {
                            //Different character insertion case
                            trace_record.put(str2[j-1], 1);
                            j--;
                        }

                        if (i == 0 && j == 0)
                            return cost;
                    }
                    // Deletion boundary condition.
                    else {
                        if (trace_record.containsKey(str1[i - 1]) &&
                                trace_record.get(str1[i - 1]) > 0)
                        {
                            cost--;
                            i--;
                        }
                        else
                        {
                            //Different character deletion case
                            trace_record.put(str1[i-1], -1);
                            i--;
                        }
                        if (i == 0 && j == 0)
                            return cost;
                    }
                } while (i>0 || j>0);
            }

            // *************************************************************
            // ** Recording operation performed on different characters ****
            // *************************************************************
            if(str1[i-1] != str2[j-1])
            {
                // *********************************************************
                // *** Insertion Case. Horizontal move in DP matrix ********
                // *********************************************************
                if (Math.min(V[i][j-1],V[i-1][j])==V[i][j-1])
                {
                    // The character to be inserted
                    int c_insert = str2[j-1];

                    // For first element in the Hashmap
                    if(trace_record.isEmpty()) trace_record.put(c_insert, 1);

                        // For existing key in the map
                    else if (trace_record.containsKey(c_insert))
                    {
                        // Checking for zero state and moving operation.
                        // If deletion of the same key exists, moving operation found.
                        // Update cost.
                        if(trace_record.get(c_insert) != 0 && trace_record.get(c_insert)<0) cost--;

                        // Update map value for the character key.
                        trace_record.put(c_insert, (trace_record.get(c_insert))+1);
                    }
                    // New new key in the map.
                    else
                        trace_record.put(c_insert,1);

                    //Moving to the next cell
                    j--;
                }
                // *********************************************************
                // ****** Deletion Case. Vertical move in DP matrix ********
                // *********************************************************
                else
                {
                    // The character to be deleted
                    int c_delete = str1[i-1];

                    // For first element in the Hashmap
                    if(trace_record.isEmpty()) trace_record.put(c_delete, -1);

                        // For existing key in the map
                    else if (trace_record.containsKey(c_delete))
                    {
                        // Checking for zero state and moving operation.
                        // If insertion of the same key exists, moving operation found.
                        // Update cost.
                        if(trace_record.get(c_delete) != 0 && trace_record.get(c_delete)>0) cost--;

                        // Update map value for the character key.
                        trace_record.put(c_delete, (trace_record.get(c_delete))-1);
                    }
                    // New new key in the map.
                    else
                        trace_record.put(c_delete,-1);

                    //Moving to the next cell
                    i--;
                }
            }
            // *********************************************************
            // *** Same Character Case. Diagonal move in DP matrix *****
            // *********************************************************
            else
            {
                i--;
                j--;
            }
            //System.out.println(trace_record);
            //System.out.println("Cost"+cost);
        }

        // Returns Parson Distance.
        return cost;
    }

    public static void main(String[] args)
    {
        // Iterator
        int i;

        // ************************************************************
        // *************** PARTIAL DEMO STEP CASES ********************
        // ************************************************************
        Integer actualSoln[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 3, 16, 17, 18, 3, 19, 20, 21};
        Integer studentSoln[][] ={
                // Initial solution presented to student.
                {0, 1, 2, 3, 4, 6, 8, 11, 14, 21},

                //Move 1-38
                {0, 1, 2, 3, 5, 4, 6, 8, 11, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 8, 11, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 10, 8, 11, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 10, 8, 13, 11, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 10, 8, 13, 11, 15, 14, 21},

                {0, 1, 2, 3, 5, 4, 22, 6, 10, 8, 13, 11, 15, 16, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 10, 8, 13, 11, 15, 16, 18, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 10, 8, 13, 11, 15, 16, 18, 19, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 9, 10, 8, 13, 11, 15, 16, 18, 19, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 9, 10, 12, 8, 13, 11, 15, 16, 18, 19, 14, 21},

                {0, 1, 2, 3, 5, 4, 22, 6, 9, 10, 12, 8, 13, 11, 15, 3, 16, 18, 19, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 9, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 19, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 9, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 3, 19, 14, 21},
                {0, 1, 2, 3, 5, 4, 22, 6, 9, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                //move from problem to solution. Removes first distractor.
                {0, 1, 2, 3, 5, 4, 6, 9, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},

                {0, 1, 2, 3, 5, 4, 7, 6, 9, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                // remove distractor (if)
                {0, 1, 2, 3, 5, 4, 7, 6, 9, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                // remove distractor  (secondInput)
                {0, 1, 2, 3, 5, 4, 7, 6, 9, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                {0, 1, 2, 3, 4, 5, 7, 6, 9, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                {0, 1, 2, 3, 4, 5, 6, 9, 7, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},

                {0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 12, 8, 13, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 10, 12, 8, 9, 13, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 12, 8, 9, 13, 10, 11, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 10, 11, 15, 12, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 10, 11, 12, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},

                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15, 13, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 3, 16, 17, 18, 3, 19, 20, 14, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 3, 16, 17, 18, 3, 19, 20, 14, 21, 15},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 3, 16, 17, 18, 3, 19, 20, 14, 15, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 18, 3, 19, 20, 14, 15, 21, 3},

                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 18, 3, 19, 20, 14, 15, 3, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 17, 18, 3, 19, 20, 14, 15, 3, 16, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 18, 3, 19, 20, 14, 15, 3, 16, 17, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 3, 19, 20, 14, 15, 3, 16, 17, 18, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 3, 20, 14, 15, 3, 16, 17, 18, 19, 21},

                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 3, 14, 15, 3, 16, 17, 18, 19, 20, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 3, 16, 17, 18, 19, 3, 20, 21},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 3, 16, 17, 18, 3, 19, 20, 21},

        };

//        for(i=0 ; i<testCases.length ; i++)
//        {
//            //System.out.print("The edit distance is: ");
//            System.out.println(parsonDistance(testCases[i][0], testCases[i][1]));
//        }


//        for( int trial = 0; trial < studentSoln.length; trial++ )
//            System.out.println(
//                    printString( studentSoln[trial] ) + ":  " +
//                     parsonDistance( studentSoln[trial], actualSoln));

        for( int trial = 0; trial < studentSoln.length; trial++ )
            System.out.println(
                    "Move " + trial + ":  " +
                     parsonDistance( studentSoln[trial], actualSoln));


    }

//    private static String printString( int data[] )
//    {
//        String result = "[";
//        for( int row = 0; row < data.length; row++ )
//            result += data[row] + ",";
//        result = result.substring(0, result.length() - 1 )+ "]";
//        return result;
//    }
}
