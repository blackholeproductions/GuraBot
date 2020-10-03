package net.celestialgaze.GuraBot.commands;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.util.BulletListBuilder;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class RoleInfo extends Command {

	public RoleInfo() {
		super(new CommandOptions()
				.setName("role")
				.setCategory("Utility")
				.setDescription("Gets more information about a certain role")
				.setUsage("<role>")
				.setUsablePrivate(false)
				.setCooldown(5.0)
				.verify());
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		Role role = SharkUtil.getRole(message, args, 0);
		EmbedBuilder eb = new EmbedBuilder();
		if (role != null) {
			BulletListBuilder blb = new BulletListBuilder()
					.add("**ID**", role.getId())
					.add("**Created at**", role.getTimeCreated().toString().replace("Z", "").replace("T", " "));
			String permissions = "";
			for(Permission permission : role.getPermissions()) {
				permissions += permission.getName() + ", ";
			}
			if (permissions.length() > 2) permissions = permissions.substring(0, permissions.length()-2);
			if (permissions.isEmpty()) permissions = "None";
			blb.add("**Permissions**", permissions);
			
			eb.setTitle(role.getName())
			  .setColor(role.getColor())
			  .setDescription(blb.build());
			
			message.getChannel().sendMessage(eb.build()).queue();
			return;
		}
		SharkUtil.error(message, "Role not found");
	}

}
