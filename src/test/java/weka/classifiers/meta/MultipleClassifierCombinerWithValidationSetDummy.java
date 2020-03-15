/**
 * 
 */
package weka.classifiers.meta;


import weka.classifiers.meta.generalOutputCombiners.MeanCombiner;
import weka.classifiers.meta.generalOutputCombiners.OutputCombiner;
import weka.core.Instance;
import weka.tools.GlobalInfoHandler;

/**
 * Dummy class. For testing purposes only
 * @author pawel trajdos
 * @since 1.4.0
 * @version 1.4.0
 */
public class MultipleClassifierCombinerWithValidationSetDummy extends MultipleClassifiersCombinerWithValidationSet implements GlobalInfoHandler {
	
	@Override
	public double getSplitFactor() {
		// TODO Auto-generated method stub
		return super.getSplitFactor();
	}

	@Override
	public void setSplitFactor(double splitFactor) {
		// TODO Auto-generated method stub
		super.setSplitFactor(splitFactor);
	}

	@Override
	public boolean isCrossvalidate() {
		// TODO Auto-generated method stub
		return super.isCrossvalidate();
	}

	@Override
	public void setCrossvalidate(boolean crossvalidate) {
		// TODO Auto-generated method stub
		super.setCrossvalidate(crossvalidate);
	}

	@Override
	public int getNumFolds() {
		// TODO Auto-generated method stub
		return super.getNumFolds();
	}

	@Override
	public void setNumFolds(int numFolds) {
		// TODO Auto-generated method stub
		super.setNumFolds(numFolds);
	}

	@Override
	public String splitFactorTipText() {
		// TODO Auto-generated method stub
		return super.splitFactorTipText();
	}

	@Override
	public String crossvalidateTipText() {
		// TODO Auto-generated method stub
		return super.crossvalidateTipText();
	}

	@Override
	public String numFoldsTipText() {
		// TODO Auto-generated method stub
		return super.numFoldsTipText();
	}
	


	protected OutputCombiner comb; 

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		
		return this.comb.getCombinedDistributionForInstance(m_Classifiers, instance);
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7977388693867029735L;

	/**
	 * 
	 */
	public MultipleClassifierCombinerWithValidationSetDummy() {
		this.comb = new MeanCombiner();
	}


}
