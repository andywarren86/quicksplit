package quicksplit.controller;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import quicksplit.controller.forms.AddResultForm;
import quicksplit.controller.forms.AddResultForm.PlayerResult;
import quicksplit.dao.GameDao;
import quicksplit.dao.PlayerDao;
import quicksplit.dao.ResultDao;
import quicksplit.dao.SeasonDao;
import quicksplit.model.GameModel;
import quicksplit.model.PlayerModel;
import quicksplit.model.SeasonModel;

@Controller
public class ResultController
{
    @Autowired PlayerDao playerDao;
    @Autowired SeasonDao seasonDao;
    @Autowired ResultDao resultDao;
    @Autowired GameDao gameDao;

    @ModelAttribute
    public void appendPlayerList( final Model model ) {
        model.addAttribute( "Players", playerDao.list() );
    }

    @InitBinder
    public void binder( final WebDataBinder binder ) {
        binder.addCustomFormatter( new DateFormatter( "yyyy-MM-dd" ), "gameDate" );
    }

    @GetMapping( "/results" )
    public String viewResults( @RequestParam( value="season", required=false ) final Long seasonId,
                               final ModelMap model )
    {
        if( seasonId == null ) {
            return "redirect:/results?season=" + seasonDao.findLatestSeason().getId();
        }

        final SeasonModel season = seasonDao.findById( seasonId );
        final List<PlayerModel> players =
            playerDao.listBySeason( season.getId() );
        final Map<GameModel, List<Long>> resultTable =
            resultDao.generateResultTable( season.getId() );

        model.addAttribute( "playerList", players )
            .addAttribute( "resultMap", resultTable )
            .addAttribute( "season", season )
            .addAttribute( "seasons", seasonDao.list() );

        return "results";
    }

    @GetMapping( "/add-result" )
    public String addResultForm( @ModelAttribute final AddResultForm form,
                                 @RequestParam( name="NewGameId", required=false )
                                     final Long newGameId,
                                 final Model model )
    {
        // set defaults
        form.setGameDate( new Date() );
        form.addResult( null, null );

        if( newGameId != null )
        {
            model.addAttribute( "NewGame", gameDao.findById( newGameId ) );
        }
        return "add-result";
    }

    @PostMapping( "/add-result" )
    public String addResultAction( @ModelAttribute final AddResultForm form,
                                   final BindingResult bindingResult )
    {
        if( bindingResult.hasErrors() ) {
            return "add-result";
        }

        // validation
        final List<PlayerResult> validResults =
            form.getResults().stream()
                .filter( r -> r.getPlayerId() != null && r.getAmount() != null )
                .collect( toList() );
        if( validResults.size() < 2 )
        {
            throw new IllegalArgumentException( "Game must have at least two results" );
        }

        final Set<Long> uniquePlayers =
            validResults.stream().map( PlayerResult::getPlayerId ).collect( toSet() );
        if( validResults.size() != uniquePlayers.size() )
        {
            throw new IllegalArgumentException( "Players must be unique" );
        }

        final BigDecimal sum = validResults.stream()
            .map( PlayerResult::getAmount )
            .reduce( BigDecimal::add ).get();
        if( sum.compareTo( BigDecimal.ZERO ) != 0 ) {
            throw new IllegalArgumentException( "Sum must equal zero" );
        }

        final SeasonModel season = seasonDao.findByDate( form.getGameDate() );
        if( season == null ) {
            throw new IllegalStateException( "No season for the selected date" );
        }

        final long gameId = gameDao.insert( season.getId(), form.getGameDate() );
        for( final PlayerResult result : form.getResults() )
        {
            if( result.getPlayerId() == null || result.getAmount() == null ){
                continue;
            }

            final long playerId = result.getPlayerId();
            final long amount = result.getAmount()
                .multiply( BigDecimal.valueOf( 100 ) )
                .longValue();
            resultDao.insert( playerId, gameId, amount );
        }
        return "redirect:add-result?NewGameId=" + gameId;
    }

}
