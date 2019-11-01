/*
 *Returns the Damerau-Levenshtein edit distance between two strings str1 and str2.
 */

public class DamerauLev {

    //Class DP matrix
    public static int V[][] = new int[128][128];

    //Damerau Levensthein Algorithm
    //Returns edit distance of str1 and str2.
    static int damerauLev(String str1, String str2)
    {
        int m, n;                //holds string length
        int dp1, dp2, dp3;       //holds parameter value for the state
        int i,j;                 //matrix iterator
        int cost;                //cost param. used for transposition.

        m = str1.length();
        n = str2.length();

        /*
         *Initializing DP matrix and calculating the state parameters.
         */
        // Source prefixes initialization
        for (i=1;i<=m;i++)
            V [i][0] = i;
        // Target prefixes initialization
        for (j=1;j<=n;j++)
            V [0][j]=j;

        //Damerau-Levenshtein Algorithm state
        for(i=1;i<=m;i++)
        {
            for(j=1;j<=n;j++) {
                //updating cost for substitutions and transpositions.
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                dp1 = V[i - 1][j] + 1;                           //deletion
                dp2 = V[i][j - 1] + 1;                           //insertion
                dp3 = V[i - 1][j - 1] + cost;                      //substitution

                V[i][j] = Math.min(dp1, Math.min(dp2, dp3));

                if ((i > 1) && (j > 1) && (str1.charAt(i - 1) == str2.charAt(j - 2)) && (str1.charAt(j - 2) == str2.charAt(i - 1))) {
                    V[i][j] = Math.min((V[i][j]), (V[i - 2][j - 2] + cost));     //transposition
                }
            }
        }

        return V[m][n];     //edit distance
    }

    //Main Test.
    public static void main(String[] args)
    {
        String str1 = "ABCDEFGH";
        String str2 = "ABCHEFGD";

        System.out.print("The edit distance is: ");
        System.out.println(damerauLev(str1, str2));

    }
}
