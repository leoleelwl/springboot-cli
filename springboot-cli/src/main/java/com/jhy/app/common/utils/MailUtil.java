package com.jhy.app.common.utils;

import com.jhy.app.common.jms.JMSTool;
import com.jhy.app.common.jms.JMSType;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@ConditionalOnProperty(name = "spring.mail",havingValue ="enabled")
public class MailUtil {

	@Autowired
	JMSTool jmsTool;

	@Value("${spring.mail.from}")
	private String from;

	@Autowired
	private JavaMailSender mailSender;

	public void send(String subjct, String text, String... to) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subjct);
		message.setText(text);
		jmsTool.sendMessage(JMSType.SEND_MAIL, message);
	}

	@SneakyThrows
	public void sendHtml(String subject, String text, String... to) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
		messageHelper.setFrom(from);
		messageHelper.setTo(to);
		messageHelper.setSubject(subject);
		// html格式，第二个参数为true
		messageHelper.setText(text, true);
		// 附件
		// messageHelper.addAttachment
		jmsTool.sendMessage(JMSType.SEND_MAIL, mimeMessage);
	}
}
