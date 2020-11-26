package net.celestialgaze.GuraBot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.DelayedRunnable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class Test extends Command {
	int trackDistance = 20;
	public Test() {
		super(new CommandOptions()
				.setName("test")
				.setDescription("test command")
				.setCategory("Debug")
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		List<Snail> snails = new ArrayList<Snail>();
		for (int i = 0; i < 10; i++) {
			snails.add(new Snail(i+1));
		}
		EmbedBuilder eb = new EmbedBuilder();
		
		message.getChannel().sendMessage(new EmbedBuilder().setDescription("Starting race...").build()).queue(response -> {
			Runnable rn = new Runnable() {

				@Override
				public void run() {
					int winner = -1;
					StringBuilder sb = new StringBuilder();
					Collections.sort(snails, new Comparator<Snail>() {
					    public int compare(Snail s1, Snail s2) {
					        return Integer.compare(s1.distance, s2.distance);
					    }
					});
					for (Snail snail : snails) {
						sb.append(snail.visualize() + "**" + snail.id + ".**\n");
						if (snail.distance >= trackDistance) {
							winner = snail.id;
						}
					}

					if (winner == -1) {
						new DelayedRunnable(this).execute(System.currentTimeMillis()+3500);
					} else {
						sb.append("**Snail " + winner + " wins!**");
						snails.clear();
					}
					response.editMessage(eb.setDescription(sb.toString()).build()).queue();
				}
				
			};
			rn.run();
		});
		
	}
	class Snail {
		public int distance = 0;
		public int id = 0;
		public Snail(int id) {
			this.id = id;
			move();
		}
		public void move() {
			Random rn = new Random();
			new DelayedRunnable(new Runnable() {

				@Override
				public void run() {
					 distance += rn.nextInt(2);
					 move();
				}
				
			}).execute(System.currentTimeMillis() + rn.nextInt(10000));
		}
		public String visualize() {
			StringBuilder sb = new StringBuilder();
			sb.append("🏁");
			for (int i = distance; i < trackDistance; i++) {
				sb.append("-");
			}
			sb.append("🐌");
			return sb.toString();
		}
	}

}
