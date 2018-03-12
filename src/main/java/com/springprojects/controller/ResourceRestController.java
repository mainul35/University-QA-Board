package com.springprojects.controller;

import com.springprojects.config.Properties;
import com.springprojects.config.Utils;
import com.springprojects.entity.Attachment;
import com.springprojects.entity.UserEntity;
import com.springprojects.service.AttachmentService;
import com.springprojects.service.UserService;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

@RestController
//@RequestMapping("/users")
public class ResourceRestController {

    @Autowired
	private Utils utils;
    @Autowired
    private AttachmentService attachmentService;

	@RequestMapping(value="/terms-and-conditions", method=RequestMethod.GET)
	@ResponseBody
	public String termsAndConditions_GET() {
		System.out.println("---------------------------------------------");
		String tndc = utils.readFile("/TermsAndConditions.txt");
		System.out.println(tndc);
		return tndc;
	}
	
	@RequestMapping(value="/file", method= {RequestMethod.GET,RequestMethod.POST} )
	@ResponseBody
	public byte[] resourceInDrive_GET(@RequestParam(name="fileId") Long fileId) {
		System.out.println("Reading resource from drive...");
		byte[] bs = null;
		try {
			bs = utils.readFile(fileId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bs;
	}
		
}
