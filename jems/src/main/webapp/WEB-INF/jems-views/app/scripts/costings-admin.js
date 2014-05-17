/*global $:true, _:true, Backbone:true, jQuery:true, google:true, jemsRegions:true,jemsCountrys:true ,jemsOrganizations:true, costingCategories:true, costingSubCategories:true,kendo:true*/
/*jslint indent: 2 */

'use strict';

function detailInit(e) {
  $('<div />').appendTo(e.detailCell).kendoGrid({
    dataSource: {
      batch: false,
      autoSync: true,
      transport: {
        read:   {
          url: '/jemscostings/subcategorys/'+e.data.id,
          type: 'POST'
        },
        update: {
          url: '/jemscostings/subcategorys/update',
          type: 'POST'
        },
        destroy: {
          url: '/jemscostings/subcategorys/destroy',
          type: 'POST'
        },
        create: {
          url: '/jemscostings/subcategorys/create',
          type: 'POST'
        }
      },
      schema: {
        model: {
          id: 'id',
          fields: {
            name: {
              type: 'string',
              validation: {
                required: true
              }
            },
            rate: {
              type: 'number',
              validation: {
                required: true,
                min: 1
              }
            },
            category: {
              type: 'string',
              defaultValue: e.data.id
            }
          }
        }
      }
    },
    columns: [
      { field: 'name', title: 'Sub Category' },
      { field: 'rate', title: 'Rate'},
      { command: 'destroy' }
    ],
    toolbar: ['create'],
    editable: true,
    navigatable: true
  });
}

function createGrid(currentOrganization){
  $('#grid').kendoGrid({
    dataSource: {
      batch: false,
      autoSync: true,
      transport: {
        read:   {
          url: function(){
            return '/jemscostings/categorys/'+currentOrganization;
          },
          type: 'POST'
        },
        update: {
          url: '/jemscostings/categorys/update',
          type: 'POST'
        },
        destroy: {
          url: '/jemscostings/categorys/destroy',
          type: 'POST'
        },
        create: {
          url: '/jemscostings/categorys/create',
          type: 'POST'
        }
      },
      schema: {
        model: {
          id: 'id',
          fields: {
            name: {
              type: 'string',
              validation: {
                required: true
              }
            },
            organization: {
              type: 'number',
              defaultValue: currentOrganization
            }
          }
        }
      }
    },
    columns: [
      { field: 'name', title: 'Category'},
      { command: 'destroy' }
    ],
    toolbar: ['create'],
    editable: true,
    navigatable: true,
    autoSync: true,
    detailInit: detailInit
  });
}

$(function () {

  createGrid($('#organization').val());

  $('#organization').change(function() {
    $('#grid').html('');
    createGrid($('#organization').val());
  });


  //var grid = $('#grid').data('kendoGrid');
  // grid.bind('save', function(e) {
  //   console.log('save: '+JSON.stringify(e.model));
  // });
  // grid.bind('saveChanges', function(e) {
  //   console.log('saveChanges: ');
  //   console.log(grid.dataSource.view().toJSON());
  // });


});