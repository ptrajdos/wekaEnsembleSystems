/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import weka.classifiers.RandomizableMultipleClassifiersCombiner;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;
import weka.core.UtilsPT;
import weka.tools.GlobalInfoHandler;
import weka.tools.data.InstancesOperator;

/**
 * MultipleClassifiersCombiner with validation set
 * @author pawel trajdos
 * @since 1.4.0
 * @version 1.4.0
 *
 */
public abstract class MultipleClassifiersCombinerWithValidationSet extends RandomizableMultipleClassifiersCombiner implements GlobalInfoHandler {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -322291974455659403L;
	
	protected Instances validationSet;
	
	protected List<double[]>[] validationResponses;
	
	
	
	protected double splitFactor=0.6;
	
	protected boolean crossvalidate=false;
	
	protected int numFolds=2;
	
	protected boolean upToDate=false;
	
	

	/**
	 * 
	 */
	public MultipleClassifiersCombinerWithValidationSet() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @return the splitFactor
	 */
	public double getSplitFactor() {
		return this.splitFactor;
	}



	/**
	 * @param splitFactor the splitFactor to set
	 */
	public void setSplitFactor(double splitFactor) {
		this.splitFactor = splitFactor;
	}
	
	public String splitFactorTipText() {
		return "Split factor";
	}



	/**
	 * @return the crossvalidate
	 */
	public boolean isCrossvalidate() {
		return this.crossvalidate;
	}



	/**
	 * @param crossvalidate the crossvalidate to set
	 */
	public void setCrossvalidate(boolean crossvalidate) {
		this.crossvalidate = crossvalidate;
	}
	
	public String crossvalidateTipText() {
		return "Determines whether crossvalidation is performed";
	}



	/**
	 * @return the numFolds
	 */
	public int getNumFolds() {
		return this.numFolds;
	}
	
	public String numFoldsTipText() {
		return "Number of folds in crossvalidation";
	}
	
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		
		if(!this.m_DoNotCheckCapabilities)
			this.getCapabilities().testWithFail(data);
		
		this.validationResponses = new List[this.m_Classifiers.length];
		for(int i=0;i<this.validationResponses.length;i++) {
			this.validationResponses[i] = new LinkedList<double[]>();
		}
		
		if(this.crossvalidate) {
			this.trainCrossvalidation(data);
		}
		else {
			this.trainSplit(data);
		}
		
		/**
		 * Build the entire ensemble
		 */
		this.trainBaseClassifiers(data);
		
		
	}
	
	public void trainCrossvalidation(Instances data) throws Exception {
		this.validationSet = new Instances(data, 0);
		Instances tmpSet = new Instances(data);
		tmpSet.stratify(this.numFolds);
		
		
		Instances train, val;
		for(int k=0;k<this.numFolds;k++) {
			train = tmpSet.trainCV(this.numFolds, k);
			val = tmpSet.testCV(this.numFolds, k);
			this.trainBaseClassifiers(train);
			this.updateValidationSet(val);
		}
		
		
	}
	
	public void trainSplit(Instances data) throws Exception {
		Instances[] tmpSet = InstancesOperator.stratifiedSplitSet(data, this.splitFactor, this.m_Seed);
		Instances train = tmpSet[0];
		Instances validation = tmpSet[1];
		this.validationSet = new Instances(data,0);
		
		this.trainBaseClassifiers(train);
		this.updateValidationSet(validation);
		
		
	}
	
	public void updateValidationSet(Instances data) throws Exception {
		
		int numValInstances = data.numInstances();
		for(int i=0;i<numValInstances;i++) {
			Instance tmpInstance = data.get(i);
			this.validationSet.add(tmpInstance);
			for(int c=0;c<this.m_Classifiers.length;c++) {
				double[] tmpResponse = this.m_Classifiers[c].distributionForInstance(tmpInstance);
				this.validationResponses[c].add(tmpResponse);
			}
		}
	}




	/**
	 * @param numFolds the numFolds to set
	 */
	public void setNumFolds(int numFolds) {
		this.numFolds = numFolds;
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tSplit factor to use"+
		          "(default:" + 0.6  + ").\n",
			      "SF", 1, "-SF"));
		 
		 newVector.addElement(new Option(
			      "\tDetermines whether crossvalidation is performed"+
		          "(default:" + false  + ").\n",
			      "CV", 0, "-CV"));
		 
		 newVector.addElement(new Option(
			      "\tThe number of folds"+
		          "(default:" + 2  + ").\n",
			      "NF", 1, "-NF"));
		 
		 
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}


	@Override
	public void setOptions(String[] options) throws Exception {
		super.setOptions(options);
		
		this.setSplitFactor(UtilsPT.parseDoubleOption(options, "SF", 0.6));
		
		this.setCrossvalidate(Utils.getFlag("CV", options));
		
		this.setNumFolds(UtilsPT.parseIntegerOption(options, "NF", 2));
	}


	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
		
		options.add("-SF");
		options.add(""+this.getSplitFactor());
		
		if(this.isCrossvalidate())
			options.add("-CV");
		
		options.add("-NF");
		options.add("" + this.getNumFolds());
		
		Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}
	
	@Override
	public String globalInfo() {
		return "MultipleClassifiersCombinerWithValidationSet";
	}


	@Override
	public Capabilities getCapabilities() {
		Capabilities baseCaps = super.getCapabilities();
		baseCaps.disable(Capability.NUMERIC_CLASS);
		baseCaps.disable(Capability.DATE_CLASS);
		baseCaps.enable(Capability.NOMINAL_CLASS);
		baseCaps.setMinimumNumberInstances(2);
		return baseCaps; 
	}
	
	protected void trainBaseClassifiers(Instances data) throws Exception {
		for(int i=0;i<this.m_Classifiers.length;i++)
			this.m_Classifiers[i].buildClassifier(data);
	}

	


}
