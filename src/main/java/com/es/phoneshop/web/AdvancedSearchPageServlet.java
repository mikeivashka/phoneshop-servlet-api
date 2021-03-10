package com.es.phoneshop.web;

import com.es.phoneshop.enumeration.SearchMode;
import com.es.phoneshop.model.product.AdvancedSearchCriteria;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductService;
import com.es.phoneshop.model.product.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.es.phoneshop.constant.RequestParameterConstants.*;
import static com.es.phoneshop.constant.UserMessageConstants.NOT_A_NUMBER_MESSAGE;

public class AdvancedSearchPageServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = ProductServiceImpl.getInstance();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        List<Product> searchResult = Collections.emptyList();
        AdvancedSearchCriteria criteria = extractSearchCriteriaFromRequest(req, errors);
        if (criteria != null && errors.isEmpty()) {
            searchResult = productService.findByAdvancedSearchCriteria(criteria);
        }
        req.setAttribute("products", searchResult);
        req.setAttribute("errors", errors);
        req.getRequestDispatcher("/WEB-INF/pages/advancedSearchPage.jsp").forward(req, resp);

    }

    private AdvancedSearchCriteria extractSearchCriteriaFromRequest(HttpServletRequest request, Map<String, String> errors) {
        String description = request.getParameter(DESCRIPTION_REQUEST_PARAMETER);
        String searchModeString = request.getParameter(SEARCH_MODE_REQUEST_PARAMETER);
        String minPriceString = request.getParameter(MIN_PRICE_REQUEST_PARAMETER);
        String maxPriceString = request.getParameter(MAX_PRICE_REQUEST_PARAMETER);
        if (description == null && searchModeString == null && minPriceString == null && maxPriceString == null) {
            return null;
        }
        SearchMode searchMode = null;
        if (searchModeString != null) {
            searchMode = SearchMode.valueOf(searchModeString.toUpperCase());
        }
        BigDecimal minPrice = extractPriceFromString(minPriceString, MIN_PRICE_REQUEST_PARAMETER, errors);
        BigDecimal maxPrice = extractPriceFromString(maxPriceString, MAX_PRICE_REQUEST_PARAMETER, errors);
        return new AdvancedSearchCriteria(minPrice, maxPrice, description, searchMode);
    }

    private BigDecimal extractPriceFromString(String priceString, String paramName, Map<String, String> errors) {
        BigDecimal price = null;
        if (priceString != null && !priceString.isEmpty()) {
            try {
                price = BigDecimal.valueOf(Double.parseDouble(priceString));
            } catch (NumberFormatException e) {
                errors.put(paramName, NOT_A_NUMBER_MESSAGE);
            }
        }
        return price;
    }
}
