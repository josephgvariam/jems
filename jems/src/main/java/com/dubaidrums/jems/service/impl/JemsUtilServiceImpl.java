package com.dubaidrums.jems.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.service.JemsUtilService;

@Service
@Transactional
public class JemsUtilServiceImpl implements JemsUtilService {

	Logger log = LogManager.getLogger(JemsUtilServiceImpl.class);

	public Date getCurrentDate() {
		DateTime dt = new DateTime();
		DateTime dtUae = dt.withZone(DateTimeZone.forID("Asia/Dubai"));

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, dtUae.getMonthOfYear() - 1);
		cal.set(Calendar.DATE, dtUae.getDayOfMonth());
		cal.set(Calendar.YEAR, dtUae.getYear());
		cal.set(Calendar.HOUR_OF_DAY, dtUae.getHourOfDay());
		cal.set(Calendar.MINUTE, dtUae.getMinuteOfHour());
		cal.set(Calendar.SECOND, dtUae.getSecondOfMinute());
		cal.set(Calendar.MILLISECOND, dtUae.getMillisOfSecond());

		Date d = cal.getTime();

		// log.info("method: getCurrentDate, t1: "+dt+", t2:"+d+", dtUae: "+dtUae);

		return d;
	}

}
