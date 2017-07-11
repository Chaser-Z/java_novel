package com.test.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/manage")
public class ManageController {
	
	private static final Logger log = Logger.getLogger(ManageController.class);

	public static final String SESSION_ID = "hytc.bss.0814152001";// SESSID

	
	
}
