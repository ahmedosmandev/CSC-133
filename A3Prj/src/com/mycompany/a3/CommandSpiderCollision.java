package com.mycompany.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class CommandSpiderCollision extends Command {


	private GameWorld gw;

	
	public CommandSpiderCollision(GameWorld gw) {
		super("Collide with Spider");
		this.gw = gw;
	}
	

	@Override
	public void actionPerformed(ActionEvent ev) {
		gw.antCollision('s');

	}
}