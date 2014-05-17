package com.dubaidrums.jems.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity

public class JemsCounters {

    @NotNull
    @Min(0L)
    private Integer qCount;

    @NotNull
    @Min(0L)
    private Integer iCount;

    @ManyToOne
    private JemsOrganization organization;

	public Integer getQCount() {
        return this.qCount;
    }

	public void setQCount(Integer qCount) {
        this.qCount = qCount;
    }

	public Integer getICount() {
        return this.iCount;
    }

	public void setICount(Integer iCount) {
        this.iCount = iCount;
    }

	public JemsOrganization getOrganization() {
        return this.organization;
    }

	public void setOrganization(JemsOrganization organization) {
        this.organization = organization;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

	public static JemsCounters fromJsonToJemsCounters(String json) {
        return new JSONDeserializer<JemsCounters>().use(null, JemsCounters.class).deserialize(json);
    }

	public static String toJsonArray(Collection<JemsCounters> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<JemsCounters> fromJsonArrayToJemsCounterses(String json) {
        return new JSONDeserializer<List<JemsCounters>>().use(null, ArrayList.class).use("values", JemsCounters.class).deserialize(json);
    }
}
