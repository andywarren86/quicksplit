(function($){
  $.fn.setError = function( error ){
    if( error == null ){
      this.closest( ".form-group" ).removeClass( "has-error" )
        .find( "span.help-block" ).remove();
    }
    else {
      this.closest( ".form-group" ).addClass( "has-error" )
        .append( $( "<span>" ).addClass( "help-block" ).text( "* " + error ) );
    }
    return this;
  }
}(jQuery));
      
// set negative class on any table cell containing a negative value
$( document ).ready( function(){
  $( "table.colourNegative td, table.colour-negative td" ).each( function( i, e ){
    if( parseFloat( $( e ).text() ) < 0 ) {
      $( e ).addClass( "negative" );
    }
  });
});

// default jquery validation options for bootstrap forms
var bootstrapOptions = {
  highlight: function(element, errorClass) {
    $(element).closest( ".form-group" ).addClass( "has-error" );
  },
  unhighlight: function(element, errorClass) {
    $(element).closest( ".form-group" ).removeClass( "has-error" );
  },
  errorElement: "span",
  errorClass: "help-block",
  errorPlacement: function(error, element) {
    if(element.parent('.input-group').length) {
        error.insertAfter(element.parent());
    } else {
        error.insertAfter(element);
    }
  }
};