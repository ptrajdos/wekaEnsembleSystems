package weka.classifiers.tools;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import weka.core.Utils;

public class StatsUtilsTest {

	@Test
	public void testWeiMedian() {
		List<Pair> p1 = new LinkedList<Pair>();
		p1.add(new Pair(1,1.0/3.0));
		p1.add(new Pair(2,1.0/3.0));
		p1.add(new Pair(3,1.0/3.0));
		
		try {
			double median = StatsUtils.weightedMedian(p1);
			assertTrue("Median Odd: ", Utils.eq(median, 2.0));
			
		} catch (Exception e) {
			fail("Exceptoin has been caught" + e.toString());
		}
		
		List<Pair> p2 = new LinkedList<Pair>();
		p2.add(new Pair(1,1.0));
		p2.add(new Pair(2,1.0));
		p2.add(new Pair(3,1.0));
		
		try {
			double median = StatsUtils.weightedMedian(p2);
			fail("No exception");
		} catch (Exception e) {
			assertTrue(true);
		}
		
		
		
		
	}
	
	public void testCreate() {
		StatsUtils ut = new StatsUtils();
		assertTrue("Not null", ut!=null);
	}

}
