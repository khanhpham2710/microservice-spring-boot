package microservice.notification_service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.notification_service.kafka.order.PurchaseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static microservice.notification_service.email.EmailTemplates.ORDER_CONFIRMATION;
import static microservice.notification_service.email.EmailTemplates.PAYMENT_CONFIRMATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String email;

    @Async
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amountReceive,
            BigDecimal totalAmount
    ) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        InternetAddress fromAddress= new InternetAddress(email, "MICROSERVICE");
        messageHelper.setFrom(fromAddress);

        final String templateName = PAYMENT_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("receive", amountReceive);
        variables.put("amount", totalAmount);
        variables.put("change", amountReceive.subtract(totalAmount));

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - Payment Success Email successfully sent to {} ", destinationEmail);
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Payment Success Email to {} ", destinationEmail);
        }

    }

    @Async
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            Long orderId,
            BigDecimal amount,
            List<PurchaseResponse> products
    ) throws MessagingException, UnsupportedEncodingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());

        InternetAddress fromAddress= new InternetAddress(email, "MICROSERVICE");
        messageHelper.setFrom(fromAddress);

        final String templateName = ORDER_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("orderId", orderId);
        variables.put("totalAmount", amount);
        variables.put("products", products);

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(ORDER_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("INFO - Order Confirmation Email successfully sent to {} ", destinationEmail);
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Order Confirmation Email to {} ", destinationEmail);
        }

    }
}