package com.pdfgenerator.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Controller
public class pdfGenerateController {
	
	 @Autowired
	    ServletContext servletContext;
	 
	   private final TemplateEngine templateEngine;
	   
	   public pdfGenerateController(TemplateEngine templateEngine) {
	        this.templateEngine = templateEngine;
	    }

	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String gereratePdf (){
		
		return "index";
	}

	  @RequestMapping(path = "/pdf")
	public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {



	    /* Create HTML using Thymeleaf template Engine */

	    WebContext context = new WebContext(request, response, servletContext);
	    String indexHtml = templateEngine.process("index", context);

	    /* Setup Source and target I/O streams */

	    ByteArrayOutputStream target = new ByteArrayOutputStream();

	    /*Setup converter properties. */
	    ConverterProperties converterProperties = new ConverterProperties();
	    converterProperties.setBaseUri("http://localhost:8080");

	    /* Call convert method */
	    HtmlConverter.convertToPdf(indexHtml, target, converterProperties);  

	    /* extract output as bytes */
	    byte[] bytes = target.toByteArray();


	    /* Send the response as downloadable PDF */

	    return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(bytes);

	}
}
