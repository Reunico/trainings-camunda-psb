package com.reunico.bpm.dmn;

import com.reunico.bpm.constant.DmnDefinitionConstants;
import com.reunico.bpm.constant.DmnResultConstants;
import com.reunico.bpm.constant.ProcessVariableConstants;
import com.reunico.bpm.domain.Customer;
import org.apache.tomcat.jni.Proc;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.ProcessEngine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProposalTest {

    @Autowired
    ProcessEngine processEngine;

    @Test
    public void testPersonalProposalTrue() {
        Customer customer = new Customer();
        customer.setAmount(new BigDecimal(300));
        customer.setCountryCode(Locale.IsoCountryCode.PART1_ALPHA2.name());
        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcessVariableConstants.CUSTOMER, customer);
        DmnDecisionTableResult result =
                processEngine
                        .getDecisionService().evaluateDecisionTableByKey(DmnDefinitionConstants.PROPOSAL, variables);
        Map<String, Object> dmnResult = result.getSingleResult();
        Assert.assertTrue((Boolean) dmnResult.get(DmnResultConstants.IS_PERSONAL));
    }

    @Test
    public void testPersonalProposalFalse() {
        Customer customer = new Customer();
        customer.setAmount(new BigDecimal(123));
        customer.setCountryCode(Locale.IsoCountryCode.PART1_ALPHA2.name());
        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcessVariableConstants.CUSTOMER, customer);
        DmnDecisionTableResult result =
                processEngine
                        .getDecisionService().evaluateDecisionTableByKey(DmnDefinitionConstants.PROPOSAL, variables);
        Map<String, Object> dmnResult = result.getSingleResult();
        Assert.assertFalse((Boolean) dmnResult.get(DmnResultConstants.IS_PERSONAL));

    }
}
