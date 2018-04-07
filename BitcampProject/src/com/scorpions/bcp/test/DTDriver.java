package com.scorpions.bcp.test;

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
		DialogTree d = new DialogTree("Hello, how can I help you today?",new Game());
		NPC npc = new NPC(10,10,10,10,10,10,d);
		Player p = new Player(10,10,10,10,10,10);
		DMIterator dm = d.getIterator();
		dm.addSpeechOption("Can I see your shop?",new Event[]{new TradeOpenEvent()});
		dm.addFlaggedSpeechOption("I heard about a bandit issue", "Yes, the bandits have been harassing the village", "knowBanditIssue");
		dm.goDown(1);
		dm.addSpeechOption("I can help with the bnadits", "Oh thank you adventurer", new Event[]{new SetFlagEvent(p,"dealWithBandits")});
		dm.addSpeechOption("Too bad", "Hrm.");
		npc.converse(p);
	}
}
