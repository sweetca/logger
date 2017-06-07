package com.logger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/static/")
public class RedirectController {

    @GetMapping("*/*")
    public ModelAndView redirectWithUsingRedirectPrefix(ModelMap model, HttpServletRequest request) {
        return new ModelAndView(
                "redirect:" + request.getRequestURI()
                .replace("/static",""),
                model
        );
    }
}
