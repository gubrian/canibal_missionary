package canibal_missionary;

public class MissionaryBoat implements Runnable{
	public int id;
	StartBoat can_mis = null;

	public MissionaryBoat(int id,StartBoat can_mis) {
		this.id = id;
		this.can_mis = can_mis;
	}

	@Override
	public void run() {
		try {
			can_mis.missionaryArrive(id);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
