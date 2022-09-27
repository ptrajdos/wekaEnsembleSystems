package weka.classifiers.meta.generalOutputCombiners;

import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionHandler;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.DistributionChecker;

public abstract class GeneralCombinerTest extends OutputCombinerTest {

	
	public void testRevision() {
		OutputCombiner comb = this.getCombiner();
		if(comb instanceof RevisionHandler) {
			String revision = ((RevisionHandler) comb).getRevision();
			assertTrue("Revision not null", revision !=null);
			assertTrue("Revision string length", revision.length()>0);
		}
	}
	
	public void testCompatibility() {
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		int numClasses = data.numClasses();
		
		GeneralCombiner comb = (GeneralCombiner) this.getCombiner();
		
		int numPredictors =10;
		double[][] rawPreds = new double[10][numClasses];
		double[] weights = new double[numPredictors];
		
		try {
			comb.checkCompatibility(rawPreds, testInstance);
			comb.checkCompatibility(rawPreds, testInstance, weights);
		}catch(Exception e) {
			fail("check compatibility Failed:"  + e.toString());
		}
		
		try {
			comb.checkCompatibility(rawPreds, testInstance,new double[numPredictors+1]);
			fail("Invalid weights no exception");
		} catch (Exception e) {
			// OK
		}
		
		try {
			comb.checkCompatibility(new double[numPredictors][numClasses+1], testInstance);
			fail("Invalid raw predictions. No exception has been thrown");
		} catch (Exception e) {
			//OK
		}
		
		try {
			comb.checkCompatibility(new double[numPredictors][numClasses+1], testInstance, weights);
			fail("Testi instance and raw predictions incompatible. No exception has been thrown.");
		} catch (Exception e) {
			//OK
		}
		
	}
	
	
	
	public void testNormalizaOutput() {
		GeneralCombiner comb = (GeneralCombiner) this.getCombiner();
		int[] numClasses = {1,2,3,10};
		for(int i=0;i<numClasses.length;i++) {
			double[] properDist = new double[numClasses[i]];
			Arrays.fill(properDist, 1.0);
			
			comb.normalizeOutput(properDist);
			assertTrue("Normalization prperties", DistributionChecker.checkDistribution(properDist));
			
			double[] zeroDist = new double[numClasses[i]];
			Arrays.fill(zeroDist, 0.0);
			comb.normalizeOutput(zeroDist);
			assertTrue("Normalization prperties", DistributionChecker.checkDistribution(zeroDist));
			
			
		}
	}
	
	public void testParallelPredictions() {
		GeneralCombiner comb = (GeneralCombiner) this.getCombiner();
		comb.setNumExecutionSlots(5);
		
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		
		int N = 10;
		Classifier[] classifiers = new Classifier[N];
		for(int i=0;i<classifiers.length;i++) {
			classifiers[i] = new J48();
			try {
				classifiers[i].buildClassifier(data);
			} catch (Exception e) {
				fail("An exception has been caught: " + e.getMessage());
			}
		}
		
		try {
			double[] distrib = comb.getCombinedDistributionForInstance(classifiers, testInstance);
			assertTrue("Distribution properties", DistributionChecker.checkDistribution(distrib));
		} catch (Exception e) {
			fail("An exception has been caught: " + e.getMessage());
		}
		
		
	}
	
}
