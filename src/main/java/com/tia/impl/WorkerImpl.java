package com.tia.impl;

import java.util.Date;
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
		Date date1 = new Date();
		partsQueue = new LocalBlockingQueue<ProductPart>();
		System.out.println("Starting producer");
  		ProductProducer productProducer = new ProductProducer(partsQueue);
  		new Thread(productProducer).start();
  		
		System.out.println("Starting consumer");
		for(int i = 0; i < poolSize; i++)  
	   	 {  
	   		 ConsumerThread jobThread = new ConsumerThread(partsQueue, mainMapWorkers);
	   		 Thread t = new Thread(jobThread);
	   		 t.start();
	   	 }
		Date date2 = new Date();

		/*System.out.println("================================================");
		System.out.println("Total bolts:6, machines:3, Products created:"+NUMBER_OF_WORKERS);
		long secondsBetween = (date1.getTime() - date2.getTime()) / 1000;
		System.out.println("Time taken to complete product assembling:"+ secondsBetween);
		System.out.println("================================================");*/
     }
}
