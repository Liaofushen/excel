package com.lfs.excel.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassTable
 *
 * @author fushenliao
 * @version 1.0.0
 * @project tecwealth
 * @create 2022/10/3
 * @modify 2022/10/3
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClassTable extends CourseTable {
    private String className = "";
    private String classPrefix = "";

    public static ClassTable getMock() {
        String tname = Double.doubleToLongBits(Math.random()) % 1000 + "班";
        return getMock(tname);
    }

    public static ClassTable getMock(String className) {
        ClassTable classTable = new ClassTable();
        classTable.setClassName(className);
        for (int j = 0; j < classTable.getDcTable().length; j++) {
            CourseVo[] wClassDay = classTable.getDcTable()[j];
            for (int i = 0; i < wClassDay.length; i++) {
                wClassDay[i] = CourseVo.builder()
                        .cname(String.format("周%s第%s", j + 1, i + 1))
                        .tname(className)
                        .build();
            }
        }
        return classTable;
    }
}
