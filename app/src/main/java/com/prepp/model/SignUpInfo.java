package com.prepp.model;


import com.google.gson.annotations.SerializedName;

public class SignUpInfo extends BaseModel {

	@SerializedName("data")
	private SignUpData signUpData;

	public SignUpData getSignUpData() {
		return signUpData;
	}


	public class SignUpData{

		@SerializedName("isValidUser")
		private boolean valid;

		private String email;

		private String accessToken;

		private int id;

		private boolean isActive;

		private boolean mobileNumberVerified;

		public String getEmail() {
			return email;
		}

		public String getAccessToken() {
			return accessToken;
		}

		public boolean isActive() {
			return isActive;
		}

		public int getId() {
			return id;
		}

		public boolean isMobileNumberVerified() {
			return mobileNumberVerified;
		}

		public boolean isValid() {
			return valid;
		}
	}
}


