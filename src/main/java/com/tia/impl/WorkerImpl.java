package com.tia.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tia.model.ProductPart;
import com.tia.processor.ConsumerThread;

/**
 * @author Prashant Shelke
 *
 */
public class WorkerImpl {
	static final int NUMBER_OF_WORKERS=3;
	private LocalBlockingQueue<ProductPart> partsQueue;
	Map<String, List<ProductPart>> mainMapWorkers = new HashMap<String, List<ProductPart>>();
	
	public WorkerImpl(int poolSize)
     {  
		partsQueue = new LocalBlockingQueue<ProductPart>();
		System.out.println("Starting producer");
  		ProductProducer productProducer = new ProductProducer(partsQueue);
  		new Thread(productProducer).start();
  		
		System.out.println("Starting consumer");
		for(int i = 0; i < poolSize; i++)  
	   	 {  
	   		 ConsumerThread jobThread = new ConsumerThread(partsQueue, mainMapWorkers);
	   		 new Thread(jobThread).start();
	   	 }
     }
}
