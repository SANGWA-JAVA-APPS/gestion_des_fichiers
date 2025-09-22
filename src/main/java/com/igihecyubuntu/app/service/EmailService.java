package com.igihecyubuntu.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from:noreply@igihecyubuntu.com}")
    private String fromEmail;

    @Value("${app.email.test-recipient:admin@igihecyubuntu.com}")
    private String testRecipient;

    String appName = "IGIHECYUBUNTU";

    public void sendRegistrationCredentials(String userEmail, String username, String plainPassword, String gender) {
        try {
            logger.info("Attempting to send registration credentials email to: {}", userEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(userEmail);
            message.setSubject(appName + " APP - Your Account Credentials");

            String emailBody = "Welcome to " + appName + " APP!\n\n"
                    + "Your account has been successfully created. Here are your login credentials:\n\n"
                    + "Username: " + username + "\n"
                    + "Password: " + plainPassword + "\n\n"
                    + "Account Details:\n"
                    + "- Email: " + userEmail + "\n"
                    + "- Gender: " + (gender != null ? gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase() : "Not specified") + "\n"
                    + "- Registration Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n\n"
                    + "Please keep these credentials safe and consider changing your password after first login.\n\n"
                    + "You can now login to the app using these credentials.\n\n"
                    + "Best regards,\n"
                    + appName + " System";

            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Registration credentials sent successfully to: {}", userEmail);
        } catch (Exception e) {
            logger.error("Error sending registration credentials to {}: {}", userEmail, e.getMessage());
            // Don't throw exception to avoid breaking registration flow
        }
    }
}
