
//set class on any table cell containing a negative value
$( document ).ready( function(){
  $( "table.colourNegative td" ).each( function( i, e ){
    if( parseFloat( $( e ).text() ) < 0 ) {
      $( e ).addClass( "negative" );
    }
  });
});