package com.dubaidrums.jems.service.impl;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsSearchService;
import com.dubaidrums.jems.service.JemsUtilService;

import flexjson.JSONSerializer;

@Service
@Transactional
public class JemsSearchServiceImpl implements JemsSearchService{

	private SolrServer server;
	Logger log = LogManager.getLogger(JemsSearchServiceImpl.class);
	
	//circular dependency?
	//@Autowired
    //JemsEventService jemsEventService;
	
	@Autowired
    JemsUtilService jemsUtilService;	
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
	DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	public long getDocumentCount() {
		if(jemsUtilService.isDev()) return -1;
	    try {
			SolrQuery q = new SolrQuery("*:*");
		    q.setRows(0);
			return server.query(q).getResults().getNumFound();
		} catch (Exception e) {
			log.error("Error getting number of documents in search index!",e);
			return -1;
		}
	}
	

	@Async
	public void createJemsEventDocument(JemsEvent je){
		if(jemsUtilService.isDev()) return;
		try {
			SolrInputDocument doc = getSolrDocument(je);
	    	server.add(doc);
	    	server.commit();	
	    	log.info("jemsEvent added to search index!");
		} catch (Exception e) {
			log.error("Unable to create jemsEvent document in search index!",e);
		}
	}
	
	@Async
	public void deleteJemsEventDocument(JemsEvent je){
		if(jemsUtilService.isDev()) return;
		try {
			server.deleteById(je.getId().toString());
			server.commit();
			log.info("Deleted jemsEvent from search index. id: "+je.getId());
		} catch (Exception e) {
			log.error("Unable to delete jemsEvent from search index!",e);
		}
	}
	
	@Async
	public void updateJemsEventDocument(JemsEvent je){
		if(jemsUtilService.isDev()) return;
		try {
			SolrInputDocument doc = getSolrDocument(je);
	    	server.add(doc);
	    	server.commit();	
	    	log.info("jemsEvent updated to search index!");
		} catch (Exception e) {
			log.error("Unable to update jemsEvent document in search index!",e);
		}
	}
	
	public void deleteIndex(){
		if(jemsUtilService.isDev()) return;
		try {
			server.deleteByQuery( "*:*" );
			server.commit();
			server.optimize();
			log.info("Search index deleted!");
		} catch (Exception e) {
			log.error("Unable to delete search index!", e);
		}
	}

	public void createIndex(List<JemsEvent> events){
		if(jemsUtilService.isDev()) return;
		try {
			//List<JemsEvent> events = jemsEventService.findAllJemsEvents();
			Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			int i = 0;
			for (JemsEvent je : events) {
				docs.add(getSolrDocument(je));				
				i++;
				if(i%100==0){
					log.info("Adding docs to search index: "+i);
					server.add(docs);
					server.commit();
					docs.clear();
				}
				//if(i==500) break;
			}
			server.commit();
			log.info("Search index created!");
			log.info("Document count in search index: "+getDocumentCount());
		} catch (Exception e) {
			log.error("Unable to create search index!", e);
		} 
	}	
	
	public void setupSearch() {
		if(jemsUtilService.isDev()) return;
		String url = "http://index.websolr.com/solr/2889d315f55";
		try {
			server = new HttpSolrServer(url);
		} catch (Exception e) {
			log.error("Unable to setup search!", e);
		}
	}
	
	private SolrInputDocument getSolrDocument(JemsEvent je){
		SolrInputDocument doc = new SolrInputDocument();
		
	    doc.addField( "id", je.getId().toString());
	    doc.addField( "title", je.getTitle());
	    doc.addField( "description", je.getDescription());
	    doc.addField( "type", je.getType().toString());
	    doc.addField( "status", je.getStatus().toString());
	    doc.addField( "startDateTime", je.getStartDateTime());
	    doc.addField( "endDateTime", je.getEndDateTime());
	    
	    doc.addField( "startDateTime_text", df.format(je.getStartDateTime()));
	    doc.addField( "endDateTime_text", df.format(je.getEndDateTime()));
	    
	    doc.addField( "location", je.getLocation());
	    doc.addField( "locationLatLong", je.getLocationLatLong());
	    
	    doc.addField( "country", je.getCountry().getName());
	    doc.addField( "region", je.getRegion().getName());
	    doc.addField( "hiringAgentCompany", je.getHiringAgentCompany());
	    doc.addField( "hiringAgentContactPerson", je.getHiringAgentContactPerson());
	    doc.addField( "hiringAgentPhone", je.getHiringAgentPhone());
	    doc.addField( "hiringAgentEmail", je.getHiringAgentEmail());
	    doc.addField( "hiringAgentAddress", je.getHiringAgentAddress());
	    doc.addField( "clientCompany", je.getClientCompany());
	    doc.addField( "clientContactPerson", je.getClientContactPerson());
	    doc.addField( "clientPhone", je.getClientPhone());
	    doc.addField( "clientEmail", je.getClientEmail());
	    doc.addField( "clientAddress", je.getClientAddress());
	    doc.addField( "notes", je.getNotes());
	    doc.addField( "notes_", je.getNotes_());
	    doc.addField( "paid", je.getPaid());
	    doc.addField( "receiptVoucherNumber", je.getReceiptVoucherNumber());
	    doc.addField( "organization", je.getOrganization().getId().intValue());
	    
//	    if(je.getJemsQuotation()!=null){
//	    	doc.addField( "qNumber", je.getJemsQuotation().getQNumber().toString());
//		    doc.addField( "qDate", je.getJemsQuotation().getQDate());	    
//		    doc.addField( "qDescription1", je.getJemsQuotation().getDescription1());
//		    doc.addField( "qDescription2", je.getJemsQuotation().getDescription2());
//		    doc.addField( "qDescription3", je.getJemsQuotation().getDescription3());
//		    doc.addField( "qDescription4", je.getJemsQuotation().getDescription4());
//		    doc.addField( "qDescription5", je.getJemsQuotation().getDescription5());	    
//		    doc.addField( "qBillTo", je.getJemsQuotation().getBillTo());
//		    doc.addField( "qEventDetails", je.getJemsQuotation().getEventDetails());	
//	    }
//	    
//	    if(je.getJemsInvoice()!=null){
//	    	doc.addField( "iNumber", je.getJemsInvoice().getINumber().toString());
//		    doc.addField( "iDate", je.getJemsInvoice().getIDate());
//		    doc.addField( "iDescription1", je.getJemsInvoice().getDescription1());
//		    doc.addField( "iDescription2", je.getJemsInvoice().getDescription2());
//		    doc.addField( "iDescription3", je.getJemsInvoice().getDescription3());
//		    doc.addField( "iDescription4", je.getJemsInvoice().getDescription4());
//		    doc.addField( "iDescription5", je.getJemsInvoice().getDescription5());
//		    doc.addField( "iBillTo", je.getJemsInvoice().getBillTo());
//		    doc.addField( "iEventDetails", je.getJemsInvoice().getEventDetails());
//	    }
	    
	    
	    
	    
	    return doc;
	}

	public String query(String q, boolean chkdates, boolean chkpaid,
			boolean chktype, boolean chkeventstatus, Date startdatetime,
			Date enddatetime, boolean paidstatus, String eventtype,
			String eventstatus, JemsUser user) {

			SolrQuery query = new SolrQuery();
			StringBuilder k = new StringBuilder();
		try {
			
			k.append("text:").append(q).append("* ");
			
			List<JemsOrganization> orgs = new ArrayList<JemsOrganization>(user.getOrganizations());
			k.append("AND (");
			for (Iterator it = orgs.iterator(); it.hasNext();) {
				JemsOrganization org = (JemsOrganization) it.next();
				k.append("organization:"+org.getId()+" ");
				if(it.hasNext()){
					k.append(" OR ");
				}
			}
			k.append(") ");
			
			
			if(chkdates && startdatetime!=null && enddatetime!=null){
				k.append("AND startDateTime:[").append(sdf.format(startdatetime)).append(" TO ").append(sdf.format(enddatetime)).append("] ");
			}
			
			if(chkpaid){
				k.append("AND paid:"+paidstatus+" ");
			}
			
			if(chktype && eventtype!=null && eventtype.length()!=0){
				k.append("AND type:"+eventtype+" ");
			}
			
			if(chkeventstatus && eventstatus!=null && eventstatus.length()!=0){
				k.append("AND status:"+eventstatus+" ");
			}
			
			log.info("SolrQuery built! user: "+user.getUserName()+" query: "+k.toString());
					
			query.setQuery(k.toString());
			query.setRows(250);

			QueryResponse rsp = server.query( query );
			SolrDocumentList docs = rsp.getResults();
			log.info("Search successful. results: "+docs.getNumFound()+" query: "+q);
			
			Map results = new HashMap();
			results.put("docs", docs);
			results.put("numFound", docs.getNumFound());
			results.put("rows", query.getRows());
			results.put("query", query.getQuery());
			results.put("success", "Search completed at "+df2.format(new Date())+" - Elapsed Time: "+rsp.getElapsedTime()+"ms");			
			results.put("error", "");
			
			return new JSONSerializer().exclude("*.class").deepSerialize(results);
		
		} catch (Exception e) {
			log.error("Error occured while searching query: "+q, e);
			
			
			Map results = new HashMap();
			SolrDocumentList docs = new SolrDocumentList();
			results.put("docs", docs);
			results.put("numFound", docs.getNumFound());
			results.put("rows", query.getRows());
			results.put("query", query.getQuery());
			results.put("success", "");			
			results.put("error", "Search Error at "+df2.format(new Date())+" - "+e.getMessage());
			
			return new JSONSerializer().exclude("*.class").deepSerialize(results);
		}		
	}

}
