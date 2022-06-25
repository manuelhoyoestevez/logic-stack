@ignore
Feature: Create non existing group
  Background:
    # Javascript helpers
    * def groupObject = function(groupCode, groupDescription) { var ret = {}; ret[code] = groupCode; ret[description] = groupDescription; return ret; }

  Scenario: Create non existing group
    # Get non existing group
    Given url urlApi
    And path '/group/' + groupCode
    And header Content-Type = 'application/json'
    And header Authorization = jwt
    When method get
    Then status 404
    And match response == { code: 404, title: 'Not found', description: '' }

    # Create non existing group
    Given url urlApi
    And path '/group'
    And request { code: groupCode, description: groupDescription }
    And header Content-Type = 'application/json'
    And header Authorization = jwt
    When method put
    Then status 200
    And match response == { code: groupCode, description: groupDescription, updated: #string }

    # Get already existing group
    Given url urlApi
    And path '/group/' + groupCode
    And header Content-Type = 'application/json'
    And header Authorization = jwt
    When method get
    Then status 200
    And match response == { code: groupCode, description: groupDescription, updated: #string }
