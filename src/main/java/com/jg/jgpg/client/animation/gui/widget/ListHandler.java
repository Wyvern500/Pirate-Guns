package com.jg.jgpg.client.animation.gui.widget;

import com.jg.jgpg.utils.MathUtils;

public class ListHandler {
	
	int visibleItems;
	int offset;
	int start;
	int end;
	int offsetStart;
	int offsetEnd;
	
	public ListHandler(int visibleItems) {
		this.visibleItems = visibleItems;
	}
	
	public void update(int totalItems, float progress) {
		int dif = totalItems - visibleItems;
		this.offset = (int) Math.floor(MathUtils.lerp(0, dif, progress));
		calculate(totalItems);
	}

	public void calculate(int totalItems) {
		if(offset < 0) {
			offset = 0;
		}
		this.start = offset;
		this.end = offset + visibleItems;
		//LogUtils.log("ListHandler", "End: " + end);
		if(end > totalItems) {
			end = totalItems;
		}
		this.offsetStart = (int) (Math.floor(start / visibleItems) * visibleItems);
		this.offsetEnd = (int) (Math.floor(end / visibleItems) * visibleItems);
		/*LogUtils.log("ListHandler", "End: " + end + " offset: " + offset + " totalItems: " + totalItems
				+ " offsetStart: " + offsetStart + " offsetEnd: " + offsetEnd + " visibleRange: " + 
				visibleItems);*/
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getVisibleItems() {
		return visibleItems;
	}

	public void setVisibleItems(int visibleItems) {
		this.visibleItems = visibleItems;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset, int totalItems) {
		this.offset = Math.max(0, offset);
		calculate(totalItems);
	}
	
	public int getOffsetStart() {
		return offsetStart;
	}

	public int getOffsetEnd() {
		return offsetEnd;
	}
	
}
