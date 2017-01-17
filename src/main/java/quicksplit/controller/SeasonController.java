package quicksplit.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import quicksplit.dao.SeasonDao;

@Controller
public class SeasonController {

	@Autowired SeasonDao seasonDao;

    @InitBinder
    public void binder( final WebDataBinder binder ) {
        binder.addCustomFormatter( new DateFormatter( "yyyy-MM-dd" ) );
    }

	@GetMapping( "/seasons" )
	public String listSeasons( final Model model ){
        model.addAttribute( "seasons", seasonDao.list() );
        return "season";
	}

	@PostMapping( "/add-season" )
	public String createSeason( @RequestParam( "start-date" ) final Date startDate,
						        @RequestParam( "end-date" ) final Date endDate )
	{
		if( !startDate.before( endDate ) )
        {
            throw new IllegalArgumentException( "Invalid dates" );
        }

        seasonDao.insert( startDate, endDate );
        return "redirect:/seasons";
	}
}
