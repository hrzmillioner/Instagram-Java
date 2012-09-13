package com.instagram;

import com.instagram.util.UriConstructor;
import com.instagram.auth.AccessToken;
import com.instagram.exception.InstagramException;
import com.instagram.io.PostMethod;
import com.instagram.io.UriFactory;
import com.instagram.model.User;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class Instagram {
	InstagramSession session;
	String redirectUri;
	String clientId;
	String clientSecret;
	AccessToken accessToken;
	User sessionUser;

	public InstagramSession session() {
		return session;
	}

	protected void setSession(InstagramSession session) {
		this.session = session;
	}

	protected String getRedirectUri() {
		return redirectUri;
	}

	public Instagram setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
		return this;
	}

	protected String getClientId() {
		return clientId;
	}

	public Instagram setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public String getAuthorizationUri() throws InstagramException {
		if (getClientId() == null || getRedirectUri() == null) {
			throw new InstagramException("Please make sure that the "
					+ "clientId and redirectUri fields are set");
		}
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("client_id", getClientId());
		args.put("redirect_uri", getRedirectUri());
		args.put("response_type", "code");
		args.put("scope", "likes+comments+relationships+basic");
		return (new UriConstructor()).constructUri(
				UriFactory.Auth.USER_AUTHORIZATION, args, false);
	}

	public Instagram build(String code) throws InstagramException {
		if (getClientSecret() == null || getClientId() == null
				|| getRedirectUri() == null) {
			throw new InstagramException("Please make sure that the"
					+ "clientId, clientSecret and redirectUri fields are set");
		}
		HashMap<String, Object> postArgs = new HashMap<String, Object>();
		postArgs.put("client_id", getClientId());
		postArgs.put("client_secret", getClientSecret());
		postArgs.put("grant_type", "authorization_code");
		postArgs.put("redirect_uri", getRedirectUri());
		postArgs.put("code", code);

		/*
		 * JSONObject response = (new PostMethod() .setPostParameters(postArgs)
		 * .setMethodURI(UriFactory.Auth.GET_ACCESS_TOKEN) ).call();
		 */
		try {
			// setAccessToken(new
			// AccessToken(response.getString("access_token")));
			setAccessToken(new AccessToken(
					"143577682.2b2ca2d.331f50cbb6b44e359668c93546c5f7a3"));
			// setSessionUser(new User(response.getJSONObject("user"),
			// getAccessToken().getTokenString()));
			// setSession(new InstagramSession(getAccessToken(),
			// getSessionUser()));
			// setSession(new InstagramSession(getAccessToken(), null));
		} catch (Exception e) {
			throw new InstagramException("JSON parsing error");
		}
		return this;
	}

	public AccessToken getAccessToken() throws InstagramException {
		if (accessToken == null)
			throw new InstagramException(
					"Token has not been fetched, please call build(String code) "
							+ "before calling getAccessToken()");
		else
			return accessToken;
	}

	protected void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public User getSessionUser() {
		return sessionUser;
	}

	protected void setSessionUser(User sessionUser) {
		this.sessionUser = sessionUser;
	}

	protected String getClientSecret() {
		return clientSecret;
	}

	public Instagram setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
		return this;
	}

}
