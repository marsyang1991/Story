package com.yang.MYModel;

import java.io.Serializable;

public class Story implements Serializable {
	private static final long serialVersionUID = 1L;
	Textbook book;
	String fileName = "";
	String fileUrl = "";
	int fileDuration = 0;
	String picBigUrl = "";
	String picNormalUrl = "";
	String picSmallUrl = "";
	int fileSize = 0;
	int renqi = 0;
	double rating = 0;
	int chapter = 1;// 章
	int part = 1;// 节
	int fileID;
	int outlineID;
	String fileTitle;
	String fileDescription;
	int hour;
	int minute;
	int second;
	int commentNumber;

	public Story() {
		super();
	}

	@Override
	public boolean equals(Object o) {
		Story story = (Story) o;
		if ((story.book == null && book == null) || story.book.equals(book)) {
			if (story.fileTitle.equals(fileTitle))
				return true;
		}
		return false;
	}

	public Textbook getBook() {
		return book;
	}

	public void setBook(Textbook book) {
		this.book = book;
	}

	public int getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
	}

	public String getImageUrl() {
		return fileName;
	}

	public void setImageUrl(String imageUrl) {
		this.fileName = imageUrl;
	}

	public int getRenqi() {
		return renqi;
	}

	public void setRenqi(int renqi) {
		this.renqi = renqi;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getChapter() {
		return chapter;
	}

	public void setChapter(int chapter) {
		this.chapter = chapter;
	}

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getId() {
		return fileID;
	}

	public void setId(int id) {
		this.fileID = id;
	}

	public String getTitle() {
		return fileTitle;
	}

	public void setTitle(String title) {
		this.fileTitle = title;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public String getDescription() {
		return fileDescription;
	}

	public void setDescription(String description) {
		this.fileDescription = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public int getFileDuration() {
		return fileDuration;
	}

	public void setFileDuration(int fileDuration) {
		this.fileDuration = fileDuration;
	}

	public String getPicBigUrl() {
		return picBigUrl;
	}

	public void setPicBigUrl(String picBigUrl) {
		this.picBigUrl = picBigUrl;
	}

	public String getPicNormalUrl() {
		return picNormalUrl;
	}

	public void setPicNormalUrl(String picNormalUrl) {
		this.picNormalUrl = picNormalUrl;
	}

	public String getPicSmallUrl() {
		return picSmallUrl;
	}

	public void setPicSmallUrl(String picSmallUrlString) {
		this.picSmallUrl = picSmallUrlString;
	}

	public int getFileID() {
		return fileID;
	}

	public void setFileID(int fileID) {
		this.fileID = fileID;
	}

	public int getOutlineID() {
		return outlineID;
	}

	public void setOutlineID(int outlineID) {
		this.outlineID = outlineID;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

}
