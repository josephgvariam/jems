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
public class JemsRegion {

	@NotNull
	@Size(min = 3, max = 500)
	private String name;

	@ManyToOne
	private JemsCountry country;

	@Value("true")
	private Boolean active;

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

	public static JemsRegion fromJsonToJemsRegion(String json) {
		return new JSONDeserializer<JemsRegion>().use(null, JemsRegion.class)
				.deserialize(json);
	}

	public static String toJsonArray(Collection<JemsRegion> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<JemsRegion> fromJsonArrayToJemsRegions(String json) {
		return new JSONDeserializer<List<JemsRegion>>()
				.use(null, ArrayList.class).use("values", JemsRegion.class)
				.deserialize(json);
	}

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

	public JemsCountry getCountry() {
		return this.country;
	}

	public void setCountry(JemsCountry country) {
		this.country = country;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
