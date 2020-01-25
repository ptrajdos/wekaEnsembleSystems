package weka.classifiers.meta;

import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;

public class SimpleSingleClassifierEnhancerTest extends AbstractClassifierTest {

	public SimpleSingleClassifierEnhancerTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		return new SimpleSingleClassifierEnhancer();
	}

	

}
