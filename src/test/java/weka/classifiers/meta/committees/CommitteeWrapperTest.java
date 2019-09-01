package weka.classifiers.meta.committees;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.CheckClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.TestInstances;

/**
 * Tests for {@link CommitteeWrapper}
 * @author pawel trajdos
 * @since 1.2.0
 * @version 1.2.0
 *
 */

public class CommitteeWrapperTest extends AbstractClassifierTest{


	public CommitteeWrapperTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		return new CommitteeWrapper();
	}
	
	public static Test suite() {
	    return new TestSuite(CommitteeWrapperTest.class);
	  }

	  public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }
	  
	  public void testCommittee() {
		  CommitteeWrapper classifier = (CommitteeWrapper) this.getClassifier();
		  try {
			Classifier[] ens = classifier.getEnsemble();
			assertTrue("Not null", ens !=null);
			assertTrue("Committee Size", ens.length >=1);
			
			Bagging bag = new Bagging();
			int numIters =10;
			bag.setNumIterations(numIters);
			classifier.setClassifier(new Bagging());
			
			TestInstances testInsts = new TestInstances();
			testInsts.setNumInstances(100);
			int numClasses =3;
			testInsts.setNumClasses(numClasses);
			Instances testData = testInsts.generate();
			Instance testInstance = testData.get(0);
			
			classifier.buildClassifier(testData);
			ens = classifier.getEnsemble();
			
			assertTrue("Not null", ens !=null);
			assertTrue("Committee Size", ens.length ==numIters);
			
			double[][] predictions = classifier.distributionForInstanceCommittee(testInstance);
			assertTrue("Distribution test", predictions !=null);
			assertTrue("prediction size", predictions.length == numIters);
			for(int i=0;i<predictions.length;i++) {
				assertTrue("Num Classes", predictions[i].length == numClasses);
			}
			
			double[] classPredictions = classifier.classifyInstanceCommittee(testInstance);
			assertTrue("Class vector length", classPredictions.length == numIters);
			for(int i=0;i<classPredictions.length;i++) {
				assertTrue("Class range", classPredictions[i] >=0 && classPredictions[i]<numClasses);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception has been caught");
		}
		  
	  }

}
