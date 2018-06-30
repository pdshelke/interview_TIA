package com.tia.impl;

import java.util.LinkedList;
import java.util.List;

import com.tia.model.Bolt;
import com.tia.model.Machine;
import com.tia.model.ProductPart;

/**
 * @author Prashant Shelke
 *
 */
public class ProductProducer implements Runnable {
	private LocalBlockingQueue<ProductPart> partsQueue;

    public ProductProducer(LocalBlockingQueue<ProductPart> queue)
    {  
        this.partsQueue = queue;
    }

    public void run() {
    	List<ProductPart> converyorBelt = loadPartsFromWarehouse();

        for (ProductPart productPart : converyorBelt) {
        	loadProductOnConveyorBelt(productPart);
            try {
                //Thread.currentThread().sleep(2*1000);
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }
    private List<ProductPart> loadPartsFromWarehouse() {
		List<ProductPart> conveyorBelt = new LinkedList<ProductPart>();
		conveyorBelt.add(getBolt(1));
		conveyorBelt.add(getMachine(1));
		
		conveyorBelt.add(getBolt(2));
		conveyorBelt.add(getMachine(2));
		
		conveyorBelt.add(getBolt(3));
		conveyorBelt.add(getMachine(3));
		
		conveyorBelt.add(getBolt(4));
		conveyorBelt.add(getBolt(5));
		conveyorBelt.add(getBolt(6));
		conveyorBelt.add(null);
		System.out.println("ConveryorBelt items:"+conveyorBelt);
		return conveyorBelt;
	}
    
    public boolean loadProductOnConveyorBelt(ProductPart productPart) {
		String threadName = Thread.currentThread().getName(); 

    	System.out.println("Loading part on conveyor:"+productPart);		
		try {
			partsQueue.put(productPart);
			synchronized (partsQueue) {
				partsQueue.notifyAll();
			}
			
		} catch (Exception ie) {
			Thread.currentThread().interrupt();
			return false;
		}
		return true;
	}
    private Bolt getBolt(final int serialNumber) {
		return new Bolt("bolt"+serialNumber, serialNumber);
	}
	private Machine getMachine(final int serialNumber) {
		return new Machine("machine"+serialNumber, serialNumber);
	}
}