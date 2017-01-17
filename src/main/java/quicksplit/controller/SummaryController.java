package quicksplit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import quicksplit.dao.PlayerDao;
import quicksplit.dao.SeasonDao;
import quicksplit.model.PlayerModel;
import quicksplit.model.SeasonModel;
import quicksplit.service.PlayerStats;
import quicksplit.service.PlayerStatsService;

@Controller
public class SummaryController
{
    @Autowired SeasonDao seasonDao;
    @Autowired PlayerDao playerDao;
    @Autowired PlayerStatsService statsService;

    @GetMapping( value={ "/", "/summary" })
    public String getSummary( @RequestParam( value="season", required=false )
                                  final Long seasonId,
                              final Model model )
    {
        final SeasonModel season;
        if( seasonId == null ) {
            season = seasonDao.findLatestSeason();
        }
        else {
            season = seasonDao.findById( seasonId );
        }

        // generate stats for each player
        final List<PlayerStats> stats = new ArrayList<>();
        playerDao.listBySeason( season.getId() ).forEach( p -> {
            stats.add( statsService.generateStats( p.getId(), season.getId() ) );
        });

        model.addAttribute( "stats", stats )
            .addAttribute( "season", season )
            .addAttribute( "seasons", seasonDao.list() );

        return "summary";
    }

    @GetMapping( "/summary-overall" )
    public String getOverallSummary( final Model model )
    {
        // generate stats for each player
        final List<PlayerStats> stats = new ArrayList<>();
        playerDao.list().forEach( p -> {
            stats.add( statsService.generateStats( p.getId() ) );
        });

        final Map<PlayerModel,PlayerStats> statsMap = new HashMap<>();
        final List<PlayerModel> players = playerDao.list();
        for( final PlayerModel p : players ) {
            statsMap.put( p, statsService.generateStats( p.getId() ) );
        }

        model.addAttribute( "stats", stats )
            .addAttribute( "seasons", seasonDao.list() );

        return "summary-overall";
    }

}
