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
		}
	}

	public void process(final String threadName, final ProductPart part) {
        rwLock.writeLock().lock();
		try {
			System.out.println("Worker:" + threadName + " assembling product");
			if(null != localMapWorkers.get(threadName)) {
				if(!checkIfProductAssembled(threadName,part)) {
					List<ProductPart> tmp = localMapWorkers.get(threadName);
					tmp.add(part);
					localMapWorkers.put(threadName,tmp);
				}
			}else{
				List<ProductPart> tmp = new ArrayList<ProductPart>();
				tmp.add(part);
				localMapWorkers.put(threadName, tmp);
			}
			checkIfProductAssembled(threadName,part);
			System.out.println("Sleeping ");
			Thread.currentThread().sleep(3*1000);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
	        rwLock.writeLock().unlock();
		}
	}
	/**
	 * @param threadName
	 * @return
	 */
	private boolean checkIfProductAssembled(final String threadName,final ProductPart part) {
        rwLock.writeLock().lock();
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
			if(boltCount == 2 && machineCount == 1) {
				System.out.println("Worker " + threadName + " assembled product >>>>>>>>>>>>>>>>>>>>>>>");
				localMapWorkers.put(threadName, new ArrayList<ProductPart>());
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
	        rwLock.writeLock().unlock();
		}
		return false;
	}
}