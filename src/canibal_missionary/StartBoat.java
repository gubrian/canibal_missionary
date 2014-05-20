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
	 * 
	 * @throws InterruptedException
	 */
	public void cannibalArrive(int id) throws InterruptedException {
		lock.lock();
		try {
			while (cBoatMax == 0) {
				try {
					c.await();
				} catch (InterruptedException e) {
				}
			}
			cOnBoat--;
			cBoatMax--;
			boatSpace--;
			message += "  Cannibal" + id;
			if (boatSpace == 0) {
				onBoat();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Thread to process missionary
	 * 
	 * @throws InterruptedException
	 */
	public void missionaryArrive(int id) throws InterruptedException {
		lock.lock();
		try {
			while (mBoatMax == 0) {
				try {
					m.await();
				} catch (InterruptedException e) {
				}
			}
			mOnBoat--;
			mBoatMax--;
			boatSpace--;
			message += "  Missionary" + id;
			if (boatSpace == 0) {
				onBoat();
			}

		} finally {
			lock.unlock();
		}
	}

	/**
	 * The thread to make boat leave.
	 */
	public void onBoat() {
		System.out.println(message);
		if (mOnBoat == 0 && cOnBoat == 0) {
			return;
		}
		boatSpace = 3;
		calculateMax();
		message = "Boat leaving with";
		m.signalAll();
		c.signalAll();
	}

	/**
	 * This function is used to get the maximum number for cannibals and
	 * missionaries in the next round.
	 */
	private void calculateMax() {
		if (mOnBoat <= 3) {
			mBoatMax = mOnBoat;
			cBoatMax = boatSpace - mBoatMax;
		} else {
			if (mOnBoat - 3 > 2 * (cOnBoat % 3)) {
				mBoatMax = 3;
			} else {
				mBoatMax = 2;
			}
			cBoatMax = 1;
		}
	}

	public static void main(String args[]) {
		int cannibalNumber = 4;
		int missionaryNumber = 1;
		StartBoat can_mis = new StartBoat(cannibalNumber, missionaryNumber);
		for (int i = 1; i <= cannibalNumber; i++) {
			new Thread(new CannibalBoat(i, can_mis)).start();
		}
		for (int i = 1; i <= missionaryNumber; i++) {
			new Thread(new MissionaryBoat(i, can_mis)).start();
		}
	}
}
