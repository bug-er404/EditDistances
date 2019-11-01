/*
* Partial implementation of the algorithm for research.
* Returns the Smith-Waterman edit distance between two strings str1 and str2.
* Need to implement Traceback to complete the algorithm. This will take additional space and complexity.
* This is a beneficial algorithm for bioinformatics where weight is considered.
* Modified the algorithm to simply work for strings.
*/
public class SmithWaterman {

    //Class DP matrix
    public static int V[][] = new int[128][128];

    //Function to check the similarity between two characters of a string
    //Called by smithWaterman
    //Returns 1 if similar and -1 if not.
    static int similarity (char x, char y)
    {
        if (x==y)
            return 1;
        else
            return -1;

    }

    //Smith Waterman Algorithm. Modified to simply work with strings.
    //Returns Edit Distance.
    static int smithWaterman(String str1, String str2)
    {
        int m, n;                 //holds length of str1 and str2 respectively.
        int dp1, dp2, dp3;       //holds parameter value of the states.
        int i,j;                 //matrix iterator.
        int V_max=0;             //edit distance variable.

        m = str1.length();
        n = str2.length();

        /*
        *Initializing DP matrix and calculating the state parameters.
        *The maximum of the matrix is stored in V_max, which is the edit distance.
        */
        V[0][0] = 0;
        for (i=1;i<=m;i++)
            V [i][0] = 0;

        for (j=1;j<=n;j++)
        {
            V [0][j]=0;
            for (i=1;i<=m;i++)
            {
                //states
                dp1=V[i-1][j-1]+similarity(str1.charAt(i-1), str2.charAt(j-1));
                dp2=V[i][j-1]-1;
                dp3=V[i-1][j]-1;

                V[i][j] = Math.max(Math.max(dp1, dp2), Math.max(dp3,0));

                if(V[i][j]>V_max)
                    V_max=V[i][j];
            }
        }
        return V_max;
    }


    public static void main(String[] args)
    {
        String str1 = "cat";
        String str2 = "cut";

        System.out.print("The edit distance is: ");
        System.out.println(smithWaterman(str1, str2));
    }

}

