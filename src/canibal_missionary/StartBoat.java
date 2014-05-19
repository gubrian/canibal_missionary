package canibal_missionary;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StartBoat {
	
	private int boatSpace = 3;
	private int cOnBoat = 0;
	private int mOnBoat = 0;
	
	final Lock lock = new ReentrantLock();
	final Condition m = lock.newCondition();
	final Condition c = lock.newCondition();
	
	private int cBoatMax = 0;
	private int mBoatMax = 0;
	
	String message = "Boat leaving with";
	
	public StartBoat(int cOnBoat, int mOnBoat) {
		this.cOnBoat = cOnBoat;
		this.mOnBoat = mOnBoat;
		calculateMax();
	}
	
	/**
	 * Thread to process cannibal.
	 * @throws InterruptedException 
	 */
	public void cannibalArrive(int id) throws InterruptedException {
		lock.lock();
		while(cBoatMax == 0) {
			c.await();
		}
		cOnBoat--;
		cBoatMax--;
		boatSpace--;
		message+= "  Cannibal" + id;
		if(boatSpace == 0) {
			onBoat();
		}
		lock.unlock();
	}
	
	/**
	 * Thread to process missionary
	 * @throws InterruptedException
	 */
	public void missionaryArrive(int id) throws InterruptedException {
		lock.lock();
		while(mBoatMax == 0) {
			m.await();
		}
		mOnBoat--;
		mBoatMax--;
		boatSpace--;
		message += "  Missionary" + id;
		if(boatSpace == 0) {
			onBoat();
		}
		lock.unlock();
	}
	
	
	/**
	 * The thread to make boat leave.
	 */
	public void onBoat() {
		System.out.println(message);
		if(mOnBoat == 0 && cOnBoat == 0) {
			return;
		}
		boatSpace = 3;		
		calculateMax();
		message = "Boat leaving with";
		m.signal();
		c.signal();	
		
	}
	
	/**
	 * This function is used to get the maximum number for cannibals and missionaries in 
	 * the next round.
	 */
	private void calculateMax() {
		if(mOnBoat <= 3) {
			mBoatMax = mOnBoat;
			cBoatMax = boatSpace - mBoatMax;
		} else {
			if(mOnBoat - 3 > 2 * (cOnBoat % 3)) {
				mBoatMax = 3;
			} else {
				mBoatMax = 2;
			}
			cBoatMax = 1;
		}
	}
	

	// The cannibal class
   class cBoat implements Runnable{
		public int id;
		
		public cBoat(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				cannibalArrive(id);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	// The missionary class
	class mBoat implements Runnable{
		public int id;
		
		public mBoat(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				missionaryArrive(id);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		int cannibalNumber = 1;
		int missionaryNumber = 5;
		StartBoat can_miss = new StartBoat(cannibalNumber, missionaryNumber);
		for(int i = 0; i < missionaryNumber;i++) {
			new Thread(can_miss.new mBoat(i)).start();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < cannibalNumber; i++) {
			new Thread(can_miss.new cBoat(i)).start();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
