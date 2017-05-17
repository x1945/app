/*
 * Copyright 2002-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sys.system;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import sys.util.StringUtil;

/**
 * This is the custom JAXB JSON ObjectMapper
 */
public class JaxbJacksonObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private final Logger LOG = LoggerFactory.getLogger(JaxbJacksonObjectMapper.class);

	public JaxbJacksonObjectMapper() {

		SimpleModule simpleModule = new SimpleModule();

		/**
		 * String
		 **/
		simpleModule.addSerializer(String.class, new JsonSerializer<String>() {

			@Override
			public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider provider)
					throws IOException, JsonProcessingException {
				jsonGenerator.writeString(StringUtil.trim(value));
			}
		});

		/**
		 * Date
		 **/
		simpleModule.addSerializer(Date.class, new JsonSerializer<Date>() {
			@Override
			public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider provider)
					throws IOException, JsonProcessingException {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				jsonGenerator.writeString(sdf.format(value));
			}
		});

		// this.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		/**
		 * 處理空值
		 */
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {

			@Override
			public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider)
					throws IOException, JsonProcessingException {
				jsonGenerator.writeString("");
			}
		});

	}

}
