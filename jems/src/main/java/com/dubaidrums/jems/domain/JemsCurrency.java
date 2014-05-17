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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;

@Entity

public class JemsCurrency {

    @NotNull
    @Size(min = 2, max = 10)
    private String isoCode;

    @NotNull
    @Size(min = 3, max = 500)
    private String name;

    @Value("true")
    private Boolean active;

	public String getIsoCode() {
        return this.isoCode;
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

	public Boolean getActive() {
        return this.active;
    }

	public void setActive(Boolean active) {
        this.active = active;
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

	public static JemsCurrency fromJsonToJemsCurrency(String json) {
        return new JSONDeserializer<JemsCurrency>().use(null, JemsCurrency.class).deserialize(json);
    }

	public static String toJsonArray(Collection<JemsCurrency> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<JemsCurrency> fromJsonArrayToJemsCurrencys(String json) {
        return new JSONDeserializer<List<JemsCurrency>>().use(null, ArrayList.class).use("values", JemsCurrency.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
