package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.classifiers.meta.generalOutputCombiners.MeanCombinerNumClass;
import weka.classifiers.meta.generalOutputCombiners.MedCombiner;
import weka.classifiers.meta.generalOutputCombiners.OutputCombiner;
import weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass;
import weka.core.RevisionHandler;
import weka.tools.tests.WekaGOEChecker;

public class CustomizableBaggingClassifier2Test extends BaggingTest {

	public CustomizableBaggingClassifier2Test(String name) {
		super(name);
	}
	
	/** Creates a default Classifier */
	  public Classifier getClassifier() {
	    return new CustomizableBaggingClassifier2();
	  }

	  public static Test suite() {
		    return new TestSuite(CustomizableBaggingClassifier2Test.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }
	  
	  public void testRevision() {
		  Classifier classifier = this.getClassifier();
		  if(classifier instanceof RevisionHandler) {
			  String revision = ((RevisionHandler) classifier).getRevision();
			  assertTrue("Revision not null", revision !=null);
			  assertTrue("Revision Length", revision.length() >0);
		  }else {
			  fail("No revision handler");
		  }
	  }
	  
	  public void testTipTexts() {
		  WekaGOEChecker check = new WekaGOEChecker();
		  check.setObject(this.getClassifier());
		  assertTrue("Call tip texts", check.checkToolTipsCall());
	  }
	  
	  public void testCombiners() {
		  CustomizableBaggingClassifier2 bag2 = (CustomizableBaggingClassifier2) this.getClassifier();
		  OutputCombiner comb = new MedCombiner();
		  bag2.setClassificationCombiner(comb);
		  assertTrue("Classification combiner.", comb.equals(bag2.getClassificationCombiner()) );
		  
		  OutputCombinerNumClass c2 = new MeanCombinerNumClass();
		  bag2.setRegressionCombiner(c2);
		  
		  assertTrue("Classification combiner.", c2.equals(bag2.getRegressionCombiner()) );
		  
	  }

	

}
