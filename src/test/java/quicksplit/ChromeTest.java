package quicksplit;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

public class ChromeTest
{
    @BeforeClass
    public static void init()
    {
        System.setProperty( "selenide.browser", "chrome" );
        System.setProperty( "webdriver.chrome.driver",
            "C:\\drivers\\chromedriver.exe" );
    }

    @Test
    public void seasonSummary()
    {
        open( "http://localhost:8080/quicksplit" );

        $( By.linkText( "Season Summary" ) ).click();
        $( By.linkText( "1" ) ).click();

        $( "h1" ).shouldHave( text( "Summary - Season 1" ) );

        SelenideElement row = $( "table.statTable tbody tr" );
        row.find( "td", 0 ).shouldHave( text( "Brad*" ) );
        row.find( "td", 2 ).shouldHave( text( "136" ) );
        row.find( "td", 3 ).shouldHave( text( "32.70" ) );
        row.find( "td", 4 ).shouldHave( text( "0.24" ) );

        $( By.linkText( "Overall" ) ).click();
        $( "h1" ).shouldHave( text( "Summary - Overall" ) );

        row = $( "table.statTable tbody tr" );
        row.find( "td", 0 ).shouldHave( text( "Mitch" ) );
        row.find( "td", 2 ).shouldHave( text( "1192" ) );
        row.find( "td", 3 ).shouldHave( text( "330.00" ) );
        row.find( "td", 4 ).shouldHave( text( "0.28" ) );
    }

    @Test
    public void resultTable()
    {
        open( "http://localhost:8080/quicksplit" );
        $( By.linkText( "Result Table" ) ).click();
        $( By.linkText( "1" ) ).click();

        $( "h1" ).shouldHave( text( "Results - Season 1" ) );
        $( "h4" ).shouldHave( text( "Thu, 22 Jul 2004 to Thu, 08 Dec 2005" ) );

        final SelenideElement headerRow = $( "table.statTable thead tr" );
        headerRow.shouldHave( text( "Ben Bill Brad*" ) );

        final SelenideElement bodyRow = $( "table.statTable tbody tr" );
        bodyRow.find( "td", 0 ).shouldHave( text( "Thu, 22 Jul 2004" ) );
        bodyRow.find( "td", 2 ).shouldHave( text( "-4.00" ) );
        bodyRow.find( "td", 3 ).shouldHave( text( "10.20" ) );
    }

    @Test
    public void accounts()
    {
        open( "http://localhost:8080/quicksplit" );
        $( By.linkText( "Accounts" ) ).click();

        $( "h3" ).shouldHave( text( "Accounts" ) );
        final SelenideElement row = $( "table tbody tr" );
        row.find( "td", 0 ).shouldHave( text( "Josh" ) );
        row.find( "td", 1 ).shouldHave( text( "87.90" ) );

        row.find( "td a" ).click();
        $( "h3" ).shouldHave( text( "Transaction History for Josh" ) );
        $( "table tbody tr" )
            .shouldHave( text( "18 Nov 2016" ) )
            .shouldHave( text( "Season #15" ) )
            .shouldHave( text( "43.20" ) )
            .shouldHave( text( "87.90" ) );

        $( "button.btn-primary" ).click();
        $( "#type" ).selectOption( "Deposit" );
        $( "#amount" ).setValue( "1.00" );
        $( "#description" ).setValue( "Automated test" );
        $( ".modal-footer button.btn-primary" ).click();
    }

}
