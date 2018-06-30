package com.tia.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.tia.impl.LocalBlockingQueue;
import com.tia.model.ProductPart;

/**
 * @author Prashant Shelke
 *
 */
public class ConsumerThread implements Runnable {
	private LocalBlockingQueue<ProductPart> partsQueue;
	Map<String, List<ProductPart>> localMapWorkers;

	public ConsumerThread(LocalBlockingQueue<ProductPart> queue, Map<String, List<ProductPart>> mapWorkers) {
		partsQueue = queue;
		localMapWorkers = Collections.synchronizedMap(mapWorkers);
	}

	public void run() {
		String threadName = Thread.currentThread().getName(); 
		System.out.println("threadName:"+threadName + " partsQueue.size():" + partsQueue.size());
		try {
			while(true) {
				ProductPart productPart = partsQueue.take();
				System.out.println("Thread"+threadName+" Consuming part:"+productPart);
				if (productPart != null) {
					process(threadName,productPart);
				}else {
					System.out.println("Last element consumed...");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
			return;
		}
	}

	public void process(final String threadName, final ProductPart part) {
		System.out.println("ThreadName:" + threadName);
		if(null != localMapWorkers.get(threadName)) {
			if(!checkIfProductAssembled(threadName)) {
				List<ProductPart> tmp = localMapWorkers.get(threadName);
				tmp.add(part);
				localMapWorkers.put(threadName,tmp);
			}
		}else{
			List<ProductPart> tmp = new ArrayList<ProductPart>();
			tmp.add(part);
			localMapWorkers.put(threadName, tmp);
		}
		checkIfProductAssembled(threadName);
	}
	/**
	 * @param threadName
	 * @return
	 */
	private boolean checkIfProductAssembled(final String threadName) {
		System.out.println("Checking assembly for worker:"+threadName);
		
		List<ProductPart> list = localMapWorkers.get(threadName);
		if(null == list) {
			list = new ArrayList<ProductPart>();
			localMapWorkers.put(threadName,list);
		}
		int boltCount=0;
		int machineCount=0;
		for (ProductPart productPart : list) {
			if(productPart.getProductName().contains("bolt")) {
				++boltCount;
			}else {
				++machineCount;
			}
		}
		if(boltCount == 2 && machineCount == 1) {
			System.out.println("Worker " + threadName + " assembled product!");
			localMapWorkers.put(threadName, new ArrayList<ProductPart>());
			return true;
		}
		return false;
	}
}