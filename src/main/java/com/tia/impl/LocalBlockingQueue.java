package com.tia.impl;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import com.tia.model.ProductPart;

/**
 * @author Prashant Shelke
 *
 * @param <T>
 */
public class LocalBlockingQueue<T> extends LinkedBlockingQueue<T> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final LinkedList<T> innerList = new LinkedList<T>();
    private boolean isEmpty = true;

    @Override
    public synchronized T take() throws InterruptedException {
        while (isEmpty) {
            wait();
        }
        T element = innerList.removeFirst();
        isEmpty = innerList.size() == 0;
        return element;
    }

    @Override
    public synchronized void put(T element) {
        isEmpty = false;
        innerList.addLast(element);
        try {
			Thread.sleep(2*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        notify();
    }
}