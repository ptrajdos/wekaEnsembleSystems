/**
 * 
 */
package weka.classifiers.meta.simpleVotingLikeCombiners;

import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.IteratedSingleClassifierEnhancer;
import weka.classifiers.MultipleClassifiersCombiner;
import weka.classifiers.meta.generalOutputCombiners.MeanCombiner;
import weka.classifiers.meta.generalOutputCombiners.MeanCombinerNumClass;
import weka.classifiers.meta.generalOutputCombiners.OutputCombiner;
import weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass;
import weka.classifiers.meta.tools.CommitteeExtractor;
import weka.classifiers.meta.tools.CommitteeResponseExtractor;
import weka.core.Instance;
import weka.core.Option;
import weka.core.RevisionUtils;
import weka.core.UtilsPT;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.3.1
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
	protected OutputCombiner classCombiner ;
	
	/**
	 * Combiner for regression outputs
	 */
	protected OutputCombinerNumClass regCombiner;

	/**
	 * 
	 */
	public OutputCombinerGeneralBased() {
		this.regCombiner =  new MeanCombinerNumClass();
		this.classCombiner = new MeanCombiner();
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
	
	public String classCombinerTipText(){
		return "Objest for combining class distributions";
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
	
	public String regCombinerTipText(){
		return "Object for cominning regression outputs";
	}

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1$");
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\t Class distribution-combining-object to use "+
		          "(default:" + MeanCombiner.class.toGenericString() +").\n",
			      "CC", 0, "-CC"));
		 newVector.addElement(new Option(
			      "\t Regresion combining-object to use "+
		          "(default:" + MeanCombiner.class.toGenericString()   +").\n",
			      "RC", 0, "-RC"));
		 
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setClassCombiner((OutputCombiner) UtilsPT.parseObjectOptions(options, "CC", new MeanCombiner(), OutputCombiner.class));
		
		this.setRegCombiner((OutputCombinerNumClass) UtilsPT.parseObjectOptions(options, "RC", new MeanCombinerNumClass(), OutputCombinerNumClass.class));
	    
	}

	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    

	    options.add("-CC");
	    options.add(UtilsPT.getClassAndOptions(this.getClassCombiner()));
	    
	    options.add("-RC");
	    options.add(UtilsPT.getClassAndOptions(this.getRegCombiner()));
	    
	    
	    return options.toArray(new String[0]);
	}

	
	

}
