package weka.classifiers.meta.generalOutputCombiners.weightcreators;

import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.tools.data.RandomDataGenerator;
import weka.tools.tests.OptionHandlerChecker;
import weka.tools.tests.SerializationChecker;
import weka.tools.tests.WekaGOEChecker;

public abstract class IWeightCreatorTest extends TestCase {
	
	protected Random rnd = new Random(0);
	protected boolean normalizedWeights=true;

	public abstract IWeightCreator getWeightCreator();
	
	
	public void testWeights() {
		IWeightCreator creator = this.getWeightCreator();
		
		int[] models = {1,3,5};
		int[] classes = {2,3,5};
		
		for(int m =0;m<models.length;m++) {
			for(int c =0;c<classes.length;c++) {
				
				RandomDataGenerator gen = new RandomDataGenerator();
				gen.setNumClasses(classes[c]);
				Instances data = gen.generateData();
				Instance tmpInstance = data.get(0);
				
				
				double[][] predictions = this.createRandomPredictions(models[m], classes[c]);
				
				double[] weights = creator.createWeights(predictions, tmpInstance);
				assertTrue("Weights Length", weights.length == models[m]);
				this.checkWeights(weights);
				
				double[][] uniformPredictions = this.createUniformPredictions(models[m], classes[c]);
				weights = creator.createWeights(uniformPredictions, tmpInstance);
				assertTrue("Weights Length", weights.length == models[m]);
				this.checkWeights(weights);
			}
		}
		
	}
	
	protected void checkWeights(double[] weights) {
		
		for(int i=0;i<weights.length;i++) {
			
			assertFalse("Not NaN", Double.isNaN(weights[i]));
			assertTrue("Is finite", Double.isFinite(weights[i]));
			if(this.normalizedWeights) {
				assertTrue("Lower Bound", weights[i] >= 0);
				assertTrue("Upper Bound", weights[i] <= 1);
			}
		}
		
		double weightSum=Utils.sum(weights);
		assertFalse("Sum Not NaN", Double.isNaN(weightSum));
		assertTrue("Sum Finite", Double.isFinite(weightSum));
		if(this.normalizedWeights) {
			assertEquals(1.0, weightSum, 1E-3);
		}
		
	}
	
	double[][] createRandomPredictions(int numModels, int numClasses){
		double[][] predictions = new double[numModels][numClasses];
				
		for(int m =0 ;m<numModels;m++){
			for(int c =0;c<numClasses;c++)
				predictions[m][c] = this.rnd.nextDouble();
			Utils.normalize(predictions[m]);
		}
		return predictions;
	}
	
	double[][] createUniformPredictions(int numModels, int numClasses){
		double[][] predictions = new double[numModels][numClasses];
		
		for(int i=0;i<numModels;i++) {
			Arrays.fill(predictions[i], 1.0);
			Utils.normalize(predictions[i]);
		}
		
		return predictions;
	}
	
	public void testSerialization() {
		IWeightCreator creator = this.getWeightCreator();
		assertTrue("Serializations" , SerializationChecker.checkSerializationCopy(creator));
	}
	
	public void testOptions() {
		
		IWeightCreator creator = this.getWeightCreator();
		
		if(creator instanceof OptionHandler) {
			OptionHandler creat = (OptionHandler) creator;
			OptionHandlerChecker.checkOptions(creat);
			WekaGOEChecker checker = new WekaGOEChecker();
			checker.setObject(creat);
			assertTrue("Tool Tips test",checker.checkToolTips());
			assertTrue("Tool Tips call test",checker.checkToolTipsCall());
		}
		
	}

}
