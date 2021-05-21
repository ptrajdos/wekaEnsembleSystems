package weka.classifiers.meta;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.tools.data.RandomDataGenerator;
import weka.tools.data.RandomDoubleGenerator;
import weka.tools.data.RandomDoubleGeneratorGaussian;
import weka.tools.tests.DistributionChecker;
import weka.tools.tests.WekaGOEChecker;

public abstract class ClusterDrivenEnsembleAbstractTest extends AbstractClassifierTest {

	public ClusterDrivenEnsembleAbstractTest(String name) {
		super(name);
	}
	
	public void testTipTexts() {
		WekaGOEChecker check = new WekaGOEChecker();
		check.setObject(this.getClassifier());
		if(check.checkGlobalInfo())
			assertTrue("Global Info call", check.checkCallGlobalInfo());
		
		if(check.checkToolTips())
			assertTrue("Tip Texts call", check.checkToolTipsCall());
	}
	
	public void testNoInstances() {
		RandomDataGenerator gen = new RandomDataGenerator();
		gen.setNumNominalAttributes(0);
		gen.setNumObjects(0);
		
		Instances dataset = gen.generateData();
		
		Classifier cla = this.getClassifier();
		  
		gen.setNumObjects(10);
		Instances data2 = gen.generateData();
		Instance testInstance = data2.get(0);
		
		try {
			cla.buildClassifier(dataset);
			
			double[] distribution = cla.distributionForInstance(testInstance);
			assertTrue("Intance test: ", DistributionChecker.checkDistribution(distribution));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("An Exception has been caught: " + e.getLocalizedMessage());
		}
	}
	
	public void testOnCondensedData() {
		 Classifier classifier = this.getClassifier();
		 RandomDataGenerator gen = new RandomDataGenerator();
		 gen.setNumNominalAttributes(0);
		 gen.setNumStringAttributes(0);
		 gen.setNumDateAttributes(0);
		 RandomDoubleGenerator doubleGen = new RandomDoubleGeneratorGaussian();
		 doubleGen.setDivisor(10000.0);
		 gen.setDoubleGen(doubleGen );
		 
		 Instances dataset = gen.generateData();
		 try {
			classifier.buildClassifier(dataset);
			for (Instance instance : dataset) {
				double[] distribution = classifier.distributionForInstance(instance);
				assertTrue("Check distribution", DistributionChecker.checkDistribution(distribution));
			}
			
		} catch (Exception e) {
			fail("An exception has been caught " + e.getMessage());
		}
	 }
	
	public void testWithMulticlassClassifier() {
		 Classifier classifier = this.getClassifier();
		 RandomDataGenerator gen = new RandomDataGenerator();
		 gen.setNumNominalAttributes(0);
		 gen.setNumStringAttributes(0);
		 gen.setNumDateAttributes(0);
		 gen.setNumClasses(3);
		 Instances data = gen.generateData();
		 
		 MultiClassClassifier multiClassifier = new MultiClassClassifier();
		 multiClassifier.setClassifier(classifier);
		 SelectedTag selTag = new SelectedTag(MultiClassClassifier.METHOD_1_AGAINST_1, MultiClassClassifier.TAGS_METHOD);
		multiClassifier.setMethod(selTag );
		 
		 try {
			multiClassifier.buildClassifier(data);
			for (Instance instance : data) {
				double[] distribution = multiClassifier.distributionForInstance(instance);
				assertTrue("Check distribution", DistributionChecker.checkDistribution(distribution));
			}
		} catch (Exception e) {
			fail("An exception has been caught " + e.getMessage());
		}
	}
	
	public void testOneAttribute() {
		 Classifier classifier = this.getClassifier();
		 RandomDataGenerator gen = new RandomDataGenerator();
		 gen.setNumClasses(2);
		 gen.setNumNominalAttributes(0);
		 gen.setNumDateAttributes(0);
		 gen.setNumStringAttributes(0);
		 gen.setNumNumericAttributes(1);
		 Instances data = gen.generateData();
		 Instance  testInstance = data.get(0);
		 
		 try {
			classifier.buildClassifier(data);
			double[] distribution = classifier.distributionForInstance(testInstance);
			assertTrue("Checking distribution, one attribute",DistributionChecker.checkDistribution(distribution));
		} catch (Exception e) {
			fail("An exception has been caught" + e.getMessage());
		}
		 
	 }
		  
	  
	  public static Test suite() {
		    return new TestSuite(ClusterDrivenEnsembleAbstractTest.class);
	  }

	  public static void main(String[] args){
		    junit.textui.TestRunner.run(suite());
	  }

	

}
