package com.inzent.deliverablesUI.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionHandlingController implements ErrorController {

	@RequestMapping(value = "/error")
	public ModelAndView handleError() {
		return new ModelAndView("error");
	}
}
