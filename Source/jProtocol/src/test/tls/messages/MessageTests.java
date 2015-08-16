package test.tls.messages;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import test.tls.messages.handshake.HandshakeTests;

@RunWith(Suite.class)
@SuiteClasses({ 
		AlertMessageTest.class, 
		ApplicationDataMessageTest.class,
		ChangeCipherSpecMessageTest.class, 
		HandshakeTests.class
		})
public class MessageTests {

}
