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
	
	String message = "";
	
	public StartBoat(int cOnBoat, int mOnBoat) {
		this.cOnBoat = cOnBoat;
		this.mOnBoat = mOnBoat;
	}
	
	/**
	 * Thread to process cannibal.
	 * @throws InterruptedException 
	 */
	public void cannibalArrive() throws InterruptedException {
		lock.lock();
		if(cBoatMax == 0) {
			c.await();
		}
	}
	
	/**
	 * Thread to process missionary
	 * @throws InterruptedException
	 */
	public void missionaryArrive() throws InterruptedException {
		lock.lock();
		while(mBoatMax == 0) {
			m.await();
		}
		
		
	}
	
	
	/**
	 * The thread to make boat leave.
	 */
	public void onBoat() {
		boatSpace = 3;			
		m.signal();
		c.signal();	
		message = "Boat leaving with ";
		cOnBoat = 0;
		mOnBoat = 0;
		calculateMax();
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
			
		}
		
	}
	
	
	public static void main(String args[]) {
		int cannibalNumber = 5;
		int missionaryNumber = 4;
		StartBoat can_miss = new StartBoat(cannibalNumber, missionaryNumber);
		for(int i = 0; i < cannibalNumber; i++) {
			new Thread(can_miss.new cBoat(i)).start();
		}
		for(int i = 0; i < missionaryNumber;i++) {
			new Thread(can_miss.new mBoat(i)).start();
		}
	}

}
