package test.tls.crypto;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AesCbcCipherTest.class,
		AesGcmCipherTest.class,
		MacTest.class, 
		PseudoRandomFunctionTest.class,
		HashTest.class
		})
public class CryptoTests {

}
