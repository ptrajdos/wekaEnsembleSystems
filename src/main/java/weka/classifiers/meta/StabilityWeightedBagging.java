/**
 * 
 */
package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.UpdateableClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;
import weka.core.UtilsPT;
import weka.tools.data.InstancesOperator;
import weka.tools.stats.distributionDistances.DistributionDistanceCalculator;
import weka.tools.stats.distributionDistances.DistributionDistanceCalculatorEuclidean;

/**
 * @author pawel trajdos
 * @since 1.10.0
 * @version 1.10.0
 *
 */
public class StabilityWeightedBagging extends CustomizableBaggingClassifier2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2243863900162142569L;
	
	protected DistributionDistanceCalculator distributionDistanceCalculator = new DistributionDistanceCalculatorEuclidean();
	
	protected double iqrFactorL=3.0;
	
	protected double iqrFactorU=0.0;
	
	protected double eps=1E-6;
	
	

	/**
	 * 
	 */
	public StabilityWeightedBagging() {
		super();
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		super.buildClassifier(data);
		
		/*
		 * The entire dataset is needed to calculate weights.
		 */
		this.m_data = new Instances(data);
		
	}

	@Override
	protected double[] getWeights(Instance instance) throws Exception {
		double[] weights = new double[this.m_Classifiers.length];
		for(int i=0;i<weights.length;i++)
			weights[i] = getClassifierWeight(i, instance);
		
		double q1 = UtilsPT.quantile(weights, 0.25);
		double q3 = UtilsPT.quantile(weights, 0.75);
		double iqrL = this.iqrFactorL*(q3-q1);
		double iqrU = this.iqrFactorU*(q3-q1);
		double threshL = -iqrL + q1;
		double threshU = iqrU + q3;
		
		for(int i=0;i<weights.length;i++)
			if(weights[i]<=threshU & weights[i]>=threshL) 
				weights[i]=1.0/(weights[i] + 1E-3);
			else
				weights[i]=0;
		
		Utils.normalize(weights);
		return weights;
	}
	
	private double getClassifierWeight(int classifierNumber, Instance instance) throws Exception {
		if(this.isClassNumeric())
			return 1.0;
	
		double[] initDistr = this.m_Classifiers[classifierNumber].distributionForInstance(instance);
		
		
		int numClasses = this.m_data.numClasses();
		double[] classWeights = new double[numClasses];
		for(int i=0;i<numClasses;i++) {
			Instance testInstance = instance.copy(instance.toDoubleArray());
			testInstance.setClassValue(i);
			Instances tmpData = this.getTrainingSet(classifierNumber);
			Classifier classifierCopy =  AbstractClassifier.makeCopy(this.m_Classifiers[classifierNumber]);
			double[] postDistr=null; 
			if(this.m_Classifiers[classifierNumber] instanceof UpdateableClassifier) {
				((UpdateableClassifier)classifierCopy).updateClassifier(testInstance);
			}else {
				tmpData.add(testInstance);
				classifierCopy.buildClassifier(tmpData);
			}
			
			 postDistr = classifierCopy.distributionForInstance(instance);
			 double distDist = this.distributionDistanceCalculator.calculateDistance(initDistr, postDistr);
			 double[] aPrioriDist = InstancesOperator.classFreq(tmpData);
			 double classSize = tmpData.numInstances() * aPrioriDist[i];
			 double dist2APriori = this.distributionDistanceCalculator.calculateDistance(initDistr, aPrioriDist) + this.eps;
			 distDist *=  classSize/dist2APriori;
			
			 classWeights[i]=distDist;
			
		}
		
		double bestWeight =  classWeights[Utils.minIndex(classWeights)];
		
		
		return bestWeight;
	}
	


	/**
	 * @return the distributionDistanceCalculator
	 */
	public DistributionDistanceCalculator getDistributionDistanceCalculator() {
		return this.distributionDistanceCalculator;
	}

	/**
	 * @param distributionDistanceCalculator the distributionDistanceCalculator to set
	 */
	public void setDistributionDistanceCalculator(DistributionDistanceCalculator distributionDistanceCalculator) {
		this.distributionDistanceCalculator = distributionDistanceCalculator;
	}
	
	public String distributionDistanceCalculatorTipText() {
		return "Distribution Distance Calculator object to use";
	}
	
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tThe object used to calculate distances between class distributions"+
		          "(default:" + DistributionDistanceCalculatorEuclidean.class.toGenericString() + ").\n",
			      "DCA", 1, "-DCA"));
		 
		 newVector.addElement(new Option(
			      "\tThe factor determining IQR selection for the lower bound"+
		          "(default:" + 3.0 + ").\n",
			      "IQRFL", 1, "-IQRFL"));
		 
		 newVector.addElement(new Option(
			      "\tThe factor determining IQR selection for the lower bound"+
		          "(default:" + 0.0 + ").\n",
			      "IQRFU", 1, "-IQRFU"));
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}
	
	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    

	    options.add("-DCA");
	    options.add(UtilsPT.getClassAndOptions(this.getDistributionDistanceCalculator()));
	    
	    options.add("-IQRFL");
	    options.add(""+this.getIQRFactorL());
	    
	    options.add("-IQRFU");
	    options.add(""+this.getIQRFactorU());
	    
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);

	}
	
	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setDistributionDistanceCalculator((DistributionDistanceCalculator) UtilsPT.parseObjectOptions(options, "DCA", new DistributionDistanceCalculatorEuclidean(), DistributionDistanceCalculator.class));
		this.setIQRFactorL(UtilsPT.parseDoubleOption(options, "IQRFL", 3.0));
		this.setIQRFactorU(UtilsPT.parseDoubleOption(options, "IQRFU", 0.0));
		
		super.setOptions(options);
	}

	/**
	 * @return the selectQuantile
	 */
	public double getIQRFactorL() {
		return this.iqrFactorL;
	}

	/**
	 * @param selectQuantile the selectQuantile to set
	 */
	public void setIQRFactorL(double selectQuantile) {
		this.iqrFactorL = selectQuantile;
	}
	
	public String IQRFactorLTipText() {
		return "Determines the IQR factor to calculate the lower threshold";
	}

	/**
	 * @return the iqrFactorU
	 */
	public double getIQRFactorU() {
		return this.iqrFactorU;
	}

	/**
	 * @param iqrFactorU the iqrFactorU to set
	 */
	public void setIQRFactorU(double iqrFactorU) {
		this.iqrFactorU = iqrFactorU;
	}
	
	public String IQRFactorUTipText() {
		return "Determines the IQR factor to calculate the upper threshold";
	} 

}
