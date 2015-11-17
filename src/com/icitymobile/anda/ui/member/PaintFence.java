//There is a fence with n posts, each post can be painted with one of the k colors.

//You have to paint all the posts such that no more than two adjacent fence posts have the same color.

//Return the total number of ways you can paint the fence.

//Note:
//n and k are non-negative integers.
public class Solution {
    public int numWays(int n, int k) {
        if (n == 0 || k == 0) return 0;
        int dp[] = new int[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                dp[i] = k;
            }
            else if (i == 1) {
                dp[i] = k * k;
            }
            else {
                dp[i] = dp[i - 1] * (k - 1) + dp[i - 2] * (k - 1);
            }
        }
        return dp[n - 1];
    }
}