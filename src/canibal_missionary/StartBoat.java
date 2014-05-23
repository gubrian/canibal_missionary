package canibal_missionary;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StartBoat {

	// The total number of people that will be on the boat.
	private int boatSpace = 3;
	// The number of cannibals and missionaries that remain on the bank.
	private int cOnBoat = 0;
	private int mOnBoat = 0;
	// Lock for multi-thread
	final Lock lock = new ReentrantLock();
	final Condition m = lock.newCondition();
	final Condition c = lock.newCondition();
	// Maximum number of cannibals and missionaries that will be on the boat.
	private int cBoatMax = 0;
	private int mBoatMax = 0;
	// Information for scheduling.
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
		message += "\r\n";
		if (mOnBoat == 0 && cOnBoat == 0) {
			BufferedWriter writer;
			try {
				writer = new BufferedWriter( new FileWriter("output.txt"));
				writer.write(message);
				System.out.println(message);
				writer.close( );
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		boatSpace = 3;
		calculateMax();
		message += "Boat leaving with";
		m.signalAll();
		c.signalAll();
	}

	/**
	 * This function is used to get the maximum number for cannibals and
	 * missionaries in the next round.
	 */
	private void calculateMax() {
		if (mOnBoat <= 3) {
			mBoatMax = mOnBoat != 1 ? mOnBoat : 0;
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
	
	/**
	 * Read and parse files.
	 * @param cannibals  the IDs for cannibals.
	 * @param missionaries  the IDs for missionaries.
	 * @throws IOException
	 */
	public static void readfile(ArrayList<Integer> cannibals, ArrayList<Integer> missionaries) throws IOException {
		FileInputStream fstream = new FileInputStream("input.txt");
		BufferedReader read= new BufferedReader(new InputStreamReader(fstream));
		StringTokenizer st;
		String data = read.readLine();
		while(!(data == null || data.equals(""))){
			st = new StringTokenizer(data);
			String species = st.nextToken();
			String idnumber = st.nextToken();
			if(species.equals("Cannibal")){
				cannibals.add(Integer.parseInt(idnumber));
			} else if(species.equals("Missionary")){
				missionaries.add(Integer.parseInt(idnumber));
			}
			data = read.readLine();
		}
		fstream.close();
	}
	
	public static void main(String args[]) throws IOException {
		ArrayList<Integer> cannibals = new ArrayList<Integer>();
		ArrayList<Integer> missionaries = new ArrayList<Integer>();
		readfile(cannibals, missionaries);
		int cannibalNumber = cannibals.size();
		int missionaryNumber = missionaries.size();
		StartBoat can_mis = new StartBoat(cannibalNumber, missionaryNumber);
		for (int i = 0; i < cannibalNumber; i++) {
			new Thread(new CannibalBoat(cannibals.get(i), can_mis)).start();
		}
		for (int i = 0; i < missionaryNumber; i++) {
			new Thread(new MissionaryBoat(missionaries.get(i), can_mis)).start();
		}
		return;
	}
}
