package com.dubaidrums.jems.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.dubaidrums.jems.domain.JemsView;
import com.dubaidrums.jems.domain.JemsViewRowMapper;
import com.dubaidrums.jems.service.JemsCsvDumpService;

@Service
//@Transactional
public class JemsCsvDumpServiceImpl implements JemsCsvDumpService{

	Logger log = LogManager.getLogger(JemsCsvDumpServiceImpl.class);
	   
	@Autowired
	private DataSource dataSource;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public String dumpCsv(boolean searchDate, String startDate, String endDate, HttpServletRequest r){
		try {
			//log.info("Using sql: "+getSql(searchDate, startDate, endDate));
			FlatFileItemWriter<JemsView> itemWriter = new FlatFileItemWriter<JemsView>();
			String filename = "/tmp/output-"+new Date().getTime()+".csv";
			FileSystemResource resource = new FileSystemResource(filename);
			
			BeanWrapperFieldExtractor<JemsView> extractor = new BeanWrapperFieldExtractor<JemsView>();
			extractor.setNames(getFields(r));		
			DelimitedLineAggregator<JemsView> lineAggregator = new DelimitedLineAggregator<JemsView>();
			lineAggregator.setDelimiter(",");
			lineAggregator.setFieldExtractor(extractor);
			
			itemWriter.setResource(resource);
			itemWriter.setLineAggregator(lineAggregator);			
			
			JdbcCursorItemReader<JemsView> itemReader = new JdbcCursorItemReader<JemsView>();
			itemReader.setSql(getSql(searchDate, startDate, endDate));
			itemReader.setRowMapper(new JemsViewRowMapper());
			itemReader.setDataSource(dataSource);
			
			log.info("Starting batch job...");	
			getJobLauncher().run(getJob(itemReader, itemWriter), new JobParameters());
			log.info("Finished batch job. CSV dumped to: "+filename);
			
			return filename;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			return "ERROR: "+e.getMessage();
		}
		
	}

	private String getSql(boolean searchDate, String startDate, String endDate) throws Exception{
		if(searchDate){			
			return String.format("SELECT * FROM jems_vw WHERE event_date >= '%s' AND event_date <= '%s' order by event_date", sdf2.format(sdf.parse(startDate)), sdf2.format(sdf.parse(endDate)));			
		}else{
			return String.format("SELECT * FROM jems_vw WHERE quotation_date >= '%s' AND quotation_date <= '%s' order by event_date", sdf2.format(sdf.parse(startDate)), sdf2.format(sdf.parse(endDate)));
		}
	}

	private SimpleJobLauncher jobLauncher = null;
	private JobRepository jobRepository = null;
	private JobBuilder jobs = null;
	private StepBuilder steps = null;
	private ResourcelessTransactionManager transactionManger = null;
	
	private ResourcelessTransactionManager getTransactionManager(){
		if(transactionManger == null){
			transactionManger = new ResourcelessTransactionManager();
		}
		return transactionManger;
	}
	
	private Job getJob(ItemReader itemReader, ItemWriter itemWriter) throws Exception{
		Step step = getStepsBuilder().chunk(1).reader(itemReader).writer(itemWriter).build();
		Job job = getJobBuilder().start(step).build();
		
		return job;
	}
	
	private JobBuilder getJobBuilder() throws Exception{
		return new JobBuilder("jobbuilder"+new Date().getTime()).repository(getJobRepository());
	}
	
	private StepBuilder getStepsBuilder() throws Exception{
		return new StepBuilder("stepbuilder"+new Date().getTime()).repository(getJobRepository()).transactionManager(getTransactionManager());
	}	
		
	private SimpleJobLauncher getJobLauncher() throws Exception{
		if(jobLauncher == null){
			jobLauncher = new SimpleJobLauncher();
		    jobLauncher.setJobRepository(getJobRepository());
		    jobLauncher.setTaskExecutor(new SyncTaskExecutor());
		}
		return jobLauncher;
	}
	
	private JobRepository getJobRepository() throws Exception{
		if(jobRepository == null){
			MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(getTransactionManager());
			jobRepository = jobRepositoryFactory.getObject();
		}
		
		return jobRepository;
	}
		
	private boolean checkOption(String s, HttpServletRequest request){
		return request.getParameter(s)!=null;
	}
	
	private String[] getFields(HttpServletRequest r){		
		List<String> result = new ArrayList<String>();
	
		if(checkOption("edate",r)){
			result.add("eventDate");
		}
		if(checkOption("enumber",r)){
			result.add("eventNumber");
		}
		if(checkOption("org",r)){
			result.add("organization");
		}
		if(checkOption("type",r)){
			result.add("eventType");
		}
		if(checkOption("status",r)){
			result.add("eventStatus");
		}
		if(checkOption("title",r)){
			result.add("eventTitle");
		}
		if(checkOption("description",r)){
			result.add("eventDescription");
		}
		if(checkOption("numfacilitators",r)){
			result.add("numberFacilitators");				
		}
		if(checkOption("numPeople",r)){
			result.add("numberPeople");
		}
		if(checkOption("companyname",r)){
			result.add("companyName");							
		}
		if(checkOption("contactperson",r)){
			result.add("companyContactPerson");
		}
		if(checkOption("contactnumber",r)){
			result.add("companyContactNumber");
		}
		if(checkOption("email",r)){
			result.add("companyEmail");
		}
		if(checkOption("currency",r)){
			result.add("currency");
		}
		if(checkOption("location",r)){
			result.add("location");
		}
		if(checkOption("gps",r)){
			result.add("gps");
		}
		if(checkOption("country",r)){
			result.add("country");
		}
		if(checkOption("Region",r)){
			result.add("region");
		}
		if(checkOption("numDrums",r)){
			result.add("numberDrums");
		}
		if(checkOption("numSessions",r)){
			result.add("numberSessions");
		}
		if(checkOption("chairs",r)){
			result.add("chairRequired");
		}
		if(checkOption("staff",r)){
			result.add("staffAssigned");
		}
		if(checkOption("receipt",r)){
			result.add("recieptVoucher");
		}			
		if(checkOption("qdate",r)){				
			result.add("quotationDate");
		}
		if(checkOption("qnum",r)){
			result.add("quotationNumber");
		}
		if(checkOption("idate",r)){
			result.add("invoiceDate");
		}
		if(checkOption("inum",r)){
			result.add("invoiceNumber");
		}
		if(checkOption("qamount",r)){
			result.add("quotedAmount");
		}
		if(checkOption("iamount",r)){
			result.add("invoicedAmount");
		}
		if(checkOption("paidstatus",r)){
			result.add("paidStatus");
		}
		if(checkOption("paidamount",r)){
			result.add("paidAmount");				
		}
		if(checkOption("paiddate",r)){
			result.add("paidDate");				
		}
		if(checkOption("elink",r)){
			result.add("eventLink");
		}
		if(checkOption("qlink",r)){
			result.add("quotationLink");
		}
		if(checkOption("ilink",r)){
			result.add("invoiceLink");
		}
		
		return result.toArray(new String[result.size()]);
	}		

}
