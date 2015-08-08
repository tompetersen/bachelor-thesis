package test.tls;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		ConnectionStateTest.class, 
		MacTest.class, 
		PseudoRandomFunctionTest.class,
		SecurityParametersTest.class,
		CipherTest.class,
		BlockCipherSuiteTest.class
		})
public class AllTests {

}
