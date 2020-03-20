/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.SingleClassifierEnhancer;
import weka.classifiers.rules.ZeroR;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.UtilsPT;
import weka.tools.GlobalInfoHandler;
import weka.tools.SerialCopier;

/**
 * Class with an alternative model
 * The alternative model is learned when
 * @author pawel trajdos
 * @since 1.5.0
 * @version 1.5.0
 *
 */
public class ClassifierWithAlternativeModel extends SingleClassifierEnhancer implements GlobalInfoHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1446203300221424585L;
	
	protected Classifier alternativeModel;
	
	protected Classifier alternativeModelPrototype;
	
	protected boolean useAlternative=false;
	
	

	/**
	 * 
	 */
	public ClassifierWithAlternativeModel() {
		this.alternativeModelPrototype = new ZeroR();
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		
		try {
			this.m_Classifier.buildClassifier(data);
		}catch(Exception e) {
			this.useAlternative = true;
		}
		this.alternativeModel = (Classifier) SerialCopier.makeCopy(this.alternativeModelPrototype);
		this.alternativeModel.buildClassifier(data);

	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tAlternative model to use"+
		          "(default:" + ZeroR.class.getCanonicalName()  + ").\n",
			      "AM", 1, "-AM"));
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		super.setOptions(options);
		this.setAlternativeModelPrototype((Classifier) UtilsPT.parseObjectOptions(options, "AM", new ZeroR(), Classifier.class));
	}

	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
		
		options.add("-AM");
		options.add(UtilsPT.getClassAndOptions(this.getAlternativeModelPrototype()));
		
		Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		
		if(this.useAlternative)
			return this.alternativeModel.distributionForInstance(instance);
		
		double[] distribution;
		
		try {
			distribution =this.m_Classifier.distributionForInstance(instance); 
		}catch(Exception e) {
			distribution = this.alternativeModel.distributionForInstance(instance);
		}
	
		return distribution;
	}

	/**
	 * @return the alternativeModelPrototype
	 */
	public Classifier getAlternativeModelPrototype() {
		return this.alternativeModelPrototype;
	}

	/**
	 * @param alternativeModelPrototype the alternativeModelPrototype to set
	 */
	public void setAlternativeModelPrototype(Classifier alternativeModelPrototype) {
		this.alternativeModelPrototype = alternativeModelPrototype;
	}
	
	public String alternativeModelPrototypeTipText() {
		return "Alternative model to use";
	}

	@Override
	public String globalInfo() {
		return "Classifier with an alternative model.\n The alternative model is used when the original classifier cannot bu built/used";
	}

	@Override
	public Capabilities getCapabilities() {
		if(!this.useAlternative)
			return super.getCapabilities();
		
		return this.alternativeModel.getCapabilities();
	}
	
	
	
	

}
