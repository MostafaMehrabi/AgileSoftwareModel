package entities;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class Worker implements Callable<Void> {
	
	private TeamMember teamMember;
	private CountDownLatch latch;
	
	public Worker(TeamMember member, CountDownLatch latch){
		teamMember = member;
		this.latch = latch;
	}

	@Override
	public Void call() throws Exception {
		teamMember.startWorking(latch);
		return null;
	}

}
