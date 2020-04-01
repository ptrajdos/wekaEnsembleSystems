/**
 * 
 */
package weka.classifiers.meta;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.J48;
import weka.tools.tests.RandomDataChecker;

/**
 * @author pawel gtrajdos
 * @since 1.6.0
 * @version 1.6.0
 * 
 *
 */
public class BKSClassifier_Crossvalidation_Test extends MultipleClassifiersCombinerWithValidationSet2Test{

	public BKSClassifier_Crossvalidation_Test(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Classifier getClassifier() {
		BKSClassifier bks = new BKSClassifier();
		ClassifierWithAlternativeModel alt = new ClassifierWithAlternativeModel();
		alt.setClassifier(new NaiveBayes());
		bks.setClassifiers(new Classifier[]{new J48(),new NaiveBayes(),new ZeroR(), new IBk(),alt});
		bks.setCrossvalidate(true);
		return bks;
	}

	 public void testAgainstRandomData() {
		 BKSClassifier bks =  (BKSClassifier) this.getClassifier();
		 assertTrue("Total Random data", RandomDataChecker.checkAgainstRandomData(bks, -0.1, 0.1));
		 assertTrue("Test, Well-separated data", RandomDataChecker.checkAgainstWellSeparatedData(bks, 0.9));
	 }
	
	

}
