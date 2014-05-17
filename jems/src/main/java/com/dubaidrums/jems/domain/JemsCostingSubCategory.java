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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity

public class JemsCostingSubCategory {

    @NotNull
    @Size(min = 3, max = 500)
    private String name;

    private Double rate;

    @ManyToOne
    private JemsCostingCategory category;

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

	public static JemsCostingSubCategory fromJsonToJemsCostingSubCategory(String json) {
        return new JSONDeserializer<JemsCostingSubCategory>().use(null, JemsCostingSubCategory.class).deserialize(json);
    }

	public static String toJsonArray(Collection<JemsCostingSubCategory> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<JemsCostingSubCategory> fromJsonArrayToJemsCostingSubCategorys(String json) {
        return new JSONDeserializer<List<JemsCostingSubCategory>>().use(null, ArrayList.class).use("values", JemsCostingSubCategory.class).deserialize(json);
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Double getRate() {
        return this.rate;
    }

	public void setRate(Double rate) {
        this.rate = rate;
    }

	public JemsCostingCategory getCategory() {
        return this.category;
    }

	public void setCategory(JemsCostingCategory category) {
        this.category = category;
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
		JemsCostingSubCategory other = (JemsCostingSubCategory) obj;
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
