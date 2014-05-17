/*global $:true, _:true, Backbone:true, jQuery:true, google:true, costings:true, jemsRegions:true,jemsCountrys:true ,jemsOrganizations:true, costingCategories:true, costingSubCategories:true,kendo:true*/
/*jslint indent: 2 */

'use strict';

function detailInit(e) {
  $('<div class="catuhgury" />').appendTo(e.detailCell).kendoGrid({
    dataSource: {
      data: getSubCategories(e.data.name),
      schema: {
        model: {
          id: 'id',
          fields: {
            subCategory: {
              type: 'string',
              editable: false
            },
            quantity: {
              type: 'number',
              validation: {
                required: true,
                min: 0
              }
            },
            rate: {
              type: 'number',
              editable: false
            },
            amount: {
              type: 'number'              
            }
          }
        }
      }
    },
    columns: [
      { field: 'subCategory', title: 'Sub Category' },
      { field: 'rate', title: 'Rate'},
      { field: 'quantity', title: 'Quantity'},
      { field: 'amount', title: "Amount", editor: function(cont, options) {
            $("<span>" + options.model.amount + "</span>").appendTo(cont);
        }}
    ],
    editable: true,
    navigatable: true,
    save: function(data) {
      if (data.values.quantity) {
        var test = data.model.set('amount', data.values.quantity * data.model.rate);
      }
      else {
        var test = data.model.set('amount', data.model.quantity * data.values.rate);
      }
      for(var i=0;i<costings.length;i++){
        if(costings[i].category===e.data.name && costings[i].subCategory===data.model.subCategory){
          costings[i].amount = data.model.amount ;
          if (data.values.quantity) {
            costings[i].quantity = data.values.quantity; 
          }else{
            costings[i].quantity = data.model.quantity;
          }
        }
      }
      updateTotal();
    }
  });
}

function updateTotal(){
  // var categorys = $('.catuhgury');
  // console.log(categorys);
  // for(var i=0;i<categorys.length;i++){    
  //   var grid = $(categorys[i]).closest(".k-grid").data("kendoGrid");
  //   console.log(grid.dataSource);
  // }

  var total = 0;
  for(var i=0;i<costings.length;i++){
    total += costings[i].rate * costings[i].quantity;
  }

  $('#amount').val(total);
  updateto();
}

function getCategories(){
  var tmp = [];
  var tmpCategories = [];

  $.each(costings, function(i, el){
    if($.inArray(el.category, tmp) === -1) {
      tmp.push(el.category);
      var c = {};
      c.name=el.category;
      tmpCategories.push(c);
    }
  });

  return tmpCategories;
}

function getSubCategories(c){
  var tmp = [];

  $.each(costings, function(i, el){
    if(el.category===c){
      el.amount=el.rate * el.quantity;
      tmp.push(el);
    }
  });

  return tmp;
}

function createGrid(){
  $('#grid').kendoGrid({
    dataSource: {
      data: getCategories(),
      schema: {
        model: {
          fields: {
            name: {
              type: 'string'
            }
          }
        }
      }
    },
    columns: [
      { field: 'name', title: 'Category'}
    ],
    editable: false,
    navigatable: true,
    autoSync: false,
    detailInit: detailInit
  });
}

function updateto(){
	var cost = $('#amount').val()
	var markup = $('#markup').val()
	
	var total = cost * (1 + (markup/100));
	$('#total').val(total.toFixed(2))
}

$(function () {
  createGrid();
  updateTotal();

  $('#saveCostingsButton').click(function(){
    //console.log(costings);
    $.ajax({
      type: 'POST',
      url: '/jemsevents/'+jemsEventId+'/costings',
      processData: false,
      contentType: 'application/json',
      data: JSON.stringify(costings),
      success: function(r) {
        //console.log(r);
        //$('#costingmsg').html('<div class="alert alert-success"><button type="button" class="close" data-dismiss="alert">&times;</button>Costings Saved</div>')
        var refreshUrl = window.location.href;
        if(refreshUrl.indexOf('?msg=1')===-1){
          window.location.replace(window.location.href+'?msg=1');          
        }else{
          window.location.replace(window.location.href);
        }      
        
      }
    });
  });

  $( "#markup" ).change(function() {
	  updateto();
	});

});