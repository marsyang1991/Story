package com.yang.MYModel;

public class User {
	int uid;
	String username;//用户名，默认手机号
	String password;//密码
	String qq;//qq
	String nickname;//真实姓名
	String email;//邮件
	String gender;//性别
	String date;//出生日期
	Textbook book;//设置教材
	String portait;//头像
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Textbook getBook() {
		return book;
	}
	public void setBook(Textbook book) {
		this.book = book;
	}
	public String getPortait() {
		return portait;
	}
	public void setPortait(String portait) {
		this.portait = portait;
	}
	public User()
	{}
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public boolean equals(Object o) {
		User user = (User) o;
		if(user.username.equals(username)&&user.password.equals(password))
			return true;
		return false;
	}
	public boolean isSameUser(User user)
	{
		if(user.username.equals(username))
			return true;
		return false;
	}
	
}
