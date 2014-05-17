package com.dubaidrums.jems.domain;

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

@Entity
public class JemsOrgTax {
    @NotNull
    @Size(min = 3, max = 500)
    private String name;

    private Double ratePercent;

    @Size(min = 3, max = 500)
    private String uuid;
    
    @ManyToOne
    private JemsOrganization organization;
    
    @Value("true")
    private Boolean active;

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

	public JemsOrganization getOrganization() {
        return this.organization;
    }

	public void setOrganization(JemsOrganization organization) {
        this.organization = organization;
    }
	
	public Boolean getActive() {
        return this.active;
    }

	public void setActive(Boolean active) {
        this.active = active;
    }	
}
