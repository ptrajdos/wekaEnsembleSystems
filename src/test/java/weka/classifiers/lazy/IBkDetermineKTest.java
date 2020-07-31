package weka.classifiers.lazy;

import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;

public class IBkDetermineKTest extends AbstractClassifierTest {

	public IBkDetermineKTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		return new IBkDetermineK();
	}

}
