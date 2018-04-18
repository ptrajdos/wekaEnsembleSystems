/**
 * 
 */
package weka.classifiers.tools;

import java.io.Serializable;

/**
 * @author Pawel Trajdos
 * The class represents weighted values
 *
 */
public class Pair implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4333628032959261664L;

	protected double value=0;
	
	protected double weight=0;
	
	public Pair(double value,double weight) {
		this.value = value;
		this.weight = weight;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return this.weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
}
