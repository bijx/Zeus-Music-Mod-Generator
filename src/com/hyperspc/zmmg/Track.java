package com.hyperspc.zmmg;

public class Track {

	private String trackName, className, tag, path;
	private int duration, decibels;
	
	public Track(String path, String trackName, String className, String tag, int duration, int decibels) {
		this.path = path;
		this.trackName = trackName;
		this.className = className;
		this.tag = tag;
		this.duration = duration;
		this.decibels = decibels;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDecibels() {
		return decibels;
	}

	public void setDecibels(int decibels) {
		this.decibels = decibels;
	}
	
	public String toString() {
		return getTrackName();
	}
	
}
