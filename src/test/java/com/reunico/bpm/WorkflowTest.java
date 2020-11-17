package com.reunico.bpm;

import com.reunico.bpm.constant.ProcessDefinitionConstants;
import com.reunico.bpm.constant.ProcessVariableConstants;
import com.reunico.bpm.domain.Customer;
import com.reunico.bpm.service.PublicService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.RequiredHistoryLevel;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

/*

  1. Бизнес-сервисы тестируются стандартно - JUnit (ЮТ), Wiremock (ИТ)
  2. Делегаты, DMN, листнеры - юнит-тестами + Camunda Java API
  3. Контекст процесса (вместе с делегатами, листнерами, EL)
      - camunda-bpm-assert (https://github.com/camunda/camunda-bpm-assert)
      - camunda-bpm-assert-scenario (https://github.com/camunda/camunda-bpm-assert-scenario/)
  4. Замокать бизнес-сервисы в делегатах можно с помощью Mockito (MockBean в Spring)
  5. Вывести красивую картинку с покрытием процесса тестами:
      - camunda-bpm-process-test-coverage (https://github.com/camunda/camunda-bpm-process-test-coverage/)
      - https://flowcov.io

 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Deployment(resources = {"process.bpmn", "proposal.dmn"})
public class WorkflowTest  {

  @Autowired
  ProcessEngine processEngine;

  @Autowired
  RuntimeService runtimeService;

  @MockBean
  PublicService publicService;

  String processDefinitionKey = ProcessDefinitionConstants.CUSTOMER_ONBOARDING;

  /* Нужно для работы Process Test Coverage */
  @Rule
  @ClassRule
  public static ProcessEngineRule rule;

  /* Нужно для работы Process Test Coverage */
  @PostConstruct
  void initRule() {
    rule = TestCoverageProcessEngineRuleBuilder.create(processEngine).build();
  }

  @Before
  public void setup() {
    /*
      Замокаем нужный метод publicService
     */
    Mockito.when(publicService.getCustomerById(123L)).thenReturn(getPoorCustomer());
    Mockito.when(publicService.getCustomerById(321L)).thenReturn(getRichCustomer());
    Mockito.when(publicService.getCustomerById(null)).thenReturn(null);
  }

  @Test
  /*
  при необходимости, можно задать нужный уровень истории
   */
  @RequiredHistoryLevel(ProcessEngineConfiguration.HISTORY_NONE)
  public void happyPathRichCustomer() {

    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,
            withVariables(ProcessVariableConstants.CUSTOMER_NUMBER, 123L));
    Assert.assertNotNull(processInstance);
    assertThat(processInstance).isStarted();
    // Для "бедных" клиентов сразу проверяем данные и делаем готовое предложение
    assertThat(processInstance).isWaitingAt("checkCustomerData");
    assertThat(processInstance).hasVariables(ProcessVariableConstants.CUSTOMER);
    assertThat(processInstance).hasVariables(ProcessVariableConstants.PROPOSAL);
    complete(task(processInstance));
    assertThat(processInstance).isWaitingAt("orderFulfillment");
    complete(externalTask(processInstance), withVariables(ProcessVariableConstants.IS_FAILED, false));
    assertThat(processInstance).isEnded();
  }

  @Test
  public void happyPathPoorCustomer() {

    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,
            withVariables(ProcessVariableConstants.CUSTOMER_NUMBER, 321L));
    Assert.assertNotNull(processInstance);
    assertThat(processInstance).isStarted();
    // Для "богатых" клиентов - персональное предложение
    assertThat(processInstance).isWaitingAt("choosePersonalProposal");
    assertThat(processInstance).hasVariables(ProcessVariableConstants.CUSTOMER);
    assertThat(processInstance).hasVariables(ProcessVariableConstants.IS_PERSONAL);
    complete(task(processInstance), withVariables(ProcessVariableConstants.PROPOSAL, "vodka"));
    assertThat(processInstance).isWaitingAt("checkCustomerData");
    complete(task(processInstance));
    assertThat(processInstance).isWaitingAt("orderFulfillment");
    complete(externalTask(processInstance), withVariables(ProcessVariableConstants.IS_FAILED, false));
    assertThat(processInstance).isEnded();
  }

  @Test
  public void handleError() {
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,
            withVariables(ProcessVariableConstants.CUSTOMER_NUMBER, null));
    assertThat(processInstance).isWaitingAt("handleError");
    complete(task(processInstance));
    assertThat(processInstance).isEnded();
  }

  @After
  public void shutdown() {
  }

  private Customer getRichCustomer() {
    return new Customer(new BigDecimal(300L), "RU");
  }
  private Customer getPoorCustomer() {
    return new Customer(new BigDecimal(30L), "RU");
  }
}
