package canibal_missionary;

public class StartBoat {
	
	private int boatSpace = 3;
	private int cOnBoat = 0;
	private int mOnBoat = 0;
	
	private int cBoatMax = 0;
	private int mBoatMax = 0;
	
	
	
	
	
	
	
   class cBoat implements Runnable{
		public int id;
		
		public cBoat(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			
		}
		
	}
	
	
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
		StartBoat can_miss = new StartBoat();
		
		for(int i = 0; i < cannibalNumber; i++) {
			new Thread(can_miss.new cBoat(i)).start();
		}
		
		for(int i = 0; i < missionaryNumber;i++) {
			
		}
		
	}

}
