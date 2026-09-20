# Open-source program readiness (checked 2026-09-20)

These are fit assessments, not applications or acceptance claims. Program terms can change; use the linked official pages before applying.

| Program | Status | Fit and next step |
| --- | --- | --- |
| [JetBrains Open Source](https://www.jetbrains.com/community/opensource/) | **WAIT FOR MORE MATURITY** for project support; **POSSIBLE / APPLY WITH REALISTIC EXPECTATIONS** for individual non-commercial IDE access | JetBrains distinguishes free individual non-commercial IDE use from All Products Pack support aimed at established projects with an active lifecycle and broad impact. This five-commit project has no verified release/device QA yet. Use individual access if eligible; revisit project support after sustained maintenance. |
| [BrowserStack Open Source](https://www.browserstack.com/open-source) | **POSSIBLE / APPLY WITH REALISTIC EXPECTATIONS** | Real Android device testing is directly useful for SMS permissions, OEM behavior and dual-SIM paths. Obtain a verified, safely signed test artifact and a synthetic-data test plan first. Program approval and SMS/SIM capabilities on hosted devices must be confirmed with BrowserStack. |
| [PVS-Studio Open Source](https://pvs-studio.com/en/order/open-source-license/) | **POSSIBLE / APPLY WITH REALISTIC EXPECTATIONS** | Java analysis is relevant. Their free open-source license is for qualifying personal, non-commercial, non-fork projects and may be renewed; maintainer must confirm eligibility and apply. It cannot replace Android lint or device QA. |
| [Snyk](https://snyk.io/plans/) | **POSSIBLE / APPLY WITH REALISTIC EXPECTATIONS** | Free SCA/SAST tiers exist. The app has no runtime third-party libraries today, so dependency findings may be limited; code analysis may still help. Compare with GitHub native security features before adding another service. |
| [GitHub security features](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/enabling-features-for-your-repository/managing-security-and-analysis-settings-for-your-repository) | **READY TO APPLY** after branch publication | Public repositories can use several security features. Enable Dependabot alerts, private vulnerability reporting, secret scanning/push protection where available, and code scanning in repository settings. The checked-in Dependabot file only schedules version checks; settings are a separate maintainer action. |
| General web-browser testing programs | **NOT APPLICABLE** as the primary QA route | This is a native Android SMS app, not a website. Android device and carrier behavior matter more than browser coverage. |

No program was applied to or accepted during this audit. Do not upload private SMS or real destination data to any testing service.
