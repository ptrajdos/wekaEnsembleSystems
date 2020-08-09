/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.meta.simpleVotingLikeCombiners.OutputCombiner;
import weka.classifiers.meta.simpleVotingLikeCombiners.OutputCombinerGeneralBased;
import weka.core.Instance;
import weka.core.Option;
import weka.core.RevisionUtils;
import weka.core.UtilsPT;

/**
 * Random subspace with customizable vote counting
 * @author pawel trajdos
 * @since 1.8.0
 * @version 1.8.0
 *
 */
public class CustomizableRandomSubspaceClassifier extends RandomSubSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2939995730842261274L;
	
	/**
	 * Output combiner to use
	 */
	protected OutputCombiner outCombiner = new OutputCombinerGeneralBased() ;

	/**
	 * 
	 */
	public CustomizableRandomSubspaceClassifier() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Bagging#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		
		
		if(this.m_ZeroR !=null)
			return this.m_ZeroR.distributionForInstance(instance);
		
		int numClasses = instance.numClasses();
		double[] predictions = new double[numClasses];
		boolean isClassNumeric= instance.classAttribute().isNumeric();
		if(isClassNumeric){
			predictions[0] = this.outCombiner.getClass(this, instance);
		}else{
			predictions = this.outCombiner.getDistributionForInstance(this, instance);
		}
		
		return predictions;
		
		
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
	
	public String outCombinerTipText(){
		return "Object for comnining outputs of the commitee";
	}



	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Bagging#getRevision()
	 */
	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1$");
	}



	/* (non-Javadoc)
	 * @see weka.classifiers.meta.RandomSubspace#listOptions()
	 */
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe Combiner object to use "+
		          "(default:" + OutputCombinerGeneralBased.class.toGenericString() + ").\n",
			      "C", 0, "-C"));
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}



	/* (non-Javadoc)
	 * @see weka.classifiers.meta.RandomSubspace#setOptions(java.lang.String[])
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setOutCombiner((OutputCombiner) UtilsPT.parseObjectOptions(options, "C", new OutputCombinerGeneralBased(), OutputCombiner.class));
		
		super.setOptions(options);
		
	}



	/* (non-Javadoc)
	 * @see weka.classifiers.meta.RandomSubspace#getOptions()
	 */
	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    

	    options.add("-C");
	    options.add(UtilsPT.getClassAndOptions(this.getOutCombiner()));
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	    
	}

}
