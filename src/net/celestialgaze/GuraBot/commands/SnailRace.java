package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.DelayedRunnable;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SnailRace extends Command {
	List<Long> channelsRunning = new ArrayList<Long>();
	public SnailRace() {
		super(new CommandOptions()
				.setName("snailrace")
				.setDescription("Make snails race each other!")
				.setCategory("Fun")
				.setCooldown(30.0)
				.setUsage("<speed multiplier> <snail amount> <track distance>")
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		int trackDistance = 10;
		int snailAmount = 10;
		double speedMultiplier = 1.0;
		
		if (args.length > 0 && SharkUtil.canParseDouble(args[0])) speedMultiplier = SharkUtil.parseDouble(args[0]);
		if (args.length > 1 && SharkUtil.canParseInt(args[1])) snailAmount = SharkUtil.parseInt(args[1]);
		if (args.length > 2 && SharkUtil.canParseInt(args[2])) trackDistance = SharkUtil.parseInt(args[2]);
		
		if (getSize(trackDistance, snailAmount) > MessageEmbed.TEXT_MAX_LENGTH) {
			SharkUtil.error(message, "Sorry, that'll overflow the embed :(");
			return;
		} else if (getDuration(trackDistance, speedMultiplier) > 180) {
			SharkUtil.error(message, "Sorry, that'll take too long :(");
			return;
		} else if (trackDistance < 0 || snailAmount < 0) {
			SharkUtil.error(message, "Sorry, can't go negative");
			return;
		} else if (speedMultiplier < 0.5) {
			SharkUtil.error(message, "Oh come on, they're slow enough.");
			return;
		} else if (speedMultiplier > 1000) {
			SharkUtil.error(message, "Okay, that's just too fast now.");
			return;
		} else if (channelsRunning.contains(message.getChannel().getIdLong())) {
			SharkUtil.error(message, "Wait for the other snails to finish racing!");
			return;
		}
		
		channelsRunning.add(message.getChannel().getIdLong());
		
		List<Snail> snails = new ArrayList<Snail>();
		for (int i = 0; i < snailAmount; i++) {
			snails.add(new Snail(i+1, speedMultiplier, trackDistance));
		}
		EmbedBuilder eb = new EmbedBuilder();
		
		final long startTime = System.currentTimeMillis();
		final int finalTrackDistance = trackDistance;
		final double finalSpeedMultiplier = speedMultiplier;
		message.getChannel().sendMessage(new EmbedBuilder().setDescription("Starting race...").build()).queue(response -> {
			Runnable rn = new Runnable() {

				@Override
				public void run() {
					int winner = -1;
					StringBuilder sb = new StringBuilder();
					Collections.sort(snails, new Comparator<Snail>() {
					    public int compare(Snail s1, Snail s2) {
					        return Double.compare(s1.distance, s2.distance);
					    }
					});
					for (Snail snail : snails) {
						sb.append(snail.visualize() + "**" + snail.id + ".**\n");
						if (snail.distance >= finalTrackDistance) {
							winner = snail.id;
						}
					}

					if (winner == -1) {
						new DelayedRunnable(this).execute(System.currentTimeMillis()+1250);
					} else {
						sb.append("**Snail " + winner + " wins in " + 
							SharkUtil.formatDuration(Math.round((System.currentTimeMillis()-startTime)*finalSpeedMultiplier))
							+ "!**");
						snails.clear();
						channelsRunning.remove(message.getChannel().getIdLong());
					}
					response.editMessage(eb.setDescription(sb.toString()).build()).queue();
				}
				
			};
			rn.run();
		});
		
	}
	class Snail {
		public double distance = 0.0;
		public int id = 0;
		private int trackDistance = 0;
		double speed = 1.0;
		public Snail(int id, double speed, int trackDistance) {
			this.id = id;
			this.trackDistance = trackDistance;
			this.speed = speed;
			move();
		}
		public void move() {
			Random rn = new Random();
			new DelayedRunnable(new Runnable() {

				@Override
				public void run() {
					 distance += (rn.nextDouble() / 5 * speed);
					 move();
				}
				
			}).execute(System.currentTimeMillis() + rn.nextInt(1000));
		}
		public String visualize() {
			StringBuilder sb = new StringBuilder();
			sb.append("🏁");
			for (int i = Math.toIntExact(Math.round(distance)); i < trackDistance; i++) {
				sb.append("-");
			}
			sb.append("🐌");
			return sb.toString();
		}
	}

	private int getSize(int trackDistance, int snailAmount) {
		return (trackDistance+25)*snailAmount+17;
	}
	
	private int getDuration(int trackDistance, double speedMultiplier) {
		return Math.toIntExact(Math.round((trackDistance * 2.5) / (speedMultiplier)));
	}
}
