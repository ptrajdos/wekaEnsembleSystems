/**
 * 
 */
package weka.classifiers.meta;

import java.util.Random;

import moa.classifiers.core.driftdetection.ADWIN;
import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.classifiers.UpdateableBatchProcessor;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.tools.data.RandomDataGenerator;
import weka.tools.data.WellSeparatedSquares;
import weka.tools.tests.DistributionChecker;
import weka.tools.tests.WekaGOEChecker;

/**
 * @author pawel trajdos
 * @since 1.3.0
 * @version 1.3.0
 *
 */
public class UpdateableClassifierWithInitialBatchADWINTest extends AbstractClassifierTest {

	public UpdateableClassifierWithInitialBatchADWINTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		return new UpdateableClassifierWithInitialBatchADWIN();
	}
	
	public void testEmptySet() {
		FastVector atts = new FastVector();
	     // - numeric
	     atts.addElement(new Attribute("att1"));
	     
	     // - nominal
	   FastVector  attVals = new FastVector();
	   int numClasses =5;
	     for (int i = 0; i < numClasses; i++)
	       attVals.addElement("val" + (i+1));
	     atts.addElement(new Attribute("att2", attVals));
	     
	     
	     Instances data = new Instances("MyRelation", atts, 0);
	     data.setClassIndex(data.numAttributes()-1);
	     
	     
	     UpdateableClassifierWithInitialBatchADWIN testClassifier = new UpdateableClassifierWithInitialBatchADWIN();
	     
	     
	     double[] vals = new double[data.numAttributes()];
	     vals[0]=44.0;
	     vals[1] = 1.0;
	     
	     Instance testInstance = new DenseInstance(1.0, vals);
	     testInstance.setDataset(data);
	     
	     
	     
	     
	     try {
	    	 testClassifier.buildClassifier(data);
	    	 double[] prediction = testClassifier.distributionForInstance(testInstance);
	    	 double expectedValue = 1.0/numClasses;
	    	 
	    	 assertTrue("Number of classes: ", numClasses == prediction.length);
	    	 
	    	 for(int i=0;i<prediction.length;i++)
	    		 assertTrue("DistributionValues", Utils.eq(prediction[i], expectedValue));
	    	 
	    	 assertTrue("Is still training", testClassifier.training);
	    	 
	    	 //Generate instances
	    	 
	    	 int trainingBatchSize = testClassifier.getInitialBatchSize();
	    	 Random rnd = new Random(0);
	    	 for(int i=0;i<trainingBatchSize;i++) {
	    		 vals = new double[2];
	    		 vals[0] = rnd.nextDouble();
	    		 vals[1] = rnd.nextInt(numClasses);
	    		 testInstance = new DenseInstance(1.0, vals);
	    	     testInstance.setDataset(data);
	    	     data.add(testInstance);
	    	 }
	    	 
	    	 testClassifier.buildClassifier(data);
	    	 assertFalse("Is still training", testClassifier.training);
	    	 
	    	 prediction = testClassifier.distributionForInstance(testInstance);
	    	 assertTrue("Number of classes", prediction.length == numClasses);
	    	 
	    	 
	     }catch(Exception e) {
	    	 fail("Exception has been caught: " + e);
	     }
	}
	
	public void testTipTexts() {
		WekaGOEChecker check = new WekaGOEChecker();
		check.setObject(this.getClassifier());
		assertTrue("Check Tip Texts", check.checkCallGlobalInfo());
		assertTrue("Check Tip Texts", check.checkToolTipsCall());
	}

	public void testUpdateableTraining() {
		this.updateableTest(getClassifier());
		
		UpdateableClassifierWithInitialBatchADWIN classifier = (UpdateableClassifierWithInitialBatchADWIN) this.getClassifier();
		classifier.setClassifier(new NaiveBayesUpdateable());
		this.updateableTest(classifier);
		
	}
	
	public void updateableTest(Classifier classifieru) {
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		UpdateableClassifierWithInitialBatchADWIN classifier = (UpdateableClassifierWithInitialBatchADWIN) classifieru;
		classifier.setInitialBatchSize(10);
		
		try {
		for (Instance instance : data) {
			classifier.updateClassifier(instance);
			
		}
		double[] response = classifier.distributionForInstance(testInstance);
		assertTrue("Batch update distribution check", DistributionChecker.checkDistribution(response));
		}catch(Exception e) {
			fail("Batch update. An exception has been caught: " + e.toString());
		}
	}
	
	public void testForceBatchFinish() {
		RandomDataGenerator gen = new RandomDataGenerator();
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		UpdateableClassifierWithInitialBatchADWIN classifier = (UpdateableClassifierWithInitialBatchADWIN) this.getClassifier();
		try {
		for(int i=0;i<5;i++) {
			classifier.updateClassifier(data.get(i));
		}
		classifier.batchFinished();
		double[] response = classifier.distributionForInstance(testInstance);
		assertTrue("Batch update distribution check", DistributionChecker.checkDistribution(response));
		}catch(Exception e) {
			fail("Force Batch Finish. Exception has been caught: " + e.toString());
		}
	}
	
	public void  testADWINSubObject() {
		UpdateableClassifierWithInitialBatchADWIN adwClass = (UpdateableClassifierWithInitialBatchADWIN) this.getClassifier();
		ADWIN adw = adwClass.adwin;
		int numElements=100;
		boolean isChanged=false;
		for(int i=0;i<numElements;i++) {
			isChanged = adw.setInput(0);
			if(isChanged)
				fail("No distribution change should have appeared here!");
		}
		
		boolean anyChangeReported=false;
		for(int i=0;i<numElements;i++) {
			isChanged = adw.setInput(1);
			if(isChanged)
				anyChangeReported=true;
		}
		
		assertTrue("A change should be reported here", anyChangeReported);
	}
	
	public void testAdwinClassifier() {
		WellSeparatedSquares gen = new WellSeparatedSquares();
		gen.setNumClasses(2);
		gen.setNumObjects(100);
		int numInstances = gen.getNumObjects();
		Instances data = gen.generateData();
		gen.setNumObjects(300);
		Instances valData = gen.generateData();
		Instances classRevData = this.changeClassAssignment(valData);
		UpdateableClassifierWithInitialBatchADWIN adwClassifier = (UpdateableClassifierWithInitialBatchADWIN) this.getClassifier();
		adwClassifier.setClassifier(new J48());
		int preInstNum, postInstNum;
		boolean anyChangeRecorded=false;
		try {
			adwClassifier.buildClassifier(data);
			for(int i=0;i<numInstances;i++) {
				preInstNum = adwClassifier.adwin.getWidth();
				adwClassifier.updateClassifier(classRevData.get(i));
				postInstNum = adwClassifier.adwin.getWidth();
				if(preInstNum > postInstNum)
					anyChangeRecorded=true;
			}
				
			assertTrue("Change should have been recorded", anyChangeRecorded);
			
		} catch (Exception e) {
			fail("An exception has been caught: " + e.getMessage());
		}
	}
	
	protected Instances changeClassAssignment(Instances instances) {
		Instances newInsts = new Instances(instances);
		int insNum = newInsts.numInstances();
		Instance tmpInstance;
		double classVal;
		for(int i=0;i<insNum;i++) {
			tmpInstance =newInsts.get(i);
			classVal = tmpInstance.classValue();
			classVal = classVal==0? 1:0;
			tmpInstance.setClassValue(classVal);
		}
		
		return newInsts;
	}
	
	public void testForceBatchFinish2() {
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setNumObjects(200);
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		UpdateableClassifierWithInitialBatchADWIN classifier = (UpdateableClassifierWithInitialBatchADWIN) this.getClassifier();
		try {
			classifier.buildClassifier(data);
			for(int i=0;i<5;i++) {
				classifier.updateClassifier(data.get(i));
			}
		classifier.batchFinished();
		double[] response = classifier.distributionForInstance(testInstance);
		assertTrue("Batch update distribution check", DistributionChecker.checkDistribution(response));
		}catch(Exception e) {
			fail("Force Batch Finish. Exception has been caught: " + e.toString());
		}
	}
	
	public void testUpdateableBatchProcessor() {
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setNumNumericAttributes(0);
		gen.setNumNominalAttributes(0);
		gen.setNumStringAttributes(10);
		
		Instances data = gen.generateData();
		Instance testInstance = data.get(0);
		
		UpdateableClassifierWithInitialBatchADWIN upda = (UpdateableClassifierWithInitialBatchADWIN) this.getClassifier();
		UpdateableBatchProcessor updBatch = new NaiveBayesMultinomialText();
		
		upda.setClassifier((Classifier) updBatch);
		double[] response; 
		try {
			upda.buildClassifier(data);
			response = upda.distributionForInstance(testInstance);
			assertTrue("Prediction", DistributionChecker.checkDistribution(response));
		} catch (Exception e) {
			fail("An exception has been caught: "  + e.getLocalizedMessage());
		}
	}
	
	

}
