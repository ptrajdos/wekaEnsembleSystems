/**
 * 
 */
package weka.classifiers.meta;

import weka.core.Instances;
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
	public ClusterDrivenEnsembleHeteroClustersAbstract() {
		super();
	}
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		super.buildClassifier(data);
		if(this.defaultModel!=null)
			return;
		Instances filteredData = Filter.useFilter(data, this.removeFilter);
		if(!this.m_DoNotCheckCapabilities)
			this.clusterer.getCapabilities().testWithFail(filteredData);
		this.clusterer.buildClusterer(filteredData);
	}


}
