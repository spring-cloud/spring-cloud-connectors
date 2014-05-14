package org.springframework.cloud;

/**
 * Test class to ensure that our generic deduction logic in AbstractServiceConnectorCreator is
 * robust against a class that isn't parameterized.
 * 
 * @author Ramnivas Laddad
 *
 */
public class NonParameterizedStubServiceConnectorCreator extends StubServiceConnectorCreator {

}
