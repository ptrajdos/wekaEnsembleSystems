/**
 * 
 */
package weka.classifiers.meta.simpleVotingLikeCombiners;

import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.meta.generalOutputCombiners.MeanCombiner;
import weka.classifiers.meta.generalOutputCombiners.MeanCombinerNumClass;
import weka.classifiers.meta.generalOutputCombiners.OutputCombiner;
import weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass;
import weka.core.Option;
import weka.core.RevisionUtils;
import weka.core.UtilsPT;

/**
 * @author pawel trajdos
 * @since 1.9.0
 * @version 1.9.0
 *
 */
public abstract class AOutputCombinerGeneralBased extends OutputCombinerBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3850999140140846979L;
	
	/**
	 * Combiner for class  soft outputs
	 */
	protected OutputCombiner classCombiner ;
	
	/**
	 * Combiner for regression outputs
	 */
	protected OutputCombinerNumClass regCombiner;

	/**
	 * 
	 */
	public AOutputCombinerGeneralBased() {
		this.regCombiner =  new MeanCombinerNumClass();
		this.classCombiner = new MeanCombiner();
	}

	/**
	 * @return the classCombiner
	 */
	public OutputCombiner getClassCombiner() {
		return this.classCombiner;
	}

	/**
	 * @param classCombiner the classCombiner to set
	 */
	public void setClassCombiner(OutputCombiner classCombiner) {
		this.classCombiner = classCombiner;
	}
	
	public String classCombinerTipText(){
		return "Objest for combining class distributions";
	}

	/**
	 * @return the regCombiner
	 */
	public OutputCombinerNumClass getRegCombiner() {
		return this.regCombiner;
	}

	/**
	 * @param regCombiner the regCombiner to set
	 */
	public void setRegCombiner(OutputCombinerNumClass regCombiner) {
		this.regCombiner = regCombiner;
	}
	
	public String regCombinerTipText(){
		return "Object for cominning regression outputs";
	}

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1$");
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\t Class distribution-combining-object to use "+
		          "(default:" + MeanCombiner.class.toGenericString() +").\n",
			      "CC", 0, "-CC"));
		 newVector.addElement(new Option(
			      "\t Regresion combining-object to use "+
		          "(default:" + MeanCombiner.class.toGenericString()   +").\n",
			      "RC", 0, "-RC"));
		 
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setClassCombiner((OutputCombiner) UtilsPT.parseObjectOptions(options, "CC", new MeanCombiner(), OutputCombiner.class));
		
		this.setRegCombiner((OutputCombinerNumClass) UtilsPT.parseObjectOptions(options, "RC", new MeanCombinerNumClass(), OutputCombinerNumClass.class));
	    
	}

	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    

	    options.add("-CC");
	    options.add(UtilsPT.getClassAndOptions(this.getClassCombiner()));
	    
	    options.add("-RC");
	    options.add(UtilsPT.getClassAndOptions(this.getRegCombiner()));
	    
	    
	    return options.toArray(new String[0]);
	}

	
	

}
