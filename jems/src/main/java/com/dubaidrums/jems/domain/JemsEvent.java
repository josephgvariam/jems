package com.dubaidrums.jems.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
public class JemsEvent {

	@NotNull
	@Size(min = 3, max = 100)
	private String title;

	@Size(max = 200)
	private String description;

	@NotNull
	@Enumerated
	private JemsEventType type;

	@NotNull
	@Enumerated
	private JemsEventStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDateTime;

	@Size(max = 1000)
	private String location;

	@ManyToOne
	private JemsCountry country;

	@ManyToOne
	private JemsRegion region;

	@Size(max = 100)
	private String locationLatLong;

	@NotNull
	@Min(0L)
	private Integer numberDrums;

	@Min(0L)
	private Integer numberPeople;

	@Min(0L)
	private Integer numberDrummers;

	@Min(0L)
	private Integer numberSessions;

	@Size(max = 100)
	private String sessionTime;

	@Value("false")
	private Boolean chairsRequired;

	@ManyToMany
	private Set<JemsUser> staffAssigned = new HashSet<JemsUser>();

	@ManyToMany
	private Set<JemsStaff> staffAssigned2 = new HashSet<JemsStaff>();

	private Integer useInDocs;

	@Size(max = 200)
	private String hiringAgentCompany;

	@Size(max = 200)
	private String hiringAgentContactPerson;

	@Size(max = 100)
	private String hiringAgentPhone;

	@Size(max = 100)
	private String hiringAgentEmail;

	@Size(max = 500)
	private String hiringAgentAddress;

	@Size(max = 200)
	private String clientCompany;

	@Size(max = 200)
	private String clientContactPerson;

	@Size(max = 100)
	private String clientPhone;

	@Size(max = 100)
	private String clientEmail;

	@Size(max = 500)
	private String clientAddress;

	@Size(max = 2500)
	private String notes;

	@Size(max = 2500)
	private String notes_;

	private Double amountPayable;

	@NotNull
	@Value("false")
	private Boolean paid;

	private Double paidAmount;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MMM-yyyy")
	private Date paidDate;

	@Enumerated
	private JemsPaymentMethod paymentMethod;

	@Size(max = 200)
	private String paymentNotes;

	@Size(max = 100)
	private String receiptVoucherNumber;

	@Size(max = 5)
	private String attachments;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDateTime;

	@ManyToOne
	private JemsOrganization organization;

	@ManyToOne
	private JemsCurrency currency;

	@Value("true")
	private Boolean active;

	@OneToOne
	private JemsQuotation jemsQuotation;

	@OneToOne
	private JemsInvoice jemsInvoice;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@OneToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE }, mappedBy = "jemsEvent")
	@Fetch(FetchMode.SELECT)
    @BatchSize(size=1000)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Set<JemsQuotation> jemsQuotations = new HashSet<JemsQuotation>();

	@OneToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE }, mappedBy = "jemsEvent")
	@Fetch(FetchMode.SELECT)
    @BatchSize(size=1000)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Set<JemsInvoice> jemsInvoices = new HashSet<JemsInvoice>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@ManyToOne
	private JemsUser modifiedUser;

	@ManyToOne
	private JemsUser createdUser;

	public Set<com.dubaidrums.jems.domain.JemsQuotation> getJemsQuotations() {
		return jemsQuotations;
	}

	public void setJemsQuotations(
			Set<com.dubaidrums.jems.domain.JemsQuotation> jemsQuotations) {
		this.jemsQuotations = jemsQuotations;
	}

	public Set<com.dubaidrums.jems.domain.JemsInvoice> getJemsInvoices() {
		return jemsInvoices;
	}

	public void setJemsInvoices(
			Set<com.dubaidrums.jems.domain.JemsInvoice> jemsInvoices) {
		this.jemsInvoices = jemsInvoices;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JemsEventType getType() {
		return this.type;
	}

	public void setType(JemsEventType type) {
		this.type = type;
	}

	public JemsEventStatus getStatus() {
		return this.status;
	}

	public void setStatus(JemsEventStatus status) {
		this.status = status;
	}

	public Date getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return this.endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public JemsCountry getCountry() {
		return this.country;
	}

	public void setCountry(JemsCountry country) {
		this.country = country;
	}

	public JemsRegion getRegion() {
		return this.region;
	}

	public void setRegion(JemsRegion region) {
		this.region = region;
	}

	public String getLocationLatLong() {
		return this.locationLatLong;
	}

	public void setLocationLatLong(String locationLatLong) {
		this.locationLatLong = locationLatLong;
	}

	public Integer getNumberDrums() {
		return this.numberDrums;
	}

	public void setNumberDrums(Integer numberDrums) {
		this.numberDrums = numberDrums;
	}

	public Integer getNumberPeople() {
		return this.numberPeople;
	}

	public void setNumberPeople(Integer numberPeople) {
		this.numberPeople = numberPeople;
	}

	public Integer getNumberDrummers() {
		return this.numberDrummers;
	}

	public void setNumberDrummers(Integer numberDrummers) {
		this.numberDrummers = numberDrummers;
	}

	public Integer getNumberSessions() {
		return this.numberSessions;
	}

	public void setNumberSessions(Integer numberSessions) {
		this.numberSessions = numberSessions;
	}

	public String getSessionTime() {
		return this.sessionTime;
	}

	public void setSessionTime(String sessionTime) {
		this.sessionTime = sessionTime;
	}

	public Boolean getChairsRequired() {
		return this.chairsRequired;
	}

	public void setChairsRequired(Boolean chairsRequired) {
		this.chairsRequired = chairsRequired;
	}

	public Set<com.dubaidrums.jems.domain.JemsUser> getStaffAssigned() {
		return this.staffAssigned;
	}

	public void setStaffAssigned(
			Set<com.dubaidrums.jems.domain.JemsUser> staffAssigned) {
		this.staffAssigned = staffAssigned;
	}

	public Set<com.dubaidrums.jems.domain.JemsStaff> getStaffAssigned2() {
		return this.staffAssigned2;
	}

	public void setStaffAssigned2(
			Set<com.dubaidrums.jems.domain.JemsStaff> staffAssigned2) {
		this.staffAssigned2 = staffAssigned2;
	}

	public Integer getUseInDocs() {
		return this.useInDocs;
	}

	public String getNiceUseInDocs() {
		if (this.useInDocs == null)
			return "Client";
		return this.useInDocs == 0 ? "Client" : "Hiring Agent";
	}

	public void setUseInDocs(Integer useInDocs) {
		this.useInDocs = useInDocs;
	}

	public String getHiringAgentCompany() {
		return this.hiringAgentCompany;
	}

	public void setHiringAgentCompany(String hiringAgentCompany) {
		this.hiringAgentCompany = hiringAgentCompany;
	}

	public String getHiringAgentContactPerson() {
		return this.hiringAgentContactPerson;
	}

	public void setHiringAgentContactPerson(String hiringAgentContactPerson) {
		this.hiringAgentContactPerson = hiringAgentContactPerson;
	}

	public String getHiringAgentPhone() {
		return this.hiringAgentPhone;
	}

	public void setHiringAgentPhone(String hiringAgentPhone) {
		this.hiringAgentPhone = hiringAgentPhone;
	}

	public String getHiringAgentEmail() {
		return this.hiringAgentEmail;
	}

	public void setHiringAgentEmail(String hiringAgentEmail) {
		this.hiringAgentEmail = hiringAgentEmail;
	}

	public String getHiringAgentAddress() {
		return this.hiringAgentAddress;
	}

	public void setHiringAgentAddress(String hiringAgentAddress) {
		this.hiringAgentAddress = hiringAgentAddress;
	}

	public String getClientCompany() {
		return this.clientCompany;
	}

	public void setClientCompany(String clientCompany) {
		this.clientCompany = clientCompany;
	}

	public String getClientContactPerson() {
		return this.clientContactPerson;
	}

	public void setClientContactPerson(String clientContactPerson) {
		this.clientContactPerson = clientContactPerson;
	}

	public String getClientPhone() {
		return this.clientPhone;
	}

	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}

	public String getClientEmail() {
		return this.clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
	}

	public String getClientAddress() {
		return this.clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes_() {
		return this.notes_;
	}

	public void setNotes_(String notes_) {
		this.notes_ = notes_;
	}

	public Double getAmountPayable() {
		return this.amountPayable;
	}

	public void setAmountPayable(Double amountPayable) {
		this.amountPayable = amountPayable;
	}

	public Boolean getPaid() {
		return this.paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public Double getPaidAmount() {
		return this.paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Date getPaidDate() {
		return this.paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public JemsPaymentMethod getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(JemsPaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentNotes() {
		return this.paymentNotes;
	}

	public void setPaymentNotes(String paymentNotes) {
		this.paymentNotes = paymentNotes;
	}

	public String getReceiptVoucherNumber() {
		return this.receiptVoucherNumber;
	}

	public void setReceiptVoucherNumber(String receiptVoucherNumber) {
		this.receiptVoucherNumber = receiptVoucherNumber;
	}

	public String getAttachments() {
		return this.attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public Date getModifiedDateTime() {
		return this.modifiedDateTime;
	}

	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public JemsUser getModifiedUser() {
		return this.modifiedUser;
	}

	public void setModifiedUser(JemsUser modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public JemsOrganization getOrganization() {
		return this.organization;
	}

	public void setOrganization(JemsOrganization organization) {
		this.organization = organization;
	}

	public JemsCurrency getCurrency() {
		return this.currency;
	}

	public void setCurrency(JemsCurrency currency) {
		this.currency = currency;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	// //////TO BE REMOVED FIX SEARCH
	public JemsQuotation getJemsQuotation() {
		return this.jemsQuotation;
	}

	public void setJemsQuotation(JemsQuotation jemsQuotation) {
		this.jemsQuotation = jemsQuotation;
	}

	public JemsInvoice getJemsInvoice() {
		return this.jemsInvoice;
	}

	public void setJemsInvoice(JemsInvoice jemsInvoice) {
		this.jemsInvoice = jemsInvoice;
	}

	// //////TO BE REMOVED

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public static com.dubaidrums.jems.domain.JemsEvent fromJsonToJemsEvent(
			String json) {
		return new JSONDeserializer<JemsEvent>().use(null, JemsEvent.class)
				.deserialize(json);
	}

	public static String toJsonArray(
			Collection<com.dubaidrums.jems.domain.JemsEvent> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<com.dubaidrums.jems.domain.JemsEvent> fromJsonArrayToJemsEvents(
			String json) {
		return new JSONDeserializer<List<JemsEvent>>()
				.use(null, ArrayList.class).use("values", JemsEvent.class)
				.deserialize(json);
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getTitleWithOrg() {
		if (this.organization.getId() == 1) {
			return "[JE] " + this.getTitle();
		}
		if (this.organization.getId() == 3) {
			return "[UT] " + this.getTitle();
		}
		if (this.organization.getId() == 4) {
			return "[FMD] " + this.getTitle();
		}
		return "[" + WordUtils.initials(this.organization.getName()) + "] "
				+ this.getTitle();
	}

	public String getCalendarStatusColor() {
		switch (this.status) {
		case Cancelled:
			return "red";
		case Confirmed:
			return "green";
		case Declined:
			return "blue";
		case Tentative:
			return "yellow";
		default:
			return "yellow";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JemsEvent other = (JemsEvent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public JemsUser getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(JemsUser createdUser) {
		this.createdUser = createdUser;
	}

}
