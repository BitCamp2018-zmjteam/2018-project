package com.scorpions.bcp.test;

import java.util.HashMap;

import com.scorpions.bcp.DialogTree;
import com.scorpions.bcp.DialogTree.DMIterator;
import com.scorpions.bcp.Game;
import com.scorpions.bcp.creature.NPC;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.Event;
import com.scorpions.bcp.event.SetFlagEvent;
import com.scorpions.bcp.event.TradeOpenEvent;

public class DTDriver {
	public static void main(String[] args) {
		NPC npc = new NPC(10,10,10,10,10,10,null,"Shop mcKeeper");
		DialogTree d = new DialogTree("Hello, how can I help you today?",new Game(null),"npcOutraged",true,npc.getUUID());
		npc.setDialogTree(d);
		Player p = new Player(10,10,10,10,10,10, "Null Nullson", "Missingno.", "Errorbarian");
		HashMap<Integer,Event> h = new HashMap<>();
		
		for (int i=-5;i<=14;i++)
			h.put(i, d.makeExtendedSpeechItem(new Event[]{d.makeSpeechItem("[FAILED] As if! Get out of my shop!"),new SetFlagEvent(p,"npcOutraged")}));
		for (int i=15;i<=30;i++)
			h.put(i, d.makeExtendedSpeechItem(new Event[]{d.makeSpeechItem("[SUCCEEDED] Please don't hurt me!"),new SetFlagEvent(p,"npcOutraged")}));
		
		p.addFlag("knowBanditIssue");
		DMIterator dm = d.getIterator();
		dm.addSpeechOption("Can I see your shop?","Of course!", new Event[]{new TradeOpenEvent()});
		dm.addFlaggedSpeechOption("I heard about a bandit issue", "Yes, the bandits have been harassing the village", "knowBanditIssue");
		dm.goDown(1);
		dm.addSpeechOption("I can help with the bandits", "Oh thank you adventurer", new Event[]{new SetFlagEvent(p,"dealWithBandits")});
		dm.addSpeechOption("Too bad", "Hrm.");
		dm.addSpeechCheckOption("I'm one of the bandits, pay up.",h,"Intimidation");
		npc.interact(p);
	}
}
