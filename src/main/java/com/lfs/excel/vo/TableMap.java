package com.lfs.excel.vo;

import cn.hutool.core.util.BooleanUtil;
import com.alibaba.excel.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;

/**
 * TableMap
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
public class TableMap {

    private String year = "";
    private String term = "";

    private Map<String, ClassTable> classTableMap = new LinkedHashMap<>();

    private static Map<String, TeacherTable> teacherTableMap = new LinkedHashMap<>();

    public static TableMap union(TableMap... tableMaps) {
        TableMap ans = new TableMap();
        for (TableMap tableMap : tableMaps) {
            ans.getClassTableMap().putAll(tableMap.getClassTableMap());
            ans.setYear(tableMap.year);
            ans.setTerm(tableMap.term);
        }
        return ans;
    }

    public static Map<String, TeacherTable> getTeacherTableMap() {
        return teacherTableMap;
    }

    public TableMap putCourse(String teacherName, String className, String course, int dayOfWeek, int courseOfDay, Boolean single) {

        classTableMap.putIfAbsent(className, new ClassTable());

        classTableMap.computeIfPresent(className, (s, classTable) -> {
            if (BooleanUtil.isTrue(single)) {
                classTable.putSingle(dayOfWeek, courseOfDay, course, teacherName);
            } else if (BooleanUtil.isFalse(single)) {
                classTable.putDouble(dayOfWeek, courseOfDay, course, teacherName);
            } else {
                classTable.put(dayOfWeek, courseOfDay, course, teacherName);
            }
            classTable.setClassName(className);
            classTable.setYear(this.year);
            classTable.setTerm(this.term);
            return classTable;
        });

        if (!StringUtils.isBlank(teacherName)) {
            teacherTableMap.putIfAbsent(teacherName, new TeacherTable());
            teacherTableMap.computeIfPresent(teacherName, (s, teacherTable) -> {

                String courseUnion = String.format("%s\n%s", className, course);
                if (BooleanUtil.isTrue(single)) {
                    teacherTable.putSingle(dayOfWeek, courseOfDay, courseUnion, teacherName);
                } else if (BooleanUtil.isFalse(single)) {
                    teacherTable.putDouble(dayOfWeek, courseOfDay, courseUnion, teacherName);
                } else {
                    teacherTable.put(dayOfWeek, courseOfDay, courseUnion, teacherName);
                }
                teacherTable.setTname(teacherName);
                teacherTable.setYear(this.year);
                teacherTable.setTerm(this.term);
                return teacherTable;
            });
        }

        return this;
    }

    public TableMap putCourse(String teacherName, String className, String course, int dayOfWeek, int courseOfDay) {

        return putCourse(teacherName, className, course, dayOfWeek, courseOfDay, null);
    }

}
