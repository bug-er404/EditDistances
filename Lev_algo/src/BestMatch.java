package org.problets.lib.bnf;

/*******************************************************
 To get the best match between two Parsons solution sequences
 accounting for the fact that there could be incorrect characters
 between stretches of matches
 @author Amruth Kumar
 @since 4 / 16 / 2019
  ******************************************************** */

/**
 Parsons constraints:
 * Each character/line number appears only once in student and actual solution
 * STRICT grading assumes that once a line is encountered in student solution, only
 those that appear after it in correct solution can be correct - rather strict,
 but enforces the order imposed by BNF grammar.
 To relax this rule, simply add additional BNF rules and match against all possible
 BNF sequences
 *  ABC-XY-DEF-PQ and ABC-YX-DEF-QP (ignore hyphens) returns a match of 3 with depth-first search.
 This algorithm is meant to return 6 corresponding to ABC and DEF
 */

public class BestMatch
{
    // *********************************************************
    // **************** Configuration Variables ****************
    // *********************************************************

    // *********************************************************
    // ******************** Class Constants ********************
    // *********************************************************

    // *********************************************************
    // ******************** Class Variables ********************
    // *********************************************************

    // *********************************************************
    // ******************** GUI Components *********************
    // *********************************************************

    // *********************************************************
    // ******************** Constructor ************************
    // *********************************************************

    // *********************************************************
    // ******************** Paint - View ***********************
    // *********************************************************

    // *********************************************************
    // ******************** actionPerformed - Controller *******
    // *********************************************************

    // *********************************************************
    // ******************** Selectors **************************
    // *********************************************************

    /**
     Compares studentSoln against each solution in actualSolns.
     If strict, it grades such that once a line n appears in student soln, among the lines that appear later,
     only those with line number greater than n can be correct.
     If not strict, fabc compared against abcf will return 3 (and 0 if strict).
     Finally, this function returns the best count among the matches.
     */
    public static int bestMatch( int studentSoln[], int actualSolns[][], boolean strict )
    {
        // The best match so far
        int nextMatch, bestMatchSoFar = 0;

        // Find the best match of studentSoln against ALL the entries in actualSolnVector
        for( int index = 0; index < actualSolns.length; index++ )
        {
            // Convert Vector of <Terminal> into array of integers and match studentSoln against it (strict)
            nextMatch = bestMatch( studentSoln, actualSolns[index], strict );

            // if nextMatch is better than bestMatchSoFar, update bestMatchSoFar
            if( nextMatch > bestMatchSoFar )
                bestMatchSoFar = nextMatch;
        }

        // Return the bestMatchSoFar
        return bestMatchSoFar;
    }


    // For debugging purposes, could extend it to capture the [row,col] of each match

    public static int bestMatch( int studentSoln[], int actualSoln[], boolean strict )
    {
        // First, create a matrix of studentSoln rows and actualSoln Column names
        int numRows = studentSoln.length;
        int numCols = actualSoln.length;
        boolean matchMatrix [][] = new boolean [numRows] [numCols];

        // Initialize the matrix to all false
        int row, col;
        for( row = 0; row < numRows; row++ )
            for( col = 0; col < numCols; col++ )
                matchMatrix[row][col] = false;

        // Now, set matchMatrix[row][col] = true
        //   if studentSoln[row] == actualSoln[col]
        for( row = 0; row < numRows; row++ )
            for( col = 0; col < numCols; col++ )
                if( studentSoln[row] == actualSoln[col] )
                {
                    matchMatrix[row][col] = true;
                    break;   // For efficiency, look no further for this row element
                }

        // Now, find the longest contiguous match starting at row = 0, and col
        if( strict )
            return bestMatchAuxStrict( matchMatrix, 0, 0, numRows, numCols );
        else
            return bestMatchAux( matchMatrix, 0, 0, numRows, numCols );
    }

    /**
     At each step, computes three scores:
     correctScore: score if studentSoln[row} matches actualSoln[col]
     If so, find the number of contiguous subsequent elements that also match
     If the number of contiguous matching elements is > 1, add it to best match returned by recursive call
     incorrectScore: score if studentSoln[row] does not match actualSoln[col]
     but, matches actualSoln[col + eta].
     If so, incorrectScore is the value returned by recursive call starting at row, col + eta

     skippedScore: score if we simply ignore studentSoln[row].
     If so, skippedScore is the value returned by recursive call looking for row+1 element in studentSoln

     Returns the best among correctScore, incorrectScore and skippedScore.

     bestMatch( "abcQPdef", "abcPQdef" ) = 3 (correctScore) + bestMatch( "QPdef", "PQdef" )
     bestMatch( "QPdef", "PQdef" ) = max{ bestMatch( "Pdef", "def" ) (incorrectScore),
     bestMatch( "Pdef", "PQdef" ) (skippedScore) }
     bestMatch( "Pdef", "def" ) = max{ 0 (incorrectScore),
     bestMatch( "def", "def" ) (skippedScore )
     which is max( 0, 3) = 3
     bestMatch( "Pdef", "PQdef" ) = plural(1) + bestMatch( "def", "Qdef" ) (correctScore)
     bestMatch( "def", "Qdef" ) = max{ bestMatch( "def", "def" ) (incorrectScore),
     bestMatch( "def", "def" ) (skippedScore) }
     The problem with this algorithm is that if the studentSoln is "fabcPQde", it will return 7
     corresponding to "abcPQde". If this should be disallowed for Parsons, i.e.,
     once you mis-identify line x, anything correct past it for lines y < x do not count,
     we need to revise this algorithm.
     */
    private static int bestMatchAux( boolean matchMatrix[][], int row, int col, int numRows, int numCols )
    {
        // If we have either exhausted studentSoln (row) or actualSoln (col), return 0
        if( row >= numRows )
            return 0;

        if( col >= numCols )
            return 0;

        // The three scores that will be compared
        int correctScore = 0; // Score if matchMatrix[row][col] is true
        int incorrectScore = 0; // Score if matchMatrix[row][col + eta] is true
        int skippedScore = 0; // Score if we ignore row altogether

        if( true == matchMatrix[row][col] )
        {
            // Find the longest contiguous match section starting at row, col
            // Note that we just verified that matchMatrix[row][col] is true
            int matchCount = 0;
            while( true == matchMatrix[row][col] )
            {
                // Increment match count
                matchCount++;

                // Increment row and col
                row++;
                col++;

                // If we have gone out of bounds of rows, stop
                if( row >= numRows )
                    break;

                // If we have gone out of bounds of cols, stop
                if( col >= numCols )
                    break;
            }

            correctScore = plural( matchCount ) + bestMatchAux( matchMatrix, row, col, numRows, numCols );
        }
        else // matchMatrix[row][col] is not true
        {
            // Find the score if we skip this element in student solution
            skippedScore = bestMatchAux( matchMatrix, row + 1, col, numRows, numCols );

            // Find the column where studentSoln[0] matches actualSoln
            for( col++ ; col < numCols; col++ )
                if( true == matchMatrix[row][col] )
                    break;

            // If studentSoln[0] was not found, which happens if
            //    this element was stepped past when looking for a previous element
            //    We could call bestMatchAux( matchMatrix, row + 1, startCol, numRows, numCols );
            //    but, this would have been covered as skippedScore on a previous step, e.g.,
            //    studentSoln abcQPdef compared to actualSoln abcPQdef:
            //       When looking for Q (to calculate incorrectScore), we step past P
            //       On the next recursive call, we cannot find P in actualSoln
            //       We could simply skip P (to calculate skippedScore).
            //       But, one of the three scores we calculated when looking for Q: skippedScore
            //         will have skipped Q and looked for P starting from the correct location in actualSoln
            //       So, the same best match will end up getting calculated twice
            if( col >= numCols )
                incorrectScore = 0;
            else
                incorrectScore = bestMatchAux( matchMatrix, row, col, numRows, numCols );
        }

        // Now, return the greatest of the three scores
        if( correctScore >= skippedScore )
            if( correctScore >= incorrectScore )
                return correctScore;
            else
                return incorrectScore;
        else
        if( skippedScore >= incorrectScore )
            return skippedScore;
        else
            return incorrectScore;
    }

    /**
     Returns a match such that an element studentSoln[index] qualifies to be correct
     as long as no actualSoln[index + eta] appears in 0 --> index in studentSoln.
     In other words, cannot assemble later statements in actualSoln earlier in studentSoln.
     Assembling earlier statements in actualSoln later in studentSoln is okay.
     */
    private static int bestMatchAuxStrict( boolean matchMatrix[][], int row, int col, int numRows, int numCols )
    {
        // If we have either exhausted studentSoln (row) or actualSoln (col), return 0
        if( row >= numRows )
            return 0;

        if( col >= numCols )
            return 0;

        // If we found the element of studentSoln in actualSoln
        if( true == matchMatrix[row][col] )
        {
            // Find the longest contiguous match section starting at row, col
            // Note that we just verified that matchMatrix[row][col] is true
            int matchCount = 0;
            while( true == matchMatrix[row][col] )
            {
                // Increment match count
                matchCount++;

                // Increment row and col
                row++;
                col++;

                // If we have gone out of bounds of rows, stop
                if( row >= numRows )
                    break;

                // If we have gone out of bounds of cols, stop
                if( col >= numCols )
                    break;
            }

            return plural( matchCount ) + bestMatchAuxStrict( matchMatrix, row, col, numRows, numCols );
        }
        else // matchMatrix[row][col] is not true
        {
            // Back up current value of col
            int currentCol = col;

            // Find the column where studentSoln[0] matches actualSoln
            for( col++ ; col < numCols; col++ )
                if( true == matchMatrix[row][col] )
                    break;

            // If studentSoln[0] was not found, which happens if
            //    this element was stepped past when looking for a previous element
            //    Ignore this element: it appeared earlier in actualSoln
            //    and continue looking from the current location in actualSoln
            if( col >= numCols )
                return bestMatchAuxStrict( matchMatrix, row + 1, currentCol, numRows, numCols );
            else
                return bestMatchAuxStrict( matchMatrix, row, col, numRows, numCols );
        }
    }

    // Returns count only if it is greater than 1. Returns 0 otherwise, to
    // emphasize that we want two or more statemnts to be back to back
    // Otherwise, ABCD matched against DCBA would still return 1!
    private static int plural( int count )
    {
        if( count > 1 )
            return count;
        else
            return 0;
    }

    // *********************************************************
    // ******************** Mutators ***************************
    // *********************************************************

    // *********************************************************
    // ******************** Code Generation ********************
    // *********************************************************

    // *********************************************************
    // ******************** Code Explanation *******************
    // *********************************************************

    // *********************************************************
    // ******************** Utility Methods ********************
    // *********************************************************

    // *********************************************************
    // ******************** Printing Methods *******************
    // *********************************************************

    // *********************************************************
    // ******************** Debugging Methods ******************
    // *********************************************************

    public static void main( String args[] )
    {
        int actualSoln[] = { 1, 2, 3, 4, 5 };

        int studentSoln[][] ={
                {1,2,3,4,5},
                {2,1,3,4,5},
                {3,1,2,4,5},
                {1,3,2,4,5},
                {2,3,1,4,5},
                {3,2,1,4,5},
                {3,2,4,1,5},
                {2,3,4,1,5},
                {4,3,2,1,5},
                {3,4,2,1,5},
                {2,4,3,1,5},
                {4,2,3,1,5},
                {4,1,3,2,5},
                {1,4,3,2,5},
                {3,4,1,2,5},
                {4,3,1,2,5},
                {1,3,4,2,5},
                {3,1,4,2,5},
                {2,1,4,3,5},
                {1,2,4,3,5},
                {4,2,1,3,5},
                {2,4,1,3,5},
                {1,4,2,3,5},
                {4,1,2,3,5},
                {5,1,2,3,4},
                {1,5,2,3,4},
                {2,5,1,3,4},
                {5,2,1,3,4},
                {1,2,5,3,4},
                {2,1,5,3,4},
                {2,1,3,5,4},
                {1,2,3,5,4},
                {3,2,1,5,4},
                {2,3,1,5,4},
                {1,3,2,5,4},
                {3,1,2,5,4},
                {3,5,2,1,4},
                {5,3,2,1,4},
                {2,3,5,1,4},
                {3,2,5,1,4},
                {5,2,3,1,4},
                {2,5,3,1,4},
                {1,5,3,2,4},
                {5,1,3,2,4},
                {3,1,5,2,4},
                {1,3,5,2,4},
                {5,3,1,2,4},
                {3,5,1,2,4},
                {4,5,1,2,3},
                {5,4,1,2,3},
                {1,4,5,2,3},
                {4,1,5,2,3},
                {5,1,4,2,3},
                {1,5,4,2,3},
                {1,5,2,4,3},
                {5,1,2,4,3},
                {2,1,5,4,3},
                {1,2,5,4,3},
                {5,2,1,4,3},
                {2,5,1,4,3},
                {2,4,1,5,3},
                {4,2,1,5,3},
                {1,2,4,5,3},
                {2,1,4,5,3},
                {4,1,2,5,3},
                {1,4,2,5,3},
                {5,4,2,1,3},
                {4,5,2,1,3},
                {2,5,4,1,3},
                {5,2,4,1,3},
                {4,2,5,1,3},
                {2,4,5,1,3},
                {3,4,5,1,2},
                {4,3,5,1,2},
                {5,3,4,1,2},
                {3,5,4,1,2},
                {4,5,3,1,2},
                {5,4,3,1,2},
                {5,4,1,3,2},
                {4,5,1,3,2},
                {1,5,4,3,2},
                {5,1,4,3,2},
                {4,1,5,3,2},
                {1,4,5,3,2},
                {1,3,5,4,2},
                {3,1,5,4,2},
                {5,1,3,4,2},
                {1,5,3,4,2},
                {3,5,1,4,2},
                {5,3,1,4,2},
                {4,3,1,5,2},
                {3,4,1,5,2},
                {1,4,3,5,2},
                {4,1,3,5,2},
                {3,1,4,5,2},
                {1,3,4,5,2},
                {2,3,4,5,1},
                {3,2,4,5,1},
                {4,2,3,5,1},
                {2,4,3,5,1},
                {3,4,2,5,1},
                {4,3,2,5,1},
                {4,3,5,2,1},
                {3,4,5,2,1},
                {5,4,3,2,1},
                {4,5,3,2,1},
                {3,5,4,2,1},
                {5,3,4,2,1},
                {5,2,4,3,1},
                {2,5,4,3,1},
                {4,5,2,3,1},
                {5,4,2,3,1},
                {2,4,5,3,1},
                {4,2,5,3,1},
                {3,2,5,4,1},
                {2,3,5,4,1},
                {5,3,2,4,1},
                {3,5,2,4,1},
                {2,5,3,4,1},
                {5,2,3,4,1},
        };

//        int actualSoln[][]={
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,2,3,4,5,6},
//                {1,2,3},
//                {1,2,3,4},
//                {1,2,3,3,4},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,3,2,4,5},
//                {1,1,3,4,5},
//                {3,1,4,1,5,1},
//                {3,4,5,1,1},
//                {3,4,5,1,1,1},
//                {1,2,3,4},
//                {1,2,3,4,5,6},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,2,3,4,5,6},
//                {1,2,2,1,3,4},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,2,3},
//                {1,2,3,4},
//                {1,2,3},
//                {1,3,5},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//                {1,2,3,4,5},
//
//        };
//        int studentSoln[][] ={
//                // PARSON DISTANCE OF 1 IN THESE CASES:
//                //One place move in
//                {1,3,2,4,5},
//                //Two places
//                {1,3,4,2,5},
//                //Three places
//                {1,3,4,5,2},
//                //Four places move in for different length
//                {1,3,4,5,6,2},
//                //Boundary move in
//                {2,3,1},
//                //Moving out
//                {4,1,2,3},
//                //Same character move in
//                {1,3,3,2,4},
//
//                // PARSON DISTANCE OF 2 IN THESE CASES:
//                //Different length
//                {1,3,2,4},
//                //Two positions
//                {1,3,4,2},
//                //Two character move in
//                {1,3,2,5,4},
//                //Two key move in
//                {3,4,5,1,2},
//
//                // Two same character move in - 2 distance
//                {3,4,1,5,1},
//                // Three same character move in - 3 distance
//                {1,1,1,3,4,5},
//                // Two same character move out - 2 distance
//                {1,3,1,4,5},
//                // Three same character move out - 3 distance
//                {1,3,1,4,1,5},
//
//                //Single adjacent transposition. 2-1
//                {1,3,2,4},
//                //Two adjacent transposition. 2-2
//                //Two numbers should be moved within.
//                {1,3,2,5,4,5},
//                //Moving within C by two locations. 2-1
//                {1,2,4,5,3},
//                //Non-Adjacent character transposition. 2-2
//                {1,4,3,2,5},
//                //Two non adjacent transpositions. 4-2
//                {1,4,5,2,3,6},
//                //Single insertion. 1-1
//                {1,2,1,3,4},
//                //Single deletion. 1-1
//                {1,2,3,4,5,6},
//                //Single substitution. 1-2
//                {1,2,3,4,4},
//                //Two insertions together. 2-2
//                {1},
//                //Two insertions apart. 2-2
//                {1,3},
//                //Two deletions together. 2-2
//                {1,2,3,4,5},
//                //Two deletions apart. 2-2
//                {1,2,3,4,5},
//                //Two substitutions together. 2-4
//                {1,9,8,4,5},
//                //Two substitutions apart. 2-4
//                {1,8,3,7,5},
//
//                //Boundary cases:
//                //Insertion in first and last character. 2-2
//                {2,3,4},
//                //Deletion in first and last character. 2-2
//                {0,1,2,3,4,5,6},
//                //Substitution in first and last character. 2-3
//                //Delete, move in, insert.
//                {9,2,3,4,1},
//                //Swapping first and last elements. 2-2
//                {5,2,3,4,1},
//
//        };


        for( int trial = 0; trial < studentSoln.length; trial++ )
            System.out.println(
                   // printString( studentSoln[trial] )
                 //   + " / "
                //    + printString(actualSoln)
              //      + ":  "
              //              +bestMatch( studentSoln[trial], actualSoln, false )
              //      + " / "
                    + bestMatch( studentSoln[trial], actualSoln, true ) );
    }

    private static String printString( int data[] )
    {
        String result = "[";
        for( int row = 0; row < data.length; row++ )
            result += data[row] + ",";
        result = result.substring(0, result.length() - 1 )+ "]";
        return result;
    }
};

// *********************************************************
// ******************** Trash Methods **********************
// *********************************************************


/*
// This does not take into account ignoring an element in student solution altogether if it is the
// first element in the student solution.
    private static int bestMatchAux( int matchMatrix[][], int row, int currentCol, int numRows, int numCols )
    {
	// Find the column where studentSoln[row] matches actualSoln
	int col;
	for( col = currentCol; col < numCols; col++ )
	    if( true == matchMatrix[row][col] )
		break;

	// If we did not find a true entry for the row starting from col
	//    studentSoln[row] was previously skipped.
	// Ignore this element and find the best match for subsequent elements in student solution
	if( col >= numCols )
	    return bestMatchAux( matchMatrix, row + 1, currentCol, numRows, numCols );

	// The counter that keeps track of the number of contiguous matches
	int matchCount = 0;

	// Find the longest contiguous match section starting at row, col
	// Note that we just verified that matchMatrix[row][col] is true
	while( true == matchMatrix[row][col] )
	{
	    // Increment match count
	    matchCount++;

	    // Increment row and col
	    row++;
	    col++;

	    // If we have gone out of bounds of rows, return matchCount
	    if( row >= numRows )
		return plural( matchCount );

	    // If we have gone out of bounds of cols, return matchCount
	    if( col >= numCols )
		return plural( matchCount );
	}

	// If we got here, we have more rows and cols to go through
	// But, matchMatrix[row][col] was false
	//    call recursively to find the col where matchMatrix[row][col] is true and
	//        find the subsequent contiguous match sequence.
	//    Add current matchCount and return
	return plural( matchCount ) + bestMatchAux( matchMatrix, row, col, numRows, numCols );
    }
*/
