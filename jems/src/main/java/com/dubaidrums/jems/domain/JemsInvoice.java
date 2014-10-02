package com.dubaidrums.jems.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
public class JemsInvoice {

	@NotNull
	@Min(0L)
	private Integer iNumber;

	@Temporal(TemporalType.TIMESTAMP)
	private Date iDate;

	@Size(max = 500)
	private String billTo;

	@Size(max = 1000)
	private String eventDetails;

	@Size(max = 500)
	private String paymentTerms;

	@Size(max = 500)
	private String clientRef;

	@Size(max = 500)
	private String description1;

	private Double amount1;

	@Size(max = 500)
	private String description2;

	private Double amount2;

	@Size(max = 500)
	private String description3;

	private Double amount3;

	@Size(max = 500)
	private String description4;

	private Double amount4;

	@Size(max = 500)
	private String description5;

	private Double amount5;

	@NotNull
	@OneToOne
	private JemsEvent jemsEvent;

	@NotNull
	private boolean active;

	@OneToMany(cascade = { javax.persistence.CascadeType.PERSIST,
			javax.persistence.CascadeType.MERGE })
	private Set<JemsTax> taxes = new HashSet<JemsTax>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@NotNull
	@Min(0L)
	private int revisionNumber;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@ManyToOne
	private JemsUser createdUser;

	@ManyToOne
	private JemsUser modifiedUser;

	@OneToOne
	// (mappedBy="jemsInvoice")
	private JemsQuotation jemsQuotation;

	public int getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(int revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public JemsEvent getJemsEvent() {
		return jemsEvent;
	}

	public void setJemsEvent(JemsEvent jemsEvent) {
		this.jemsEvent = jemsEvent;
	}

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

	public String toString() {
		// return ReflectionToStringBuilder.toString(this,
		// ToStringStyle.SHORT_PREFIX_STYLE);
		return "InvoiceId" + this.getId();
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public static com.dubaidrums.jems.domain.JemsInvoice fromJsonToJemsInvoice(
			String json) {
		return new JSONDeserializer<JemsInvoice>().use(null, JemsInvoice.class)
				.deserialize(json);
	}

	public static String toJsonArray(
			Collection<com.dubaidrums.jems.domain.JemsInvoice> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<com.dubaidrums.jems.domain.JemsInvoice> fromJsonArrayToJemsInvoices(
			String json) {
		return new JSONDeserializer<List<JemsInvoice>>()
				.use(null, ArrayList.class).use("values", JemsInvoice.class)
				.deserialize(json);
	}

	public Integer getINumber() {
		return this.iNumber;
	}

	public void setINumber(Integer iNumber) {
		this.iNumber = iNumber;
	}

	public Date getIDate() {
		return this.iDate;
	}

	public void setIDate(Date iDate) {
		this.iDate = iDate;
	}

	public String getBillTo() {
		return this.billTo;
	}

	public void setBillTo(String billTo) {
		this.billTo = billTo;
	}

	public String getEventDetails() {
		return this.eventDetails;
	}

	public void setEventDetails(String eventDetails) {
		this.eventDetails = eventDetails;
	}

	public String getPaymentTerms() {
		return this.paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getClientRef() {
		return this.clientRef;
	}

	public void setClientRef(String clientRef) {
		this.clientRef = clientRef;
	}

	public String getDescription1() {
		return this.description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public Double getAmount1() {
		return this.amount1;
	}

	public void setAmount1(Double amount1) {
		this.amount1 = amount1;
	}

	public String getDescription2() {
		return this.description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public Double getAmount2() {
		return this.amount2;
	}

	public void setAmount2(Double amount2) {
		this.amount2 = amount2;
	}

	public String getDescription3() {
		return this.description3;
	}

	public void setDescription3(String description3) {
		this.description3 = description3;
	}

	public Double getAmount3() {
		return this.amount3;
	}

	public void setAmount3(Double amount3) {
		this.amount3 = amount3;
	}

	public String getDescription4() {
		return this.description4;
	}

	public void setDescription4(String description4) {
		this.description4 = description4;
	}

	public Double getAmount4() {
		return this.amount4;
	}

	public void setAmount4(Double amount4) {
		this.amount4 = amount4;
	}

	public String getDescription5() {
		return this.description5;
	}

	public void setDescription5(String description5) {
		this.description5 = description5;
	}

	public Double getAmount5() {
		return this.amount5;
	}

	public void setAmount5(Double amount5) {
		this.amount5 = amount5;
	}

	public Set<com.dubaidrums.jems.domain.JemsTax> getTaxes() {
		return this.taxes;
	}

	public void setTaxes(Set<com.dubaidrums.jems.domain.JemsTax> taxes) {
		this.taxes = taxes;
	}

	public Double getTotalAmount() {
		Double subtotal = getSubTotalAmount();
		if (getTaxes().size() == 0)
			return subtotal;
		else {
			Double total = subtotal;
			for (JemsTax jt : getTaxes()) {
				total += (jt.getRatePercent() * subtotal) / 100;
			}
			return total;
		}
	}

	public Double getSubTotalAmount() {
		Double total = (amount1 == null ? 0.0 : amount1)
				+ (amount2 == null ? 0.0 : amount2)
				+ (amount3 == null ? 0.0 : amount3)
				+ (amount4 == null ? 0.0 : amount4)
				+ (amount5 == null ? 0.0 : amount5);
		return total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iNumber == null) ? 0 : iNumber.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		JemsInvoice other = (JemsInvoice) obj;
		if (iNumber == null) {
			if (other.iNumber != null)
				return false;
		} else if (!iNumber.equals(other.iNumber))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public com.dubaidrums.jems.domain.JemsInvoice buildCopy() {
		JemsInvoice ji = new JemsInvoice();
		ji.setActive(this.isActive());
		ji.setAmount1(this.getAmount1());
		ji.setAmount2(this.getAmount2());
		ji.setAmount3(this.getAmount3());
		ji.setAmount4(this.getAmount4());
		ji.setAmount5(this.getAmount5());
		ji.setBillTo(this.getBillTo());
		ji.setClientRef(this.getClientRef());
		ji.setDescription1(this.getDescription1());
		ji.setDescription2(this.getDescription2());
		ji.setDescription3(this.getDescription3());
		ji.setDescription4(this.getDescription4());
		ji.setDescription5(this.getDescription5());
		ji.setEventDetails(this.getEventDetails());
		ji.setJemsEvent(this.getJemsEvent());
		ji.setPaymentTerms(this.getPaymentTerms());
		ji.setTaxes(this.getTaxes());
		ji.setCreatedDate(this.getCreatedDate());
		ji.setCreatedUser(this.getCreatedUser());
		ji.setModifiedDate(this.getModifiedDate());
		ji.setModifiedUser(this.getModifiedUser());
		return ji;
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

	public JemsUser getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(JemsUser modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public JemsQuotation getJemsQuotation() {
		return jemsQuotation;
	}

	public void setJemsQuotation(JemsQuotation jemsQuotation) {
		this.jemsQuotation = jemsQuotation;
	}
}
