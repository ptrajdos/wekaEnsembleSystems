/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.meta.generalOutputCombiners.weightcreators.IWeightCreator;
import weka.classifiers.meta.generalOutputCombiners.weightcreators.WeightCreatorUniform;
import weka.core.Instance;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.UtilsPT;

/**
 * @author pawel trajdos
 * @since 1.12.0
 * @version 1.12.0
 *
 */
public class WeightCreatingCombiner implements OutputCombiner, Serializable, OptionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5773016169195571903L;
	
	protected OutputCombiner baseCombiner = new MeanCombiner();
	
	protected IWeightCreator weightCreator = new WeightCreatorUniform();



	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance)
			throws Exception {
		
		double[] weights = this.weightCreator.createWeights(rawPredictions, testInstance);
		
		return this.getCombinedDistributionForInstance(rawPredictions, testInstance,weights);
	}

	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance,
			double[] weights) throws Exception {
		
		return this.baseCombiner.getCombinedDistributionForInstance(rawPredictions, testInstance,weights);
	}

	@Override
	public double[] getCombinedDistributionForInstance(Classifier[] committee, Instance testInstance) throws Exception {
		double[][] rawPredictions = this.extractCommiteePredictions(committee, testInstance);
		
		double[] weights = this.weightCreator.createWeights(rawPredictions, testInstance);
		
		return this.getCombinedDistributionForInstance(rawPredictions, testInstance,weights);
	}

	@Override
	public double[] getCombinedDistributionForInstance(Classifier[] committee, Instance testInstance, double[] weights)
			throws Exception {
		return this.baseCombiner.getCombinedDistributionForInstance(committee, testInstance,weights);
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\t Combiner object to use "+
		          "(default:" + MeanCombiner.class.toGenericString() +").\n",
			      "BC", 1, "-BC"));
		 
		 newVector.addElement(new Option(
			      "\t Weight creator object to use "+
		          "(default:" + MeanCombiner.class.toGenericString()   +").\n",
			      "WC", 1, "-WC"));
		 
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setBaseCombiner((OutputCombiner) UtilsPT.parseObjectOptions(options, "BC", new MeanCombiner(), OutputCombiner.class));
		
		this.setWeightCreator((IWeightCreator) UtilsPT.parseObjectOptions(options, "WC", new WeightCreatorUniform(), IWeightCreator.class));
		
	}

	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    

	    options.add("-BC");
	    options.add(UtilsPT.getClassAndOptions(this.getBaseCombiner() ));
	    
	    options.add("-WC");
	    options.add(UtilsPT.getClassAndOptions(this.getWeightCreator() ));
	    
	    
	    return options.toArray(new String[0]);
	}

	/**
	 * @return the baseCombiner
	 */
	public OutputCombiner getBaseCombiner() {
		return this.baseCombiner;
	}

	/**
	 * @param baseCombiner the baseCombiner to set
	 */
	public void setBaseCombiner(OutputCombiner baseCombiner) {
		this.baseCombiner = baseCombiner;
	}
	
	public String baseCombinerTipText() {
		return "Base combiner object to use";
	}

	/**
	 * @return the weightCreator
	 */
	public IWeightCreator getWeightCreator() {
		return this.weightCreator;
	}

	/**
	 * @param weightCreator the weightCreator to set
	 */
	public void setWeightCreator(IWeightCreator weightCreator) {
		this.weightCreator = weightCreator;
	}
	
	public String weightCreatorTipText() {
		return "Weight creator object to use";
	}
	
	protected double[][] extractCommiteePredictions(Classifier[] committee, Instance instance) throws Exception{
		
		double[][] predictions = new double[committee.length][];
		
		for(int i=0;i<predictions.length;i++) {
			predictions[i] = committee[i].distributionForInstance(instance);
		}
		
		return predictions;
		
	}

}
