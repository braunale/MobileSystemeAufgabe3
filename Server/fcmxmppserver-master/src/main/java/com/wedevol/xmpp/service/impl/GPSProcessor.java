package com.wedevol.xmpp.service.impl;

import com.wedevol.xmpp.bean.CcsInMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.service.PayloadProcessor;

import tracking.GPS;

public class GPSProcessor implements PayloadProcessor{

	@Override
	public void handleMessage(CcsInMessage msg) {
		
		System.out.println("GPS Processor triggered!");
		String latitude = msg.getDataPayload().get("latitude"); 
		String longitude = msg.getDataPayload().get("longitude"); 
		String timestamp = msg.getDataPayload().get("timestamp"); 
		
		if(latitude != null && longitude != null && timestamp != null) {
			System.out.println("GPS Data received: \nTimestamp= "+timestamp+
					"Latitude= "+latitude+" longitude= "+longitude);
			CcsClient.person.addGPS(new GPS(Long.valueOf(timestamp), Double.valueOf(latitude), Double.valueOf(longitude)));
		}else {
			System.out.println("Something went wrong...");
		}		
	}
}
