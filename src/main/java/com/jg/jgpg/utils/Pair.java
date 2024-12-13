package com.jg.jgpg.utils;

public class Pair<T, R> {
	
	protected T t;
	protected R r;
	
	public Pair(T t, R r) {
		this.t = t;
		this.r = r;
	}

	public T getLeft() {
		return t;
	}

	public R getRight() {
		return r;
	}
	
}