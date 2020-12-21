/**
 * 
 */
package weka.classifiers.meta.simpleVotingLikeCombiners;

import weka.classifiers.Classifier;
import weka.classifiers.IteratedSingleClassifierEnhancer;
import weka.classifiers.MultipleClassifiersCombiner;
import weka.classifiers.meta.tools.CommitteeExtractor;
import weka.classifiers.meta.tools.CommitteeResponseExtractor;
import weka.core.Instance;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.9.0
 *
 */
public class OutputCombinerGeneralBased extends AOutputCombinerGeneralBased {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8667443066243535579L;

	/**
	 * 
	 */
	public OutputCombinerGeneralBased() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see weka.classifiers.meta.customizableBagging.OutputCombiner#getDistributionForInstance(weka.classifiers.IteratedSingleClassifierEnhancer, weka.core.Instance)
	 */
	@Override
	public double[] getDistributionForInstance(IteratedSingleClassifierEnhancer itClassifier, Instance instance)
			throws Exception {
		Classifier[] committee = CommitteeExtractor.getCommittee(itClassifier);
		double[][] responses = CommitteeResponseExtractor.distributionsForInstanceCommittee(committee, instance);
		double[] distribution = this.classCombiner.getCombinedDistributionForInstance(responses, instance);
		return distribution;
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.customizableBagging.OutputCombiner#getClass(weka.classifiers.IteratedSingleClassifierEnhancer, weka.core.Instance)
	 */
	@Override
	public double getClass(IteratedSingleClassifierEnhancer itClassifier, Instance instance) throws Exception {
		Classifier[] committee = CommitteeExtractor.getCommittee(itClassifier);
		double[] responses = CommitteeResponseExtractor.classifyCommitee(committee, instance);
		double result = this.regCombiner.getClass(responses, instance);
		return result;
	}
	
	@Override
	public double[] getDistributionForInstance(MultipleClassifiersCombiner classifier, Instance instance)
			throws Exception {
		Classifier[] committee = CommitteeExtractor.getCommittee(classifier);
		double[][] responses = CommitteeResponseExtractor.distributionsForInstanceCommittee(committee, instance);
		double[] distribution = this.classCombiner.getCombinedDistributionForInstance(responses, instance);
		return distribution;
	}

	@Override
	public double getClass(MultipleClassifiersCombiner classifier, Instance instance) throws Exception {
		Classifier[] committee = CommitteeExtractor.getCommittee(classifier);
		double[] responses = CommitteeResponseExtractor.classifyCommitee(committee, instance);
		double result = this.regCombiner.getClass(responses, instance);
		return result;
	}

}
