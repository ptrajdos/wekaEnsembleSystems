/**
 * 
 */
package weka.classifiers.meta;

import java.util.Random;

import weka.classifiers.AbstractClassifierTest;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.datagenerators.clusterers.SubspaceCluster;

/**
 * @author pawel trajdos
 * @since 1.3.0
 * @version 1.3.0
 *
 */
public class UpdateableClassifierWithInitialBatchTest extends AbstractClassifierTest {

	public UpdateableClassifierWithInitialBatchTest(String name) {
		super(name);
	}

	@Override
	public Classifier getClassifier() {
		return new UpdateableClassifierWithInitialBatch();
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
	     
	     
	     UpdateableClassifierWithInitialBatch testClassifier = new UpdateableClassifierWithInitialBatch();
	     
	     
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
	    		 vals = new double[2]
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
	
	

	

}
