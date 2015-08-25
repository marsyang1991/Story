package com.yang.MYModel;

public class CourseModel {
	private String name;
	private VersionModel parent;
	public CourseModel() {
		super();
	}

	public CourseModel(String name,VersionModel parent) {
		super();
		this.name = name;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public VersionModel getParent() {
		return parent;
	}

	public void setParent(VersionModel parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + "]";
	}
}
