package weka.classifiers.meta;

import weka.classifiers.Classifier;
import weka.classifiers.tools.FailingClassifier2;
import weka.classifiers.trees.J48;

public class ClassifierWithAlternativeModel3Test extends ClassifierWithAlternativeModelTest {

	

	public ClassifierWithAlternativeModel3Test(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		ClassifierWithAlternativeModel alt = (ClassifierWithAlternativeModel) super.getClassifier();
		alt.setClassifier(new FailingClassifier2());
		alt.setAlternativeModelPrototype(new J48());
		return alt;
	}
	
	
	
	
	

}
