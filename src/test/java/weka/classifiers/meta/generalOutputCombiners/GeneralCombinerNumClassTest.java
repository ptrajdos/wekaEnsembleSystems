package weka.classifiers.meta.generalOutputCombiners;

import java.util.Arrays;

import junit.framework.TestCase;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;
import weka.tools.SerialCopier;
import weka.tools.data.RandomDataGenerator;

public abstract class GeneralCombinerNumClassTest extends TestCase {

	public abstract OutputCombinerNumClass getCombiner();
	
	public void testSerialization() {
		try {
			OutputCombinerNumClass comb = (OutputCombinerNumClass) SerialCopier.makeCopy(getCombiner());
		} catch (Exception e) {
			fail("Serialization Failed");
		}
	}
	
	public void testCheckCompatibility() {
		GeneralCombinerNumClass comb = (GeneralCombinerNumClass) this.getCombiner();
		RandomDataGenerator genFail = new RandomDataGenerator();
		genFail.setNumNominalAttributes(0);
		Instances failData  =genFail.generateData();
		
		try {
			int numVals=10;
			comb.checkCompatibility(new double[numVals], failData.get(0), new double[numVals]);
			fail("Invalid nominal class data. No exception has been caught.");
		} catch (Exception e) {
		}
		
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setAddClassAttrib(false);
		gen.setNumNominalAttributes(0);
		
		Instances data = gen.generateData();
		data.setClassIndex(0);
		Instance testInstance = data.get(0);
		int numPreds = 10;
		
		
		try {
			comb.checkCompatibility(new double[numPreds], testInstance, new double[numPreds]);
		} catch (Exception e) {
			fail("Compatibility check. Exception has been caught: " +e.toString() );
		}
		
		try {
			comb.checkCompatibility(new double[numPreds], testInstance, new double[numPreds+1]);
			fail("Incompatible predictions and weights. No exceptoin has been caught");
		} catch (Exception e) {
		}
		
	}
	
	public void testGetClass() {
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setAddClassAttrib(false);
		gen.setNumNominalAttributes(0);
		Instances data = gen.generateData();
		data.setClassIndex(0);
		Instance testInstance = data.get(0);
		
		Classifier[] committee = {new LinearRegression(), new LinearRegression()};
		
		
		double[] weights = new double[committee.length];
		Arrays.fill(weights, 1.0);
		
		
		OutputCombinerNumClass comb = this.getCombiner();
		
		try {
			for (Classifier classifier : committee) {
				classifier.buildClassifier(data);
			}
			double classVal = comb.getClass(committee, testInstance);
			classVal = comb.getClass(committee, testInstance, weights);
		} catch (Exception e) {
			fail("Class combination. Exception has been caught: " + e.toString());
		}
		
	}
}
