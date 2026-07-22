# EA Governance Hub

An open-source Java prototype for centralising enterprise architecture project governance, reviews, risks, technology standards, integrations and audit evidence.

## Why this project exists

Architecture information is often distributed across email, spreadsheets, Jira, ServiceNow, SharePoint, LeanIX and CMDB platforms. This project demonstrates a single governance hub that connects project-level decisions with enterprise-level standards and assurance.

The included sample use case is an **MPM Property Leasing CRM project on Microsoft Dynamics 365**. Additional projects can be created from the portfolio screen.

## Implemented

- Multi-project EA portfolio
- Project sponsors, owners, architects, dates, stage and overall risk
- Business problem and expected outcome
- Architecture review and approval authority
- Mandatory architecture information and evidence requirements
- SSO, security, hosting, data-residency and retention requirements
- Enterprise technology standards
- Solution, risk, review and integration registers
- Project assignment or enterprise-wide classification for register entries
- BCrypt-secured administrator login and CSRF protection
- Append-only application audit events
- H2 local persistence and PostgreSQL production profile
- Excel integration simulator for Jira, ServiceNow, SharePoint, LeanIX, CMDB and SSO
- Automated Spring context and edit-workflow tests

## Integration simulator

[`outputs/EA_Integration_Simulator.xlsx`](outputs/EA_Integration_Simulator.xlsx) provides mock records, endpoint configuration and field mappings. It allows integration contracts to be reviewed before access to real enterprise APIs is available.

The workbook is currently a standalone simulator. Upload/synchronisation and live API adapters are planned work; the application does not yet import the workbook automatically.

## Run locally

Requirements: Java 21 or newer and Maven 3.9 or newer.

```powershell
$env:EA_ADMIN_PASSWORD="choose-a-strong-local-password"
$env:PORT="8091"
mvn spring-boot:run
```

Open `http://localhost:8091` and sign in as `admin`.

Local data is stored in `work/` and is excluded from Git.

## Test

```powershell
$env:EA_ADMIN_PASSWORD="test-only-password"
mvn test
```

## Production considerations

This repository is a working MVP, not a production-certified banking platform. A production rollout should add:

- Microsoft Entra ID or another bank-approved OIDC provider
- Database migrations with Flyway
- Fine-grained role and maker-checker authorization
- Document storage and malware scanning
- Secret-manager integration
- Live Jira, ServiceNow, Microsoft Graph, LeanIX and CMDB adapters
- Backup, monitoring, vulnerability scanning and penetration testing
- Bank-approved retention, privacy and data-residency controls

## Technology

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Thymeleaf
- H2 for local development
- PostgreSQL driver for production

## License

MIT
