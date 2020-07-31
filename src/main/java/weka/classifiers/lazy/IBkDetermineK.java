package weka.classifiers.lazy;

import weka.core.Instance;

/**
 * Class for determining the number of nearest neighbours
 * @author pawel trajdos
 * @since 1.7.0
 * @version 1.7.0
 *
 */

public class IBkDetermineK extends IBk{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5776374976130641779L;

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		
		int nn = this.determineNN();
		this.setKNN(nn);
		return super.distributionForInstance(instance);
	}
	
	protected int determineNN() {
		int numInstances = this.m_Train.numInstances();
		int numClasses = this.m_Train.numClasses();
		
		int neighbours =(int) Math.ceil(Math.sqrt(numInstances));
		if(this.m_Train.classAttribute().isNominal() && (numInstances%numClasses==0))
			neighbours++;
		return neighbours;
	}

	


}
