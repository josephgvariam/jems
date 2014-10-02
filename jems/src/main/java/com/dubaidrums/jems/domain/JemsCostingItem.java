package com.dubaidrums.jems.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
public class JemsCostingItem {

	@NotNull
	@Size(min = 3, max = 500)
	private String category;

	@NotNull
	@Size(min = 3, max = 500)
	private String subCategory;

	private Double rate;

	private Double quantity;

	@ManyToOne
	private JemsOrganization organization;

	@ManyToOne
	private JemsEvent jemsEvent;

	public JemsEvent getJemsEvent() {
		return this.jemsEvent;
	}

	public void setJemsEvent(JemsEvent jemsEvent) {
		this.jemsEvent = jemsEvent;
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public static JemsCostingItem fromJsonToJemsCostingItem(String json) {
		return new JSONDeserializer<JemsCostingItem>().use(null,
				JemsCostingItem.class).deserialize(json);
	}

	public static String toJsonArray(Collection<JemsCostingItem> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<JemsCostingItem> fromJsonArrayToJemsCostingItems(
			String json) {
		return new JSONDeserializer<List<JemsCostingItem>>()
				.use(null, ArrayList.class)
				.use("values", JemsCostingItem.class).deserialize(json);
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return this.subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public Double getRate() {
		return this.rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public JemsOrganization getOrganization() {
		return this.organization;
	}

	public void setOrganization(JemsOrganization organization) {
		this.organization = organization;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

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
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((organization == null) ? 0 : organization.hashCode());
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
		JemsCostingItem other = (JemsCostingItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		return true;
	}

	public Double getAmount() {
		return getRate() * getQuantity();
	}

}
