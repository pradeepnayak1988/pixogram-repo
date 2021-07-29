package com.ibm.mediaservice.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "media")
public class Media {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mediaid")
	private Long id;

	@Column(name = "userid")
	private String userid;

	@Column(name = "mediaurl")
	private String mediaurl;

	@Column(name = "filename")
	private String filename;

	@Column(name = "description")
	private String description;

	@Column(name = "type")
	private String type;

	// @Embedded
// private Tags tags ;

	@Convert(converter = StringListConverter.class)
	private List<String> tags;

	@Convert(converter = StringListConverter.class)
	private List<String> effects;

	@Lob
	@Column(name = "Imagesize")
	private byte[] image;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "upload_date")
	private Date upload_date;

	@Column(name = "likes")
	String likes;

	@Column(name = "unlike")
	String unlike;

	@Column(name = "shares")
	String shares;

	@Convert(converter = StringListConverter.class)
	private List<String> numberofcomments;

	public Media() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMediaurl() {
		return mediaurl;
	}

	public void setMediaurl(String mediaurl) {
		this.mediaurl = mediaurl;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Date getUpload_date() {
		return upload_date;
	}

	public void setUpload_date(Date upload_date) {
		this.upload_date = upload_date;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public String getUnlike() {
		return unlike;
	}

	public void setUnlike(String unlike) {
		this.unlike = unlike;
	}

	public String getShares() {
		return shares;
	}

	public void setShares(String shares) {
		this.shares = shares;
	}

		public List<String> getEffects() {
		return effects;
	}

	public void setEffects(List<String> effects) {
		this.effects = effects;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getNumberofcomments() {
		return numberofcomments;
	}

	public void setNumberofcomments(List<String> numberofcomments) {
		this.numberofcomments = numberofcomments;
	}

	@Override
	public String toString() {
		return "Media [id=" + id + ", userid=" + userid + ", mediaurl=" + mediaurl + ", filename=" + filename
				+ ", description=" + description + ", type=" + type + ", tags=" + tags + ", effects=" + effects
				+ ", image=" + Arrays.toString(image) + ", upload_date=" + upload_date + ", likes=" + likes
				+ ", unlike=" + unlike + ", shares=" + shares + ", numberofcomments=" + numberofcomments + "]";
	}
	

}
