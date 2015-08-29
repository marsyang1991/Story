/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package kankan.wheel.widget.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;

public class ListMapWheelAdapter extends AbstractWheelTextAdapter {

	// items
	private List<Map<String, String>> items;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the current context
	 * @param items
	 *            the items
	 */
	public ListMapWheelAdapter(Context context, List<Map<String, String>> items) {
		super(context);

		// setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
		this.items = items;
	}

	@Override
	public CharSequence getItemText(int index) {
		if (index >= 0 && index < items.size()) {
			String item = items.get(index).get("name");
			return item.toString();
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}
}
