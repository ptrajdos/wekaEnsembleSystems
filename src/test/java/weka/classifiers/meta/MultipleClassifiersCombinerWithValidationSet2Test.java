/**
 * 
 */
package weka.classifiers.meta;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.DistributionChecker;

/**
 * @author pawel
 *
 */
public class MultipleClassifiersCombinerWithValidationSet2Test
		extends MultipleClassifiersCombinerWithValidationSetTest {

	/**
	 * @param name
	 */
	public MultipleClassifiersCombinerWithValidationSet2Test(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Classifier getClassifier() {
		MultipleClassifiersCombinerWithValidationSet comb =(MultipleClassifiersCombinerWithValidationSet) super.getClassifier();
		comb.setCrossvalidate(true);
		return comb;
	}
	
	public void testCrossvalidation() {
		MultipleClassifiersCombinerWithValidationSet comb = (MultipleClassifiersCombinerWithValidationSet) this.getClassifier();
		checkCrossVal(comb, 10, 11);
		checkCrossVal(comb, 10, 10);
		checkCrossVal(comb, 10, 9);
		checkCrossVal(comb, 10, 2);
		checkCrossVal(comb, 10, 1);
		
	}
	
	
	public void checkCrossVal(MultipleClassifiersCombinerWithValidationSet comb,int numFolds, int numInstances) {
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setNumObjects(numInstances);
		comb.setNumFolds(numFolds);
		
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		try {
			comb.buildClassifier(data);
			double[] distribution = comb.distributionForInstance(testInstance);
			assertTrue("Checking distribution", DistributionChecker.checkDistribution(distribution));
		} catch (Exception e) {
			fail("Check Cross Val. Failed for: numFolds: " + numFolds + "\t numInstances: " + numInstances + " With Exception: " + e.toString());
		}
	}
	
	

}
