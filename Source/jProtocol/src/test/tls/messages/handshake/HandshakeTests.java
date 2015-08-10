package test.tls.messages.handshake;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		HandshakeMessageTest.class,
		ClientHelloMessageTest.class, 
		ServerHelloMessageTest.class 
		})
public class HandshakeTests {

}
