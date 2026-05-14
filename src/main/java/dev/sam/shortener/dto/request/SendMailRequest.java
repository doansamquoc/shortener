package dev.sam.shortener.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMailRequest {
    String to;
    String cc;
    String bcc;
    String subject;
    String body;
    boolean html;
}
