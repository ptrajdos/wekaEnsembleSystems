/**
 * 
 */
package weka.classifiers.meta.customizableBagging;

import java.lang.reflect.Field;

import weka.classifiers.meta.Bagging;
import weka.core.Instance;

/**
 * @author pawel
 *
 */
public class CustomizableBagging extends Bagging {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2418734202968291123L;
	
	protected OutputCombiner outCombiner = new OutputCombinerGeneralBased();
	
	

	/**
	 * Simple constructor
	 */
	public CustomizableBagging() {
		super();
	}



	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Bagging#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		int numClasses = instance.numClasses();
		double[] predictions = new double[numClasses];
		boolean isClassNumeric= this.isClassNumeric();
		if(isClassNumeric){
			predictions[0] = this.outCombiner.getClass(this, instance);
		}else{
			predictions = this.outCombiner.getDistributionForInstance(this, instance);
		}
		
		return predictions;
	}
	
	public boolean isClassNumeric()throws Exception{
		Class cla = Bagging.class;
		Field f = cla.getDeclaredField("m_Numeric");
		f.setAccessible(true);
		boolean response = f.getBoolean(this);
		
		return response;
		
	}



	/**
	 * @return the outCombiner
	 */
	public OutputCombiner getOutCombiner() {
		return this.outCombiner;
	}



	/**
	 * @param outCombiner the outCombiner to set
	 */
	public void setOutCombiner(OutputCombiner outCombiner) {
		this.outCombiner = outCombiner;
	}

	
	
	
	
	

}
