package net.frontlinesms.test.smslib;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.handler.ATHandler.SynchronizedWorkflow;

import static org.mockito.Mockito.*;

public class SmslibTestUtils {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static CService mockCService() throws SMSLibDeviceException, IOException {
		CService s = mock(CService.class);
		// Make sure that synchronized jobs run on the CService actually get executed - 
		// otherwise the mock will just return null!
		when(s.doSynchronized(any(SynchronizedWorkflow.class))).thenAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return ((SynchronizedWorkflow<?>) invocation.getArguments()[0]).run();
			}
		});
		return s;
	}
}