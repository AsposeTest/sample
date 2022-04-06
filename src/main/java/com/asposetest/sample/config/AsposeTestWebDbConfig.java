package com.asposetest.sample.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * AsposeTestWebDbConfig is used for defining custom DB source configurations.
 * This is initialized from AsposeTestWebAppConfig createDispatcherServlet().
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
@Configuration
@ComponentScan("com.asposetest.sample.dao")
@ComponentScan("com.asposetest.sample.service")
@ComponentScan("com.asposetest.sample.config")
public class AsposeTestWebDbConfig {

}