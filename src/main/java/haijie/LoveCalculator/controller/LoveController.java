package haijie.LoveCalculator.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import haijie.LoveCalculator.models.LoveProperties;
import haijie.LoveCalculator.service.LoveService;

@Controller
//@RequestMapping(path="result")
public class LoveController {
    
    @Autowired
    private LoveService loveSvc;

    @GetMapping(path = "/result")
    public String getLovePercent(@RequestParam(required = true) String fname,
    @RequestParam(required = true) String sname, Model model) throws IOException, InterruptedException{
        
        Optional<LoveProperties> love = loveSvc.getLovePercent(fname,sname);
        model.addAttribute("result",love.get()); //get the whole love
        return "result";
    }

    @GetMapping(path = "/list")
    public String getAllContact(Model model) {
        List<LoveProperties> result = loveSvc.findAll();
        model.addAttribute("loves", result);
        return "listLoves";
        
    }
}
