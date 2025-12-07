package com.usermodule.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
@Slf4j
public class NumberUtil {

    static String[][] mainValues = {{"", "alphaNumeric.hundred", "alphaNumeric.twoHundred", "alphaNumeric.threeHundred",
            "alphaNumeric.fourHundred", "alphaNumeric.fiveHundred", "alphaNumeric.sixHundred",
            "alphaNumeric.sevenHundred", "alphaNumeric.eightHundred", "alphaNumeric.nineHundred"},
            {"", "", "alphaNumeric.twenty", "alphaNumeric.thirty", "alphaNumeric.forty",
                    "alphaNumeric.fifty", "alphaNumeric.sixty", "alphaNumeric.seventy",
                    "alphaNumeric.eighty", "alphaNumeric.ninety"},
            {"", "alphaNumeric.one", "alphaNumeric.two", "alphaNumeric.three", "alphaNumeric.four",
                    "alphaNumeric.five", "alphaNumeric.six", "alphaNumeric.seven", "alphaNumeric.eight",
                    "alphaNumeric.nine", "alphaNumeric.ten"}};
    static String[] tenToNineteen = {"alphaNumeric.ten", "alphaNumeric.eleven", "alphaNumeric.twelve", "alphaNumeric.thirteen",
            "alphaNumeric.fourteen", "alphaNumeric.fifteen", "alphaNumeric.sixteen",
            "alphaNumeric.seventeen", "alphaNumeric.eighteen", "alphaNumeric.nineteen"};
    static String[] nPos = {"alphaNumeric.trillion", "alphaNumeric.milliard", "alphaNumeric.million", "alphaNumeric.thousand", ""};
    private final ResourceBundleUtil resourceBundleUtil;

    /**
     * @param number
     * @return
     */
    public String formatDouble(Double number) {
        MessageFormat format = new MessageFormat("{0,number,############.####}");
        Object[] objs = {number};
        return format.format(objs);
    }

    public Double roundDouble(Double number, Integer numberOfExponentialPart) {
        String exponentialPart = "4";
        if (numberOfExponentialPart != null) {
            exponentialPart = String.valueOf(numberOfExponentialPart);
        }

        String roundedStr = String.format("%." + exponentialPart + "f", number);
        number = Double.valueOf(roundedStr);
        return number;
    }

    /**
     * @param number
     * @return
     */
    public Double roundDoubleTo4(Double number) {
        if (number != null) {
            number = roundDouble(number, 4);
        }

        return number;
    }

    public Double truncateDoubleTo1000(Double number) {
        number = nullToZero(number);
        if (number > 0) {
            long check = (long) Math.floor(number / 1000);
            number = check * 1000.0;
        }

        return number;
    }

    public Double roundDoubleTo2(Double number) {
        if (number != null) {
            number = roundDouble(number, 2);
        }

        return number;
    }

    public String numberToAlphabetic(String number) {
        //            PropertyResourceBundle myResources =
//                    new PropertyResourceBundle(ResourceLocator.getResourceAsStream("resources/BusinessApplicationResources.properties"));

        String alphabetic1 = "";
        String alphabetic2;
        int cntr2, cntr1;

        switch (number.length() % 3) {
            case 1:
                alphabetic1 = "00";
                break;
            case 2:
                alphabetic1 = "0";
                break;
        }
        number = alphabetic1 + number;
        alphabetic1 = "";

        if (number.length() <= 3) {
            for (cntr1 = 0; cntr1 <= 2; cntr1++) {
                if ((cntr1 == 1) && number.charAt(cntr1) == '1') {
                    int i = Integer.parseInt(number.substring(cntr1 + 1, cntr1 + 2));
                    alphabetic1 = alphabetic1 + " " + resourceBundleUtil.getMessage(tenToNineteen[i]);
                    break;
                } //end inner if
                else {
                    int i = Integer.parseInt(number.substring(cntr1, cntr1 + 1));
                    if (!mainValues[cntr1][i].equals(""))
                        alphabetic1 = alphabetic1 + " " + resourceBundleUtil.getMessage(mainValues[cntr1][i]) + " " +
                                resourceBundleUtil.getMessage("alphaNumeric.and");
                } //end else
            } //end for

            if (!alphabetic1.equals(""))
                if ((alphabetic1.substring(alphabetic1.length() - 1
                ).trim().equals(resourceBundleUtil.getMessage("alphaNumeric.and"))) &&
                        ((alphabetic1.charAt(alphabetic1.length() - 2) == ' '))) {
                    alphabetic1 = (alphabetic1.substring(0, alphabetic1.length() - 2)).trim();
                }

            return alphabetic1;
        } //end outer if

        cntr2 = 0;
        for (cntr1 = 4 - ((number).length() / 3) + 1; cntr1 <= 4; cntr1++) {
            alphabetic2 = numberToAlphabetic(number.substring(cntr2 * 3, cntr2 * 3 + 3));
            if (!alphabetic2.trim().equals("")) {
                alphabetic1 = alphabetic1 + alphabetic2;
                if (!nPos[cntr1].equals(""))
                    alphabetic1 = alphabetic1 + " " + resourceBundleUtil.getMessage(nPos[cntr1]);
                if (cntr1 != 4)
                    alphabetic1 = alphabetic1 + " " + resourceBundleUtil.getMessage("alphaNumeric.and");
            }
            cntr2++;
        }

        if (!alphabetic1.equals(""))
            if ((alphabetic1.substring(alphabetic1.length() - 1
            ).trim().equals(resourceBundleUtil.getMessage("alphaNumeric.and"))) &&
                    ((alphabetic1.charAt(alphabetic1.length() - 2) == ' '))) {
                alphabetic1 = (alphabetic1.substring(0, alphabetic1.length() - 2)).trim();
            }

        return alphabetic1;
    }

    public Integer nullToZero(Integer in) {
        return (in == null ? 0 : in);
    }

    public Integer nullToTwo(Integer in) {
        return (in == null ? 2 : in);
    }

    public Long nullToZero(Long in) {
        return (in == null ? 0L : in);
    }

    public Double nullToZero(Double in) {
        return (in == null ? 0D : in);
    }

    public Long minusToZero(Long in) {
        return (in < 0 ? 0L : in);
    }

    public Double minusToZero(Double in) {
        return (in < 0.0 ? 0D : in);
    }

    public Double nullToInvalid(Double in) {
        return (in == null ? -1 : in);

    }

    public Integer nullToInvalid(Integer in) {
        return (in == null ? -1 : in);

    }

    public Long nullToInvalid(Long in) {
        return (in == null ? -1L : in);

    }

    public String formatDoubleCommaSeparated(Double number) {
        MessageFormat format = new MessageFormat("{0,number,###,###,###}");
        Object[] objs = {number};
        return format.format(objs);
    }

}
