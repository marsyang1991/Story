package com.yang.MYModel;

import java.util.List;

public class GradeModel {
	private String name;
	private List<VersionModel> versionList;
	private CityModel parent;

	public GradeModel() {
		super();
	}

	public GradeModel(String name, List<VersionModel> versionList,
			CityModel parent) {
		super();
		this.name = name;
		this.versionList = versionList;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public CityModel getParent() {
		return parent;
	}

	public void setParent(CityModel parent) {
		this.parent = parent;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<VersionModel> getVersionList() {
		return versionList;
	}

	public void setversionList(List<VersionModel> versionList) {
		this.versionList = versionList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", versionList=" + versionList
				+ "parent=" + parent + "]";
	}
}
