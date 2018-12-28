package com.wedevol.xmpp.service.impl;

import com.wedevol.xmpp.bean.CcsInMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.service.PayloadProcessor;

import tracking.Accelerator;
import tracking.GPS;

public class AcceleratorProcessor implements PayloadProcessor{

	@Override
	public void handleMessage(CcsInMessage msg) {
		
		System.out.println("Accelerator Processor triggered!");
		String velX = msg.getDataPayload().get("velX"); 
		String velY = msg.getDataPayload().get("velY"); 
		String velZ = msg.getDataPayload().get("velZ"); 
		String timestamp = msg.getDataPayload().get("timestamp"); 
		
		
		if(velX != null && velY != null && velZ != null &&  timestamp != null) {
			System.out.println("Accelerator Data received: \nTimestamp= "+timestamp+
					" X= "+velX+" y= "+velY+" z = "+velZ);
			CcsClient.person.addAccelerator(new Accelerator(Long.valueOf(timestamp), 
					Double.valueOf(velX), Double.valueOf(velY), Double.valueOf(velZ)));
		}else {
			System.out.println("Something went wrong...");
		}
		
	}
}
