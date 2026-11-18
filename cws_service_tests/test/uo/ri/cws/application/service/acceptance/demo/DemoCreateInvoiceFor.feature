Feature: [I.C] Create one invoice for one or multiple jobs
  As a Cashier
  I want to create an invoice for several jobs done for a single client 
  Because I want to get paid

  Scenario: [demo.I.C.1] Create one invoice for an existing workorder
    Given [demo.I.C.1] a client registered with a classic vehicle and one finished workorder
    When [demo.I.C.1] I create an invoice for the workorders
    Then [demo.I.C.1] an invoice is created
    And [demo.I.C.1] the workorder is marked as INVOICED
