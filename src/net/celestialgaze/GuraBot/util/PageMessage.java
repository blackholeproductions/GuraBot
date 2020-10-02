package net.celestialgaze.GuraBot.util;

import net.dv8tion.jda.api.entities.Message;

public class PageMessage extends InteractableMessage {
	private int page = 1;
	private int maxPage = Integer.MAX_VALUE;
	private ArgRunnable<Integer> update;
	
	public PageMessage(Message message, long ownerId, ArgRunnable<Integer> runnable) {
		super(message, ownerId);
		this.update = runnable;
		init();
	}
	public PageMessage(Message message, long ownerId, ArgRunnable<Integer> runnable, int page, int maxPage) {
		super(message, ownerId);
		this.page = page;
		this.update = runnable;
		this.maxPage = maxPage;
		init();
	}
	
	private void init() {
		addButton("◀️", new Runnable() {

			@Override
			public void run() {
				if (page-1 > 0) page--;
				update();
			}
			
		});
		addButton("▶️", new Runnable() {

			@Override
			public void run() {
				if (page < maxPage) page++;
				update();
			}
			
		});
		new DelayedRunnable(new ArgRunnable<Long>(getMessage().getIdLong()) {
			@Override
			public void run() {
				list.remove(getArg());
			}
		}).execute(System.currentTimeMillis()+5*(60*1000));
	}
	
	public void setArgRunnable(ArgRunnable<Integer> runnable) {
		this.update = runnable;
	}
	public void update() {
		update.run(page);
	}
	public void setMaxSize(int value) {
		maxPage = value;
	}
	public int getPage() {
		return page;
	}

}
