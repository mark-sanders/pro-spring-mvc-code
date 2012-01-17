package com.apress.prospringmvc.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.web.context.support.ServletRequestHandledEvent;

/**
 * {@link ApplicationListener} which reacts on {@link ServletRequestHandledEvent} and simply logs the information. 
 * 
 * @author Marten Deinum
 *
 */
public class RequestHandledEventListener implements ApplicationListener<ServletRequestHandledEvent> {

    private final Logger logger = LoggerFactory.getLogger(RequestHandledEventListener.class);

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        this.logger.info("{}", event);
    }

}