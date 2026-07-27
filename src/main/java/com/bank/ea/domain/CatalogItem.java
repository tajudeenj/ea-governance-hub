package com.bank.ea.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Entity @Table(name="catalog_items")
public class CatalogItem {
 public enum Type { APPLICATION, TECHNOLOGY, REVIEW, RISK, API }
 public enum Status { DRAFT, ACTIVE, UNDER_REVIEW, APPROVED, CONDITIONALLY_APPROVED, RETURNED_FOR_REWORK, RESTRICTED, DEPRECATED, REJECTED, EXCEPTION_GRANTED, CLOSED, RETIRED }
 public enum ReviewAuthority { EA, GISD, RISK, COMPLIANCE, JOINT_EA_GISD }
 public enum Decision { PENDING, APPROVED, CONDITIONALLY_APPROVED, REJECTED, RETURNED_FOR_REWORK, EXCEPTION_GRANTED }
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.EAGER) private Project project; @Transient private Long projectId;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Type type;
 @NotBlank @Column(nullable=false) private String name; @Column(length=2000) private String description;
 private String owner; @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status=Status.DRAFT;
 private String criticality; private String classification; private String targetDate;
 @Enumerated(EnumType.STRING) private ReviewAuthority reviewAuthority;
 @Enumerated(EnumType.STRING) private Decision decision;
 @Column(length=3000) private String decisionReason;
 private String likelihood; private String impact;
 @Column(length=3000) private String remediationAction;
 private String actionOwner; private String actionDueDate; private String evidenceLink; private String resubmissionDate;
 private Boolean exceptionRequested=false; private String exceptionApprover; private String exceptionExpiryDate;
 private String escalationAuthority; private String escalationStatus;
 @Column(length=2000) private String closureEvidence;
 @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
 @Column(nullable=false) private Instant updatedAt=Instant.now(); @PreUpdate void updated(){updatedAt=Instant.now();}
 public Long getId(){return id;} public void setId(Long v){id=v;} public Project getProject(){return project;} public void setProject(Project v){project=v;} public Long getProjectId(){return projectId!=null?projectId:(project==null?null:project.getId());} public void setProjectId(Long v){projectId=v;} public Type getType(){return type;} public void setType(Type v){type=v;} public String getName(){return name;} public void setName(String v){name=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;} public String getOwner(){return owner;} public void setOwner(String v){owner=v;} public Status getStatus(){return status;} public void setStatus(Status v){status=v;} public String getCriticality(){return criticality;} public void setCriticality(String v){criticality=v;} public String getClassification(){return classification;} public void setClassification(String v){classification=v;} public String getTargetDate(){return targetDate;} public void setTargetDate(String v){targetDate=v;}
 public ReviewAuthority getReviewAuthority(){return reviewAuthority;} public void setReviewAuthority(ReviewAuthority v){reviewAuthority=v;} public Decision getDecision(){return decision;} public void setDecision(Decision v){decision=v;} public String getDecisionReason(){return decisionReason;} public void setDecisionReason(String v){decisionReason=v;} public String getLikelihood(){return likelihood;} public void setLikelihood(String v){likelihood=v;} public String getImpact(){return impact;} public void setImpact(String v){impact=v;} public String getRemediationAction(){return remediationAction;} public void setRemediationAction(String v){remediationAction=v;} public String getActionOwner(){return actionOwner;} public void setActionOwner(String v){actionOwner=v;} public String getActionDueDate(){return actionDueDate;} public void setActionDueDate(String v){actionDueDate=v;} public String getEvidenceLink(){return evidenceLink;} public void setEvidenceLink(String v){evidenceLink=v;} public String getResubmissionDate(){return resubmissionDate;} public void setResubmissionDate(String v){resubmissionDate=v;} public Boolean getExceptionRequested(){return exceptionRequested;} public void setExceptionRequested(Boolean v){exceptionRequested=v;} public String getExceptionApprover(){return exceptionApprover;} public void setExceptionApprover(String v){exceptionApprover=v;} public String getExceptionExpiryDate(){return exceptionExpiryDate;} public void setExceptionExpiryDate(String v){exceptionExpiryDate=v;} public String getEscalationAuthority(){return escalationAuthority;} public void setEscalationAuthority(String v){escalationAuthority=v;} public String getEscalationStatus(){return escalationStatus;} public void setEscalationStatus(String v){escalationStatus=v;} public String getClosureEvidence(){return closureEvidence;} public void setClosureEvidence(String v){closureEvidence=v;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}

