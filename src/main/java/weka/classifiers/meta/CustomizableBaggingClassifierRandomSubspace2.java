/**
 * 
 */
package weka.classifiers.meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.rules.ZeroR;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.UtilsPT;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.tools.SerialCopier;

/**
 * @author pawel trajdos
 * @since 1.10.0
 * @version 1.11.0
 *
 */
public class CustomizableBaggingClassifierRandomSubspace2 extends CustomizableBaggingClassifier2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1175757335084131648L;

	protected int minNumberOfAttributes=2;
	protected double attributePercentage=0.5;
	
	protected Classifier defaultModel = null;
	
	protected Classifier tmpModel=null;
	/**
	 * @return the minNumberOfAttributes
	 */
	public int getMinNumberOfAttributes() {
		return this.minNumberOfAttributes;
	}
	/**
	 * @param minNumberOfAttributes the minNumberOfAttributes to set
	 */
	public void setMinNumberOfAttributes(int minNumberOfAttributes) {
		this.minNumberOfAttributes = minNumberOfAttributes;
	}
	
	public String minNumberOfAttributesTipText() {
		return "Min number of attributes to select";
	}
	/**
	 * @return the attributePercentage
	 */
	public double getAttributePercentage() {
		return this.attributePercentage;
	}
	/**
	 * @param attributePercentage the attributePercentage to set
	 */
	public void setAttributePercentage(double attributePercentage) {
		this.attributePercentage = attributePercentage;
	}
	
	public String attributePercentageTipText() {
		return "The percentage of attributes to use";
	}
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe min number of attributes to use"+
		          "(default:" + 2 + ").\n",
			      "MAT", 1, "-MAT"));
		 
		 newVector.addElement(new Option(
			      "\tAttribute percentage to use"+
		          "(default:" + 0.1 + ").\n",
			      "ATTP", 1, "-ATTP"));
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		 
		 return newVector.elements();
		    
	}
	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    

	    options.add("-MAT");
	    options.add(""+this.getMinNumberOfAttributes());
	    
	    options.add("-ATTP");
	    options.add(""+this.getAttributePercentage());
	    
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}
	@Override
	public void setOptions(String[] options) throws Exception {
		this.setMinNumberOfAttributes(UtilsPT.parseIntegerOption(options, "MAT", 2));
		this.setAttributePercentage(UtilsPT.parseDoubleOption(options, "ATTP", 0.1));
		super.setOptions(options);
	}
	
	
	
	protected static class FilteredRemoveClassifier extends FilteredClassifier{

		/**
		 * For serialization puropses
		 */
		private static final long serialVersionUID = -1600342695813267823L;
		
		/**
		 * Instances saved during the learning process
		 */
		protected Instances m_data;
		
		protected int minNumberOfAttributes=2;
		protected double attributePercentage=0.5;
		
		public FilteredRemoveClassifier(int minNumberOfAttributes, double attributePercentage) {
			this.minNumberOfAttributes = minNumberOfAttributes;
			this.attributePercentage = attributePercentage;
		}
		
		protected int getNumberOfNonClassAttributes() {
			if(this.m_data==null)
				return 0;
			
			int classIdx = this.m_data.classIndex();
			int numAtrrs = this.m_data.numAttributes();
			int realAttrs =  numAtrrs + (classIdx>=0? -1:0);
			
			return realAttrs;
		}
		
		protected int getNumberOfAttributesToSelect() {
			if(this.m_data == null)
				return this.minNumberOfAttributes;
			
			int realAttrs = getNumberOfNonClassAttributes();
			int roundedPerc = (int)Math.ceil(realAttrs*this.attributePercentage);
			int attrs2Select = Math.max(this.minNumberOfAttributes, roundedPerc);
			
			attrs2Select = attrs2Select>realAttrs? realAttrs: attrs2Select;
			
			return attrs2Select;
		}
		
		protected Integer[] generateNonClassAttributeIndices() {
			if(this.m_data == null)
				return new Integer[0];
			
			int numAttrs = this.m_data.numAttributes();
			int classAttrIdx = this.m_data.classIndex();
			int numNonClassAttrs = this.getNumberOfNonClassAttributes();
			
			Integer[] indices = new Integer[numNonClassAttrs];
			int counter=0;
			for(int i=0;i<numAttrs;i++) {
				if(i!=classAttrIdx) {
					indices[counter++]=i+1; 
				}
			}
			
			return indices;
		}
		
		protected String randomSubSpaceString() {
			Integer[] indices = this.generateNonClassAttributeIndices();
			int subSpaceSize = this.getNumberOfAttributesToSelect();
			int classIndex = this.m_data.classIndex() + 1;
			
			Random random = new Random(this.m_Seed);
			
		    Collections.shuffle(Arrays.asList(indices), random);
		    StringBuffer sb = new StringBuffer("");
		    for(int i = 0; i < subSpaceSize; i++) {
		      sb.append(indices[i]+",");
		    }
		    sb.append(classIndex);
		    
		    if (getDebug())
		      System.out.println("subSPACE = " + sb);

		    return sb.toString();
		  }
		
		protected Filter getSubSpaceFilter() throws Exception {
			Remove rm = new Remove();
		      rm.setOptions(new String[]{"-V", "-R", this.randomSubSpaceString()});
			return rm;
		}
		
		@Override
		public void buildClassifier(Instances data) throws Exception {
			this.m_data = new Instances(data);
			this.setFilter(this.getSubSpaceFilter());
			super.buildClassifier(data);
		}
		
		
	}
	
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		
		this.m_data = new Instances(data);
		
		if(this.tmpModel !=null) {
			this.m_Classifier = this.tmpModel;
		}else {
			this.tmpModel = this.m_Classifier;
		}
			
		
		if(this.m_data.numAttributes() == 1) {
			this.defaultModel = new ZeroR();
			this.defaultModel.buildClassifier(this.m_data);
			return;
		}
		this.defaultModel=null;
		
		FilteredRemoveClassifier tmpFiltered = new FilteredRemoveClassifier(this.minNumberOfAttributes, this.attributePercentage);
		Classifier tmpClassifier = (Classifier) SerialCopier.makeCopy(this.m_Classifier);
		
		tmpFiltered.setClassifier(tmpClassifier);
		//tmpFiltered.setClassifier(new REPTree());
		this.setClassifier(tmpFiltered);
	
		
		if(!this.m_DoNotCheckCapabilities)
			this.getCapabilities().testWithFail(data);
		
		
		super.buildClassifier(data);
	}
@Override
public double[] distributionForInstance(Instance instance) throws Exception {
	if(this.defaultModel !=null) {
		return this.defaultModel.distributionForInstance(instance);
	}
	return super.distributionForInstance(instance);
}
	
}
