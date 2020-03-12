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
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.3.1
 *
 */
public class CustomizableVote extends Vote {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7309582288977776183L;
	
	protected OutputCombiner outCombiner  ;
	
	protected boolean useCombiningObject=false;

	/**
	 * 
	 */
	public CustomizableVote() {
		super();
		this.outCombiner = new OutputCombinerGeneralBased();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Vote#listOptions()
	 */
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe Combiner object to use "+
		          "(default:" + OutputCombinerGeneralBased.class.toGenericString() + ").\n",
			      "C", 0, "-C"));
		 
		 newVector.addElement(new Option(
			      "\t If the combiner object should be used "+
		          "(default:" + 0 + ").\n",
			      "IC", 0, "-IC"));
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		 
		 return newVector.elements();
	}






	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Vote#getOptions()
	 */
	@Override
	public String[] getOptions() {
Vector<String> options = new Vector<String>();
	    

	    options.add("-C");
	    options.add(UtilsPT.getClassAndOptions(this.getOutCombiner()));
	    
	    
	    options.add("-IC");
	    String isCombining = (this.isUseCombiningObject()? "1":"0");
	    options.add(isCombining);
	    
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}






	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Vote#setOptions(java.lang.String[])
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setOutCombiner((OutputCombiner) UtilsPT.parseObjectOptions(options, "C", new OutputCombinerGeneralBased(), OutputCombiner.class));
		
		this.setUseCombiningObject(UtilsPT.parseIntegerOption(options, "IC", 0)==0? false:true);
		
				
		super.setOptions(options);
	}






	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Vote#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		if(! this.useCombiningObject)
			return super.distributionForInstance(instance);
		
		int numClasses = instance.numClasses();
		double[] predictions = new double[numClasses];
		
		if(instance.classAttribute().isNumeric()) {
			predictions[0] = this.outCombiner.getClass(this, instance);
		}else {
			predictions = this.outCombiner.getDistributionForInstance(this, instance);
		}
		
		
		
		return predictions;
	}




	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Bagging#getRevision()
	 */
	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1$");
	}

	public String useCombiningObjectTipText() {
		return "Determines whether the combining object is used";
	}

	/**
	 * @return the useCombiningObject
	 */
	public boolean isUseCombiningObject() {
		return this.useCombiningObject;
	}

	/**
	 * @param useCombiningObject the useCombiningObject to set
	 */
	public void setUseCombiningObject(boolean useCombiningObject) {
		this.useCombiningObject = useCombiningObject;
	}



	public String outCombinerTipText() {
		return "The object used to combine the outputs of the ensemble";
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		runClassifier(new CustomizableVote(), args);

	}

}
