package com.wedevol.xmpp.service.impl;

import com.wedevol.xmpp.bean.CcsInMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.service.PayloadProcessor;

import tracking.GPS;
import tracking.Light;

public class LightProcessor implements PayloadProcessor{

	@Override
	public void handleMessage(CcsInMessage msg) {
		
		System.out.println("Light Processor triggered!");
		String light = msg.getDataPayload().get("light"); 
		String timestamp = msg.getDataPayload().get("timestamp"); 
		
		if(light != null && timestamp != null) {
			System.out.println("Brightness Data received: \nTimestamp= "+timestamp+
					" Brightness= "+light);
			CcsClient.person.addLight(new Light(Long.valueOf(timestamp), Double.valueOf(light)));
		}else {
			System.out.println("Something went wrong...");
		}
		
	}
}
