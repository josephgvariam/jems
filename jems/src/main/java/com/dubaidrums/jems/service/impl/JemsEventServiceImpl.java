package com.dubaidrums.jems.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsInvoice;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.domain.JemsQuotation;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.repository.JemsEventRepository;
import com.dubaidrums.jems.service.JemsClientService;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsInvoiceService;
import com.dubaidrums.jems.service.JemsOrganizationService;
import com.dubaidrums.jems.service.JemsQuotationService;
import com.dubaidrums.jems.service.JemsSearchService;
import com.dubaidrums.jems.service.JemsUtilService;
import com.dubaidrums.jems.util.dashboard.OrgData;
import com.dubaidrums.jems.util.dashboard.QIData;
import com.google.ical.compat.jodatime.LocalDateIteratorFactory;
import com.google.ical.values.DateValue;
import com.google.ical.values.DateValueImpl;
import com.google.ical.values.Frequency;
import com.google.ical.values.RRule;
import com.google.ical.values.Weekday;
import com.google.ical.values.WeekdayNum;

import flexjson.JSONDeserializer;

@Service
@Transactional
public class JemsEventServiceImpl implements JemsEventService {

	Logger log = LogManager.getLogger(JemsEventServiceImpl.class);

	@Autowired
	JemsEventRepository jemsEventRepository;

	@Autowired
	JemsQuotationService jemsQuotationService;

	@Autowired
	JemsInvoiceService jemsInvoiceService;

	@Autowired
	JemsSearchService jemsSearchService;

	@Autowired
	JemsClientService jemsClientService;

	@Autowired
	JemsUtilService jemsUtilService;

	@Autowired
	JemsOrganizationService jemsOrganizationService;

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public long countAllJemsEvents() {
		return jemsEventRepository.count();
	}

	public void deleteJemsEvent(JemsEvent jemsEvent) {
		jemsSearchService.deleteJemsEventDocument(jemsEvent);
		// jemsClientService.eventDeleted(jemsEvent);

		jemsEvent.setActive(false);
		jemsEventRepository.save(jemsEvent);

		// jemsEventRepository.delete(jemsEvent);
	}

	public JemsEvent findJemsEvent(Long id) {
		return jemsEventRepository.findOne(id);
	}

	public List<JemsEvent> findAllJemsEvents() {
		// return jemsEventRepository.findAll();
		return jemsEventRepository.findByActiveTrue();
	}

	// public List<JemsEvent> findJemsEventEntries(int firstResult, int
	// maxResults) {
	// return jemsEventRepository.findAll(new
	// org.springframework.data.domain.PageRequest(firstResult / maxResults,
	// maxResults)).getContent();
	// }

	public Page<JemsEvent> findJemsEventEntries(JemsUser user, int firstResult,
			int maxResults) {
		return jemsEventRepository.findByOrganizationInAndActiveTrue(user
				.getOrganizations(), new PageRequest(firstResult / maxResults,
				maxResults, new Sort(Sort.Direction.DESC, "startDateTime")));
	}

	public void saveJemsEvent(JemsEvent jemsEvent) {
		jemsEventRepository.save(jemsEvent);
		jemsSearchService.createJemsEventDocument(jemsEvent);
		jemsClientService.eventSavedOrUpdated(jemsEvent);
		// jemsMessageService.sendMessage("event saved: "+jemsEvent.getId());
	}

	public JemsEvent updateJemsEvent(JemsEvent jemsEvent) {
		JemsEvent je = jemsEventRepository.save(jemsEvent);
		jemsSearchService.updateJemsEventDocument(jemsEvent);
		jemsClientService.eventSavedOrUpdated(jemsEvent);
		// jemsMessageService.sendMessage("event updated: "+jemsEvent.getId());
		return je;

	}

	@Override
	public List<JemsEvent> getCalendarEventsList(String selectedDate,
			JemsUser user) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = format.parse(selectedDate);
		} catch (ParseException e) {
			d = jemsUtilService.getCurrentDate();
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d);
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		cal1.set(Calendar.DAY_OF_MONTH, 1);
		while (cal1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			cal1.add(Calendar.DAY_OF_MONTH, -1);
		}
		// extend range
		cal1.add(Calendar.DAY_OF_MONTH, -1);
		cal1.add(Calendar.DAY_OF_MONTH, -1);
		Date start = cal1.getTime();

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d);
		cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);

		cal2.add(Calendar.MONTH, 1);
		cal2.set(Calendar.DAY_OF_MONTH, 1);
		cal2.add(Calendar.DATE, -1);

		while (cal2.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
			cal2.add(Calendar.DAY_OF_MONTH, 1);
		}
		// Extend Range
		cal2.add(Calendar.DAY_OF_MONTH, 1);
		cal2.add(Calendar.DAY_OF_MONTH, 1);
		Date end = cal2.getTime();

		log.info("Getting events between start: " + format.format(start)
				+ ", end: " + format.format(end));

		return jemsEventRepository
				.findByStartDateTimeBetweenAndOrganizationInAndActiveTrue(
						start, end, user.getOrganizations());
	}

	public String getEventDetails(JemsEvent je) {
		StringBuilder sb = new StringBuilder();
		sb.append("Start Date: " + dateFormat.format(je.getStartDateTime()))
				.append("\n");
		sb.append("End Date: " + dateFormat.format(je.getEndDateTime()))
				.append("\n");

		int numAttendees = 0;
		if (je.getNumberPeople() == null || je.getNumberPeople() == 0) {
			numAttendees = je.getNumberDrums();
		} else {
			numAttendees = je.getNumberPeople();
		}

		sb.append("Number Of Attendees: " + numAttendees).append("\n");
		sb.append("Event Type: " + je.getType().toString().replaceAll("_", " "));
		return sb.toString();
	}

	public String getBillTo(JemsEvent je) {
		StringBuilder sb = new StringBuilder();

		if (je.getUseInDocs() == 0) {
			// client
			sb.append(je.getClientContactPerson()).append("\n");
			sb.append(je.getClientCompany()).append("\n");
			sb.append(je.getClientAddress()).append("\n");
			sb.append(je.getClientPhone()).append("\n");
			sb.append(je.getClientEmail()).append("\n");
		} else {
			// hiring agent
			sb.append(je.getHiringAgentContactPerson()).append("\n");
			sb.append(je.getHiringAgentCompany()).append("\n");
			sb.append(je.getHiringAgentAddress()).append("\n");
			sb.append(je.getHiringAgentPhone()).append("\n");
			sb.append(je.getHiringAgentEmail()).append("\n");

		}
		return sb.toString();
	}

	@Override
	public List<JemsEvent> findByStartDateTimeBetweenAndOrganizationIn(
			Date start, Date end, Set<JemsOrganization> orgs) {
		return jemsEventRepository
				.findByStartDateTimeBetweenAndOrganizationInAndActiveTrue(
						start, end, orgs);
	}

	public List<JemsEvent> findByStartDateTimeBetweenAndOrganizationId(
			Date start, Date end, Long orgId) {
		return jemsEventRepository
				.findByStartDateTimeBetweenAndOrganizationIdAndActiveTrue(
						start, end, orgId);
	}

	@Override
	public void generateRepeatEvents(String params, JemsEvent jemsEvent) {
		try {
			log.info("Repeat Events params: " + params);
			Map<String, String> map = new JSONDeserializer<Map<String, String>>()
					.deserialize(params);

			RRule rrule = new RRule();

			rrule.setInterval(Integer.parseInt(map.get("interval")));

			if (map.containsKey("count")) {
				rrule.setCount(Integer.parseInt(map.get("count")));
			} else if (map.containsKey("untilDate")) {
				rrule.setUntil(getDateValue(map.get("untilDate")));
			}

			int frequency = Integer.parseInt(map.get("frequency"));
			switch (frequency) {
			case 0:
				rrule.setFreq(Frequency.DAILY);
				break;
			case 1:
				rrule.setFreq(Frequency.WEEKLY);

				if (map.containsKey("byDay")) {
					String str = map.get("byDay");
					String[] str_ = str.split("#");

					List<WeekdayNum> byDay = new ArrayList();
					for (String s : str_) {
						WeekdayNum wn = new WeekdayNum(0, Weekday.valueOf(s));
						byDay.add(wn);
					}

					rrule.setByDay(byDay);

				}

				break;
			case 2:
				rrule.setFreq(Frequency.MONTHLY);

				if (map.containsKey("byMonth")) {
					int[] byMonth = new int[] { Integer.parseInt(map
							.get("byMonth")) };
					rrule.setByMonthDay(byMonth);
				} else if (map.containsKey("byDay")) {
					List<WeekdayNum> byDay = new ArrayList();

					String str = map.get("byDay"); // 2SA, -1FR
					int num = Integer.parseInt(str.replaceAll("[A-Z]+", ""));

					str = str.replaceAll("\\d+", "");
					str = str.replaceAll("-", "");

					WeekdayNum wn = new WeekdayNum(num, Weekday.valueOf(str));
					byDay.add(wn);

					rrule.setByDay(byDay);
				}

				break;
			case 3:
				rrule.setFreq(Frequency.YEARLY);
				break;
			}

			LocalDate start = getLocalDate(map.get("startDate"));
			Date startTime = jemsEvent.getStartDateTime();
			Date endTime = jemsEvent.getEndDateTime();
			Calendar cal = Calendar.getInstance();
			for (LocalDate date : LocalDateIteratorFactory
					.createLocalDateIterable(rrule.toIcal(), start, true)) {
				JemsEvent je = buildCopy(jemsEvent);
				log.info("Generating Repeat event for date = " + date);

				cal.setTime(startTime);
				cal.set(date.getYear(), date.getMonthOfYear() - 1,
						date.getDayOfMonth());
				je.setStartDateTime(cal.getTime());

				cal.setTime(endTime);
				cal.set(date.getYear(), date.getMonthOfYear() - 1,
						date.getDayOfMonth());
				je.setEndDateTime(cal.getTime());

				je.setActive(true);
				saveJemsEvent(je);
			}
		} catch (Exception e) {
			log.error("Unable to generate Recurring event!", e);
		}

	}

	public JemsEvent buildCopy(JemsEvent j) {
		JemsEvent x = new JemsEvent();

		x.setActive(j.getActive());
		x.setAmountPayable(j.getAmountPayable());
		x.setAttachments(j.getAttachments());
		x.setChairsRequired(j.getChairsRequired());
		x.setClientAddress(j.getClientAddress());
		x.setClientCompany(j.getClientCompany());
		x.setClientContactPerson(j.getClientContactPerson());
		x.setClientEmail(j.getClientEmail());
		x.setClientPhone(j.getClientPhone());
		x.setCountry(j.getCountry());
		x.setCurrency(j.getCurrency());
		x.setDescription(j.getDescription());
		x.setEndDateTime(j.getEndDateTime());
		// x.setFileList(fileList);
		x.setHiringAgentAddress(j.getHiringAgentAddress());
		x.setHiringAgentCompany(j.getHiringAgentCompany());
		x.setHiringAgentContactPerson(j.getHiringAgentContactPerson());
		x.setHiringAgentEmail(j.getHiringAgentEmail());
		x.setHiringAgentPhone(j.getHiringAgentPhone());
		x.setId(null);
		// x.setJemsCostingItems(jemsCostingItems);
		// x.setJemsInvoice(jemsInvoice);
		// x.setJemsQuotation(jemsQuotation);
		x.setLocation(j.getLocation());
		x.setLocationLatLong(j.getLocationLatLong());
		x.setModifiedDateTime(new Date());
		x.setModifiedUser(j.getModifiedUser());
		x.setNotes(j.getNotes());
		x.setNotes_(j.getNotes_());
		x.setNumberDrummers(j.getNumberDrummers());
		x.setNumberDrums(j.getNumberDrums());
		x.setNumberPeople(j.getNumberPeople());
		x.setNumberSessions(j.getNumberSessions());
		x.setOrganization(j.getOrganization());
		x.setPaid(j.getPaid());
		x.setPaidAmount(j.getPaidAmount());
		x.setPaidDate(j.getPaidDate());
		x.setPaymentMethod(j.getPaymentMethod());
		x.setPaymentNotes(j.getPaymentNotes());
		x.setReceiptVoucherNumber(j.getReceiptVoucherNumber());
		x.setRegion(j.getRegion());
		x.setSessionTime(j.getSessionTime());
		x.setStaffAssigned(j.getStaffAssigned());
		x.setStartDateTime(j.getStartDateTime());
		x.setStatus(j.getStatus());
		x.setTitle(j.getTitle());
		x.setType(j.getType());
		x.setUseInDocs(j.getUseInDocs());
		// x.setVersion(version);

		return x;
	}

	private DateValue getDateValue(String s) throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat fmt1 = new SimpleDateFormat("dd/MM/yyyy");
		cal.setTime(fmt1.parse(s));

		return new DateValueImpl(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	}

	private LocalDate getLocalDate(String s) throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat fmt1 = new SimpleDateFormat("dd/MM/yyyy");
		cal.setTime(fmt1.parse(s));

		return new LocalDate(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	}

	// public Map<String, List> getReportData(String startDate, String endDate,
	// String type, Set<JemsOrganization> orgs) {
	public Map<String, List> getReportData(String startDate, String endDate,
			String type, Long org, JemsUser user) {		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy")
				.withZone(DateTimeZone.forID("Asia/Dubai"));
		DateTime start = formatter.parseDateTime(startDate).toDateMidnight()
				.toInterval().getStart();
		DateTime end = formatter.parseDateTime(endDate).toDateMidnight()
				.toInterval().getEnd();

		List<JemsEvent> events;
		Map<String, OrgData> orgs = new HashMap<String, OrgData>();

		if (org == -1) {
			events = findByStartDateTimeBetweenAndOrganizationIn(
					start.toDate(), end.toDate(), user.getOrganizations());
			for (JemsOrganization jo : user.getOrganizations()) {
				OrgData od = new OrgData();
				od.setOrgName(jo.getName());
				od.setInvoiceAmount(BigDecimal.ZERO);
				od.setNumInvoices(0);
				od.setNumQuotations(0);
				od.setPaidAmount(BigDecimal.ZERO);
				od.setQuotationAmount(BigDecimal.ZERO);

				orgs.put(od.getOrgName(), od);
			}
		} else {
			events = findByStartDateTimeBetweenAndOrganizationId(
					start.toDate(), end.toDate(), org);
			OrgData od = new OrgData();
			od.setOrgName(jemsOrganizationService.findJemsOrganization(org)
					.getName());
			od.setInvoiceAmount(BigDecimal.ZERO);
			od.setNumInvoices(0);
			od.setNumQuotations(0);
			od.setPaidAmount(BigDecimal.ZERO);
			od.setQuotationAmount(BigDecimal.ZERO);

			orgs.put(od.getOrgName(), od);
		}

		List<JemsQuotation> quotations = new ArrayList<JemsQuotation>();
		List<JemsInvoice> invoices = new ArrayList<JemsInvoice>();

		// PIE CHART
		Map<String, Integer> eventTypeMap = new HashMap<String, Integer>();

		for (JemsEvent e : events) {
			if (e.getActive()) {
				for (JemsQuotation jq : e.getJemsQuotations()) {
					if (jq != null && jq.isActive()) {
						quotations.add(jq);
					}
				}

				for (JemsInvoice ji : e.getJemsInvoices()) {
					if (ji != null && ji.isActive()) {
						invoices.add(ji);
					}
				}

				String eventType = e.getType().toString();
				Integer i = eventTypeMap.get(eventType);
				if (i == null) {
					eventTypeMap.put(eventType, 1);
				} else {
					eventTypeMap.put(eventType, ++i);
				}
			}
		}

		log.info("method: getReportData, startDate: " + startDate
				+ ", endDate: " + endDate + ", type: " + type
				+ ", msg: quotations-" + quotations.size() + " invoices:"
				+ invoices.size());

		List<QIData> quotationDataList = new ArrayList<QIData>();
		List<QIData> invoiceDataList = new ArrayList<QIData>();
		List<String> ticks = new ArrayList<String>();
		LocalDate lstart = start.toLocalDate();
		LocalDate lend = end.toLocalDate();
		LocalDate current = lstart;

		while (current.compareTo(lend) < 0) {
			ticks.add(current.toString());

			QIData qd = new QIData(current, BigDecimal.ZERO);
			QIData id = new QIData(current, BigDecimal.ZERO);

			quotationDataList.add(qd);
			invoiceDataList.add(id);

			if (type.equals("Day")) {
				current = current.plusDays(1);
			} else if (type.equals("Week")) {
				current = current.plusWeeks(1);
			} else if (type.equals("Month")) {
				current = current.plusMonths(1);
			} else {
				current = current.plusYears(1);
			}
		}

		for (JemsQuotation q : quotations) {
			// LocalDate qDate = new LocalDate(q.getQDate());
			LocalDate qDate = new LocalDate(q.getJemsEvent().getStartDateTime());

			for (QIData qd : quotationDataList) {
				if (type.equals("Day")) {
					if (qd.getDate().equals(qDate)) {
						qd.add(q.getTotalAmount());
						break;
					}
				} else if (type.equals("Week")) {
					if (qd.getDate().getWeekOfWeekyear() == qDate.getWeekOfWeekyear() && qd.getDate().getYear() == qDate.getYear()) {
						qd.add(q.getTotalAmount());
						break;
					}
				} else if (type.equals("Month")) {
					if (qd.getDate().getMonthOfYear() == qDate.getMonthOfYear() && qd.getDate().getYear() == qDate.getYear()) {
						// log.info("Quotation: "+q.getQNumber()+" qDate: "+qDate+" amt: "+q.getTotalAmount()+" bucket: "+qd.getDate()+" total: "+qd.getTotal());
						qd.add(q.getTotalAmount());
						break;
					}
				} else {
					if (qd.getDate().getYear() == qDate.getYear()) {
						qd.add(q.getTotalAmount());
						break;
					}
				}
			}

			OrgData od = orgs.get(q.getJemsEvent().getOrganization().getName());
			if (od != null) {
				od.setNumQuotations(od.getNumQuotations() + 1);
				od.setQuotationAmount(od.getQuotationAmount().add(q.getTotalAmount()));
				if (q.getJemsEvent() != null && q.getJemsEvent().getPaidAmount() != null) {
					od.setPaidAmount(od.getPaidAmount().add(q.getJemsEvent().getPaidAmount()));
				}
			}
		}

		for (JemsInvoice q : invoices) {
			// LocalDate qDate = new LocalDate(q.getIDate());
			LocalDate qDate = new LocalDate(q.getJemsEvent().getStartDateTime());

			for (QIData qd : invoiceDataList) {
				if (type.equals("Day")) {
					if (qd.getDate().equals(qDate)) {
						qd.add(q.getTotalAmount());
						break;
					}
				} else if (type.equals("Week")) {
					if (qd.getDate().getWeekOfWeekyear() == qDate
							.getWeekOfWeekyear()) {
						qd.add(q.getTotalAmount());
						break;
					}
				} else if (type.equals("Month")) {
					if (qd.getDate().getMonthOfYear() == qDate.getMonthOfYear()) {
						qd.add(q.getTotalAmount());
						break;
					}
				} else {
					if (qd.getDate().getYear() == qDate.getYear()) {
						qd.add(q.getTotalAmount());
						break;
					}
				}
			}

			OrgData od = orgs.get(q.getJemsEvent().getOrganization().getName());
			if (od != null) {
				od.setNumInvoices(od.getNumInvoices() + 1);
				od.setInvoiceAmount(od.getInvoiceAmount().add(q.getTotalAmount()));
			}
		}

		Map<String, List> data = new HashMap<String, List>();
		List<BigDecimal> quotationData = new ArrayList<BigDecimal>();
		List<BigDecimal> invoiceData = new ArrayList<BigDecimal>();

		for (QIData qd : quotationDataList) {
			quotationData.add(qd.getTotal());
		}
		for (QIData qd : invoiceDataList) {
			invoiceData.add(qd.getTotal());
		}

		data.put("quotation", quotationData);
		data.put("invoice", invoiceData);
		data.put("ticks", ticks);

		// OIE CHARAT
		List<List> eventTypeData = new ArrayList<List>();

		for (Entry<String, Integer> entry : eventTypeMap.entrySet()) {
			List tmp = new ArrayList();
			tmp.add(entry.getKey());
			tmp.add(entry.getValue());

			eventTypeData.add(tmp);
		}
		data.put("eventTypes", eventTypeData);

		data.put("orgdata", new ArrayList<OrgData>(orgs.values()));

		return data;
	}

}
