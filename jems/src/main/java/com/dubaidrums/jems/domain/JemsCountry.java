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
public class JemsCountry {

	@NotNull
	@Size(min = 2, max = 10)
	private String isoCode;

	@NotNull
	@Size(min = 3, max = 500)
	private String name;

	@ManyToOne
	private JemsCurrency currency;

	@Value("true")
	private Boolean active;

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
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

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public static JemsCountry fromJsonToJemsCountry(String json) {
		return new JSONDeserializer<JemsCountry>().use(null, JemsCountry.class)
				.deserialize(json);
	}

	public static String toJsonArray(Collection<JemsCountry> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<JemsCountry> fromJsonArrayToJemsCountrys(
			String json) {
		return new JSONDeserializer<List<JemsCountry>>()
				.use(null, ArrayList.class).use("values", JemsCountry.class)
				.deserialize(json);
	}

	public String getIsoCode() {
		return this.isoCode;
	}

	public String getIsoCodeSmall() {
		return this.isoCode.toLowerCase();
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

}
