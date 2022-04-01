package vttp2022.ssf.assessment.videosearch.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import vttp2022.ssf.assessment.videosearch.models.Game;
import vttp2022.ssf.assessment.videosearch.service.SearchService;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService svc;

    @GetMapping
    public String search(@RequestParam(name="search", required=true) String searchName, @RequestParam(name="page_size", defaultValue = "10") Integer numberOfResult, Model model) {


        List<Game> last = svc.search(searchName, numberOfResult);
                        
        if (last.isEmpty()) {
            return "error";
        }

        model.addAttribute("list", last);

        return "new";
    }
}
