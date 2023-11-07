package com.mycompany.a2;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class CommandLeftHeading extends Command{
	private GameWorld gw;
	
	public CommandLeftHeading(GameWorld gw) {
		super("Left");
		this.gw = gw;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev)
	{
		gw.changeHeading('l');
	}
}
