


$(function(){
	
	/*Bei allen tooltip titeln erzeuge ein tooltip*/
	$('[data-toggle="tooltip"]').tooltip();
	
   
    /*Änderung des Textes*/
    $(".neuesTicketSpalte").hover(
    	function(){
    		$(".textNeuesTicket").text("Erstelle ein neues Ticket");
    	},
        function() {
    		$(".textNeuesTicket").text("Neues Ticket");
        }
    );
    
    
    /*Einfache Alert Ausgabe*/
    $(".neuesTicketSpalte").click(function(){
		alert('Ok lets go!');
	});
    
});


/* Bei klick auf 'Dein Profil */
function openNav() {
    document.getElementById("myNav").style.width = "100%";
}

/* Bei Klick auf den 'Close' Button in 'Dein Profil' */
function closeNav() {
    document.getElementById("myNav").style.width = "0%";
} 