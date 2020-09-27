package net.celestialgaze.GuraBot.commands.classes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import net.celestialgaze.GuraBot.GuraBot;
import net.celestialgaze.GuraBot.json.BotInfo;
import net.celestialgaze.GuraBot.json.BotStat;
import net.celestialgaze.GuraBot.util.DelayedRunnable;
import net.celestialgaze.GuraBot.util.SharkUtil;

public abstract class Command implements ICommand {
	public static final String DEFAULT_CATEGORY = "Uncategorized";
	protected String name; // The name of the command
	protected String usage; // Usage that appears in the help menu
	protected String description; // Description that appears in the help menu
	protected String category = DEFAULT_CATEGORY; // Category that the command belongs to
	protected Map<String, Command> subcommands = new HashMap<String, Command>(); // Maps command name to the command in question
	Permission permission = null; // The required permission for this command. null is none needed
	protected boolean usablePrivately = true; // Whether this command is usable within DMs
	protected boolean needBotAdmin = false; // Whether the user requires bot admin to run this command
	protected double cooldownDuration = 0.5; // The cooldown that this command will have for each user.
	protected Map<Long, DelayedRunnable> userCooldowns = new HashMap<Long, DelayedRunnable>(); // Current users that have a cooldown
	
	public Command(Pair<CommandOptions, Boolean> pair) {
		preInit(pair, false);
	}
	public Command(Pair<CommandOptions, Boolean> pair, boolean initialize) {
		preInit(pair, initialize);
	}
	private void preInit(Pair<CommandOptions, Boolean> pair, boolean initialize) {
		if (!pair.getRight()) {
			System.err.println(name + " command was initialized incorrectly");
			return;
		}
		CommandOptions options = pair.getLeft();
		this.name = options.name;
		this.usage = options.usage;
		this.description = options.description;
		this.category = options.category;
		this.permission = options.permission;
		this.usablePrivately = options.usablePrivately;
		this.needBotAdmin = options.needBotAdmin;
		this.cooldownDuration = options.cooldownDuration;
		System.out.println("Sucessfully initialized " + name + " command");
		if (initialize) init();
	}
	public abstract void init();
	
	@Override
	public void attempt(Message message, String[] args, String[] modifiers) {
		if (canRun(message, false)) {
			try {
				run(message, args, modifiers); // Run first so any cooldown changes carry over
				
				DelayedRunnable runnable = new DelayedRunnable(() -> {
					userCooldowns.remove(message.getAuthor().getIdLong());
				}).execute(System.currentTimeMillis()+Math.round(cooldownDuration*1000));

				userCooldowns.put(message.getAuthor().getIdLong(), runnable);
			} catch (InsufficientPermissionException e) {
				SharkUtil.error(message, "I don't have the " + e.getPermission().getName() + " permission required to do this.");
			} catch (Exception e) {
				String argsString = "";
				for (String arg : args) {
					argsString += arg + " ";
				}
				argsString = argsString.trim();
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace();
				e.printStackTrace(pw);
				String error = "Something went horribly wrong...\n" + sw.toString().substring(0, Integer.min(700, sw.toString().length())) +
						"Args: " + argsString + "... \nFull message: " + 
						message.getContentRaw().substring(0, Integer.min(message.getContentRaw().length(), 100));
				SharkUtil.error(message, error);
				BotInfo.addLongStat(BotStat.ERRORS);
				if (message.getAuthor().getIdLong() != Long.parseLong("218525899535024129")) {
					message.getChannel().sendMessage("Reporting to cel...").queue(response -> {
						GuraBot.jda.getUserById(Long.parseLong("218525899535024129")).openPrivateChannel().queue(channel -> {
							channel.sendMessage("fix ur bot").queue(crashMsg -> {
								SharkUtil.error(crashMsg, error);
								response.editMessage("Successfully reported to cel.").queue();
							});
						});
					});
				}
			}
		}
	}
	public boolean canRun(Message message) {
		return canRun(message, true);
	}
	public boolean canRun(Message message, boolean silent) {
		if (!usablePrivately && message.getChannelType().equals(ChannelType.PRIVATE)) {
			error(message, "Sorry, this command is not available in DMs.", silent);
			return false;
		}
		if (permission != null && message.getChannelType().equals(ChannelType.TEXT) && !message.getMember().hasPermission(permission)) {
			error(message, "You need the " + permission.getName() + " permission to use this command", silent);
			return false;
		}
		if (needBotAdmin && message.getAuthor().getIdLong() != Long.valueOf("218525899535024129")) {
			error(message, "You're not celestialgaze#0001!", silent);
			return false;
		}
		if (cooldownDuration > 0.0 && userCooldowns.containsKey(message.getAuthor().getIdLong())) {
			error(message, "You must wait " + 
					String.format("%.2f", userCooldowns.get(message.getAuthor().getIdLong()).getTimeRemaining()/1000.0) + " seconds", silent);
			return false;
		}
		return true;
	}
	
	protected abstract void run(Message message, String[] args, String[] modifiers);

	private static void error(Message message, String error, boolean silent) {
		if (!silent) SharkUtil.error(message, error);
	}
	
	public String getName() {
		return name;
	}
	public String getUsage() {
		return usage;
	}
	public String getDescription() {
		return description;
	}
	public boolean isUsablePrivately() {
		return usablePrivately;
	}
	public boolean hasPermission() {
		return permission != null;
	}
	public Permission getPermission() {
		return permission;
	}
	public String getCategory() {
		return category;
	}
	
	public Map<String, Command> getSubcommands() {
		return subcommands;
	}
}
