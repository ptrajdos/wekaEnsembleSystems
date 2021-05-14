/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.ParallelIteratedSingleClassifierEnhancer;
import weka.classifiers.rules.ZeroR;
import weka.clusterers.Clusterer;
import weka.clusterers.NumberOfClustersRequestable;
import weka.clusterers.SimpleKMeans;
import weka.core.Capabilities;
import weka.core.CapabilitiesHandler;
import weka.core.Instances;
import weka.core.Option;
import weka.core.UtilsPT;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.tools.GlobalInfoHandler;

/**
 * Classifier driven ensemble -- Abstract class.
 * @author pawel trajdos
 * @version 1.11.0
 * @since 1.11.0
 * 
 *
 */
public abstract class ClusterDrivenEnsembleAbstract extends ParallelIteratedSingleClassifierEnhancer implements GlobalInfoHandler, CapabilitiesHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5475184314766797315L;
	
	protected Clusterer clusterer = new SimpleKMeans();
	
	protected boolean isClassNumeric=false;
	
	protected Remove removeFilter = new Remove();
	
	protected ZeroR defaultModel;
	
	/**
	 * 
	 */
	public ClusterDrivenEnsembleAbstract() {
		super();
		try {
			this.m_NumIterations = this.clusterer.numberOfClusters();
		} catch (Exception e) {
			this.m_NumIterations = this.defaultNumberOfIterations();
		}
	}
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		this.m_NumIterations = this.clusterer.numberOfClusters();
		
		if(data.numAttributes()==1 && data.classIndex()>=0) {
			this.defaultModel = new ZeroR();
			this.defaultModel.buildClassifier(data);
			return;
		}
		
		this.defaultModel=null;
		
		if(!this.m_DoNotCheckCapabilities)
			 getCapabilities().testWithFail(data);
		this.isClassNumeric = data.classAttribute().isNumeric();
		super.buildClassifier(data);
		
		int classIndex = data.classIndex();
		this.removeFilter.setAttributeIndicesArray(new int[] {classIndex});
		this.removeFilter.setInputFormat(data);
		this.removeFilter.setInvertSelection(false);
		Instances filteredData = Filter.useFilter(data, this.removeFilter);
		if(!this.m_DoNotCheckCapabilities)
			this.clusterer.getCapabilities().testWithFail(filteredData);
		this.clusterer.buildClusterer(filteredData);
	}


	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe Clusterer object to use"+
		          "(default:" + SimpleKMeans.class.toGenericString() + ").\n",
			      "CLU", 1, "-CLU"));
		
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		this.setClusterer((Clusterer) UtilsPT.parseObjectOptions(options, "CLU", new SimpleKMeans(), Clusterer.class));
		super.setOptions(options);
	}

	@Override
	public String[] getOptions() {
	    Vector<String> options = new Vector<String>();
	    

	    options.add("-CLU");
	    options.add(UtilsPT.getClassAndOptions(this.clusterer));
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}
	
	@Override
	public Capabilities getCapabilities() {
		Capabilities baseCaps = super.getCapabilities();
			
		return baseCaps;
	}

	/**
	 * @return the clusterer
	 */
	public Clusterer getClusterer() {
		return this.clusterer;
	}


	/**
	 * @param clusterer the clusterer to set
	 */
	public void setClusterer(Clusterer clusterer) {
		this.clusterer = clusterer;
	}
	
	public String clustererTipText() {
		return "Clusterer prototype to use";
	}
	
	@Override
	public void setNumIterations(int numIterations) {
		super.setNumIterations(numIterations);
		if(this.clusterer instanceof NumberOfClustersRequestable)
			try {
				((NumberOfClustersRequestable) this.clusterer).setNumClusters(numIterations);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
