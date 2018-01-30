package com.wal.monkeys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wal.monkeys.model.User;
import com.wal.monkeys.processor.KafkaaConsumer;
import com.wal.monkeys.processor.KafkaaProducer;




@Controller
public class RegistrationController {
	


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView showRegister(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("register");
		mav.addObject("user", new User());
		return mav;
	}
	
	@RequestMapping(value = "/registerProcess", method = RequestMethod.POST)
	public ModelAndView addUser(HttpServletRequest request, HttpServletResponse response, 
			@ModelAttribute("user") User user) {
		
		System.out.println("User Object:" +user.toString());
		
		KafkaaProducer producer = new KafkaaProducer();
		producer.publishMessage(user);
		
		KafkaaConsumer consumer = new KafkaaConsumer();
		consumer.logstshToElastic();
		
		
		return new ModelAndView("registered");
	}
	

}

