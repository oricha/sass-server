package com.stack.sass;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Output;

import com.stack.sass.SassCompiler;
import com.stack.sass.Application;

/**
 * 
 * @author kmuniz
 *
 */
@Controller
public class SassServer {

	private static final Logger LOG = LoggerFactory.getLogger(SassServer.class);

	SassCompiler sassCompiler = new SassCompiler();

	@RequestMapping(method = RequestMethod.POST, value = "/", produces = "application/json")
	public ResponseEntity sendFile(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes, Model model) {
		String name = multipartFile.getName();
		String result = "";
		if (!multipartFile.isEmpty()) {

			try {
				File file = new File(Application.ROOT + "/" + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
				FileCopyUtils.copy(multipartFile.getInputStream(), stream);
				stream.close();
				String outFile = file.getAbsolutePath().replaceFirst("\\.scss$", ".css");
				sassCompiler.initCompiler();
				Output out = sassCompiler.compileFile(file.getAbsolutePath(), outFile, outFile);
				result = out.getCss();

			} catch (FileNotFoundException e) {
				LOG.error("message", // TODO better Message
						"You failed to upload " + name + " => " + e.getMessage());
				result = e.getMessage();
			} catch (IOException e) {
				LOG.error("message", // TODO better Message
						"Failed with files " + name + " => " + e.getMessage());
				result = e.getMessage();
			} catch (CompilationException e) {
				LOG.error("message", // TODO better Message
						"Failed compiling sass " + name + " => " + e.getMessage());
				result = e.getMessage();
			}
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String provideUploadInfo(Model model) {
		return "uploadForm";
	}
}
