package shipmenttrackingservice.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class TrackingNumberGenerator {

    private static final String PREFIX = "TRK";
    // Okunabilirliği artırmak için birbiriyle karışabilen 0, O, 1, I karakterleri çıkarıldı
    private static final String ALPHABET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int RANDOM_PART_LENGTH = 5;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private final SecureRandom random = new SecureRandom();

    public String generate() {
        String datePart = LocalDate.now().format(DATE_FORMATTER);
        StringBuilder randomPart = new StringBuilder(RANDOM_PART_LENGTH);

        for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            randomPart.append(ALPHABET.charAt(index));
        }

        return String.format("%s-%s-%s", PREFIX, datePart, randomPart.toString());
    }
}