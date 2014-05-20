package canibal_missionary;

public class CannibalBoat implements Runnable{
	public int id;
	StartBoat can_mis = null;

	public CannibalBoat(int id, StartBoat can_mis) {
		this.id = id;
		this.can_mis = can_mis;
	}

	@Override
	public void run() {
		try {
			can_mis.cannibalArrive(id);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
