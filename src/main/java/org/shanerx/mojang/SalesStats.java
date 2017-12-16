/*
 *     Copyright 2016-2017 SparklingComet @ http://shanerx.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.shanerx.mojang;

/**
 * This class is used for retrieving information from the Mojang store.
 */
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

	/**
	 * Gets the total amount of sales over time.
	 *
	 * @return all the sales made
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Gets the sales made within the last 24 hours.
	 *
	 * @return the sales made today
	 */
	public int getLast24hrs() {
		return last24hrs;
	}

	/**
	 * Gets the sale rate, aka the average amount of sales in a second.
	 *
	 * @return the mean sale rate
	 */
	public int getSaleVelocityPerSeconds() {
		return salesPerSec;
	}

	/**
	 * This enum represents the possible options for the shop-API query.
	 */
	public enum Options {
		ITEM_SOLD_MINECRAFT,
		PREPAID_CARD_REDEEMED_MINECRAFT,
		ITEM_SOLD_COBALT,
		ITEM_SOLD_SCROLLS;

		/**
		 * Returns the string version of this enum, to be used when querying the API.
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}
}