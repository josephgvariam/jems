package com.dubaidrums.jems.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity

public class JemsUser {

    @NotNull
    @Size(min = 3, max = 500)
    private String userName;

    @Size(min = 3, max = 500)
    private String password;

    @NotNull
    private Boolean enabled;

    @NotNull
    @Size(min = 3, max = 500)
    private String email;

    @NotNull
    @Size(min = 3, max = 500)
    private String mobile;

    @NotNull
    @Size(min = 3, max = 500)
    private String fullName;

    @ManyToMany
    private Set<JemsRole> roles = new HashSet<JemsRole>();

    @ManyToMany
    private Set<JemsOrganization> organizations = new HashSet<JemsOrganization>();

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static JemsUser fromJsonToJemsUser(String json) {
        return new JSONDeserializer<JemsUser>().use(null, JemsUser.class).deserialize(json);
    }

	public static String toJsonArray(Collection<JemsUser> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<JemsUser> fromJsonArrayToJemsUsers(String json) {
        return new JSONDeserializer<List<JemsUser>>().use(null, ArrayList.class).use("values", JemsUser.class).deserialize(json);
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

	public String getUserName() {
        return this.userName;
    }

	public void setUserName(String userName) {
        this.userName = userName;
    }

	public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public Boolean getEnabled() {
        return this.enabled;
    }

	public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public String getMobile() {
        return this.mobile;
    }

	public void setMobile(String mobile) {
        this.mobile = mobile;
    }

	public String getFullName() {
        return this.fullName;
    }

	public void setFullName(String fullName) {
        this.fullName = fullName;
    }

	public Set<JemsRole> getRoles() {
        return this.roles;
    }

	public void setRoles(Set<JemsRole> roles) {
        this.roles = roles;
    }

	public Set<JemsOrganization> getOrganizations() {
		Set<JemsOrganization> orgs = new HashSet<JemsOrganization>();
		for (JemsOrganization jo : organizations) {
			if(jo.getActive()){
				orgs.add(jo);
			}
		}
		return orgs;
        //return this.organizations;
    }

	public void setOrganizations(Set<JemsOrganization> organizations) {
        this.organizations = organizations;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		JemsUser other = (JemsUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	
	
}
