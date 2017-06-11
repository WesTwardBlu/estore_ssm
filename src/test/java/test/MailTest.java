package test;

import static org.junit.Assert.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Ignore;
import org.junit.Test;

import com.blue.bean.User;
import com.blue.utils.MailUtils;

public class MailTest {

	@Ignore
	@Test
	public void testMail() throws AddressException, MessagingException {
			User user= new User();
			user.setActivecode("ecadd874-177d-429b-8a0a-40f020ef18f6");
			String msg= "注册成功，请在12小时内<a href='http://localhost:8091/estore/user/activecode/" + user.getActivecode()+ "'>激活</a>";
			MailUtils.sendMail("935105579@qq.com", msg);
	}

}
