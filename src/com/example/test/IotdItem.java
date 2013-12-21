package com.example.test;

import android.graphics.Bitmap;

public class IotdItem {

	private StringBuffer datum;
	private StringBuffer title;
	private Bitmap img;
	private StringBuffer description;
	
	public IotdItem(){
		description = new StringBuffer();
		datum = new StringBuffer();
		title = new StringBuffer();
	}

	public String getDatum() {
		return datum.toString();
	}

	public void appendDatum(String datum) {
		this.datum.append(datum);
	}

	public String getTitle() {
		return title.toString();
	}

	public void appendTitle(String title) {
		this.title.append(title);
	}

	public Bitmap getImg() {
		return img;
	}

	public void setImg(Bitmap img) {
		this.img = img;
	}

	public String getDescription() {
		return description.toString();
	}

	public void appendDescription(String description) {
		this.description.append(description);
	}
}