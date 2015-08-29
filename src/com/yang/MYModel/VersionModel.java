package com.yang.MYModel;

import java.util.List;

public class VersionModel {
	private String name;
	private List<CourseModel> courseList;
	private GradeModel parent;

	public VersionModel() {
		super();
	}

	public VersionModel(String name, List<CourseModel> courseList,
			GradeModel parent) {
		super();
		this.name = name;
		this.courseList = courseList;
		this.parent = parent;
	}

	public GradeModel getParent() {
		return parent;
	}

	public void setParent(GradeModel parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CourseModel> getcourseList() {
		return courseList;
	}

	public void setcourseList(List<CourseModel> courseList) {
		this.courseList = courseList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", courseList=" + courseList + "]";
	}

	@Override
	public boolean equals(Object o) {
		VersionModel v = (VersionModel) o;
		if (v.parent.equals(this.parent)) {

		} else {
			return false;
		}
		return super.equals(o);
	}

}
