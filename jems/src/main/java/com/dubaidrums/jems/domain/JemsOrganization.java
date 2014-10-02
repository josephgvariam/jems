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
import org.springframework.beans.factory.annotation.Value;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
public class JemsOrganization {

	@NotNull
	@Size(min = 3, max = 500)
	private String name;

	@ManyToOne
	private JemsCountry country;

	@ManyToOne
	private JemsRegion region;

	@ManyToOne
	private JemsCurrency currency;

	@NotNull
	@Size(max = 500)
	private String adminEmail;

	@Value("true")
	private Boolean active;

	@Size(max = 500)
	private String defaultGps;

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public static JemsOrganization fromJsonToJemsOrganization(String json) {
		return new JSONDeserializer<JemsOrganization>().use(null,
				JemsOrganization.class).deserialize(json);
	}

	public static String toJsonArray(Collection<JemsOrganization> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<JemsOrganization> fromJsonArrayToJemsOrganizations(
			String json) {
		return new JSONDeserializer<List<JemsOrganization>>()
				.use(null, ArrayList.class)
				.use("values", JemsOrganization.class).deserialize(json);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public JemsCurrency getCurrency() {
		return this.currency;
	}

	public void setCurrency(JemsCurrency currency) {
		this.currency = currency;
	}

	public String getAdminEmail() {
		return this.adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getDefaultGps() {
		return this.defaultGps;
	}

	public void setDefaultGps(String defaultGps) {
		this.defaultGps = defaultGps;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		JemsOrganization other = (JemsOrganization) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
