/**
 * 
 */
package weka.classifiers.meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.meta.generalOutputCombiners.MeanCombiner;
import weka.classifiers.meta.generalOutputCombiners.MeanCombinerNumClass;
import weka.classifiers.meta.generalOutputCombiners.OutputCombiner;
import weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass;
import weka.core.Instance;
import weka.core.Option;
import weka.core.UtilsPT;

/**
 * @author pawel trajdos
 * @since 1.11.0
 * @version 1.11.0
 *
 */
public class CustomizableRotationForest extends RotationForest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4459785241537070183L;
	
	/**
	 * Combiner for class  soft outputs
	 */
	protected OutputCombiner classificationCombiner  = new MeanCombiner();
	
	/**
	 * Combiner for regression outputs
	 */
	protected OutputCombinerNumClass regressionCombiner = new MeanCombinerNumClass();

	/**
	 * 
	 */
	public CustomizableRotationForest() {
		super();
	}
	
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe Combiner object to use with classification problem"+
		          "(default:" + MeanCombiner.class.toGenericString() + ").\n",
			      "CCL", 1, "-CCL"));
		 
		 newVector.addElement(new Option(
			      "\tThe Combiner object to use with regression"+
		          "(default:" + MeanCombinerNumClass.class.toGenericString() + ").\n",
			      "CRE", 1, "-CRE"));
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setClassificationCombiner((OutputCombiner) UtilsPT.parseObjectOptions(options, "CCL", new MeanCombiner(), OutputCombiner.class));
		
		this.setRegressionCombiner((OutputCombinerNumClass) UtilsPT.parseObjectOptions(options, "CRE", new MeanCombinerNumClass(), OutputCombinerNumClass.class));
		
		super.setOptions(options);
	}

	@Override
	public String[] getOptions() {
		
		Vector<String> options = new Vector<String>();
	    

	    options.add("-CCL");
	    options.add(UtilsPT.getClassAndOptions(this.getClassificationCombiner()));
	    
	    options.add("-CRE");
	    options.add(UtilsPT.getClassAndOptions(this.getRegressionCombiner()));
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
		
	
	}

	/**
	 * @return the classificationCombiner
	 */
	public OutputCombiner getClassificationCombiner() {
		return this.classificationCombiner;
	}

	/**
	 * @param classificationCombiner the classificationCombiner to set
	 */
	public void setClassificationCombiner(OutputCombiner classificationCombiner) {
		this.classificationCombiner = classificationCombiner;
	}
	
	public String classificationCombinerTipText() {
		return "Combiner used in  classification problems";
	}

	/**
	 * @return the regressionCombiner
	 */
	public OutputCombinerNumClass getRegressionCombiner() {
		return this.regressionCombiner;
	}

	/**
	 * @param regressionCombiner the regressionCombiner to set
	 */
	public void setRegressionCombiner(OutputCombinerNumClass regressionCombiner) {
		this.regressionCombiner = regressionCombiner;
	}
	
	public String regressionCombinerTipText() {
		return "Combiner used in  regression problems";
	}
	

	
	protected Instance preprocessInstance(Instance instance) throws Exception {
		Instance preprocessedInstance;
		this.m_RemoveUseless.input(instance);
	    preprocessedInstance =this.m_RemoveUseless.output();
	    this.m_RemoveUseless.batchFinished();

	    this.m_Normalize.input(preprocessedInstance);
	    preprocessedInstance =this.m_Normalize.output();
	    this.m_Normalize.batchFinished();
	    
	    return preprocessedInstance;
	}
	
	protected double[] getWeights(Instance instance) {
		double[] weights = new double[this.m_Classifiers.length];
		Arrays.fill(weights, 1.0);
		
		return weights;
	}
	
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		Instance preprocessedInstance = this.preprocessInstance(instance);
		double[] weights = this.getWeights(preprocessedInstance);
		
		double[] distribution =null;
		
		boolean isClassNumeric = preprocessedInstance.classAttribute().isNumeric();
		if(isClassNumeric) {
			double[] predictions = new double[this.m_Classifiers.length];
			for(int i=0;i<this.m_Classifiers.length;i++) {
				Instance convertedInstance = convertInstance(preprocessedInstance, i);
				predictions[i] = this.m_Classifiers[i].classifyInstance(convertedInstance);
			}
			
			distribution = new double[] {this.regressionCombiner.getClass(predictions, preprocessedInstance, weights)};
		}else {
			double[][] predictions = new double[this.m_Classifiers.length][];
			for(int i=0;i<this.m_Classifiers.length;i++) {
				Instance convertedInstance = convertInstance(preprocessedInstance, i);
				predictions[i] = this.m_Classifiers[i].distributionForInstance(convertedInstance);
			}			
			distribution = this.classificationCombiner.getCombinedDistributionForInstance(predictions, preprocessedInstance,weights);
		}
		
		return distribution;
	}

}
