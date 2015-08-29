package com.yang.MYModel;

import java.util.List;

public class CityModel {
	private String name;
	private List<GradeModel> gradeList;

	public CityModel() {
		super();
	}

	public CityModel(String name, List<GradeModel> gradeList) {
		super();
		this.name = name;
		this.gradeList = gradeList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GradeModel> getGradeList() {
		return gradeList;
	}

	public void setGradeList(List<GradeModel> gradeList) {
		this.gradeList = gradeList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", gradeList=" + gradeList + "]";
	}
}
