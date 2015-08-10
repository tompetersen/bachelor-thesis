package test.tls;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.tls.ciphersuites.CiphersuiteTests;
import test.tls.crypto.CryptoTests;
import test.tls.messages.MessageTests;

@RunWith(Suite.class)
@SuiteClasses({ 
		ConnectionStateTest.class, 
		SecurityParametersTest.class,
		MessageTests.class,
		CryptoTests.class,
		CiphersuiteTests.class
		})
public class AllTests {

}
