package com.dubaidrums.jems.service.impl;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.service.JemsEmailService;
import com.dubaidrums.jems.service.JemsUtilService;

@Service
@Transactional
public class JemsEmailServiceImpl implements JemsEmailService {

	Logger log = LogManager.getLogger(JemsEmailServiceImpl.class);

	@Autowired
	private transient JavaMailSender mailTemplate;

	@Autowired
	JemsUtilService jemsUtilService;

	@Async
	public void sendEmail(String subject, String body, String attachment) {	
		try {			
			String mailFrom = "jems3@dubaidrums.com";
			String mailTo = "jems3@dubaidrums.com";
			
			//body = body.replaceAll("href='/jems", "href='http://jems.dubaidrums.cloudbees.net/jems");
			body = body.replaceAll("href='/jems", "href='http://jems.dubaidrums.com/jems");
			
			MimeMessage message = mailTemplate.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailFrom);
			helper.setTo(mailTo);		
			helper.setSubject(subject);
			//helper.setText("<html>\n<body>\n<p>\nDear "+name+"<br/>\nThank you for purchasing your tickets to our Desert Drumming event at the Gulf Ventures camp in Al Awir.<br/>\nPlease find attached your payment voucher and display it at the event gates.<br/>\nYour voucher number is : <strong>"+txnId+"</strong></br>\nDetails of the location with map and coordinates are available for your GPS receiver.<br/>\n<br/>\nFor details please go to our website at:<br/>\n<a href='http://www.dubaidrums.com/full-moon-desert-drumming-event'>http://www.dubaidrums.com/full-moon-desert-drumming-event</a><br/>\n<br/>\nNearer the time we will send you further information on drivers and contact liaisons accordingly<br/>\n<br/>\nThank You and Regards<br/>\n<br/>\nCapt. Guy Odell<br/>\nCOO Jupiter Eclipse Group<br/>\nDubai Drums<br/>\n+971 50 6139180<br/>\n<body>\n</html>", true);
			helper.setText("<html><body><p>"+body+"</p></body></html>", true);
				
			if(attachment!=null){
				
				FileSystemResource file = new FileSystemResource(new File(System.getProperty("java.io.tmpdir")+"/"+attachment));
				helper.addAttachment(attachment, file);
			}
			mailTemplate.send(message);        
			log.info("Email Sent!");
		} catch (Exception e) {
			log.error("Error while sending email: "+e.getMessage());
		}
		 
	}

}
