/**
 * 
 */
package weka.classifiers.meta;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.meta.customizableBagging.OutputCombiner;
import weka.classifiers.meta.customizableBagging.OutputCombinerGeneralBased;
import weka.core.Instance;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.RevisionUtils;
import weka.core.Utils;

/**
 * @author pawel
 *
 */
public class CustomizableBagging extends Bagging {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2418734202968291123L;
	
	protected OutputCombiner outCombiner = new OutputCombinerGeneralBased();
	
	

	/**
	 * Simple constructor
	 */
	public CustomizableBagging() {
		super();
	}



	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Bagging#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		int numClasses = instance.numClasses();
		double[] predictions = new double[numClasses];
		boolean isClassNumeric= this.isClassNumeric();
		if(isClassNumeric){
			predictions[0] = this.outCombiner.getClass(this, instance);
		}else{
			predictions = this.outCombiner.getDistributionForInstance(this, instance);
		}
		
		return predictions;
	}
	
	public boolean isClassNumeric()throws Exception{
		Class cla = Bagging.class;
		Field f = cla.getDeclaredField("m_Numeric");
		f.setAccessible(true);
		boolean response = f.getBoolean(this);
		
		return response;
		
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
	 * @see weka.classifiers.meta.Bagging#listOptions()
	 */
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe distance function to use "+
		          "(default: weka.classifiers.meta.customizableBagging.OutputCombinerGeneralBased).\n",
			      "C", 0, "-C"));
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}



	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Bagging#setOptions(java.lang.String[])
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		String combinerString = Utils.getOption('C', options);
	    if(combinerString.length() != 0) {
	      String combinerClassSpec[] = Utils.splitOptions(combinerString);
	      if(combinerClassSpec.length == 0) { 
	        throw new Exception("Invalid Distance function " +
	                            "specification string."); 
	      }
	      String className = combinerClassSpec[0];
	      combinerClassSpec[0] = "";

	      this.setOutCombiner(
	                  (OutputCombiner) Utils.forName( OutputCombiner.class, 
	                                 className, 
	                                 combinerClassSpec)
	                                        );
	    }
	    else 
	      this.setOutCombiner(new OutputCombinerGeneralBased());
		
		super.setOptions(options);
		
	}



	/* (non-Javadoc)
	 * @see weka.classifiers.meta.Bagging#getOptions()
	 */
	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    

	    options.add("-C");
	    String combinerOptions = (this.outCombiner instanceof OptionHandler)? Utils.joinOptions(((OptionHandler)this.outCombiner).getOptions()):"";
	    options.add(this.outCombiner.getClass().getName()+" "+combinerOptions); 
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	    
	}
	
	
	

}
