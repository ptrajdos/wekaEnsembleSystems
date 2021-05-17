/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.rules.ZeroR;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;
import weka.filters.Filter;

/**
 * @author pawel trajdos
 * @since 1.11.0
 * @version 1.11.0
 *
 */
public abstract class ClusterDrivenEnsembleHeteroClustersAbstract extends ClusterDrivenEnsembleAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7729335605198217354L;
	/**
	 * 
	 */
	protected Instances[] clusterRelatedSets;
	
	protected boolean weightedInstances=false;

	/**
	 * 
	 */
	public ClusterDrivenEnsembleHeteroClustersAbstract() {
		super();
	}
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		
		if(data.numAttributes()==1 && data.classIndex()>=0) {
			this.defaultModel = new ZeroR();
			this.defaultModel.buildClassifier(data);
			return;
		}
		
		this.defaultModel=null;
		
		if(!this.m_DoNotCheckCapabilities)
			 getCapabilities().testWithFail(data);
		this.isClassNumeric = data.classAttribute().isNumeric();
		
		
		int classIndex = data.classIndex();
		this.removeFilter.setAttributeIndicesArray(new int[] {classIndex});
		this.removeFilter.setInputFormat(data);
		this.removeFilter.setInvertSelection(false);
		
		
		
		
		Instances filteredData = Filter.useFilter(data, this.removeFilter);
		if(!this.m_DoNotCheckCapabilities)
			this.clusterer.getCapabilities().testWithFail(filteredData);
		this.clusterer.buildClusterer(filteredData);
		
		this.m_Classifiers = AbstractClassifier.makeCopies(this.m_Classifier, this.clusterer.numberOfClusters());
		
		this.generateTrainData(data);
		
		for(int c =0;c<this.m_Classifiers.length;c++) {
			this.m_Classifiers[c].buildClassifier(this.clusterRelatedSets[c]);
		}
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

	


}
