package com.scorpions.bcp;

import java.util.ArrayList;

import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.Event;

/**
 * The dialog tree that players can interact with
 * @author Morgan
 *
 */
public class DialogTree {
	//base is the starting line "i.e. How can I help you today?"
	private SpeechItem base;
	private String flag;
	private boolean outrage;
	public DialogTree(String base) {
		this.base=new SpeechItem(base);
	}
	/**
	 * Make a new DialogTree item
	 * @param base the initial line of dialog
	 * @param flag a flag required/prohibiting conversation
	 * @param outrage if true, flag prohibits conversation
	 */
	public DialogTree(String base, String flag, boolean outrage) {
		this.base = new SpeechItem(base);
		this.flag = flag;
		this.outrage = outrage;
	}
	/**
	 * Check flag to start
	 * @author Morgan
	 * @param p the player to check
	 * @return true if the conversation can start (two cases)
	 * <br>1. The character requires a flag and has it
	 * <br>2. The character prohibits a flag and the character doesn't have it
	 */
	public boolean canStart(Player p) {
		return p.hasFlag(flag) ^ outrage;
	}
	//How DM moves down the tree to add stuff
	public class DMIterator {
		//TODO - Add options for DM to add/change/remove dialog options
		//TODO - Possibly - Add way for DM to interfere with running conversation
		private SpeechItem sp;
		public DMIterator() {
			sp = base;
		}
		public boolean addSpeechOption(String option) {
			sp.addSpeechOption(option);
			return true;
		}
		public boolean addSpeechOption(String option, String npcResponse) {
			sp.addSpeechOption(option, npcResponse);
			return true;
		}
		public boolean addSpeechOption(String option, Event npcResponse) {
			sp.addSpeechOption(option, npcResponse);
			return true;
		}
		public boolean removeSpeechOption(int index) {
			sp.removePlayerOption(index);
			return true;
		}
		public boolean clearPlayerOptions() {
			sp.clearPlayerOptions();
			return true;
		}
		public boolean changeBaseDialog(String s) {
			sp.changeBaseDialog(s);
			return true;
		}
		//Descend a level into the conversation
		public boolean goDown(int index) {
			if (sp.playerOptions.size() >= index) { //Out of range
				System.err.println("playerOptions not that long (DT01)");
				return false;
			} else if (sp.playerOptions.get(index).npcResponse == null) { //There is no NPC response yet
				System.err.println("NPC response not present (DT02)");
				return false;
			} else if (!(sp.playerOptions.get(index).npcResponse instanceof SpeechItem)) { //NPC's response isn't a speechItem
				System.err.println("NPC response not a SpeechItem (DT03)");
				return false;
			} else { //DM can go down to this level
				try {
					sp = (SpeechItem) sp.playerOptions.get(index).npcResponse;
				} catch (ClassCastException e) {
					System.err.println("The NPC's response is not a SpeechItem (This error should not happen) (DT04)");
				}
				return true;
			}
		}
		//Show current level of dialog
		public String toString() {
			String output="";
			for (PlayerSpeechItem p : sp.playerOptions) {
				//TODO - Concatenate the various options to the output string
			}
			return output;
		}
	}
	/**
	 * NPC talks, player answers (as a PlayerSpeechOption)
	 * If there are no items in playerOptions, then conversation ends once the NPC says their line
	 * @author Morgan
	 *
	 */
	private SpeechItem endConvo = new SpeechItem("Goodbye");
	private class SpeechItem extends Event {
		private String base; //What the NPC says
		private ArrayList<PlayerSpeechItem> playerOptions; //What the player can say in response
		public SpeechItem(String base) {
			this.base=base;
		}
		public void changeBaseDialog(String base) {
			this.base = base;
		}
		public void clearPlayerOptions() {
			playerOptions.clear();
		}
		public void removePlayerOption(int index) {
			playerOptions.remove(index);
		}
		/*
		 * Various options the DM can use to build the conversation
		 */
		public void addSpeechOption(String playerOption) {
			playerOptions.add(new PlayerSpeechItem(playerOption, endConvo));
		}
		public void addSpeechOption(String playerOption,String npcResponse) {
			playerOptions.add(new PlayerSpeechItem(playerOption, new SpeechItem(npcResponse)));
		}
		public void addSpeechOption(String playerOption,Event npcResponse) {
			playerOptions.add(new PlayerSpeechItem(playerOption, npcResponse));
		}
		//Show the dialog tree, allow selection
		public void doInteraction(Player pl) {
			for (PlayerSpeechItem p : playerOptions) {
				if (p.canShow(pl)) {
					//Placeholder - Output the options that can be shown to that player
				}
			}
		}
	}
	/**
	 * Player talks, NPC answers (as a SpeechItem/other playerInteraction)
	 * @author Morgan
	 *
	 */
	public class PlayerSpeechItem {
		String playerSays; //What the player said
		Event npcResponse; //NPC's response
		public PlayerSpeechItem(String player, Event npc) {
			playerSays = player;
			npcResponse = npc;
		}
		public void changePlayerSays(String ps) {
			playerSays = ps;
		}
		public void addNPCResponse(SpeechItem npc) {
			npcResponse = npc;
		}
		/**
		 * Can the player be shown this dialog option?
		 */
		public boolean canShow(Player p) {
			return true;
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
		public boolean canShow(Player p) {
			return p.hasFlag(flag);
		}
	}
}
