package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.clusterers.ClassSpecificClustererClassCombined;



public class ClusterDrivenEnsembleHeteroClusters_C_Test extends ClusterDrivenEnsembleHeteroClustersTest {

	
	 public ClusterDrivenEnsembleHeteroClusters_C_Test(String name) {
		super(name);
	}
	 
	 @Override
	public Classifier getClassifier() {
		 ClusterDrivenEnsembleHeteroClusters clus = (ClusterDrivenEnsembleHeteroClusters) super.getClassifier();
		 clus.setClusterer(new ClassSpecificClustererClassCombined());
		 //clus.setClassifier(new SMO());
		
		return clus;
	}

	public static Test suite() {
		    return new TestSuite(ClusterDrivenEnsembleHeteroClusters_C_Test.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }



}
