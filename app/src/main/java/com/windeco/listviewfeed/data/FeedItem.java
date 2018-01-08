package com.windeco.listviewfeed.data;

public class FeedItem {
	private String category;
	private String name, company, subject, profilePic, likeCount, image;

	public FeedItem() {
	}

	public FeedItem(String category, String name, String company, String subject, String profilePic, String likeCount, String image) {
		super();
		this.category = category;
		this.name = name;
		this.company = company;
		this.subject = subject;
		this.profilePic = profilePic;
		this.likeCount = likeCount;
		this.image = image;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {

		this.image = image;
	}
}
