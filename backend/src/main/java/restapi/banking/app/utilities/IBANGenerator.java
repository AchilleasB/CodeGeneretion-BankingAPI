package restapi.banking.app.utilities;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class IBANGenerator implements IdentifierGenerator {

    // this method will return an iban . Ex . NL24 ABNA 1234 5678 10
    public static String generateIBAN() {
        return "NL" + generateRandomNumbers(2) + "ABNA" +
                generateRandomNumbers(4) + generateRandomNumbers(4) + generateRandomNumbers(2);
    }

    // Generate random numbers of specified length
    private static String generateRandomNumbers(int length) {
        String numbers = "0123456789";
        StringBuilder randomNumbers = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomNumbers.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return randomNumbers.toString();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return generateIBAN();
    }
}
