package com.yang.MYModel;

import java.io.Serializable;

public class Textbook implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String city;
	int areaID;
	String version;
	int versionId;
	String course;
	int courseId;
	String grade;
	int gradeId;
	int  isBixiu;//1为必修，2为选修
	int subversion;//1为上册，2为下册
	int chapterID;
	
	public int getChapterID() {
		return chapterID;
	}

	public void setChapterID(int chapterID) {
		this.chapterID = chapterID;
	}

	public Textbook() {
		super();
	}

	public int getAreaID() {
		return areaID;
	}

	public void setAreaID(int areaID) {
		this.areaID = areaID;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getGradeId() {
		return gradeId;
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

	public int getIsBixiu() {
		return isBixiu;
	}

	public void setIsBixiu(int isBixiu) {
		this.isBixiu = isBixiu;
	}

	public int getSubversion() {
		return subversion;
	}

	public void setSubversion(int subversion) {
		this.subversion = subversion;
	}

	@Override
	public boolean equals(Object o) {
		Textbook book = (Textbook)o;
		if(this.city.equals(book.city)&&this.course.equals(book.course)&&this.grade.equals(book.grade)&&this.version.equals(book.version))
			return true;
		else 
			return false;
	}
	
}
