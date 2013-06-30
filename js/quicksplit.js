// zebrafy the table rows
function zebrafyTable( table )
{
  $( "tbody tr:even", table ).removeClass( "odd" ).addClass( "even" );
  $( "tbody tr:odd", table ).removeClass( "even" ).addClass( "odd" );
}

//set class on any table cell containing a negative value
function colourNegativeCells( table )
{
	$( "td", table ).each( function( i, e ){
		if( parseFloat( $( e ).text() ) < 0 )
		{
			$( e ).addClass( "negative" );
		}
	});
}

$(function(){
	// create the menu
	$( "#menu" ).menu();
	$( "#menu ul" ).css( "z-index", 100 );
	$( "#menu-div" ).show();
});