/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;

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
	
	protected Instances[] clusterRelatedSets;
	
	protected boolean weightedInstances=false;

	/**
	 * 
	 */
	public ClusterDrivenEnsembleHeteroClusters() {
		super();
	}
	
	@Override
	protected Instances getTrainingSet(int iteration) throws Exception {
		return this.clusterRelatedSets[iteration];
	}
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		super.buildClassifier(data);
		
		if(this.defaultModel !=null)
			return;
		this.generateTrainData(data);
		
		this.buildClassifiers();
	}
	
	protected void generateTrainData(Instances trainData) throws Exception {
		
		int numDatasets = this.m_Classifiers.length;
		this.clusterRelatedSets = new Instances[numDatasets];
		
		for(int i =0;i<this.clusterRelatedSets.length;i++)
			this.clusterRelatedSets[i] = new Instances(trainData, 0);
		
		for (Instance instance : trainData) {
			this.removeFilter.input(instance);
			Instance filteredInstance = this.removeFilter.output();
			double[] distribution = this.clusterer.distributionForInstance(filteredInstance);
			if(this.weightedInstances && (this.m_Classifier instanceof WeightedInstancesHandler )) {
				for(int i=0;i<distribution.length;i++) {
					if(Utils.eq(distribution[i], 0))continue;
					
					Instance tmpInstance = instance.copy(instance.toDoubleArray());
					tmpInstance.setWeight(distribution[i]);
					this.clusterRelatedSets[i].add(tmpInstance);
				}
			}else {
				int maxIdx = Utils.maxIndex(distribution);
				this.clusterRelatedSets[maxIdx].add(instance);
			}
			
		}
	}
	
	@Override
	protected double[] getWeights(Instance instance)throws Exception {
		this.removeFilter.input(instance);
		Instance filteredInstance = this.removeFilter.output();
		
		double[] clustererResponse = this.clusterer.distributionForInstance(filteredInstance);
		int maxClusterIdx = Utils.maxIndex(clustererResponse);
		
		double[] weights = new double[this.m_Classifiers.length];
		weights[maxClusterIdx] =1;
		
		return weights;
	}
	
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tDetermines whether weighted instance representation is used"+
		          "(default:" + false + ").\n",
			      "WEI", 0, "-WEI"));
		
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setWeightedInstances(Utils.getFlag("WEI", options));
		super.setOptions(options);
	}

	@Override
	public String[] getOptions() {
	    Vector<String> options = new Vector<String>();
	    

	    if(this.isWeightedInstances())
	    	options.add("-WEI");
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}

	
	/**
	 * @return the weightedInstances
	 */
	public boolean isWeightedInstances() {
		return this.weightedInstances;
	}

	/**
	 * @param weightedInstances the weightedInstances to set
	 */
	public void setWeightedInstances(boolean weightedInstances) {
		this.weightedInstances = weightedInstances;
	}
	
	public String weightedInstancesTipText() {
		return "Determines whether cluster assignment is crisp or soft(represented by instance weighting)";
	}

	@Override
	public String globalInfo() {
		return "Cluster driven ensemble with heterogeneous clusters (clusters containing multiple classes)";
	}

}
