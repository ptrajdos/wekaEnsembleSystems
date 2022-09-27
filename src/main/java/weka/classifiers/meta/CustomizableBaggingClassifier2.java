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
 * @since 1.10.0
 * @version 1.10.0
 *
 */
public class CustomizableBaggingClassifier2 extends Bagging {

	/**
	 * 
	 */
	private static final long serialVersionUID = 74987415633056387L;
	
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
	public CustomizableBaggingClassifier2() {
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
	    options.add(UtilsPT.getClassAndOptions(this.classificationCombiner));
	    
	    options.add("-CRE");
	    options.add(UtilsPT.getClassAndOptions(this.regressionCombiner));
	    
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

	
	
	public boolean isClassNumeric() {
		return this.m_data.classAttribute().isNumeric();
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		double[] distribution=new double[instance.numClasses()];
		double[] weights = this.getWeights(instance);
		if(this.isClassNumeric()) {
			distribution[0] = this.regressionCombiner.getClass(this.m_Classifiers, instance, weights);
		}else {
			distribution = this.classificationCombiner.getCombinedDistributionForInstance(this.m_Classifiers, instance, weights);
		}
		
		return distribution;
	}
	
	protected double[] getWeights(Instance instance) throws Exception {
		double[] weights = new double[this.m_Classifiers.length];
		Arrays.fill(weights, 1.0);
		
		return weights;
	}
	
	
	
	

}
