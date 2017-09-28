package entities;

import java.util.concurrent.Callable;

public class Worker implements Callable<Void> {
	
	TeamMember teamMember;
	
	public Worker(TeamMember member){
		teamMember = member;
	}

	@Override
	public Void call() throws Exception {
		teamMember.startWorking();
		return null;
	}

}
