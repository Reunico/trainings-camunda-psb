package com.reunico.bpm.delegate;

import com.reunico.bpm.constant.ErrorMessageConstants;
import com.reunico.bpm.constant.ProcessVariableConstants;
import com.reunico.bpm.domain.Customer;
import com.reunico.bpm.service.PublicService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class GetCustomer implements JavaDelegate {

    private final PublicService publicService;

    public GetCustomer(PublicService publicService) {
        this.publicService = publicService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
            Long customerNumber = (Long) delegateExecution.getVariable(ProcessVariableConstants.CUSTOMER_NUMBER);
            Customer customer = publicService.getCustomerById(customerNumber);
            if (customer == null) {
                throw new BpmnError(ErrorMessageConstants.GET_CUSTOMER_ERROR);
            } else {
                delegateExecution.setVariable(ProcessVariableConstants.CUSTOMER, customer);
            }
    }
}
