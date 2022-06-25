Feature: General scenario

  Background:
    # Javascript helpers
    * def stringLength = function(str) { return str.length; }
    * def singleObject = function(field, value) { var ret = {}; ret[field] = value; return ret; }
    * def updateObject = function(obj, field, value) { obj[field] = value; return obj; }

    # JWT
    * def bearer = 'Bearer '
    * print bearer

  Scenario: Create non existing group
    * call read('classpath:karate/utility/create-non-existing-group.feature') { groupCode: 'groupA', groupDescription: 'Group A' }
