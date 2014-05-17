/*global $:true, _:true, Backbone:true, jQuery:true, google:true, jemsRegions:true,jemsCountrys:true ,jemsOrganizations:true*/
/*jslint indent: 2 */

'use strict';



$(function () {
  var cal = $('#calendar');
  var events = [];
  cal.width('100%');
  cal.height(window.innerHeight-40);

  $(window).resize(function() {
    cal.css('height', window.innerHeight-40);
  }); 

  //var selectedDate;
  var oldCalStartDate;

  cal.wijevcal({
    viewType: 'month',
    navigationBarVisible  :true,
    dataStorage: {
      addEvent: function(obj, successCallback, errorCallback) {
        //console.log('addEvent');
      },
      updateEvent: function(obj, successCallback, errorCallback) {
        //console.log('updateEvent');
      },
      deleteEvent: function(obj, successCallback, errorCallback) {
        //console.log('deleteEvent');
      },
      // loadEvents: function(visibleCalendars, successCallback, errorCallback) {
      //   console.log('loadEvents');
      //   var events = [];
      //   successCallback(events);
      // },
      loadEvents: _loadEvents,
      addCalendar: function(obj, successCallback, errorCallback) {
        //console.log('addCalendar');
      },
      updateCalendar: function(obj, successCallback, errorCallback) {
        //console.log('updateCalendar');
      },
      deleteCalendar: function(obj, successCallback, errorCallback) {
        //console.log('deleteCalendar');
      },
      loadCalendars: function(successCallback, errorCallback) {
        //console.log('loadCalendars');
      }
    },
    initialized: function (e) {
      //console.log('initialized');
    },
    selectedDatesChanged: function () {    
      var selectedDate = cal.wijevcal('option', 'selectedDate');
      console.log('selectedDatesChanged: '+selectedDate);

      var startDate = getCalStartDate(selectedDate);

      if (oldCalStartDate.getMonth() !== startDate.getMonth() || oldCalStartDate.getFullYear() !== startDate.getFullYear() || oldCalStartDate.getDate() !== startDate.getDate()) {        
      //if(oldCalStartDate!==startDate){
        console.log("--- calStartDate changed: "+startDate);
        console.log("oldCalStartDate: "+oldCalStartDate);

        oldCalStartDate=startDate;
      
        _loadEvents(null, function (events) {
          if (events) {
            //console.log('Setting eventsData...');
        	  //console.log(events);
            cal.wijevcal('option', 'eventsData', events);
            //console.debug(cal);
            cal.wijevcal('refresh');
          }
        });
      }
    },
    beforeEditEventDialogShow: function (e, args){
      //console.log(args);
      if(args.data.id){
        window.open('/jemsevents/'+args.data.id,'_newtab');
      }else{
    	  window.open('/jemsevents?form&startDateTime='+formatDate(args.data.start)+'&endDateTime='+formatDate(args.data.end),'_newtab');
      }
      return false;
    }
  });

  function formatDate(d){
    //dd/MM/yyyy hh:mm a
    var hh = d.getHours();
    var m = d.getMinutes();

    var dd = 'AM';
    var h = hh;
    if (h >= 12) {
      h = hh-12;
      dd = 'PM';
    }
    if (h === 0) {
      h = 12;
    }
    m = m<10?'0'+m:m;


    /* if you want 2 digit hours:*/
    h = h<10?'0'+h:h;

    var dateStr = d.getDate() +'/'+ (d.getMonth()+1) +'/'+ d.getFullYear() +' '+ h +':'+ m +' '+ dd;

    //console.log(d+'->'+dateStr);

    return dateStr;

  }

  function _loadEvents(visibleCalendars, successCallback, errorCallback) {     
    var selectedDate = cal.wijevcal('option', 'selectedDate');    
    console.log('_loadEvents: '+selectedDate);

    if(!oldCalStartDate){
      console.log('initializing oldCalStartDate...');
      oldCalStartDate = getCalStartDate(selectedDate);
    }

    var year = selectedDate.getFullYear();
    var month = selectedDate.getMonth()+1;
    var day = selectedDate.getDate();
    var dateStr = year + '-' + month + '-' + day;
    var url = '/jemsevents/calendar/events/'+dateStr;

    cal.wijevcal('showLoadingLabel', 'Loading Events');

    $.getJSON(url, function(data, status) {
      //console.log('Successfully loaded: '+url+' status: '+status);
      //console.log(data);

      events = [];
      var event, startDate, endDate, d;
      $.each(data, function(i,e){

        d = e.start.split('-');
        startDate = new Date(d[0], (d[1] - 1), d[2], d[3], d[4]);

        d = e.end.split('-');
        endDate = new Date(d[0], (d[1] - 1), d[2], d[3], d[4]);

        event = {
          id: e.id,
          subject: e.subject,
          description: e.description,
          color: e.color,
          start: startDate,
          end: endDate,
          allday: false,
          orgid: e.orgid
        };
        events.push(event);
      });
      successCallback(events);
      cal.wijevcal('hideLoadingLabel');
    }).error(function(jqXHR, status, errorThrown) {
      console.log('Error loading: '+url+' status: '+status);
      console.log("Error response: " + jqXHR.responseText);
    });
  }

function filterCalendar(){
  var status = $('#statusselect').val();
  var org = $('#orgsselect').val();
  
  if(!status){
    status = [];
  }
  if(!org){
    org = [];
  }

  console.log('selected orgs: '+org);

  var newEvents = [];
  for(var i=0;i<events.length;i++){
    if(status.indexOf(events[i].color) > -1 && org.indexOf(events[i].orgid)>-1){
      newEvents.push(events[i]);
    } 
  }
  
  cal.wijevcal('option', 'eventsData', newEvents);          
  cal.wijevcal('refresh');
}  

  
  $('#statusselect').multiselect({	  
	  buttonClass: 'btn btn-default btn-small',	  
	  onChange: function(element, checked) {		
		  filterCalendar();  
	  }
  });  

  var orgsdata_=[];
  var orgsvalues=[];
  for(var i=0;i<orgs.length;i++){
    var orgo = new Object();
    orgo.label = orgs[i].name;
    orgo.value = orgs[i].id;
    orgsdata_.push(orgo);
    orgsvalues.push(orgo.value);
  }

  $("#orgsselect").multiselect({buttonClass: 'btn btn-default btn-small'});
  $("#orgsselect").multiselect('dataprovider', orgsdata_);  
  $("#orgsselect").multiselect('select', orgsvalues);

  var options = {    
    onChange: function(element, checked) {     
      filterCalendar();
    }
  };
  $('#orgsselect').multiselect('setOptions', options);
  $("#orgsselect").multiselect('rebuild');

});






// function _toDayDate(dt) {
//   return new Date(dt.getFullYear(), dt.getMonth(), dt.getDate());
// }

function _addDays(dt, num) {
  return new Date(dt.getFullYear(), dt.getMonth(), dt.getDate() + num,
          dt.getHours(), dt.getMinutes(), dt.getSeconds(),
          dt.getMilliseconds());
}

// function checkcal(selectedDate){
//   var firstDayOfWeek = 0;
//   var startDate = new Date(selectedDate.getFullYear(), selectedDate.getMonth(), 1);
//   while (startDate.getDay() !== firstDayOfWeek) {
//     startDate = _addDays(startDate, -1);
//   }
//   var endDate = new Date(selectedDate.getFullYear(), selectedDate.getMonth(),
//           _daysInMonth(selectedDate.getMonth(),
//           selectedDate.getFullYear()));

//   var curDate = startDate;
//   var curColumnInd = 0;

//   while (curDate < endDate || curColumnInd < 7) {
//     if (curDate > endDate) {
//       endDate = curDate;
//     }
//     curColumnInd += 1;
//     curDate = _addDays(curDate, 1);

//     if (curColumnInd > 6) {
//       if (curDate > endDate) {
//         break;
//       }
//       curColumnInd = 0;
//     }
//   }


//   console.log('startDate: '+startDate);
//   console.log('endDate: '+endDate);
// }

function getCalStartDate(selectedDate){
      var firstDayOfWeek = 0;
      
      var startDate = new Date(selectedDate.getFullYear(),selectedDate.getMonth(), 1);
      while (startDate.getDay() !== firstDayOfWeek) {
        startDate = _addDays(startDate, -1);
      }
      console.log('CalStartDate: '+startDate);

      // var endDate = new Date(selectedDate.getFullYear(), selectedDate.getMonth(),_daysInMonth(selectedDate.getMonth(),selectedDate.getFullYear()));
      // while (endDate.getDay() !== firstDayOfWeek) {
      //   endDate = _addDays(endDate, 1);
      // }
      // endDate = _addDays(endDate, -1);      
      //console.log(endDate);

      return startDate;
}

function _daysInMonth (month, year) {
  var dd = new Date(year, month + 1, 0);
  return dd.getDate();
}