package com.scorpions.bcp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.Event;
import com.scorpions.bcp.event.interact.PlayerMessageEvent;

/**
 * The dialog tree that players can interact with
 * @author Morgan
 *
 */
public class DialogTree implements Serializable{
	private static final long serialVersionUID = 1770872409725720898L;
	//base is the starting line "i.e. How can I help you today?"
	private SpeechItem base;
	private String flag="";
	private boolean outrage=true;
	private Player converser;
	private Game wholeGame;
	public DialogTree(String base,Game wholeGame) {
		this.base=new SpeechItem(base);
		this.wholeGame = wholeGame;
	}
	/**
	 * Make a new DialogTree item
	 * @param base the initial line of dialog
	 * @param flag a flag required/prohibiting conversation
	 * @param outrage if true, flag prohibits conversation
	 */
	public DialogTree(String base, Game wholeGame, String flag, boolean outrage) {
		this.base = new SpeechItem(base);
		this.wholeGame = wholeGame;
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
	public DMIterator getIterator() {
		return new DMIterator();
	}
	/**
	 * Start the conversation between the host NPC and the palyer p
	 * @author Morgan
	 *
	 */
	public void converse(Player p) { //TODO - add more conversation interaction
		converser = p;
		
		SpeechItem s = base;
		wholeGame.queueEvent(new PlayerMessageEvent(converser,s.show()));
		Scanner sc = new Scanner(System.in);
		do {
			int choice = sc.nextInt();
			if (s.playerOptions.size() > choice) {
				if (s.playerOptions.get(choice).getResponse(p) instanceof SpeechItem) {
					s = (SpeechItem)(s.playerOptions.get(choice).getResponse(p));
					wholeGame.queueEvent(new PlayerMessageEvent(converser,s.show()));
				} else {
					wholeGame.queueEvent(s.playerOptions.get(choice).getResponse(p));
					break;
				}
			}
		} while (!s.playerOptions.isEmpty());
		sc.close();
	}
	/**How DM moves down the tree to add stuff
	 * 
	 * @author Morgan
	 *
	 */
	public class DMIterator implements Serializable {
		private static final long serialVersionUID = -5940095089491535793L;
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
		public boolean addSpeechOption(String option, Event[] outcome) {
			sp.addExtendedSpeechOption(option,outcome);
			return true;
		}
		public boolean addSpeechOption(String option, String npcResponse, Event[] outcome) {
			sp.addExtendedSpeechOption(option, npcResponse, outcome);
			return true;
		}
		public boolean addFlaggedSpeechOption(String option, String flag) {
			sp.addFlaggedSpeechOption(option,flag);
			return true;
		}
		public boolean addFlaggedSpeechOption(String option, String npcResponse, String flag) {
			sp.addFlaggedSpeechOption(option, npcResponse, flag);
			return true;
		}
		public boolean addFlaggedSpeechOption(String option, String npcLine, Event[] npcResponse, String flag) {
			sp.addFlaggedSpeechOption(option, npcLine, npcResponse, flag);
			return true;
		}
		public void addSpeechCheckOption(String option, HashMap<Integer, Event> h, String skill) {
			sp.addSpeechCheckOption(option, h, skill);
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
			if (sp.playerOptions.size() < index) { //Out of range
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
			String output=sp.base+"\n----------\n";
			for (PlayerSpeechItem p : sp.playerOptions) {
				output += p.playerSays + "\n";
			}
			return output;
		}
	}
	private SpeechItem endConvo = new SpeechItem("Goodbye");

	public SpeechItem makeSpeechItem(String s) {
		return new SpeechItem(s);
	}
	
	public ExtendedSpeechItem makeExtendedSpeechItem(Event[] e) {
		return new ExtendedSpeechItem("",e);
	}
	/**
	 * NPC talks, player answers (as a PlayerSpeechOption)
	 * If there are no items in playerOptions, then conversation ends once the NPC says their line
	 * @author Morgan
	 *
	 */
	private class SpeechItem extends Event {
		private static final long serialVersionUID = 6551357719201392186L;
		private String base; //What the NPC says
		protected ArrayList<PlayerSpeechItem> playerOptions; //What the player can say in response
		public SpeechItem(String base) {
			playerOptions = new ArrayList<>();
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
		public void addFlaggedSpeechOption(String playerOption, String flag) {
			playerOptions.add(new FlaggedSpeechItem(playerOption, endConvo, flag));
		}
		public void addFlaggedSpeechOption(String playerOption,String npcResponse, String flag) {
			playerOptions.add(new FlaggedSpeechItem(playerOption, new SpeechItem(npcResponse), flag));
		}
		public void addFlaggedSpeechOption(String playerOption, String npcLine, Event[] npcResponse, String flag) {
			playerOptions.add(new FlaggedSpeechItem(playerOption, new ExtendedSpeechItem(npcLine, npcResponse), flag));
		}
		public void addExtendedSpeechOption(String playerOption, Event[] outcome) {
			playerOptions.add(new PlayerSpeechItem(playerOption, new ExtendedSpeechItem("",outcome)));
		}
		public void addExtendedSpeechOption(String playerOption,String npcResponse, Event[] outcome) {
			playerOptions.add(new PlayerSpeechItem(playerOption, new ExtendedSpeechItem(npcResponse,outcome)));
		}
		public void addSpeechCheckOption(String option, HashMap<Integer, Event> h, String skill) {
			playerOptions.add(new PlayerCheckSpeechItem(option,h,skill));
		}
		//Show the dialog tree, allow selection
		protected String show() {
			String output = base + "\n----------\n";
			int i = 0;
			for (PlayerSpeechItem p : playerOptions) {
				if (p.canShow(converser)) {
					//Placeholder - Output the options that can be shown to that player
					output += "[" + i + "] " + p.toString() + "\n";
					i++;
				}
			}
			return output;
		}
		@Override
		protected void enact(Game g) {} //SpeechItem is a subclass of Event for generality purposes
	}
	
	/**
	 * A SpeechItem that triggers an event when the speechItem is reached
	 * @author Morgan
	 *
	 */
	private class ExtendedSpeechItem extends SpeechItem {
		private static final long serialVersionUID = 3550003039970461100L;
		Event[] trigger; //Events triggered on start of esi
		public ExtendedSpeechItem(String base, Event[] outcome) {
			super(base);
			this.trigger = outcome;
		}
		protected String show() {
			for (Event e : trigger) {
				if (e instanceof SpeechItem) {
					wholeGame.queueEvent(new PlayerMessageEvent(converser,((SpeechItem)e).show()));
				} else {
					wholeGame.queueEvent(e);
				}
			}
			return super.show();
		}
	}
	/**
	 * Player talks, NPC answers (as a SpeechItem/other playerInteraction)
	 * @author Morgan
	 *
	 */
	private class PlayerSpeechItem implements Serializable {
		private static final long serialVersionUID = -4910592610642348689L;
		String playerSays; //What the player said
		Event npcResponse; //NPC's response
		public PlayerSpeechItem(String player, Event npc) {
			playerSays = player;
			npcResponse = npc;
		}
		public Event getResponse(Player p) {
			return npcResponse;
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
		public String toString() {
			return playerSays;
		}
	}
	/**
	 * Like a PlayerSpeechItem, but requires the player to have a certain achievement (a flag) to show up
	 * @author Morgan
	 *
	 */
	private class FlaggedSpeechItem extends PlayerSpeechItem {
		private static final long serialVersionUID = -8805072947967548197L;
		String flag; //For future: multiple flags in boolean combos
		public FlaggedSpeechItem(String base, Event npc, String flag) {
			super(base,npc);
			this.flag = flag;
		}
		public boolean canShow(Player p) {
			return p.hasFlag(flag);
		}
		public String toString() {
			return "["+flag+"] "+playerSays;
		}
	}
	
	/**
	 * Similar to a PlayerSpeechItem, but involves a skill check and can have multiple possible outcomes, depending on skill and chance 
	 * @author Morgan
	 *
	 */
	private class PlayerCheckSpeechItem extends PlayerSpeechItem {
		private static final long serialVersionUID = 6217165769150100400L;
		HashMap<Integer,Event> outcomes;
		String skill;
		public PlayerCheckSpeechItem(String option, HashMap<Integer, Event> h, String skill) {
			super(option,h.get(20));
			this.outcomes = h;
			this.skill = skill;
		}
		public void addNPCResponse(SpeechItem npc) {
		}
		public Event getResponse(Player p) {
			int skillMod = p.getSkillMod(skill);
			int outcome = (int)(Math.random()*19+skillMod+1); //1-20, weighted by skill
			return outcomes.get(outcome);
		}
	}
}
