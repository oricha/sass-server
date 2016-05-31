package com.stack.sass;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SassServerTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	

	@Before
	public void before() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void testUpload() throws Exception {

	            FileInputStream fis = new FileInputStream("C:\\theme.scss");
	            MockMultipartFile multipartFile = new MockMultipartFile("file", fis);

	            HashMap<String, String> contentTypeParams = new HashMap<String, String>();
	            MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

	            this.mockMvc.perform(MockMvcRequestBuilders.post("/")
	                    .content(multipartFile.getBytes())
	                    .contentType(mediaType))
	                    .andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testHome() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk());
	}
}