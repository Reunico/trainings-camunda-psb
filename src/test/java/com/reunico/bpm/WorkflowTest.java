package com.reunico.bpm;

import com.reunico.bpm.constant.ProcessDefinitionConstants;
import com.reunico.bpm.constant.ProcessVariableConstants;
import com.reunico.bpm.delegate.GetCustomer;
import com.reunico.bpm.service.PublicService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.assertions.bpmn.ProcessDefinitionAssert;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WorkflowTest extends AbstractProcessEngineRuleTest {

  @Mock
  private PublicService publicService;

  @Autowired
  public RuntimeService runtimeService;

  @Before
  public void setup() {
    GetCustomer getCustomer = new GetCustomer();
    getCustomer.setPublicService(publicService);
    Mocks.register("getCustomer", getCustomer);
  }

  @Test
  public void shouldExecuteHappyPath() {
    // given
    String processDefinitionKey = ProcessDefinitionConstants.CUSTOMER_ONBOARDING;
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,
            withVariables(ProcessVariableConstants.CUSTOMER_NUMBER, Long.MAX_VALUE));
    assertThat(processInstance).isStarted();
    // when



    // then
    assertThat(processInstance).isStarted()
        .task()
        .hasDefinitionKey("Activity_1e68uzv");
  }

  @After
  public void shutdown() {
    Mocks.reset();
  }

}
