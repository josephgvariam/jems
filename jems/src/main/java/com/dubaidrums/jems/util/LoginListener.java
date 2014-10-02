package com.dubaidrums.jems.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.util.ClassUtils;

public class LoginListener implements
		ApplicationListener<AbstractAuthenticationEvent> {

	Logger log = LogManager.getLogger(LoginListener.class);

	public void onApplicationEvent(AbstractAuthenticationEvent event) {
		if (event instanceof InteractiveAuthenticationSuccessEvent) {
			return;
		}

		final StringBuilder builder = new StringBuilder();
		builder.append("Authentication event ");
		builder.append(ClassUtils.getShortName(event.getClass()));
		builder.append(": ");
		builder.append(event.getAuthentication().getName());
		builder.append("; details: ");
		builder.append(event.getAuthentication().getDetails());

		if (event instanceof AbstractAuthenticationFailureEvent) {
			builder.append("; exception: ");
			builder.append(((AbstractAuthenticationFailureEvent) event)
					.getException().getMessage());
		}

		log.info(builder.toString());
	}

}
