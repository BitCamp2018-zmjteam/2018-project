package com.scorpions.bcp;

import java.util.ArrayList;

import com.scorpions.bcp.creature.Player;

/**
 * The dialog tree that players can interact with
 * @author Morgan
 *
 */
public class DialogTree {
	//base is the starting line "i.e. How can I help you today?"
	private SpeechItem base;
	public DialogTree(String base) {
		this.base=new SpeechItem(base);
	}
	//How DM moves down the tree to add stuff
	public class Iterator {
		private SpeechItem sp;
		public Iterator() {
			sp = base;
		}
	}
	/**
	 * NPC talks, player answers (as a PlayerSpeechOption)
	 * @author Morgan
	 *
	 */
	private SpeechItem endConvo = new SpeechItem("Goodbye");
	private class SpeechItem implements Interaction {
		private String base; //What the NPC says
		private ArrayList<PlayerSpeechItem> playerOptions; //What the player can say in response
		public SpeechItem(String base) {
			this.base=base;
		}
		public void addSpeechOption(String playerOption) {
			playerOptions.add(new PlayerSpeechItem(playerOption, endConvo));
		}
		public void addSpeechOption(String playerOption,String npcResponse) {
			playerOptions.add(new PlayerSpeechItem(playerOption, new SpeechItem(npcResponse)));
		}
		//Show the dialog tree, allow selection
		public void doInteraction() {
			
		}
	}
	/**
	 * Player talks, NPC answers (as a SpeechItem)
	 * @author Morgan
	 *
	 */
	public class PlayerSpeechItem {
		String playerSays; //What the player said
		SpeechItem npcResponse; //NPC's response
		public PlayerSpeechItem(String player, SpeechItem npc) {
			playerSays = player;
			npcResponse = npc;
		}
		public void addNPCResponse(SpeechItem npc) {
			npcResponse = npc;
		}
	}
	/**
	 * Like a PlayerSpeechItem, but requires the player to have a certain achievement (a flag) to show up
	 * @author Morgan
	 *
	 */
	private class FlaggedSpeechItem extends PlayerSpeechItem {
		String flag; //For future: multiple flags in boolean combos
		public FlaggedSpeechItem(String base, SpeechItem npc, String flag) {
			super(base,npc);
			this.flag = flag;
		}
		public boolean PlayerHasFlag(Player p) {
			return p.hasFlag(flag);
		}
	}
}
