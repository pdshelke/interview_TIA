package com.tia.impl;

import org.apache.log4j.Logger;

/**
 * @author Prashant Shelke
 *
 */
public class ProductAssembly {
	static Logger logger = Logger.getLogger(ProductAssembly.class.getName());
	static final int NUMBER_OF_WORKERS=3;

	public static void main(String[] args) {
		try {
			new ProductAssembly().createProducts();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void createProducts() throws InterruptedException {
		// Warehouse warehouse = new Warehouse(3, 6, 60);//3-machines 6 bolts
		// and 60-sec for product manufacturing
		new WorkerImpl(NUMBER_OF_WORKERS);
	}
}