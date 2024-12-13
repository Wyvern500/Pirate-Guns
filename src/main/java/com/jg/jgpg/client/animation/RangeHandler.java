package com.jg.jgpg.client.animation;

import com.jg.jgpg.utils.MathUtils;

public class RangeHandler {
	
	int visibleRange;
	int offset;
	int start;
	int end;
	int offsetStart;
	int offsetEnd;
	
	public RangeHandler(int visibleRange) {
		this.visibleRange = visibleRange;
	}
	
	public void update(int totalItems, float progress) {
		int dif = totalItems - visibleRange;
		this.offset = (int) Math.floor(MathUtils.lerp(0, dif, progress));
		calculate(totalItems);
	}

	public void calculate(int totalItems) {
		if(offset < 0) {
			offset = 0;
		}
		this.start = offset;
		this.end = offset + visibleRange;
		this.offsetStart = (int) (Math.floor(start / visibleRange) * visibleRange);
		this.offsetEnd = (int) (Math.floor(end / visibleRange) * visibleRange);
	}
	
	public void clear() {
		end = 0;
		offset = 0;
		offsetEnd = 0;
		offsetStart = 0;
		start = 0;
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

	public int getVisibleRange() {
		return visibleRange;
	}

	public void setVisibleRange(int visibleRange) {
		this.visibleRange = visibleRange;
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
