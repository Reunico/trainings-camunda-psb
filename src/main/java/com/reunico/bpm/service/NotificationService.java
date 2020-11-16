package com.reunico.bpm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendMessage(String message) {
        logger.debug(message);
    }
}
