package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.classifiers.rules.ZeroR;
import weka.core.Instances;
import weka.tools.data.RandomDataGenerator;



public class ClusterDrivenEnsembleHeteroClustersTest extends ClusterDrivenEnsembleAbstractTest {

	public ClusterDrivenEnsembleHeteroClustersTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		ClusterDrivenEnsembleHeteroClusters clus = new ClusterDrivenEnsembleHeteroClusters();
		clus.setClassifier(new ZeroR());
		return clus;
	}
	
	public void testWeighted() {
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances testData = gen.generateData();
		
		ClusterDrivenEnsembleHeteroClusters classifier = (ClusterDrivenEnsembleHeteroClusters) this.getClassifier();
		classifier.setWeightedInstances(true);
		String[] options = classifier.getOptions();
		assertTrue("Options not null", options !=null);
		
		try {
			classifier.buildClassifier(testData);
			
		} catch (Exception e) {
			fail("An Exception has been caught: " + e.getMessage());
		}
		
		
	}
	
	 public static Test suite() {
		    return new TestSuite(ClusterDrivenEnsembleHeteroClustersTest.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }



}
