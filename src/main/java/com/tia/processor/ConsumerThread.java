package com.tia.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.tia.impl.LocalBlockingQueue;
import com.tia.model.ProductPart;

/**
 * @author Prashant Shelke
 *
 */
public class ConsumerThread implements Runnable {
	private LocalBlockingQueue<ProductPart> partsQueue;
	Map<String, List<ProductPart>> localMapWorkers;
    ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private volatile static int finishedProductCount = 0;
    private final int FINAL_EXPECTED_PRODUCTS=3;
    
	public ConsumerThread(LocalBlockingQueue<ProductPart> queue, Map<String, List<ProductPart>> mapWorkers) {
		partsQueue = queue;
		localMapWorkers = Collections.synchronizedMap(mapWorkers);
	}

	public void run() {
		String threadName = Thread.currentThread().getName(); 
		//System.out.println("threadName:"+threadName + " partsQueue.size():" + partsQueue.size());
		try {
			while(true) {
				System.out.println("finishedProductCount:"+ finishedProductCount);
				if(finishedProductCount == FINAL_EXPECTED_PRODUCTS) {
					/*synchronized(this) {
						notifyAll();
					}*/
					return;
				}
				ProductPart productPart;
				synchronized(partsQueue){
					productPart = partsQueue.take();
				}
				
				System.out.println("Thread"+threadName+" Consuming part:"+productPart);
				if (productPart != null) {
					process(threadName, productPart);
				}else {
					System.out.println("Last element consumed...");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

	public void process(final String threadName, final ProductPart part) {
		try {
			//System.out.println("Worker:" + threadName + " assembling product");
			if(null != localMapWorkers.get(threadName)) {
				if(checkIfProductAssembled(threadName,part) == ProductAssembled.NO) {
					List<ProductPart> tmp = localMapWorkers.get(threadName);
					tmp.add(part);
					localMapWorkers.put(threadName,tmp);
					reCheckAssembly(threadName);
				}else if(checkIfProductAssembled(threadName,part) == ProductAssembled.YES){
					localMapWorkers.put(threadName, new ArrayList<ProductPart>());
				}else{
					System.out.println("threadName:"+threadName+"Product count exceeded, putting product back to conveyor:"+ part);
					synchronized(partsQueue){
						partsQueue.put(part);
						Thread.currentThread().sleep(2*1000);
					}
				}
			}else{
				List<ProductPart> tmp = new ArrayList<ProductPart>();
				tmp.add(part);
				localMapWorkers.put(threadName, tmp);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param threadName
	 * @return
	 */
	private ProductAssembled checkIfProductAssembled(final String threadName,final ProductPart part) {
		try {
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
			System.out.println("threadName: "+threadName+ " bolt count:"+boltCount + " machineCount:"+machineCount);
			if(boltCount == 2 && machineCount == 1) {
				System.out.println("Worker " + threadName + " assembled product >>>>>>>>>>>>>>>>>>>>>>>");
				++finishedProductCount;

				/*synchronized(this) {
					wait();
				}*/
				return ProductAssembled.YES;
			}
			if(boltCount > 2 || machineCount > 1) {
				return ProductAssembled.EXCEEDED;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ProductAssembled.NO;
	}
	public enum ProductAssembled {
	      YES, NO, EXCEEDED
	}
	
	/**
	 * @param threadName
	 * @return
	 */
	private void reCheckAssembly(final String threadName) {
		try {
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
			System.out.println("threadName: "+threadName+ " bolt count:"+boltCount + " machineCount:"+machineCount);
			if(boltCount == 2 && machineCount == 1) {
				System.out.println("Worker " + threadName + " assembled product >>>>>>>>>>>>>>>>>>>>>>>");
				list = new ArrayList<ProductPart>();
				localMapWorkers.put(threadName,list);
				++finishedProductCount;

				/*synchronized(this) {
					wait();
				}*/
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}