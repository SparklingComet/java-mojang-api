package org.shanerx.mojang;

@SuppressWarnings("unused")
public class SalesStats {

	private int total;
	private int last24hrs;
	private int salesPerSec;
	
	protected SalesStats(int total, int last24hrs, int salesPerSec) {
		this.total = total;
		this.last24hrs = last24hrs;
		this.salesPerSec = salesPerSec;
	}
	
	public int getTotal() {
		return total;
	}
	
	public int getLast24hrs() {
		return last24hrs;
	}
	
	public int getSaleVelocityPerSeconds() {
		return salesPerSec;
	}
	
	public enum Options {
		ITEM_SOLD_MINECRAFT,
		PREPAID_CARD_REDEEMED_MINECRAFT,
		ITEM_SOLD_COBALT,
		ITEM_SOLD_SCROLLS;
		
		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}
}