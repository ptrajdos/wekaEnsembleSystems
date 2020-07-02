/**
 * 
 */
package weka.classifiers.meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.math3.util.Pair;

import weka.classifiers.Classifier;
import weka.classifiers.meta.generalOutputCombiners.OutputCombiner;
import weka.classifiers.meta.generalOutputCombiners.VoteCombiner;
import weka.classifiers.rules.ZeroR;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;
import weka.core.UtilsPT;
import weka.tools.SerialCopier;

/**
 * @author pawel trajdos
 * @since 1.5.0
 * @version 1.5.0
 *
 */
public class BKSClassifier extends MultipleClassifiersCombinerWithValidationSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2922311049461753454L;
	
	/**
	 * Classifier prototype for clasyfying object is subspaces
	 */
	protected Classifier subSpaceClassifierPrototype;
	
	/**
	 * Backup combiner. Used when an instance does not belong to a known subspace;
	 */
	protected OutputCombiner outCombiner;
	
	/**
	 * BKS subspaces 
	 * Instances and Decision rule (Classifier) for the subspace
	 */
	
	protected HashMap<String, Pair<Instances, Classifier>> bksSubspaces;

	/**
	 * 
	 */
	public BKSClassifier() {
		this.subSpaceClassifierPrototype = new ZeroR();
		this.outCombiner = new VoteCombiner();
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		super.buildClassifier(data);
		this.bksSubspaces = new HashMap<String, Pair<Instances,Classifier>>();
		this.buildBKSSubspaces();
		this.trainSubspaces();
		
	}
	
	protected void buildBKSSubspaces() throws Exception {
		int numInstances = this.validationSet.size();
		
		for(int i=0;i<numInstances;i++) {
			Pair<Instances, Classifier> tmpPair;
			String responsePattern = this.getResponsePattern(i);
			if(this.bksSubspaces.containsKey(responsePattern)) {
				tmpPair = this.bksSubspaces.get(responsePattern);
				tmpPair.getKey().add(this.validationSet.get(i));
			}else {
				Instances tmpSet = new Instances(this.validationSet, 0);
				tmpSet.add(this.validationSet.get(i));
				tmpPair = new Pair<Instances, Classifier>(tmpSet, (Classifier) SerialCopier.makeCopy(this.subSpaceClassifierPrototype));
				this.bksSubspaces.put(responsePattern, tmpPair);
			}
		}
	}
	
	protected void trainSubspaces() throws Exception {
		for(Pair<Instances, Classifier> pair: this.bksSubspaces.values()) {
			pair.getValue().buildClassifier(pair.getKey());
		}
	}
	
	
	protected String getResponsePattern( int instanceNum) {
		int numClassifiers = this.validationResponses.length;
		double[] resPAttern = new double[numClassifiers];
		
		for(int c=0;c<numClassifiers;c++) {
			resPAttern[c] = Utils.maxIndex(this.validationResponses[c].get(instanceNum));
		}
		
		return Arrays.toString(resPAttern);
	}
	
	protected String getResponsePattern(Instance inst) throws Exception {
		int numClassifiers = this.validationResponses.length;
		double[] respPattern = new double[numClassifiers];
		for(int c=0;c<numClassifiers;c++) {
			respPattern[c] = Utils.maxIndex(this.m_Classifiers[c].distributionForInstance(inst));
		}
		
		return Arrays.toString(respPattern);
	}
	
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		
		
		double[] distribution = this.outCombiner.getCombinedDistributionForInstance(this.m_Classifiers, instance);
		
		String responsePattern = this.getResponsePattern(instance);
		if(this.bksSubspaces.containsKey(responsePattern)) {
			Pair<Instances, Classifier> tmpPair = this.bksSubspaces.get(responsePattern);
			return tmpPair.getValue().distributionForInstance(instance);
		}
		
		//Default response if the response pattern is unknown
		return distribution;
	}

	/**
	 * @return the subSpaceClassifierPrototype
	 */
	public Classifier getSubSpaceClassifierPrototype() {
		return this.subSpaceClassifierPrototype;
	}

	/**
	 * @param subSpaceClassifierPrototype the subSpaceClassifierPrototype to set
	 */
	public void setSubSpaceClassifierPrototype(Classifier subSpaceClassifierPrototype) {
		this.subSpaceClassifierPrototype = subSpaceClassifierPrototype;
	}
	
	public String subSpaceClassifierPrototypeTipText() {
		return "Prototype for subspece classifer";
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tSubspace classifier prototype to use"+
		          "(default:" + ZeroR.class.getCanonicalName()  + ").\n",
			      "SSC", 1, "-SSC"));
		 
		 newVector.addElement(new Option(
			      "\tSubspace classifier prototype to use"+
		          "(default:" + VoteCombiner.class.getCanonicalName()  + ").\n",
			      "BOC", 1, "-BOC"));
		 
		 
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		super.setOptions(options);
		
		this.setSubSpaceClassifierPrototype((Classifier) UtilsPT.parseObjectOptions(options, "SSC", new ZeroR(), Classifier.class));
		this.setOutCombiner((OutputCombiner) UtilsPT.parseObjectOptions(options, "BOC", new VoteCombiner(), OutputCombiner.class));
	}

	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
		
		options.add("-SSC");
		options.add(UtilsPT.getClassAndOptions(this.getSubSpaceClassifierPrototype()));
		
		options.add("-BOC");
		options.add(UtilsPT.getClassAndOptions(this.getOutCombiner()));
		
		Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}

	@Override
	public String globalInfo() {
		return "BKS Classifier";
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
	
	public String outCombinerTipText() {
		return "Backup combiner used when an instance belongs to an unknown subspace";
	}
	
	
	
	

}
