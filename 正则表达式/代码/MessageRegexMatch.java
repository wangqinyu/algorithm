package com.wondersgroup.hiip.empi.service;

import com.wondersgroup.hiip.empi.exception.PatientError;
import com.wondersgroup.hiip.empi.exception.PatientException;
import com.wondersgroup.hiip.empi.domain.Code;
import com.wondersgroup.hiip.empi.domain.ValueSet;
import org.apache.log4j.Logger;

import java.util.regex.Pattern;

/**
 * 输入匹配检验类
 */
public class MessageRegexMatch {
    private static Logger logger = Logger.getLogger(MessageRegexMatch.class);
    /**
     * 检验是否为合法字符
     * @param str
     * @return
     */
    public static void legalMatch(String str,String target) throws PatientException {
        String pattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";
        if (!Pattern.matches(pattern,str)){
            throw new PatientException(PatientError.ERR_INPUT_LEGAL,target);
        }
    }

    /**
     * 检验是否为中英文
     * @param str
     * @return
     */
    public static void characterMatch(String str,String target) throws PatientException {
        String pattern = "^[\\u4e00-\\u9fa5a-zA-Z]+$";
        if (!Pattern.matches(pattern,str)){
            throw new PatientException(PatientError.ERR_INPUT_CHARACTER,target);
        }
    }

    /**
     * 检验是否为中文
     * @param str
     * @return
     */
    public static void chineseMatch(String str,String target) throws PatientException {
        String pattern = "^[\\u4e00-\\u9fa5]+$";
        if (!Pattern.matches(pattern,str)){
            throw new PatientException(PatientError.ERR_INPUT_CHINESE,target);
        }
    }


    /**
     * 检验是否为英文
     * @param str
     * @return
     */
    public static void englishMatch(String str,String target) throws PatientException {
        String pattern = "^[a-zA-Z]+$";
        if (!Pattern.matches(pattern,str)){
            throw new PatientException(PatientError.ERR_INPUT_ENGLISH,target);
        }
    }

/**
     * 检验是否为英文或数字
     * @param str
     * @return
     */
    public static void englishMathMatch(String str,String target) throws PatientException {
        String pattern = "^[a-zA-Z0-9]+$";
        if (!Pattern.matches(pattern,str)){
            throw new PatientException(PatientError.ERR_INPUT_ENGLISHMATH,target);
        }
    }


    /**
     * 检验是否为数字
     * @param str
     * @return
     */
    public static void numberMatch(String str,String target) throws PatientException {
        String pattern = "^[0-9]+$";
        if (!Pattern.matches(pattern,str)){
            throw new PatientException(PatientError.ERR_INPUT_NUMBER,target);
        }
    }


    /**
     * 检验是否为11位数字（如手机号）或者座机电话
     * @param str
     * @return
     */
    public static void numberTelMatch(String str,String target) throws PatientException {
        String pattern = "^[0-9-]{10,12}";
        if (!Pattern.matches(pattern,str)){
            throw new PatientException(PatientError.ERR_INPUT_TELECOM,target);
        }
    }


    /**
     * 检验是否为身份证号码（含15位、18位）
     * @param str
     * @return
     */
    public static void numberL15or18Match(String str,String target) throws PatientException {
        String pattern = "^[0-9xX]{15}|^[0-9xX]{18}";
        if (!Pattern.matches(pattern,str)){
            throw new PatientException(PatientError.ERR_INPUT_IDENTIFIER,target);
        }
    }

    /**
     * 输入的国际代码检测
     * @param oid
     * @param code
     * @return
     */
    public static Code checkCode(String oid, String code,String target) throws PatientException {
        ValueSetLoad.getInstance();
        if (ValueSetLoad.getValueSetMap().containsKey(oid)){
            ValueSet valueSetResult = ValueSetLoad.getValueSetMap().get(oid);
            if (valueSetResult.getCode_ValueSet().containsKey(code)) {
                Code codeResult = valueSetResult.getCode_ValueSet().get(code);
                return codeResult;
            }else {
                logger.info("oid正确：" + oid);
                logger.info("code错误：" + code);
                throw new PatientException(PatientError.ERR_VALUESET_CODE,target);
            }
        }else {
            System.out.println("oid错误：" + oid);
            throw new PatientException(PatientError.ERR_VALUESET_SYSTEM,target);
        }
    }
    public static void main(String[] args) {
        String pattern = "^[0-9-]{10,12}";
        boolean b = Pattern.matches(pattern,"182983928080");
        System.out.println(b);
        //System.out.println(numberL15or18Match("32412331X122222101"));
    }


}
