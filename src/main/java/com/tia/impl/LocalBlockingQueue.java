package com.tia.impl;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

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

    public synchronized T take() throws InterruptedException {
        while (isEmpty) {
            wait();
        }
        T element = innerList.removeFirst();
        isEmpty = innerList.size() == 0;
        return element;
    }

    public synchronized void put(T element) {
        isEmpty = false;
        innerList.addLast(element);
        notify();
    }
}