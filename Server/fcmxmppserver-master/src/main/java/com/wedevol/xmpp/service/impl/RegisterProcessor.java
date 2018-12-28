package com.wedevol.xmpp.service.impl;

import com.wedevol.xmpp.bean.CcsInMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.service.PayloadProcessor;

/**
 * Handles a user registration request
 */
public class RegisterProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(CcsInMessage msg) {
		// TODO: handle the user registration. Keep in mind that a user name can
		// have more reg IDs associated. The messages IDs should be unique. 
		
		System.out.println("Register Processor triggered!");
		String firstname = msg.getDataPayload().get("firstname"); 
		String lastname = msg.getDataPayload().get("lastname"); 
		String mail = msg.getDataPayload().get("mail"); 
		String googleID = msg.getDataPayload().get("googleID"); 
		String token = msg.getFrom(); 
		
		if(firstname != null && lastname !=null && mail != null && googleID !=null &&token != null) {
			System.out.println("Recevied Account data: ");
			System.out.println("Name "+firstname+ " " +lastname);
			System.out.println("Mail: "+mail);
			System.out.println("GoogleID: "+googleID);
			System.out.println("Token: "+token);
			
			CcsClient.person.setFirstname(firstname);
			CcsClient.person.setLastname(lastname);
			CcsClient.person.setMail(mail);
			CcsClient.person.setGoogleID(googleID);
			CcsClient.person.setToken(token);
		}else {
			System.out.println("Something went wrong...");
		}
		
		
	}

}