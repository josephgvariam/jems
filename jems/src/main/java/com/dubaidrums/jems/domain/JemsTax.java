package com.dubaidrums.jems.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
public class JemsTax {

	@NotNull
	@Size(min = 3, max = 500)
	private String name;

	private Double ratePercent;

	@Size(min = 3, max = 500)
	private String uuid;

	@Value("false")
	private transient Boolean checked;

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRatePercent() {
		return this.ratePercent;
	}

	public void setRatePercent(Double ratePercent) {
		this.ratePercent = ratePercent;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public static JemsTax fromJsonToJemsTax(String json) {
		return new JSONDeserializer<JemsTax>().use(null, JemsTax.class)
				.deserialize(json);
	}

	public static String toJsonArray(Collection<JemsTax> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<JemsTax> fromJsonArrayToJemsTaxes(String json) {
		return new JSONDeserializer<List<JemsTax>>().use(null, ArrayList.class)
				.use("values", JemsTax.class).deserialize(json);
	}
}
