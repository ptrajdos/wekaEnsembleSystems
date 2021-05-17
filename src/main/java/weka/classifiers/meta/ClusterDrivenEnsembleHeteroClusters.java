/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.core.Instance;
import weka.core.Option;
import weka.core.Utils;

/**
 * @author pawel trajdos
 * @since 1.11.0
 * @version 1.11.0
 *
 */
public class ClusterDrivenEnsembleHeteroClusters extends ClusterDrivenEnsembleHeteroClustersAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -305362011029577752L;
	
	
	
	
	
	protected boolean weightedOutput=false;

	/**
	 * 
	 */
	public ClusterDrivenEnsembleHeteroClusters() {
		super();
	}
	
	
	@Override
	protected double[] getWeights(Instance instance)throws Exception {
		this.removeFilter.input(instance);
		Instance filteredInstance = this.removeFilter.output();
		
		double[] clustererResponse = this.clusterer.distributionForInstance(filteredInstance);
		if(this.weightedOutput) {
			return clustererResponse;
		}
			
		
		int maxClusterIdx = Utils.maxIndex(clustererResponse);
		
		double[] weights = new double[this.m_Classifiers.length];
		weights[maxClusterIdx] =1;
		
		return weights;
	}
	
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		

		 newVector.addElement(new Option(
			      "\tDetermines whether all cluster-related-classifiers are used during the classification phase"+
		          "(default:" + false + ").\n",
			      "WEIO", 0, "-WEIO"));
		
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setWeightedOutput(Utils.getFlag("WEIO", options));
		
		super.setOptions(options);
	}

	@Override
	public String[] getOptions() {
	    Vector<String> options = new Vector<String>();
	   
	    
	    if(this.isWeightedOutput())
	    	options.add("-WEIO");
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}

	
	
	@Override
	public String globalInfo() {
		return "Cluster driven ensemble with heterogeneous clusters (clusters containing multiple classes)";
	}

	/**
	 * @return the weightedOutput
	 */
	public boolean isWeightedOutput() {
		return this.weightedOutput;
	}

	/**
	 * @param weightedOutput the weightedOutput to set
	 */
	public void setWeightedOutput(boolean weightedOutput) {
		this.weightedOutput = weightedOutput;
	}
	
	public String weightedOutputTipText() {
		return "Determines whether all cluster-related classifiers are used during the prediction phase";
	}

}
