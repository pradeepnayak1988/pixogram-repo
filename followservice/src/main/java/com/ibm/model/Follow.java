package com.ibm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Follow {

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getMyid() {
		return myid;
	}



	public void setMyid(int myid) {
		this.myid = myid;
	}



	public int getUserid() {
		return userid;
	}



	public void setUserid(int userid) {
		this.userid = userid;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int myid;
	private int userid;
	
    public Follow() {}

	public Follow(int myid, int userid) {
		super();
		this.myid = myid;
		this.userid = userid;
	}
	
	
	
}
