package net.celestialgaze.GuraBot.commands.module;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.celestialgaze.GuraBot.commands.Commands;
import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandModule;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.IPageCommand;
import net.celestialgaze.GuraBot.commands.classes.Subcommand;
import net.celestialgaze.GuraBot.util.BulletListBuilder;
import net.celestialgaze.GuraBot.util.InteractableMessage;
import net.celestialgaze.GuraBot.util.PageMessage;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class ModuleList extends Subcommand implements IPageCommand {

	public ModuleList(Command parent) {
		super(new CommandOptions()
				.setName("list")
				.setDescription("Gets a list of all modules")
				.setUsablePrivate(false)
				.verify(),
				parent);
	}

	@Override
	public void init() {}

	public EmbedBuilder createEmbed(Message userMessage, Message messageToEdit, int page) {
		return createEmbed(userMessage, messageToEdit, page, null);
	}
	@Override
	public EmbedBuilder createEmbed(Message userMessage, Message messageToEdit, int page, List<Command> extra) {
		int pageSize = PAGE_SIZE;
		int startPosition = (page-1)*pageSize;
		int totalPages = Math.toIntExact(Math.round(Math.ceil((Commands.modules.values().size()+0.0)/(PAGE_SIZE+0.0))));
		List<CommandModule> modules = new ArrayList<CommandModule>(Commands.modules.values());
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Module List - Page (" + page + "/" + totalPages + ")");
		((PageMessage)InteractableMessage.list.get(messageToEdit.getIdLong())).setMaxSize(totalPages);
		modules.sort(Comparator.comparing(CommandModule::getCategory));
		Map<String, ArrayList<CommandModule>> categorized = new HashMap<String, ArrayList<CommandModule>>();
		for (int i = 0; i < modules.size(); i++) {
			if (i < startPosition) continue;
			if (i > startPosition+pageSize-1) break;
			CommandModule module = modules.get(i);
			if (!categorized.containsKey(module.getCategory())) {
				categorized.put(module.getCategory(), new ArrayList<CommandModule>());
			}
			categorized.get(module.getCategory()).add(module);
		}
		for (int i = 0; i < categorized.size(); i++) {
			String categoryName = new ArrayList<String>(categorized.keySet()).get(i);
			List<CommandModule> category = categorized.get(categoryName);
			BulletListBuilder blb = new BulletListBuilder();
			for (int j = 0; j < category.size(); j++) {
				CommandModule module = category.get(j);
				blb.add(module.getName() + " " + (module.isEnabled(userMessage.getGuild().getIdLong()) ?
						"**(Enabled)**" : "**(Disabled)**"), module.getDescription());
			}
			eb.addField(categoryName, blb.build(), false);
		}
		return eb;
	}
	
	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		SharkUtil.sendHelpMenu(message, this);
	}


}
