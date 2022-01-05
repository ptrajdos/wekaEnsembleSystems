package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.meta.simpleVotingLikeCombiners.OutputCombinerGeneralBased;
import weka.core.Instances;
import weka.core.Option;
import weka.core.UtilsPT;

public class RandomSubSpaceClassifierTuner extends SimpleSingleClassifierEnhancer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7954045886694683041L;
	
	protected double attribFraction = 0.5;
	protected double overproductRate =1.0;
	
	public RandomSubSpaceClassifierTuner() {
		super();
		this.m_Classifier = new RandomSubSpace();
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		
		if(!(this.m_Classifier instanceof RandomSubSpace))
			throw new IllegalArgumentException("Internal classifier is not a RandomSubspace instance");
		
		this.tuneParameters(data);
		
		super.buildClassifier(data);

	}
	
	protected void tuneParameters(Instances data) {
		
		int numInstances = data.numInstances();
		int numAttributes = data.numAttributes();
		
		double subspaceSize = (this.attribFraction*numInstances)/numAttributes;
		((RandomSubSpace)this.m_Classifier).setSubSpaceSize(subspaceSize);
		
		int iterNum = (int) Math.ceil(numInstances*this.overproductRate/this.attribFraction) ;
		
		((RandomSubSpace)this.m_Classifier).setNumIterations(iterNum);
	}

	@Override
	public String globalInfo() {
		return "Classifier that tunes RandomSubspace parameters";
	}

	/**
	 * @return the attribFraction
	 */
	public double getAttribFraction() {
		return this.attribFraction;
	}

	/**
	 * @param attribFraction the attribFraction to set
	 */
	public void setAttribFraction(double attribFraction) {
		this.attribFraction = attribFraction;
	}
	
	public String attribFractionTipText() {
		return "Determines the fraction of instances used to generate attributes.";
	}

	/**
	 * @return the overproductRate
	 */
	public double getOverproductRate() {
		return this.overproductRate;
	}

	/**
	 * @param overproductRate the overproductRate to set
	 */
	public void setOverproductRate(double overproductRate) {
		this.overproductRate = overproductRate;
	}
	
	public String overproductRateTipText() {
		return "Coefficients determines a multiplier of classifiers to build.";
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tAttribute fraction "+
		          "(default:" + 0.5 + ").\n",
			      "ATF", 1, "-ATF"));
		 
		 newVector.addElement(new Option(
			      "\tOverproduct rate "+
		          "(default:" + 1.0 + ").\n",
			      "OPR", 1, "-OPR"));
		 
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		 
		 return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setAttribFraction(UtilsPT.parseDoubleOption(options, "ATF", 0.5));
		
		this.setOverproductRate(UtilsPT.parseDoubleOption(options, "OPR", 1.0));
		
		super.setOptions(options);
	}

	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();

	    options.add("-ATF");
	    options.add(""+this.getAttribFraction());
	    
	    options.add("-OPR");
	    options.add(""+this.getOverproductRate());
	   
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}
	
	
	
	

}
