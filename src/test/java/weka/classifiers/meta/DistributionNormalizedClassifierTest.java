package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.tools.tests.WekaGOEChecker;

public class DistributionNormalizedClassifierTest extends AbstractClassifierTest{

	public DistributionNormalizedClassifierTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		return new DistributionNormalizedClassifier();
	}
	
	public static Test suite() {
	    return new TestSuite(DistributionNormalizedClassifierTest.class);
	  }

	  public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }

	  public void testGlobalInfo() {
			WekaGOEChecker check = new WekaGOEChecker();
			check.setObject(this.getClassifier());
			assertTrue("Global Info", check.checkCallGlobalInfo());
			assertTrue("TipTexts", check.checkToolTipsCall());
		}
	

	

}
