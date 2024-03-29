/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.SingleClassifierEnhancer;
import weka.classifiers.meta.generalOutputCombiners.MeanCombiner;
import weka.classifiers.meta.generalOutputCombiners.MeanCombinerNumClass;
import weka.classifiers.meta.generalOutputCombiners.OutputCombiner;
import weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass;
import weka.classifiers.rules.ZeroR;
import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Capabilities;
import weka.core.CapabilitiesHandler;
import weka.core.Instance;
import weka.core.Option;
import weka.core.UtilsPT;
import weka.filters.Filter;
import weka.tools.GlobalInfoHandler;

/**
 * Classifier driven ensemble -- Abstract class.
 * @author pawel trajdos
 * @version 1.11.0
 * @since 1.11.0
 * 
 *
 */
public abstract class ClusterDrivenEnsembleAbstract extends SingleClassifierEnhancer implements GlobalInfoHandler, CapabilitiesHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5475184314766797315L;
	
	protected Clusterer clusterer = new SimpleKMeans();
	
	protected boolean isClassNumeric=false;
	
	protected Filter preClusterFilter;
	
	protected ZeroR defaultModel;
	
	protected Classifier[] m_Classifiers;
	
	/**
	 * Combiner for class  soft outputs
	 */
	protected OutputCombiner classificationCombiner  = new MeanCombiner();
	
	/**
	 * Combiner for regression outputs
	 */
	protected OutputCombinerNumClass regressionCombiner = new MeanCombinerNumClass();
	
	/**
	 * 
	 */
	public ClusterDrivenEnsembleAbstract() {
		super();
	}
	
	
	protected abstract double[] getWeights(Instance instance) throws Exception;
	
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		if(this.defaultModel!=null)
			return this.defaultModel.distributionForInstance(instance);
		double[] weights = this.getWeights(instance);
		double[] distribution=null;
		if(this.isClassNumeric) {
			distribution = new double[] {this.regressionCombiner.getClass(this.m_Classifiers, instance, weights)};
		}else {
			distribution = this.classificationCombiner.getCombinedDistributionForInstance(this.m_Classifiers, instance, weights);
		}
		return distribution;
	}


	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe Clusterer object to use"+
		          "(default:" + SimpleKMeans.class.toGenericString() + ").\n",
			      "CLU", 1, "-CLU"));
		 
		 newVector.addElement(new Option(
			      "\tThe Classification combiner to use"+
		          "(default:" + MeanCombiner.class.toGenericString() + ").\n",
			      "CLAC", 1, "-CLAC"));
		 
		 newVector.addElement(new Option(
			      "\tThe Regression combiner to use"+
		          "(default:" + MeanCombiner.class.toGenericString() + ").\n",
			      "REGC", 1, "-REGC"));
		
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		this.setClusterer((Clusterer) UtilsPT.parseObjectOptions(options, "CLU", new SimpleKMeans(), Clusterer.class));
		this.setClassificationCombiner((OutputCombiner) UtilsPT.parseObjectOptions(options, "CLAC", new MeanCombiner(), OutputCombiner.class));
		this.setRegressionCombiner((OutputCombinerNumClass) UtilsPT.parseObjectOptions(options, "REGC", new MeanCombinerNumClass(), OutputCombinerNumClass.class));
		
		super.setOptions(options);
	}

	@Override
	public String[] getOptions() {
	    Vector<String> options = new Vector<String>();
	    

	    options.add("-CLU");
	    options.add(UtilsPT.getClassAndOptions(this.getClusterer()));
	    
	    options.add("-CLAC");
	    options.add(UtilsPT.getClassAndOptions(this.getClassificationCombiner()));
	    
	    options.add("-REGC");
	    options.add(UtilsPT.getClassAndOptions(this.getRegressionCombiner()));
	    
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
	
	

	/**
	 * @return the classificationCombiner
	 */
	public OutputCombiner getClassificationCombiner() {
		return this.classificationCombiner;
	}

	/**
	 * @param classificationCombiner the classificationCombiner to set
	 */
	public void setClassificationCombiner(OutputCombiner classificationCombiner) {
		this.classificationCombiner = classificationCombiner;
	}
	
	public String classificationCombinerTipText() {
		return "The Classification combiner to use";
	}

	/**
	 * @return the regressionCombiner
	 */
	public OutputCombinerNumClass getRegressionCombiner() {
		return this.regressionCombiner;
	}

	/**
	 * @param regressionCombiner the regressionCombiner to set
	 */
	public void setRegressionCombiner(OutputCombinerNumClass regressionCombiner) {
		this.regressionCombiner = regressionCombiner;
	}
	
	public String regressionCombinerTipText(){
		return "The regression combiner to use";
	}

}
