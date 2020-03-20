package weka.classifiers.meta;

import weka.classifiers.Classifier;
import weka.classifiers.tools.FailingClassifier;
import weka.classifiers.trees.J48;

public class ClassifierWithAlternativeModel2Test extends ClassifierWithAlternativeModelTest {

	

	public ClassifierWithAlternativeModel2Test(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		ClassifierWithAlternativeModel alt = (ClassifierWithAlternativeModel) super.getClassifier();
		alt.setClassifier(new FailingClassifier());
		alt.setAlternativeModelPrototype(new J48());
		return alt;
	}
	
	
	
	
	

}
