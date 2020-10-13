package com.slmnhrrn.coronavirustracker.controllers;

import com.slmnhrrn.coronavirustracker.models.LocationStats;
import com.slmnhrrn.coronavirustracker.services.CoronavirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronavirusDataService coronavirusDataService;

    @GetMapping("/")
    public String home(Model model){
        List<LocationStats> allStats = coronavirusDataService.getAllStats();
        int totalCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalCases);
        return "home";
    }

}
