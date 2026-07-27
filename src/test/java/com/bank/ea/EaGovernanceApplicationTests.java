package com.bank.ea;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EaGovernanceApplicationTests {
  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate jdbc;

  @Test void contextLoads() {}

  @Test @WithMockUser(roles={"ADMIN","ARCHITECT"})
  void existingRecordCanBeOpenedAndUpdated() throws Exception {
    Long id = jdbc.queryForObject("select id from catalog_items where name = ?", Long.class, "MPM Leasing CRM");
    mvc.perform(get("/catalog/APPLICATION/{id}", id))
        .andExpect(status().isOk()).andExpect(view().name("form"));
    mvc.perform(post("/catalog/save").with(csrf())
        .param("id", id.toString()).param("type", "APPLICATION")
        .param("name", "MPM Leasing CRM").param("description", "Updated CRM scope")
        .param("owner", "MPM CRM Product Owner").param("status", "APPROVED")
        .param("criticality", "Critical").param("classification", "Confidential")
        .param("targetDate", "2026-12-15"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/catalog/APPLICATION"));
  }

  @Test @WithMockUser(roles={"ADMIN","ARCHITECT"})
  void riskCanCaptureGisdRejectionAndRemediation() throws Exception {
    Long id = jdbc.queryForObject("select id from catalog_items where name = ?", Long.class,
        "Excessive Dynamics privileges");
    mvc.perform(post("/catalog/save").with(csrf())
        .param("id", id.toString()).param("type", "RISK")
        .param("name", "Excessive Dynamics privileges").param("description", "Privileged access risk")
        .param("owner", "Information Security").param("status", "REJECTED")
        .param("criticality", "Critical").param("classification", "Restricted")
        .param("targetDate", "2026-09-15").param("reviewAuthority", "GISD")
        .param("decision", "REJECTED").param("decisionReason", "Segregation of duties is insufficient")
        .param("likelihood", "Likely").param("impact", "Severe")
        .param("remediationAction", "Redesign privileged roles").param("actionOwner", "CRM Security Lead")
        .param("actionDueDate", "2026-08-30").param("exceptionRequested", "true"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/catalog/RISK"));
    mvc.perform(get("/catalog/RISK/{id}", id)).andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Segregation of duties is insufficient")));
  }
}

