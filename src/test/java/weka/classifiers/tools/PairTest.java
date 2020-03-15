package weka.classifiers.tools;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import weka.core.Utils;
import weka.tools.SerialCopier;

public class PairTest {

	@Test
	public void test() {
		double val1 =1.0;
		double wei1 =6.66;
		Pair pair = new Pair(1.0, 6.66);
		try {
			Pair pCopy = (Pair) SerialCopier.makeCopy(pair);
			assertTrue("GetValue", Utils.eq(val1, pCopy.getValue()));
			assertTrue("Get Weight",Utils.eq(wei1,pCopy.getWeight()));
			
			double val2 = 3.33;
			double wei2 = 1.4;
			
			pCopy.setValue(val2);
			pCopy.setWeight(wei2);
			
			assertTrue("GetValue", Utils.eq(val2, pCopy.getValue()));
			assertTrue("Get Weight",Utils.eq(wei2,pCopy.getWeight()));
		} catch (Exception e) {
			fail("Exception has been caught" + e.toString());
		}
	}

}
