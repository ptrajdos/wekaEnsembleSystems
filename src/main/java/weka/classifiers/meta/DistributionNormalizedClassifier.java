/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.SingleClassifierEnhancer;
import weka.classifiers.meta.distrNormalizer.INormalizer;
import weka.classifiers.meta.distrNormalizer.SumNormalizer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.UtilsPT;

/**
 * Class for distribution normalizing wrapper
 * @author pawel trajdos
 * @since 1.1.0
 * @version 1.3.0
 *
 */
public class DistributionNormalizedClassifier extends SingleClassifierEnhancer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9059036086616862937L;
	
	protected INormalizer normalizer;

	/**
	 * 
	 */
	public DistributionNormalizedClassifier() {
		super();
		this.normalizer = new SumNormalizer();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.Classifier#buildClassifier(weka.core.Instances)
	 */
	@Override
	public void buildClassifier(Instances data) throws Exception {
		this.m_Classifier.buildClassifier(data);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		double[] distr =m_Classifier.distributionForInstance(instance); 
		return this.normalizer.normalize(distr);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.SingleClassifierEnhancer#listOptions()
	 */
	@Override
	public Enumeration<Option> listOptions() {
		
		Vector<Option> newVector = new Vector<Option>(1);
		
		newVector.addElement(new Option(
			      "\tNormalizer object to use "+
		          "(default: weka.classifiers.meta.distrNormalizer.SumNormalizer).\n",
			      "N", 1, "-N"));
		
		newVector.addAll(Collections.list(super.listOptions()));
	    
		return newVector.elements();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.SingleClassifierEnhancer#getOptions()
	 */
	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
		
		options.add("-N");
		options.add(UtilsPT.getClassAndOptions(this.getNormalizer()));
		
		
		Collections.addAll(options, super.getOptions());
	    return options.toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.SingleClassifierEnhancer#setOptions(java.lang.String[])
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setNormalizer((INormalizer) 
				UtilsPT.parseObjectOptions(options, "N", new SumNormalizer(), INormalizer.class));
		super.setOptions(options);
	}

	/**
	 * @return the normalizer
	 */
	public INormalizer getNormalizer() {
		return this.normalizer;
	}

	/**
	 * @param normalizer the normalizer to set
	 */
	public void setNormalizer(INormalizer normalizer) {
		this.normalizer = normalizer;
	}

	public String normalizerTipText() {
		return "Normalizer object to use";
	}
	
	public String globalInfo() {
		return "Classifier wrapper for applying different strategies of output normalization";
	}
	
	

}
