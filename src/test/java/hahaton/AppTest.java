package hahaton;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void TSP()
    {

        long[][] a = {{10000L, 1L, 100L, 10000L},
                      {10000L, 10000L, 10000L, 1},
                      {1L, 10000L, 10000L, 10000L},
                      {10L, 10L, 10L, 10L}};
        ArrayList<ArrayList<Long>> aa = new ArrayList<>();
        ArrayList<Integer> vs = new ArrayList<Integer>();
        for (int i = 0; i < a.length; i++) {
            vs.add(i + 1);
            aa.add(new ArrayList<>());
            for (int j = 0; j < a[i].length; j++) {
                aa.get(i).add(a[i][j]);
            }
        }
        Distances distances = new Distances(aa);
        ArrayList<Integer> res = Solution.TSP(distances, vs);
        //System.out.println(res);
        assertTrue( res.get(0) == 2 && res.get(1) == 0 && res.get(2) == 1 && res.get(3) == 3);
    }
}
