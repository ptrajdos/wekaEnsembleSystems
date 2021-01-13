package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;



public class FilteredClassifierUpdateableTest extends FilteredClassifierTest {

	public FilteredClassifierUpdateableTest(String name) {
		super(name);
	}
	
	/** Creates a default FilteredClassifier */
	  public Classifier getClassifier() {
		FilteredClassifier fClass = new FilteredClassifierUpdateable();
		fClass.setClassifier(new NaiveBayes());
	    return fClass;
	  }

	  public static Test suite() {
	    return new TestSuite(FilteredClassifierUpdateableTest.class);
	  }

	  public static void main(String[] args){
	    junit.textui.TestRunner.run(suite());
	  }


	

}
