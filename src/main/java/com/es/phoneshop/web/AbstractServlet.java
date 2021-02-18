package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;

import javax.servlet.http.HttpServlet;
import java.text.ParseException;
import java.util.NoSuchElementException;

import static com.es.phoneshop.constant.RequestParameterConstants.ERROR_MESSAGE_REQUEST_PARAMETER;
import static com.es.phoneshop.constant.UserMessageConstants.NOT_A_NUMBER_MESSAGE;
import static com.es.phoneshop.constant.UserMessageConstants.UNKNOWN_ERROR_MESSAGE;

public abstract class AbstractServlet extends HttpServlet {
    protected String handleDefaultCartModificationException(Exception e) {
        StringBuilder paramToAdd = new StringBuilder(ERROR_MESSAGE_REQUEST_PARAMETER + "=");
        if (e.getClass() == OutOfStockException.class) {
            paramToAdd.append(e.getMessage());
        } else if (e.getClass() == ParseException.class) {
            paramToAdd.append(NOT_A_NUMBER_MESSAGE);
        } else if (e.getClass() == NoSuchElementException.class){
            paramToAdd.append(UNKNOWN_ERROR_MESSAGE);
        }
        return paramToAdd.toString();
    }
}
