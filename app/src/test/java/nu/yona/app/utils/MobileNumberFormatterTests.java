package nu.yona.app.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MobileNumberFormatterTests {


    @Test
    public void formatPhoneNumber_keepsCorrectNumber() {
        String input = "+31612345678";
        String expected = "+31612345678";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_fixesLeadingZeroInDutchNumber() {
        String input = "+310612345678";
        String expected = "+31612345678";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_removesSpaces() {
        String input = "+316012 345 67";
        String expected = "+31601234567";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_addsDutchCountryCodeToLeadingSix() {
        String input = "612345678";
        String expected = "+31612345678";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_addsDutchCountryCodeToLeadingZeroSixAndRemovesZero() {
        String input = "0612345678";
        String expected = "+31612345678";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_removesSpacesAndAddsDutchCountryCode() {
        String input = "6123 4567 8";
        String expected = "+31612345678";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_formatsNonDutchNumber() {
        String input = "+45 612345678";
        String expected = "+45612345678";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_shortNumber() {
        String input = "+31 456789";
        String expected = "+31456789";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_veryShortNumber() {
        String input = "+31 456";
        String expected = "+31456";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_onlyCountryCode() {
        String input = "+31";
        String expected = "+31";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }

    @Test
    public void formatPhoneNumber_empty() {
        String input = "";
        String expected = "";
        assertEquals(expected, MobileNumberFormatter.formatDutchAndInternationalNumber(input));
    }
}
