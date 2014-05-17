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

@Entity

public class JemsRole {

	public JemsRole(String rolename) {
		this.roleName = rolename;
	}

    public JemsRole() {
		// TODO Auto-generated constructor stub
	}

    @NotNull
    @Size(min = 3, max = 500)
    private String roleName;

	public String getRoleName() {
        return this.roleName;
    }

	public void setRoleName(String roleName) {
        this.roleName = roleName;
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
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static JemsRole fromJsonToJemsRole(String json) {
        return new JSONDeserializer<JemsRole>().use(null, JemsRole.class).deserialize(json);
    }

	public static String toJsonArray(Collection<JemsRole> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<JemsRole> fromJsonArrayToJemsRoles(String json) {
        return new JSONDeserializer<List<JemsRole>>().use(null, ArrayList.class).use("values", JemsRole.class).deserialize(json);
    }
}
