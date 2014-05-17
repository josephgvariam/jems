/*global $:true, _:true, Backbone:true, jQuery:true, google:true, jemsRegions:true,jemsCountrys:true ,jemsOrganizations:true*/
/*jslint indent: 2 */

'use strict';

$(function () {
  $('#geocodebutton').click(function(){
    codeAddress();
  });

  $("#address").keypress(function(event) {
    if (event.which == 13) {
        event.preventDefault();
        codeAddress();
    }
  });

  $('#startdatetimepicker').datetimepicker({
    format: 'dd/MM/yyyy HH:mm PP',
    pickDate: true,
    pickTime: true,
    pick12HourFormat: true,
    pickSeconds: false,
    maskInput: true
  });

  $('#enddatetimepicker').datetimepicker({
    format: 'dd/MM/yyyy HH:mm PP',
    pickDate: true,
    pickTime: true,
    pick12HourFormat: true,
    pickSeconds: false,
    maskInput: true
  });

  $('#paiddatepicker').datetimepicker({
    format: 'dd/MM/yyyy HH:mm PP',
    pickDate: true,
    pickTime: false,
    pick12HourFormat: true,
    pickSeconds: false,
    maskInput: true
  });

  $('#staffAssigned2').multiselect({
    maxHeight: 200
  });

  $('#paidOKButton').click(function() {
    var amt = $('#paidAmount').val();
    if(amt && isNaN(amt)){
      $('#paidAmountControlGroup').addClass('error');
      $('#paidAmountHelpBlock').text('Invalid Amount');
      return false;
    }else{
      $('#paidAmountControlGroup').removeClass('error');
      $('#paidAmountHelpBlock').text('');
      return true;
    }
  });

  $('#organization').change(function() {
    var organizationId = parseInt($(this).val(), 10);
    var organizationsLength = jemsOrganizations.length;
    for(var i=0;i<organizationsLength;i++){
      if(jemsOrganizations[i].id===organizationId){
        $('#currency').val(jemsOrganizations[i].currency.id).attr('selected',true);
        $('#country').val(jemsOrganizations[i].country.id).attr('selected',true);
        $('#locationLatLong').val(jemsOrganizations[i].defaultGps);
        $('#country').trigger('change');
        break;
      }
    }
  });

  $('#country').change(function() {
    var countryId = parseInt($(this).val(), 10);
    var regionsLength = jemsRegions.length;
    var options = '';

    for(var i=0;i<regionsLength;i++){
      if(jemsRegions[i].country.id===countryId){
        options += '<option value="'+ jemsRegions[i].id + '">' + jemsRegions[i].name + '</option>';
      }
    }

    $('#region').find('option').remove().end().append($(options));
  });

  $('#mapButton').click(function() {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = 'http://maps.googleapis.com/maps/api/js?key=AIzaSyC5giGH20GTU1dEiGEGYj-780uVVoZitNk&sensor=false&callback=initializeMap';
    document.body.appendChild(script);
  });

  $('#recurringeventparams').click(function(){
    if($('#recurringeventparams').prop('checked')){
      $('#recurringEventModal').modal().css({
        width: 'auto',
        'margin-left': function () {
          return -($(this).width() / 2);
        }
      });
    }else{
      $('#recurringeventparams').val('');
      $('#recurringeventsummary').val('');
      $('#repeatEventSummaryLabel').html('');
    }
  });

  $('#recurringOKButton').click(function(){
    $('#recurringeventparams').val(getRRuleParams());
    var summary = updateRepeatSummary();
    $('#recurringeventsummary').val(summary);
    $('#repeatEventSummaryLabel').html(summary);
  });

  $('#recurringCancelButton').click(function(){

	  $('#recurringeventparams').val('');
	  $('#recurringeventsummary').val('');
	  $('#repeatEventSummaryLabel').html('');
	  $('#recurringeventparams').prop('checked',true)	  
	});
  
  
  
  $('#repeatFrequency').change(function(){
    updateRepeatSummary();
  });
  $('#repeatFrequencyValue').change(function(){
    updateRepeatSummary();
  });
  $('#chkbxMO').change(function() {
    updateRepeatSummary();
  });
  $('#chkbxTU').change(function() {
    updateRepeatSummary();
  });
  $('#chkbxWE').change(function() {
    updateRepeatSummary();
  });
  $('#chkbxTH').change(function() {
    updateRepeatSummary();
  });
  $('#chkbxFR').change(function() {
    updateRepeatSummary();
  });
  $('#chkbxSA').change(function() {
    updateRepeatSummary();
  });
  $('#chkbxSU').change(function() {
    updateRepeatSummary();
  });
  $('#repeatByMonthDayOfMonth').change(function() {
    updateRepeatSummary();
  });
  $('#repeatByMonthDayOfWeek').change(function() {
    updateRepeatSummary();
  });
  $('#endsOnAfterOccurrencesBtn').change(function() {
    updateRepeatSummary();
  });
  $('#endsAfterOccurrences').change(function() {
    updateRepeatSummary();
  });
  $('#endOnDateBtn').change(function() {
    updateRepeatSummary();
  });
  $('#endsOnDate').on('changeDate', function(e) {
    updateRepeatSummary();
  });

});

$('#recurringEventModal').on('shown', function () {
  $('#repeatOnWeekly').show();
  $('#repeatByMonthly').hide();

  $('#endsOnDate').datetimepicker({
    format: 'dd/MM/yyyy',
    pickDate: true,
    pickTime: false,
    pick12HourFormat: true,
    pickSeconds: false,
    maskInput: true
  });

  $('#startsOn').val($('#startDateTime').val());

  updateRepeatSummary();
});


var map;
var marker;
var coords;
var geocoder;

$('#mapModal').on('shown', function () {
  google.maps.event.trigger(map, 'resize');
  var coords = $('#locationLatLong').val().split(',');
  map.setCenter(new google.maps.LatLng(coords[0], coords[1]));
});

$('#mapModal').on('hide', function () {
  $('#locationLatLong').val(marker.getPosition().toUrlValue());
});

var initializeMap = function(){
  coords = $('#locationLatLong').val().split(',');

  var mapOptions = {
    zoom: 12,
    center: new google.maps.LatLng(coords[0], coords[1]),
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };

  map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);

  marker = new google.maps.Marker({
    position: new google.maps.LatLng(coords[0], coords[1]),
    map: map,
    title: 'Event',
    draggable: true
  });

  google.maps.event.addListener(map, 'click', function(e) {
    marker.setPosition(e.latLng);
  });

  geocoder = new google.maps.Geocoder();
};

var codeAddress = function() {
  var latlng = '';
  var address = '';
  var request = {};

  request.region = countryISOCode;

  var inputStr = document.getElementById('address').value;


  if(inputStr.match(/^(\-?\d+(\.\d+)?),\s*(\-?\d+(\.\d+)?)$/)){
    var latlngStr = inputStr.split(",",2);
    var lat = parseFloat(latlngStr[0]);
    var lng = parseFloat(latlngStr[1]);
    latlng = new google.maps.LatLng(lat, lng);
    request.latLng = latlng;
  }else{
    request.address = inputStr+', '+country;
  }




  //console.log(request);  
  geocoder.geocode( request, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
      map.setCenter(results[0].geometry.location);
      marker.setPosition(results[0].geometry.location);
    } else {
      console.log('Geocode was not successful for the following reason: ' + status);
    }
  });
}

var updateRepeatSummary = function(){
  //console.log('update '+new Date().getTime());
  var summary = '';
  var repeatFrequency = $('#repeatFrequency').val();
  var repeatFrequencyValue = $('#repeatFrequencyValue').val();

  if(repeatFrequency==='Daily'){
    $('#repeatFrequencyLabel').html(' Days');
    $('#repeatOnWeekly').hide();
    $('#repeatByMonthly').hide();

    if(repeatFrequencyValue==='1'){
      summary = 'Daily'+getEnds();
    }else{
      summary = 'Every '+repeatFrequencyValue+' days'+getEnds();
    }

  }else if(repeatFrequency==='Weekly'){
    $('#repeatFrequencyLabel').html(' Weeks');
    $('#repeatOnWeekly').show();
    $('#repeatByMonthly').hide();

    if(repeatFrequencyValue==='1'){
      summary = 'Weekly on '+getWeeklyOnDay()+getEnds();
    }else{
      summary = 'Every '+repeatFrequencyValue+' weeks on '+getWeeklyOnDay()+getEnds();
    }

  }else if(repeatFrequency==='Monthly'){
    $('#repeatFrequencyLabel').html(' Months');
    $('#repeatOnWeekly').hide();
    $('#repeatByMonthly').show();

    if(repeatFrequencyValue==='1'){
      summary = 'Monthly on '+getRepeatBy()+getEnds();
    }else{
      summary = 'Every '+repeatFrequencyValue+' months on '+getRepeatBy()+getEnds();
    }

  }else{
    $('#repeatFrequencyLabel').html(' Years');
    $('#repeatOnWeekly').hide();
    $('#repeatByMonthly').hide();

    if(repeatFrequencyValue==='1'){
      summary = 'Annually on '+getStartsOnDayOfTheYear()+getEnds();
    }else{
      summary = 'Every '+repeatFrequencyValue+' years on '+getStartsOnDayOfTheYear()+getEnds();
    }

  }

  $('#repeatSummary').html('<strong>Summary: </strong>'+summary);

  return summary;

};


var month=['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

var getRepeatBy = function(){
  if($('#repeatByMonthDayOfMonth').prop('checked')){
    return getStartsOnDayOfTheMonth();
  }else{
    return getStartsOnDayOfTheWeek();
  }
};

var getStartsOnDay = function(){
  //return "Sunday";
  return days[($('#startdatetimepicker').data('datetimepicker').getDate()).getDay()];
};

var getStartsOnDayOfTheMonth = function(){
  //return "day 17";
  return 'day ' + ($('#startdatetimepicker').data('datetimepicker').getDate()).getDate();
};

var getStartsOnByDay = function(){
  //return "2SA";
  var day = ($('#startdatetimepicker').data('datetimepicker').getDate()).getDate();
  var numb = '';
  if(day<8) {numb = '1';}
  else if(day<15) {numb = '2';}
  else if(day<22) {numb = '3';}
  else if(day<29) {numb = '4';}
  else {numb = '-1';}

  return numb+getStartsOnDay().substring(0, 2).toUpperCase();
};

var getStartsOnDayOfTheWeek = function(){
  var day = ($('#startdatetimepicker').data('datetimepicker').getDate()).getDate();
  var numb = '';
  if(day<8) {numb = 'first ';}
  else if(day<15) {numb = 'second ';}
  else if(day<22) {numb = 'third ';}
  else if(day<29) {numb = 'fourth ';}
  else {numb = 'last ';}

  return numb+getStartsOnDay();
};

var getStartsOnDayOfTheYear = function(){
  //return "November 17";
  var startDate = $('#startdatetimepicker').data('datetimepicker').getDate();
  return month[startDate.getMonth()]+' '+startDate.getDate();
};

var getEndsOnDay = function(){
  //return "25 Dec 2011";
  var endDate = $('#endsOnDate').data('datetimepicker').getDate();

  return endDate.getDate()+' '+month[endDate.getMonth()]+' '+endDate.getFullYear();
};

var getWeeklyByDay = function(){
  var mon = $('#chkbxMO').prop('checked');
  var tue = $('#chkbxTU').prop('checked');
  var wed = $('#chkbxWE').prop('checked');
  var thu = $('#chkbxTH').prop('checked');
  var fri = $('#chkbxFR').prop('checked');
  var sat = $('#chkbxSA').prop('checked');
  var sun = $('#chkbxSU').prop('checked');

  var s = '';

  if(!(mon || tue || wed || thu || fri || sat || sun)){
    return getStartsOnDay().substring(0, 2).toUpperCase();
  }

  if(mon) {s = 'MO#';}
  if(tue) {s = s + 'TU#';}
  if(wed) {s = s + 'WE#';}
  if(thu) {s = s + 'TH#';}
  if(fri) {s = s + 'FR#';}
  if(sat) {s = s + 'SA#';}
  if(sun) {s = s + 'SU#';}

  s = s.substring(0, s.length-1);
  return s;
};
  
var getWeeklyOnDay = function(){
  var mon = $('#chkbxMO').prop('checked');
  var tue = $('#chkbxTU').prop('checked');
  var wed = $('#chkbxWE').prop('checked');
  var thu = $('#chkbxTH').prop('checked');
  var fri = $('#chkbxFR').prop('checked');
  var sat = $('#chkbxSA').prop('checked');
  var sun = $('#chkbxSU').prop('checked');

  var s = '';

  if(mon && tue && wed && thu && fri && sat && sun){
    return 'all days';
  }

  if(!(mon || tue || wed || thu || fri || sat || sun)){
    return getStartsOnDay();
  }

  if(mon) {s = 'Monday, ';}
  if(tue) {s = s + 'Tuesday, ';}
  if(wed) {s = s + 'Wednesday, ';}
  if(thu) {s = s + 'Thursday, ';}
  if(fri) {s = s + 'Friday, ';}
  if(sat) {s = s + 'Saturday, ';}
  if(sun) {s = s + 'Sunday, ';}

  s = s.substring(0, s.length-2);

  return s;
};

var getEnds = function(){
  if($('#endsOnAfterOccurrencesBtn').prop('checked')){
    return ', '+$('#endsAfterOccurrences').val() + ' times';
  }else{
    return ', Until '+getEndsOnDay();
  }
};

var getRRuleParams = function(){
  var params = {};
  params.frequency = $('#repeatFrequency').prop('selectedIndex').toString();
  params.interval = $('#repeatFrequencyValue').val();

  var startDate = $('#startdatetimepicker').data('datetimepicker').getDate();

  params.startDate = startDate.getDate()+'/'+(startDate.getMonth()+1)+'/'+startDate.getFullYear();

  if($('#endsOnAfterOccurrencesBtn').prop('checked')){
    params.count = $('#endsAfterOccurrences').val();
  }else{
    var endDate = $('#endsOnDate').data('datetimepicker').getDate();
    params.untilDate = endDate.getDate()+'/'+(endDate.getMonth()+1)+'/'+endDate.getFullYear();
  }

  if(params.frequency==='1'){
    params.byDay = getWeeklyByDay();
  }else if(params.frequency==='2'){
    if($('#repeatByMonthDayOfMonth').prop('checked')){
      params.byMonth = startDate.getDate();
    }else{
      params.byDay = getStartsOnByDay();
    }
  }

  //console.log(JSON.stringify(params));
  return JSON.stringify(params);
};
