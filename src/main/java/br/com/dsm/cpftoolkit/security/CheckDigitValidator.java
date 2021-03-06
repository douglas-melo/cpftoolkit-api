package br.com.dsm.cpftoolkit.security;

import java.util.Arrays;

/*
 * Classe para validar o dígito verificador do CPF inconsistente.
 *
 * Possui os métodos para validar o dígito verificador a fim
 * de verificar a consistência do CPF.
 */
class CheckDigitValidator implements EvaluableCPF {

    private UntrustedCPF cpf;

    CheckDigitValidator() {}

    CheckDigitValidator(UntrustedCPF cpf) {
        this.cpf = cpf;
    }

    private String getCPF() {
        return cpf.getCPF().replaceAll("[\\s.\\-]", "");
    }

    @Override
    final public int validateDigits() {
        return getCPFCheckDigits().equals(getFirstCheckDigit() + "" + getSecondCheckDigit()) ? 0 : 1;
    }

    private String getCPFCheckDigits() {
        return getCPF().substring(9, 11);
    }

    private String getCPFWithoutCheckDigits() {
        return getCPF().substring(0, 9);
    }

    private String getCPFWithFirstCheckDigit() {
        return getCPFWithoutCheckDigits() + "" + getFirstCheckDigit();
    }

    private int applyRemainderOperation(int checkDigit) {
        return checkDigit == 10 ? 0 : checkDigit;
    }

    private int reduceCPFDigits(int[] digits) {
        return Arrays.stream(digits).sum();
    }

    private int getFirstCheckDigit() {
        int[] digits = new int[9];

        for (int i = 0; i < getCPFWithoutCheckDigits().length(); i++) {
            digits[i] = ((i + 1) * (getCPFWithoutCheckDigits().charAt(i) - '0'));
        }
        int firstCheckDigit = reduceCPFDigits(digits) % 11;
        return applyRemainderOperation(firstCheckDigit);
    }

    private int getSecondCheckDigit() {
        int[] digits = new int[10];

        for (int y = 0; y < getCPFWithFirstCheckDigit().length(); y++) {
            digits[y] = ((y) * (getCPFWithFirstCheckDigit().charAt(y) - '0'));
        }
        int secondCheckDigit = reduceCPFDigits(digits) % 11;
        return applyRemainderOperation(secondCheckDigit);
    }

    @Override
    public Reason getReason() {
        return Reason.INVALID_CPF;
    }
}
