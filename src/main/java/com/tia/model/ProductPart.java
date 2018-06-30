package com.tia.model;

/**
 * @author Prashant Shelke
 *
 */
public class ProductPart {
	private String productName;
	private int serialNumber;
	
	/**
	 * @param productName
	 * @param serialNumber
	 */
	protected ProductPart(String productName, int serialNumber) {
		super();
		this.productName = productName;
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductPart [productName=" + productName + ", serialNumber=" + serialNumber + "]";
	}
}
