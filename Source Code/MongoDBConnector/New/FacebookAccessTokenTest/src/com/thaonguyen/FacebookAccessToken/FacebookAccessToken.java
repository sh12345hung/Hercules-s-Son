package com.thaonguyen.FacebookAccessToken;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;

public class FacebookAccessToken {

	public static void main(String[] args) {
		String accessToken = "AACEdose0cBAE7OaZBFhLtqsP6uCGiZAc0QN06nyt70ZBcI7OEoZB7gYB6RwYrjBSBRM6mdNK4bohtLtKhJOviqWvF94dqYTXICYn2QSpvNmCfoGkcy3sbEgSYEwG1MWAvE8xDX4zGvk0U3u09H0EOgGLWiQdz3DbB51ESYGfXVZAsARylAZCYfk0rtWo8esZD";
		
		FacebookClient client = new DefaultFacebookClient(accessToken, Version.LATEST);
		User me = client.fetchObject("me", User.class, Parameter.with("fields", "id,name,email,birthday"));
		
		System.out.println(me.getId());
		System.out.println(me.getName());
		System.out.println(me.getEmail());
		System.out.println(me.getBirthday());
	}

}
