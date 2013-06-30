$(function(){
	$( "#dialog-form" ).dialog({
		title: "Add Filter",
		autoOpen: false,
		height: 300,
		width: 350,
		resizable: false,
	    modal: true,
	    buttons: {
		  "Add Filter": function(){
			  $.post( 
		        "AddFilterAction",
		        $( "form[name='AddFilterForm']" ).serialize(),
		        function( data, status, jqXhr )
		        {
		        	location.reload();
		        })
		  },
		  "Cancel": function(){
			  $( "#dialog-form" ).dialog( "close" );
		  }
	  }
	});
	
	// filter specific stuff
	$( "#FilterType" ).change( function(){
		var filterType = $(this).val();
		
		// hide all filter fields then show only relevant field
		$( "#dialog-form :input" ).not( "#FilterType" ).prop( "disabled", true ).parent().hide();
		
		if( filterType == "GAME_TYPE" )
		{
			$( "#FilterGameType" ).prop( "disabled", false ).parent().show();
		}
		else if( filterType == "EXCLUDE_PLAYER" || filterType == "CONTAIN_PLAYER" )
		{
			$( "#FilterPlayer" ).prop( "disabled", false ).parent().show();
			$( "#FilterPlayer" ).autocomplete({ source: players, delay: 0 }); // TODO do we always need to set autocomplete?
		}
	});
	
});

function removeFilter( index )
{
  $.post( 
    "RemoveFilterAction",
    "i=" + index,
    function( data, status, jqXhr )
    {
    	location.reload();
    }
  );
}
