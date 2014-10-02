package com.dubaidrums.jems.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
public class JemsSms {

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<JemsUser> sendTo = new HashSet<JemsUser>();

	@Size(max = 114)
	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date sentTime;

	@ManyToOne
	private JemsUser sentBy;

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public static JemsSms fromJsonToJemsSms(String json) {
		return new JSONDeserializer<JemsSms>().use(null, JemsSms.class)
				.deserialize(json);
	}

	public static String toJsonArray(Collection<JemsSms> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<JemsSms> fromJsonArrayToJemsSmses(String json) {
		return new JSONDeserializer<List<JemsSms>>().use(null, ArrayList.class)
				.use("values", JemsSms.class).deserialize(json);
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public Set<JemsUser> getSendTo() {
		return this.sendTo;
	}

	public void setSendTo(Set<JemsUser> sendTo) {
		this.sendTo = sendTo;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public JemsUser getSentBy() {
		return this.sentBy;
	}

	public void setSentBy(JemsUser sentBy) {
		this.sentBy = sentBy;
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
}
