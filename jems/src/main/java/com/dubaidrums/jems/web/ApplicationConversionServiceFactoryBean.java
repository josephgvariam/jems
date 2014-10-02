package com.dubaidrums.jems.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.dubaidrums.jems.domain.JemsCostingCategory;
import com.dubaidrums.jems.domain.JemsCostingItem;
import com.dubaidrums.jems.domain.JemsCostingSubCategory;
import com.dubaidrums.jems.domain.JemsCounters;
import com.dubaidrums.jems.domain.JemsCountry;
import com.dubaidrums.jems.domain.JemsCurrency;
import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsInvoice;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.domain.JemsQuotation;
import com.dubaidrums.jems.domain.JemsRegion;
import com.dubaidrums.jems.domain.JemsRole;
import com.dubaidrums.jems.domain.JemsSms;
import com.dubaidrums.jems.domain.JemsStaff;
import com.dubaidrums.jems.domain.JemsTax;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.repository.JemsTaxRepository;
import com.dubaidrums.jems.service.JemsCostingCategoryService;
import com.dubaidrums.jems.service.JemsCostingItemService;
import com.dubaidrums.jems.service.JemsCostingSubCategoryService;
import com.dubaidrums.jems.service.JemsCountersService;
import com.dubaidrums.jems.service.JemsCountryService;
import com.dubaidrums.jems.service.JemsCurrencyService;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsInvoiceService;
import com.dubaidrums.jems.service.JemsOrganizationService;
import com.dubaidrums.jems.service.JemsQuotationService;
import com.dubaidrums.jems.service.JemsRegionService;
import com.dubaidrums.jems.service.JemsRoleService;
import com.dubaidrums.jems.service.JemsSmsService;
import com.dubaidrums.jems.service.JemsStaffService;
import com.dubaidrums.jems.service.JemsUserService;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends
		FormattingConversionServiceFactoryBean {

	Logger log = LogManager
			.getLogger(ApplicationConversionServiceFactoryBean.class);

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
		registry.addConverter(getBooleanToStringConverter());
	}

	private Converter<Boolean, String> getBooleanToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<Boolean, String>() {
			public String convert(Boolean bool) {
				return bool ? "Yes" : "No";
			}
		};
	}

	// private Converter<String, Boolean> getStringToBooleanConverter() {
	// return new org.springframework.core.convert.converter.Converter<String,
	// Boolean>() {
	// public Boolean convert(String s) {
	// System.out.println("============================== s: "+s);
	// if(s!=null && s.length()!=0){
	// if(s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("true") ||
	// s.equalsIgnoreCase("on")){
	// return true;
	// }else{
	// return false;
	// }
	// }else{
	// return false;
	// }
	// }
	// };
	// }

	@Autowired
	JemsCostingCategoryService jemsCostingCategoryService;

	@Autowired
	JemsCostingItemService jemsCostingItemService;

	@Autowired
	JemsCostingSubCategoryService jemsCostingSubCategoryService;

	@Autowired
	JemsCountersService jemsCountersService;

	@Autowired
	JemsCountryService jemsCountryService;

	@Autowired
	JemsCurrencyService jemsCurrencyService;

	@Autowired
	JemsEventService jemsEventService;

	@Autowired
	JemsInvoiceService jemsInvoiceService;

	@Autowired
	JemsOrganizationService jemsOrganizationService;

	@Autowired
	JemsQuotationService jemsQuotationService;

	@Autowired
	JemsRegionService jemsRegionService;

	@Autowired
	JemsRoleService jemsRoleService;

	@Autowired
	JemsSmsService jemsSmsService;

	@Autowired
	JemsTaxRepository jemsTaxRepository;

	@Autowired
	JemsUserService jemsUserService;

	@Autowired
	JemsStaffService jemsStaffService;

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

	public Converter<JemsCostingCategory, String> getJemsCostingCategoryToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsCostingCategory, java.lang.String>() {
			public String convert(JemsCostingCategory jemsCostingCategory) {
				return new StringBuilder()
						.append(jemsCostingCategory.getName()).toString();
			}
		};
	}

	public Converter<Long, JemsCostingCategory> getIdToJemsCostingCategoryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsCostingCategory>() {
			public com.dubaidrums.jems.domain.JemsCostingCategory convert(
					java.lang.Long id) {
				return jemsCostingCategoryService.findJemsCostingCategory(id);
			}
		};
	}

	public Converter<String, JemsCostingCategory> getStringToJemsCostingCategoryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsCostingCategory>() {
			public com.dubaidrums.jems.domain.JemsCostingCategory convert(
					String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsCostingCategory.class);
			}
		};
	}

	public Converter<JemsCostingItem, String> getJemsCostingItemToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsCostingItem, java.lang.String>() {
			public String convert(JemsCostingItem jemsCostingItem) {
				return new StringBuilder()
						.append(jemsCostingItem.getCategory()).append(' ')
						.append(jemsCostingItem.getSubCategory()).append(' ')
						.append(jemsCostingItem.getRate()).append(' ')
						.append(jemsCostingItem.getQuantity()).toString();
			}
		};
	}

	public Converter<Long, JemsCostingItem> getIdToJemsCostingItemConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsCostingItem>() {
			public com.dubaidrums.jems.domain.JemsCostingItem convert(
					java.lang.Long id) {
				return jemsCostingItemService.findJemsCostingItem(id);
			}
		};
	}

	public Converter<String, JemsCostingItem> getStringToJemsCostingItemConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsCostingItem>() {
			public com.dubaidrums.jems.domain.JemsCostingItem convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsCostingItem.class);
			}
		};
	}

	public Converter<JemsCostingSubCategory, String> getJemsCostingSubCategoryToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsCostingSubCategory, java.lang.String>() {
			public String convert(JemsCostingSubCategory jemsCostingSubCategory) {
				return new StringBuilder()
						.append(jemsCostingSubCategory.getName()).append(' ')
						.append(jemsCostingSubCategory.getRate()).toString();
			}
		};
	}

	public Converter<Long, JemsCostingSubCategory> getIdToJemsCostingSubCategoryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsCostingSubCategory>() {
			public com.dubaidrums.jems.domain.JemsCostingSubCategory convert(
					java.lang.Long id) {
				return jemsCostingSubCategoryService
						.findJemsCostingSubCategory(id);
			}
		};
	}

	public Converter<String, JemsCostingSubCategory> getStringToJemsCostingSubCategoryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsCostingSubCategory>() {
			public com.dubaidrums.jems.domain.JemsCostingSubCategory convert(
					String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsCostingSubCategory.class);
			}
		};
	}

	public Converter<JemsCounters, String> getJemsCountersToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsCounters, java.lang.String>() {
			public String convert(JemsCounters jemsCounters) {
				return new StringBuilder().append(jemsCounters.getQCount())
						.append(' ').append(jemsCounters.getICount())
						.toString();
			}
		};
	}

	public Converter<Long, JemsCounters> getIdToJemsCountersConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsCounters>() {
			public com.dubaidrums.jems.domain.JemsCounters convert(
					java.lang.Long id) {
				return jemsCountersService.findJemsCounters(id);
			}
		};
	}

	public Converter<String, JemsCounters> getStringToJemsCountersConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsCounters>() {
			public com.dubaidrums.jems.domain.JemsCounters convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsCounters.class);
			}
		};
	}

	public Converter<JemsCountry, String> getJemsCountryToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsCountry, java.lang.String>() {
			public String convert(JemsCountry jemsCountry) {
				return new StringBuilder().append(jemsCountry.getName())
						.toString();
			}
		};
	}

	public Converter<Long, JemsCountry> getIdToJemsCountryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsCountry>() {
			public com.dubaidrums.jems.domain.JemsCountry convert(
					java.lang.Long id) {
				return jemsCountryService.findJemsCountry(id);
			}
		};
	}

	public Converter<String, JemsCountry> getStringToJemsCountryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsCountry>() {
			public com.dubaidrums.jems.domain.JemsCountry convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsCountry.class);
			}
		};
	}

	public Converter<JemsCurrency, String> getJemsCurrencyToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsCurrency, java.lang.String>() {
			public String convert(JemsCurrency jemsCurrency) {
				return new StringBuilder().append(jemsCurrency.getName())
						.toString();
			}
		};
	}

	public Converter<Long, JemsCurrency> getIdToJemsCurrencyConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsCurrency>() {
			public com.dubaidrums.jems.domain.JemsCurrency convert(
					java.lang.Long id) {
				return jemsCurrencyService.findJemsCurrency(id);
			}
		};
	}

	public Converter<String, JemsCurrency> getStringToJemsCurrencyConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsCurrency>() {
			public com.dubaidrums.jems.domain.JemsCurrency convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsCurrency.class);
			}
		};
	}

	public Converter<JemsEvent, String> getJemsEventToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsEvent, java.lang.String>() {
			public String convert(JemsEvent jemsEvent) {
				// return new
				// StringBuilder().append(jemsEvent.getTitle()).append(' ').append(jemsEvent.getDescription()).append(' ').append(jemsEvent.getStartDateTime()).append(' ').append(jemsEvent.getEndDateTime()).toString();
				return new StringBuilder().append(jemsEvent.getId()).toString();
			}
		};
	}

	public Converter<Long, JemsEvent> getIdToJemsEventConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsEvent>() {
			public com.dubaidrums.jems.domain.JemsEvent convert(
					java.lang.Long id) {
				return jemsEventService.findJemsEvent(id);
			}
		};
	}

	public Converter<String, JemsEvent> getStringToJemsEventConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsEvent>() {
			public com.dubaidrums.jems.domain.JemsEvent convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsEvent.class);
			}
		};
	}

	public Converter<JemsInvoice, String> getJemsInvoiceToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsInvoice, java.lang.String>() {
			public String convert(JemsInvoice jemsInvoice) {
				return new StringBuilder().append(jemsInvoice.getINumber())
						.append(' ').append(jemsInvoice.getIDate()).append(' ')
						.append(jemsInvoice.getBillTo()).append(' ')
						.append(jemsInvoice.getEventDetails()).toString();
			}
		};
	}

	public Converter<Long, JemsInvoice> getIdToJemsInvoiceConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsInvoice>() {
			public com.dubaidrums.jems.domain.JemsInvoice convert(
					java.lang.Long id) {
				return jemsInvoiceService.findJemsInvoice(id);
			}
		};
	}

	public Converter<String, JemsInvoice> getStringToJemsInvoiceConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsInvoice>() {
			public com.dubaidrums.jems.domain.JemsInvoice convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsInvoice.class);
			}
		};
	}

	public Converter<JemsOrganization, String> getJemsOrganizationToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsOrganization, java.lang.String>() {
			public String convert(JemsOrganization jemsOrganization) {
				return new StringBuilder().append(jemsOrganization.getName())
						.toString();
			}
		};
	}

	public Converter<Long, JemsOrganization> getIdToJemsOrganizationConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsOrganization>() {
			public com.dubaidrums.jems.domain.JemsOrganization convert(
					java.lang.Long id) {
				return jemsOrganizationService.findJemsOrganization(id);
			}
		};
	}

	public Converter<String, JemsOrganization> getStringToJemsOrganizationConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsOrganization>() {
			public com.dubaidrums.jems.domain.JemsOrganization convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsOrganization.class);
			}
		};
	}

	public Converter<JemsQuotation, String> getJemsQuotationToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsQuotation, java.lang.String>() {
			public String convert(JemsQuotation jemsQuotation) {
				// return new
				// StringBuilder().append(jemsQuotation.getQNumber()).append(' ').append(jemsQuotation.getQDate()).append(' ').append(jemsQuotation.getBillTo()).append(' ').append(jemsQuotation.getEventDetails()).toString();
				return new StringBuilder().append(jemsQuotation.getId())
						.toString();
			}
		};
	}

	public Converter<Long, JemsQuotation> getIdToJemsQuotationConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsQuotation>() {
			public com.dubaidrums.jems.domain.JemsQuotation convert(
					java.lang.Long id) {
				return jemsQuotationService.findJemsQuotation(id);
			}
		};
	}

	public Converter<String, JemsQuotation> getStringToJemsQuotationConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsQuotation>() {
			public com.dubaidrums.jems.domain.JemsQuotation convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsQuotation.class);
			}
		};
	}

	public Converter<JemsRegion, String> getJemsRegionToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsRegion, java.lang.String>() {
			public String convert(JemsRegion jemsRegion) {
				return new StringBuilder().append(jemsRegion.getName())
						.toString();
			}
		};
	}

	public Converter<Long, JemsRegion> getIdToJemsRegionConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsRegion>() {
			public com.dubaidrums.jems.domain.JemsRegion convert(
					java.lang.Long id) {
				return jemsRegionService.findJemsRegion(id);
			}
		};
	}

	public Converter<String, JemsRegion> getStringToJemsRegionConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsRegion>() {
			public com.dubaidrums.jems.domain.JemsRegion convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsRegion.class);
			}
		};
	}

	public Converter<JemsRole, String> getJemsRoleToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsRole, java.lang.String>() {
			public String convert(JemsRole jemsRole) {
				return new StringBuilder().append(jemsRole.getRoleName())
						.toString();
			}
		};
	}

	public Converter<Long, JemsRole> getIdToJemsRoleConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsRole>() {
			public com.dubaidrums.jems.domain.JemsRole convert(java.lang.Long id) {
				return jemsRoleService.findJemsRole(id);
			}
		};
	}

	public Converter<String, JemsRole> getStringToJemsRoleConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsRole>() {
			public com.dubaidrums.jems.domain.JemsRole convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsRole.class);
			}
		};
	}

	public Converter<JemsSms, String> getJemsSmsToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsSms, java.lang.String>() {
			public String convert(JemsSms jemsSms) {
				return new StringBuilder().append(jemsSms.getMessage())
						.append(' ').append(jemsSms.getSentTime()).toString();
			}
		};
	}

	public Converter<Long, JemsSms> getIdToJemsSmsConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsSms>() {
			public com.dubaidrums.jems.domain.JemsSms convert(java.lang.Long id) {
				return jemsSmsService.findJemsSms(id);
			}
		};
	}

	public Converter<String, JemsSms> getStringToJemsSmsConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsSms>() {
			public com.dubaidrums.jems.domain.JemsSms convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsSms.class);
			}
		};
	}

	public Converter<JemsTax, String> getJemsTaxToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsTax, java.lang.String>() {
			public String convert(JemsTax jemsTax) {
				return new StringBuilder().append(jemsTax.getName())
						.append(' ').append(jemsTax.getRatePercent())
						.append(' ').append(jemsTax.getUuid()).toString();
			}
		};
	}

	public Converter<Long, JemsTax> getIdToJemsTaxConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsTax>() {
			public com.dubaidrums.jems.domain.JemsTax convert(java.lang.Long id) {
				return jemsTaxRepository.findOne(id);
			}
		};
	}

	public Converter<String, JemsTax> getStringToJemsTaxConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsTax>() {
			public com.dubaidrums.jems.domain.JemsTax convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsTax.class);
			}
		};
	}

	public Converter<JemsUser, String> getJemsUserToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsUser, java.lang.String>() {
			public String convert(JemsUser jemsUser) {
				return new StringBuilder().append(jemsUser.getUserName())
						.toString();
			}
		};
	}

	public Converter<Long, JemsUser> getIdToJemsUserConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsUser>() {
			public com.dubaidrums.jems.domain.JemsUser convert(java.lang.Long id) {
				return jemsUserService.findJemsUser(id);
			}
		};
	}

	public Converter<String, JemsUser> getStringToJemsUserConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsUser>() {
			public com.dubaidrums.jems.domain.JemsUser convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsUser.class);
			}
		};
	}

	// ///
	public Converter<JemsStaff, String> getJemsStaffToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.dubaidrums.jems.domain.JemsStaff, java.lang.String>() {
			public String convert(JemsStaff jemsStaff) {
				return new StringBuilder().append(jemsStaff.getName())
						.toString();
			}
		};
	}

	public Converter<Long, JemsStaff> getIdToJemsStaffConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.dubaidrums.jems.domain.JemsStaff>() {
			public com.dubaidrums.jems.domain.JemsStaff convert(
					java.lang.Long id) {
				return jemsStaffService.findJemsStaff(id);
			}
		};
	}

	public Converter<String, JemsStaff> getStringToJemsStaffConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.dubaidrums.jems.domain.JemsStaff>() {
			public com.dubaidrums.jems.domain.JemsStaff convert(String id) {
				return getObject().convert(getObject().convert(id, Long.class),
						JemsStaff.class);
			}
		};
	}

	// ///

	public Converter<String, Date> getStringToDateConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, Date>() {
			public Date convert(String s) {
				try {
					return df.parse(s);
				} catch (ParseException e) {
					// e.printStackTrace();
					return null;
				}
			}
		};
	}

	public Converter<Date, String> getDateToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<Date, String>() {
			public String convert(Date d) {
				return df.format(d);
			}
		};
	}

	public void installLabelConverters(FormatterRegistry registry) {
		registry.addConverter(getJemsCostingCategoryToStringConverter());
		registry.addConverter(getIdToJemsCostingCategoryConverter());
		registry.addConverter(getStringToJemsCostingCategoryConverter());
		registry.addConverter(getJemsCostingItemToStringConverter());
		registry.addConverter(getIdToJemsCostingItemConverter());
		registry.addConverter(getStringToJemsCostingItemConverter());
		registry.addConverter(getJemsCostingSubCategoryToStringConverter());
		registry.addConverter(getIdToJemsCostingSubCategoryConverter());
		registry.addConverter(getStringToJemsCostingSubCategoryConverter());
		registry.addConverter(getJemsCountersToStringConverter());
		registry.addConverter(getIdToJemsCountersConverter());
		registry.addConverter(getStringToJemsCountersConverter());
		registry.addConverter(getJemsCountryToStringConverter());
		registry.addConverter(getIdToJemsCountryConverter());
		registry.addConverter(getStringToJemsCountryConverter());
		registry.addConverter(getJemsCurrencyToStringConverter());
		registry.addConverter(getIdToJemsCurrencyConverter());
		registry.addConverter(getStringToJemsCurrencyConverter());
		registry.addConverter(getJemsEventToStringConverter());
		registry.addConverter(getIdToJemsEventConverter());
		registry.addConverter(getStringToJemsEventConverter());
		registry.addConverter(getJemsInvoiceToStringConverter());
		registry.addConverter(getIdToJemsInvoiceConverter());
		registry.addConverter(getStringToJemsInvoiceConverter());
		registry.addConverter(getJemsOrganizationToStringConverter());
		registry.addConverter(getIdToJemsOrganizationConverter());
		registry.addConverter(getStringToJemsOrganizationConverter());
		registry.addConverter(getJemsQuotationToStringConverter());
		registry.addConverter(getIdToJemsQuotationConverter());
		registry.addConverter(getStringToJemsQuotationConverter());
		registry.addConverter(getJemsRegionToStringConverter());
		registry.addConverter(getIdToJemsRegionConverter());
		registry.addConverter(getStringToJemsRegionConverter());
		registry.addConverter(getJemsRoleToStringConverter());
		registry.addConverter(getIdToJemsRoleConverter());
		registry.addConverter(getStringToJemsRoleConverter());
		registry.addConverter(getJemsSmsToStringConverter());
		registry.addConverter(getIdToJemsSmsConverter());
		registry.addConverter(getStringToJemsSmsConverter());
		registry.addConverter(getJemsTaxToStringConverter());
		registry.addConverter(getIdToJemsTaxConverter());
		registry.addConverter(getStringToJemsTaxConverter());
		registry.addConverter(getJemsUserToStringConverter());
		registry.addConverter(getIdToJemsUserConverter());
		registry.addConverter(getStringToJemsUserConverter());
		registry.addConverter(getDateToStringConverter());
		registry.addConverter(getStringToDateConverter());

		registry.addConverter(getJemsStaffToStringConverter());
		registry.addConverter(getIdToJemsStaffConverter());
		registry.addConverter(getStringToJemsStaffConverter());

		// registry.addConverter(getStringToBooleanConverter());
	}

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		installLabelConverters(getObject());
	}
}
