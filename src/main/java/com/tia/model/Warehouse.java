package com.tia.model;

/**
 * @author Prashant Shelke
 * Added this model to initialize Product assembly
 * One product generates from 2 bolts + 1 machine
 */
public class Warehouse {
	private int machineCount;
	private int boltCount;
	private int assemblyDuration;//In seconds
	
	/**
	 * @param machineCount
	 * @param boltCount
	 * @param assemblyDuration
	 */
	public Warehouse(int machineCount, int boltCount, int assemblyDuration) {
		super();
		this.machineCount = machineCount;
		this.boltCount = boltCount;
		this.assemblyDuration = assemblyDuration;
	}
	
	public int getMachineCount() {
		return machineCount;
	}
	public void setMachineCount(int machineCount) {
		this.machineCount = machineCount;
	}
	public int getBoltCount() {
		return boltCount;
	}
	public void setBoltCount(int boltCount) {
		this.boltCount = boltCount;
	}
	public int getAssemblyDuration() {
		return assemblyDuration;
	}
	public void setAssemblyDuration(int assemblyDuration) {
		this.assemblyDuration = assemblyDuration;
	}
}
