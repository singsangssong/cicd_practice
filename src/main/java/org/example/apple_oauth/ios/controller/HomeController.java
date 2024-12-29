package org.example.apple_oauth.ios.controller;

import lombok.RequiredArgsConstructor;
import org.example.apple_oauth.ios.service.AppleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final AppleService appleService;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("appleUrl", appleService.getAppleLogin());

        return "index";
    }
}
