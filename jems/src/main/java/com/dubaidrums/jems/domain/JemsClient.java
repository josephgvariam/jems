package com.dubaidrums.jems.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class JemsClient {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@NotNull
	private Boolean active;

	@NotNull
	@Size(max = 200)
	private String company;

	@Size(max = 200)
	private String contactPerson;

	@Size(max = 100)
	private String phone;

	@Size(max = 100)
	private String email;

	@Size(max = 500)
	private String address;

	@ManyToMany
	private Set<JemsEvent> events = new HashSet<JemsEvent>();

	public Set<JemsEvent> getEvents() {
		return events;
	}

	public void setEvents(Set<JemsEvent> events) {
		this.events = events;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String toString() {
		// return ReflectionToStringBuilder.toString(this,
		// ToStringStyle.SHORT_PREFIX_STYLE);
		List<Long> eventIds = new ArrayList<Long>();
		for (JemsEvent e : getEvents()) {
			eventIds.add(e.getId());
		}
		return "JemsClient[company=" + company + ", contactPerson="
				+ contactPerson + ", email=" + email + ", address=" + address
				+ ", events=" + eventIds + " ]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result
				+ ((contactPerson == null) ? 0 : contactPerson.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
		JemsClient other = (JemsClient) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equalsIgnoreCase(other.company))
			return false;
		if (contactPerson == null) {
			if (other.contactPerson != null)
				return false;
		} else if (!contactPerson.equalsIgnoreCase(other.contactPerson))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equalsIgnoreCase(other.email))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equalsIgnoreCase(other.phone))
			return false;
		return true;
	}

	public int getEventsSize() {
		return events.size();
	}

}
