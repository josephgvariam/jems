/*global $:true, _:true, Backbone:true, jQuery:true, google:true, jemsRegions:true,jemsCountrys:true ,jemsOrganizations:true*/
/*jslint indent: 2 */

'use strict';

var dateRangePicker;
var type = 'Week';
var org = -1;

var quotaionInvoiceBarChart;
var eventTypePieChart;

function reDraw() {  
  if(quotaionInvoiceBarChart) quotaionInvoiceBarChart.destroy();
  if(eventTypePieChart) eventTypePieChart.destroy();

  $('#quotationsinvoices').html('');
  $('#eventtypes').html('');

  var startDate = dateRangePicker.data('daterangepicker').startDate.toString('dd-MM-yyyy');
  var endDate = dateRangePicker.data('daterangepicker').endDate.toString('dd-MM-yyyy');

  $('#loadingModal').modal('show');
  $.getJSON('/jemsevents/reports', {
      startDate: startDate,
      endDate: endDate,
      type: type,
      org: org
    },
    function(data){
      drawQuotationsInvoiceBarChart(data.quotation, data.invoice, data.ticks);

      if(data.eventTypes.length!=0){
        drawEventTypesPieChart(data.eventTypes);
      } 
      
      drawOrgsData(data.orgdata);

      $('#loadingModal').modal('hide');
    }
  );
}

function drawOrgsData(data){
	$('#orgsdatatable').remove();
	$('#orgsdata').append('<table id="orgsdatatable" class="table table-bordered"><thead><tr><th>Organization</th><th>#Quotations</th><th>#Invoices</th><th>Quotation Amount (AED)</th><th>Invoice Amount</th><th>Paid Amount</th><th>Outstanding</th><th>QI Ratio</th></tr></thead><tbody>');
	
    
	
	
	
	for(var i=0;i<data.length;i++){
		$('#orgsdatatable').append('<tr><td>'+data[i].orgName+'</td><td>'+data[i].numQuotations+'</td><td>'+data[i].numInvoices+'</td><td>'+data[i].quotationAmount+'</td><td>'+data[i].invoiceAmount+'</td><td>'+data[i].paidAmount+'</td><td>'+(data[i].invoiceAmount-data[i].paidAmount)+'</td><td>'+(data[i].numInvoices/data[i].numQuotations).toFixed(2)+'</td></tr>');
	}
	
	
	$('#orgsdata').append('</tbody></table>');
}

function drawEventTypesPieChart(data){
  eventTypePieChart = jQuery.jqplot ('eventtypes', [data], 
    {
      title: 'Event Types', 
      seriesDefaults: {
        renderer: jQuery.jqplot.PieRenderer, 
        rendererOptions: {
          showDataLabels: true
        }
      }, 
      legend: { show:true, location: 'e',rendererOptions: {numberColumns: 2} }
    }
  );  
}

function drawQuotationsInvoiceBarChart(s1, s2, ticks){
  var options = {
    title: 'Quotations & Invoices',
    seriesDefaults: {
      renderer:$.jqplot.BarRenderer,
      pointLabels: { show: true }
    },
    axes: {
      xaxis: {
        renderer: $.jqplot.CategoryAxisRenderer,
        ticks: ticks
      }
    },
    series:[
      {label:'Quotations'},
      {label:'Invoices'}
    ],
    legend: {
      show: true,
      location: 'se',
      placement: 'outside'
    },     
  };

  quotaionInvoiceBarChart = $.jqplot('quotationsinvoices', [s1,s2] ,options);
}

$(function () {
  $('#loadingModal').modal('show');
  dateRangePicker = $('#reportrange').daterangepicker({
      ranges: {
        'Last 7 Days': [Date.today().add({ days: -6 }), 'today'],
        'Last 30 Days': [Date.today().add({ days: -29 }), 'today'],
        'This Month': [Date.today().moveToFirstDayOfMonth(), Date.today().moveToLastDayOfMonth()],
        'Last Month': [Date.today().moveToFirstDayOfMonth().add({ months: -1 }), Date.today().moveToFirstDayOfMonth().add({ days: -1 })]
      },
      opens: 'left',
      format: 'dd/MM/yyyy',
      separator: ' to ',
      startDate: Date.today().add({ days: -29 }),
      endDate: Date.today(),
      minDate: '01/01/2012',
      maxDate: '31/12/2013',
      locale: {
        applyLabel: 'Submit',
        fromLabel: 'From',
        toLabel: 'To',
        customRangeLabel: 'Custom Range',
        daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr','Sa'],
        monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
        firstDay: 1
      },
      showWeekNumbers: true,
      buttonClasses: ['btn-danger'],
      dateLimit: false,
      showDropdowns: true
    }, 
    function(start, end) {
      $('#reportrange span').html(start.toString('MMMM d, yyyy') + ' - ' + end.toString('MMMM d, yyyy'));
      reDraw();
    }
  );

  //Set the initial state of the picker label
  $('#reportrange span').html(Date.today().add({ days: -29 }).toString('MMMM d, yyyy') + ' - ' + Date.today().toString('MMMM d, yyyy'));

  $('div.btn-group button').click(function (){
    type = this.innerHTML;
    reDraw();
  });

  $('#organization').change(function() {    
    org = parseInt($('#organization').val(), 10);
    reDraw();
  });

  $('#eventtypes').bind('jqplotDataMouseOver', 
    function (ev, seriesIndex, pointIndex, data) {
      $('#infoEventTypes').html(data[0]+" - "+data[1]);
    }
  );

  reDraw();

});
