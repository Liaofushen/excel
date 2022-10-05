package com.lfs.excel.constans;

/**
 * SystemConst
 *
 * @author fushenliao
 * @version 1.0.0
 * @project tecwealth
 * @create 2022/10/3
 * @modify 2022/10/3
 */
public class SystemConst {

    public static final Integer DAYS_OF_WEEK = 5;
    public static final Integer CLASSES_OF_DAY = 8;

    public static String classCn = "班";

    public static String[] classIndexZhs = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
    public static String[] classIndexEns = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};


    public static final String TEMPLATE_TEACHER_XLSX = "template/output_template_teacher.xlsx";

    public static final String TEMPLATE_CLASS_XLSX = "template/output_template_class.xlsx";

    public static final String TEMPLATE_TOTAL_XLSX = "template/output_template_total.xlsx";

    public static int toClassId(String className) {
        for (int i = classIndexEns.length - 1; i >= 0; i--) {
            if (className.contains(classIndexEns[i] + classCn) || className.contains(classIndexZhs[i] + classCn)) {
                return i;
            }
        }
        return 0;
    }
}
