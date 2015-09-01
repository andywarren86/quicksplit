<%@ tag description="Page head element" pageEncoding="UTF-8" %>

  <%@ attribute name="title" required="true" %>
  
   <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <title>${title}</title>
    
    <!-- jQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!--<script src="js/jquery-1.5.1.min.js"></script>-->
    <script src="js/jquery-ui-1.8.14.custom.min.js"></script>
    <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.14.custom.css" rel="stylesheet" />
 
    <!-- Bootstrap -->
    <script src="js/bootstrap.js"></script>
    <link type="text/css" href="css/bootstrap.css" rel="stylesheet">
    <link type="text/css" href="css/bootstrap-theme.css" rel="stylesheet">
    
    <!-- Quicksplit -->
    <script src="js/quicksplit.js"></script>
    <link type="text/css" href="css/quicksplit.css" rel="stylesheet"/>
      
    <!-- Custom Page Content -->
    <jsp:doBody/>
    
  </head>