package com.ibm.mediaservice.dto;

import org.springframework.web.multipart.MultipartFile;

public class MediaDTO {
	  private String userId ;
	  private String title;
	  private String desc;
	  private String [] tags;
	  private String [] effect;
	  private MultipartFile file;
	  
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public String[] getEffect() {
		return effect;
	}
	public void setEffect(String[] effect) {
		this.effect = effect;
	}
	
}
