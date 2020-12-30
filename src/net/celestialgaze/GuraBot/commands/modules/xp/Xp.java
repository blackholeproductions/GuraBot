package net.celestialgaze.GuraBot.commands.modules.xp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import org.bson.Document;

import net.celestialgaze.GuraBot.commands.classes.Command;
import net.celestialgaze.GuraBot.commands.classes.CommandOptions;
import net.celestialgaze.GuraBot.commands.classes.HelpCommand;
import net.celestialgaze.GuraBot.commands.classes.SettingsCmd;
import net.celestialgaze.GuraBot.commands.modules.xp.leaderboard.XpLeaderboard;
import net.celestialgaze.GuraBot.commands.modules.xp.roles.XpRoles;
import net.celestialgaze.GuraBot.commands.modules.xp.toggle.XpToggle;
import net.celestialgaze.GuraBot.db.ServerInfo;
import net.celestialgaze.GuraBot.util.ImageUtil;
import net.celestialgaze.GuraBot.util.ImageUtil.Alignment;
import net.celestialgaze.GuraBot.util.SharkUtil;
import net.celestialgaze.GuraBot.util.XPUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class Xp extends HelpCommand {

	public Xp() {
		super(new CommandOptions()
				.setName("xp")
				.setDescription("Get the XP you have earned")
				.setUsage("<user>")
				.setCategory("XP")
				.setCooldown(5.0)
				.setUsablePrivate(false)
				.verify(),
				"XP");
	}

	@Override
	public void commandInit() {
		XpHelp help = new XpHelp(this);
		XpToggle toggle = new XpToggle(this);
		XpRoles roles = new XpRoles(this);
		XpLeaderboard leaderboard = new XpLeaderboard(this);
		XpGive give = new XpGive(this);
		SettingsCmd settings = new SettingsCmd(module, this);
		XpLegacy legacy = new XpLegacy(this);
		addSubcommand(help);
		addSubcommand(toggle);
		addSubcommand(roles);
		addSubcommand(leaderboard);
		addSubcommand(give);
		addSubcommand(settings);
		addSubcommand(legacy);
	}

	@Override
	protected void run(Message message, String[] args, String[] modifiers) {
		Member member = SharkUtil.getMember(message, args, 0);
		User user = null;
		if (member == null) {
			user = message.getAuthor();
			member = message.getMember();
		} else {
			user = member.getUser();
		}
		ServerInfo si = ServerInfo.getServerInfo(message.getGuild().getIdLong());
		Document xpDoc = si.getModuleDocument("xp");
		long experience = si.getXP(user.getIdLong(), xpDoc);
		int level = XPUtil.getLevel(experience);
		long xpForLevel = XPUtil.getExpForLevel(level);
		long xpInLevel = XPUtil.getExpInLevel(experience);
		Role role = member.getGuild().getRoleById(XPUtil.getHighestRole(message.getGuild(), level, xpDoc));
		
		int width = 1200,
			height = 300;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Map<Key, Object> rhs = new HashMap<Key, Object>();
		rhs.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rhs.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		Graphics2D imgG2 = img.createGraphics();
		imgG2.setRenderingHints(rhs);
		BufferedImage avatar = null;
		try {
			avatar = ImageUtil.circle(ImageIO.read(new URL(user.getEffectiveAvatarUrl()+"?size=2048")));
		} catch (Exception e) {
			e.printStackTrace();
			SharkUtil.error(message, "Error retrieving avatar of user.");
		}
		BufferedImage guildIcon = null;
		try {
			if (message.getGuild().getIconUrl() != null) {
				guildIcon = ImageUtil.circle(ImageIO.read(new URL(message.getGuild().getIconUrl()+"?size=2048")));
			} else {
				String[] guildName = message.getGuild().getName().split(" ");
				String guildAbrv = "";
				for (int i = 0; i < guildName.length; i++) {
					guildAbrv += guildName[i].substring(0, 1);
				}
				BufferedImage guildIconPre = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
				Graphics2D giG2 = guildIconPre.createGraphics();
				giG2.setBackground(new Color(0x23272A));
				giG2.clearRect(0, 0, guildIconPre.getWidth(), guildIconPre.getHeight());
				giG2.setRenderingHints(rhs);
				giG2.setFont(new Font("MS UI Gothic", Font.BOLD, 48));
				ImageUtil.drawString(guildAbrv, guildIconPre.getWidth() / 2, (guildIconPre.getHeight() / 2)+16, giG2, Alignment.MIDDLE, new Color(0xffffff));
				guildIcon = ImageUtil.circle(guildIconPre);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SharkUtil.error(message, "Error retrieving guild icon.");
		}
		imgG2.drawImage(avatar, 45, 5, 290, 290, null);
		imgG2.setStroke(new BasicStroke(8));
		imgG2.setColor(new Color(member.getColorRaw()));
		imgG2.drawOval(45, 5, 290, 290);
		
		Font bigFont = new Font("MS UI Gothic", Font.BOLD, 68);
			imgG2.setFont(bigFont);
			imgG2.drawString(user.getName(), 375, 142);
		Font smallFont = new Font("MS UI Gothic", Font.BOLD, 47);
			imgG2.setFont(smallFont);
			if (XpModule.instance.showServerInRankCard.get(message.getGuild())) {
				ImageUtil.drawString(
					message.getGuild().getName(),
					440, 73, imgG2, Alignment.LEFT, new Color(member.getColorRaw()).darker()
				);
				imgG2.setStroke(new BasicStroke(2));
				imgG2.drawImage(guildIcon, 375, 32, 56, 56, null);
				imgG2.drawOval(375, 32, 56, 56);
			}
			ImageUtil.drawString(
				"Level " + level + (!role.getId().contentEquals(message.getGuild().getPublicRole().getId()) ? " (" + role.getName() + ")" : ""),
				1155, 145, imgG2, Alignment.RIGHT, new Color(role.getColorRaw())
			);
			ImageUtil.drawString(
				xpInLevel + " / " + xpForLevel + " (" + experience + " total, #" + XPUtil.getRank(message.getGuild(), member, xpDoc) + ")",
				1155, 270, imgG2, Alignment.RIGHT, new Color(member.getColorRaw())
			);
		BufferedImage progressBar = new BufferedImage(788, 75, BufferedImage.TYPE_INT_ARGB);
		Graphics2D pbG2 = progressBar.createGraphics();
		pbG2.setRenderingHints(rhs);
		pbG2.setClip(new RoundRectangle2D.Float(0, 0, progressBar.getWidth(), progressBar.getHeight(), 90f, 90f));
		pbG2.setBackground(new Color(member.getColorRaw()).darker());
		pbG2.clearRect(0, 0, progressBar.getWidth(), progressBar.getHeight());
		pbG2.setColor(new Color(member.getColorRaw()));
		try {
			int barWidth = Math.toIntExact(Math.round(((xpInLevel + 0.0) / (xpForLevel + 0.0))*progressBar.getWidth()));
			pbG2.fillRect(0, 0,  barWidth, progressBar.getHeight());
			pbG2.setFont(new Font("MS UI Gothic", Font.BOLD, 47));
			if (pbG2.getFontMetrics().stringWidth(Math.round(((xpInLevel + 0.0) / (xpForLevel + 0.0))*100) + "%") >= barWidth) {
				ImageUtil.drawString(
					Math.round(((xpInLevel + 0.0) / (xpForLevel + 0.0))*100) + "%",
					barWidth + 10, progressBar.getHeight() / 2 + 20, pbG2, Alignment.LEFT, new Color(member.getColorRaw())
				);
			} else {
				ImageUtil.drawString(
					Math.round(((xpInLevel + 0.0) / (xpForLevel + 0.0))*100) + "%",
					barWidth / 2, progressBar.getHeight() / 2 + 20, pbG2, Alignment.MIDDLE, new Color(member.getColorRaw()).darker()
				);
			}
		} catch (Exception e) {} // no one likes you
		imgG2.setColor(ImageUtil.brighter(new Color(member.getColorRaw()), 0.075));
		imgG2.drawImage(progressBar, 380, 155, progressBar.getWidth(), progressBar.getHeight(), null);
		imgG2.setStroke(new BasicStroke(5));
		imgG2.drawRoundRect(380, 155, progressBar.getWidth(), progressBar.getHeight(), 90, 90);
		try {
			message.getChannel().sendFile(ImageUtil.toBaos(img).toByteArray(), "image.png")
					.queue();
		} catch (IOException e) {
			e.printStackTrace();
			SharkUtil.error(message, "Error while generating image");
		}
	}
}
