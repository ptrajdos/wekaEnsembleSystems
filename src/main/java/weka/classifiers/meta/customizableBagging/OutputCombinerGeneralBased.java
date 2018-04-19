/**
 * 
 */
package weka.classifiers.meta.customizableBagging;

import weka.classifiers.IteratedSingleClassifierEnhancer;
import weka.classifiers.meta.generalOutputCombiners.MeanCombiner;
import weka.classifiers.meta.generalOutputCombiners.MeanCombinerNumClass;
import weka.classifiers.meta.generalOutputCombiners.OutputCombiner;
import weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass;
import weka.core.Instance;
import weka.core.RevisionUtils;

/**
 * @author pawel
 *
 */
public class OutputCombinerGeneralBased extends OutputCombinerBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3850999140140846979L;
	
	/**
	 * Combiner for class  soft outputs
	 */
	protected OutputCombiner classCombiner = new MeanCombiner();
	
	/**
	 * Combiner for regression outputs
	 */
	protected OutputCombinerNumClass regCombiner =  new MeanCombinerNumClass();

	/**
	 * 
	 */
	public OutputCombinerGeneralBased() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.customizableBagging.OutputCombiner#getDistributionForInstance(weka.classifiers.IteratedSingleClassifierEnhancer, weka.core.Instance)
	 */
	@Override
	public double[] getDistributionForInstance(IteratedSingleClassifierEnhancer itClassifier, Instance instance)
			throws Exception {
		double[][] responses = OutputExtractorIteratedSingleClassifierEnhancer.getDistributions(itClassifier, instance);
		double[] distribution = this.classCombiner.getCombinedDistributionForInstance(responses, instance);
		return distribution;
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.customizableBagging.OutputCombiner#getClass(weka.classifiers.IteratedSingleClassifierEnhancer, weka.core.Instance)
	 */
	@Override
	public double getClass(IteratedSingleClassifierEnhancer itClassifier, Instance instance) throws Exception {
		double[] responses = OutputExtractorIteratedSingleClassifierEnhancer.getClasses(itClassifier, instance);
		double result = this.regCombiner.getClass(responses, instance);
		return result;
	}
	
	

	/**
	 * @return the classCombiner
	 */
	public OutputCombiner getClassCombiner() {
		return this.classCombiner;
	}

	/**
	 * @param classCombiner the classCombiner to set
	 */
	public void setClassCombiner(OutputCombiner classCombiner) {
		this.classCombiner = classCombiner;
	}

	/**
	 * @return the regCombiner
	 */
	public OutputCombinerNumClass getRegCombiner() {
		return this.regCombiner;
	}

	/**
	 * @param regCombiner the regCombiner to set
	 */
	public void setRegCombiner(OutputCombinerNumClass regCombiner) {
		this.regCombiner = regCombiner;
	}

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1$");
	}
	

}
