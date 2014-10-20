package ch.unibas.medizin.osce.shared;


public  class SPData {

	Long id;
	
	String name;
	
	String firstName;
	
	String email;
	
	String street;
	
	String city;
	
	String telephone;
	
	String height;
	
	String weight;
	
	Boolean isSentEditReuest;
	
	Boolean isDataChanged;

	String imagePath;
	
	String videoPath;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Boolean getIsSentEditReuest() {
		return isSentEditReuest;
	}

	public void setIsSentEditReuest(Boolean isSentEditReuest) {
		this.isSentEditReuest = isSentEditReuest;
	}

	public Boolean getIsDataChanged() {
		return isDataChanged;
	}

	public void setIsDataChanged(Boolean isDataChanged) {
		this.isDataChanged = isDataChanged;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String vedioPath) {
		this.videoPath = vedioPath;
	}
	
}
