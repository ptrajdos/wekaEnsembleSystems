/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners.weightcreators;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.meta.generalOutputCombiners.MeanCombiner;
import weka.core.Instance;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.core.UtilsPT;
import weka.tools.stats.distributionDistances.DistributionDistanceCalculator;
import weka.tools.stats.distributionDistances.DistributionDistanceCalculatorHellinger;

/**
 * @author pawel trajdos
 * @since 1.12.0
 * @version 1.12.0
 *
 */
public class WeightCreatorDistanceToUniformDist implements IWeightCreator, Serializable, OptionHandler {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7830450327157962037L;
	
	protected DistributionDistanceCalculator distCalculator = new DistributionDistanceCalculatorHellinger();

	@Override
	public double[] createWeights(double[][] predictions, Instance instance) {
		int numClasses = predictions[0].length;
		double[] uniformOutput = new double[numClasses];
		Arrays.fill(uniformOutput, 1.0);
		Utils.normalize(uniformOutput);
		
		int numModels = predictions.length;
		
		double[] weights = new double[numModels];
		double weightSum=0;
		
		for(int i=0;i<numModels;i++) {
			weights[i] = this.distCalculator.calculateDistance(uniformOutput, predictions[i]);
			weightSum += weights[i];
		}
		
		if(Utils.eq(weightSum, 0.0)) {
			Arrays.fill(weights, 0.0);
			weights[0]=1.0;
			return weights;
		}
		
		Utils.normalize(weights);
		
		return weights;
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\t Distance calculator object to use "+
		          "(default:" + DistributionDistanceCalculatorHellinger.class.toGenericString() +").\n",
			      "DC", 1, "-DC"));
		 
		 
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setDistCalculator((DistributionDistanceCalculator) UtilsPT.parseObjectOptions(options, "DC", new DistributionDistanceCalculatorHellinger(), DistributionDistanceCalculator.class));
		
	}

	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    

	    options.add("-DC");
	    options.add(UtilsPT.getClassAndOptions(this.getDistCalculator() ));
	    
	    return options.toArray(new String[0]);
	}

	/**
	 * @return the distCalculator
	 */
	public DistributionDistanceCalculator getDistCalculator() {
		return this.distCalculator;
	}

	/**
	 * @param distCalculator the distCalculator to set
	 */
	public void setDistCalculator(DistributionDistanceCalculator distCalculator) {
		this.distCalculator = distCalculator;
	}
	
	public String distCalculatorTipText() {
		return "Distance calculator to use";
	}
	

}
