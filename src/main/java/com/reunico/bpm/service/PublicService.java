package com.reunico.bpm.service;

import com.reunico.bpm.domain.Customer;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpRequest;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.spin.Spin;
import org.camunda.spin.json.SpinJsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.connect.Connectors.getConnector;

@Service
public class PublicService {

    @Value("${url}")
    private String url;

    public Customer getCustomerById(Long number) {
        Customer customer = null;
        HttpConnector httpConnector = Connectors.getConnector(HttpConnector.ID);
        HttpRequest request = httpConnector.createRequest().url(url).get();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        request.setRequestParameter("headers", headers);
        HttpResponse response = request.execute();
        if (response.getStatusCode() == 200 && !response.getResponse().isEmpty()) {
            // SpinJsonNode node = Spin.JSON(response.getResponse());
            customer = Spin.JSON(response.getResponse()).mapTo(Customer.class);

        }
        return customer;
    }

}
