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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity

public class JemsCostingCategory {

    @NotNull
    @Size(min = 3, max = 500)
    private String name;

    @ManyToOne
    private JemsOrganization organization;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<JemsCostingSubCategory> subcategories = new HashSet<JemsCostingSubCategory>();

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public JemsOrganization getOrganization() {
        return this.organization;
    }

	public void setOrganization(JemsOrganization organization) {
        this.organization = organization;
    }

	public Set<JemsCostingSubCategory> getSubcategories() {
        return this.subcategories;
    }

	public void setSubcategories(Set<JemsCostingSubCategory> subcategories) {
        this.subcategories = subcategories;
    }

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static JemsCostingCategory fromJsonToJemsCostingCategory(String json) {
        return new JSONDeserializer<JemsCostingCategory>().use(null, JemsCostingCategory.class).deserialize(json);
    }

	public static String toJsonArray(Collection<JemsCostingCategory> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<JemsCostingCategory> fromJsonArrayToJemsCostingCategorys(String json) {
        return new JSONDeserializer<List<JemsCostingCategory>>().use(null, ArrayList.class).use("values", JemsCostingCategory.class).deserialize(json);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		JemsCostingCategory other = (JemsCostingCategory) obj;
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
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		return true;
	}
	
	
}
