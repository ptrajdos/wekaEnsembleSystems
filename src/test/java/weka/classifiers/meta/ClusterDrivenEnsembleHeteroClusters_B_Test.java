package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.classifiers.rules.ZeroR;
import weka.core.Instances;
import weka.tools.data.RandomDataGenerator;



public class ClusterDrivenEnsembleHeteroClusters_B_Test extends ClusterDrivenEnsembleHeteroClustersTest {

	
	 public ClusterDrivenEnsembleHeteroClusters_B_Test(String name) {
		super(name);
	}
	 
	 @Override
	public Classifier getClassifier() {
		 ClusterDrivenEnsembleHeteroClusters clus = (ClusterDrivenEnsembleHeteroClusters) super.getClassifier();
		 clus.setWeightedOutput(true);
		
		return clus;
	}

	public static Test suite() {
		    return new TestSuite(ClusterDrivenEnsembleHeteroClusters_B_Test.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }



}
