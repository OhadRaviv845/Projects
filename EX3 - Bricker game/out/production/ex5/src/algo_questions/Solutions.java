package algo_questions;
import java.util.Arrays;


public class Solutions {

    // Taking two arrays, we sort both, then iterating and trying to fit the biggest assignment to the bigget time slot.
    // In this problem it is not said that any task has a different value, nor does it say that we can add time slots or
    // two tasks in one, so our purpose is just to return the biggest amount of tasks possible to execute.
    public static int alotStudyTime(int[] tasks, int[] timeSlots) {
        int i,j;
        Arrays.sort(tasks);
        Arrays.sort(timeSlots);
        j = 0;
        for (i = 0; i < tasks.length; i++) {
            while (j < timeSlots.length && timeSlots[j] < tasks[i])
                j++;
            if (j == timeSlots.length)
                break;
            ++j;
        }
        return i;
    }

    public static int bucketWalk(int n) {
        int fib = 1, prevFib = 0, nextFib;
        for (int i = 0; i < n; i++) {
            nextFib = fib + prevFib;
            prevFib = fib;
            fib = nextFib;
        }
        return fib;
    }

    // We will want to compare the shortest leap track possible.
    public static int minLeap(int[] leapNum) {
        // Keeping our last jump max step, our current max step, and our next possible max step.
        int prevMax = 0, currMax = leapNum[0], nextMax;
        int step;
        // Initiating the step as 1 to avoid the case of zero jumps.
        for (step = 1; currMax < leapNum.length; step++) {
            // Setting next step's max leap as our current and then comparing it with all options.
            nextMax = currMax;
            for (int i = prevMax+1; i <= currMax; i++) {
                if (i + leapNum[i] > nextMax)
                    nextMax = i + leapNum[i];
            }
            // After the leap prev becomes curr, next becomes curr, then another iteration etc.
            prevMax = currMax;
            currMax = nextMax;
        }
        if (prevMax >= leapNum.length-1)
            return step-1;
        return step;
    }

    public static int numTrees(int n) {
        // Declaring an empty array of n tree sizes.
        int[] nt = new int[n+1];
        // For the empty option of tree size = 0, there is only one possibility.
        nt[0] = 1;
        // Iterating through all the different possible tree sizes.
        for (int i = 1; i <= n; i++) {
            int sum = 0;
            // Iterating over all different possibilites for the root of the tree.
            for (int j = 0; j < i; j++)
                sum += nt[j]*nt[i-j-1];
            // Variable sum calculates all possible options for unique trees.
            nt[i] = sum;
        }
        return nt[n];
    }
}
