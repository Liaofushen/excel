package com.lfs.excel.util;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.util.StringUtils;
import com.lfs.excel.vo.TableMap;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.*;

/**
 * ReadUtil
 *
 * @author fushenliao
 * @version 1.0.0
 * @project tecwealth
 * @create 2022/10/3
 * @modify 2022/10/3
 */
public class ReadUtil {

    public static void main(String[] args) {


        TableMap tableMap = readTables(new String[]{"input/input_初一.xls", "input/input_初二.xls", "input/input_初三.xls"}, "2022", "1");
        System.out.println(JSONUtil.toJsonStr(tableMap.getTeacherTableMap()));

    }

    public static TableMap readTables(String files[], String year, String term) {
        List<TableMap> ans = new ArrayList<>();
        for (String file : files) {
            ExcelReader reader = ExcelUtil.getReader(new File(file).getAbsoluteFile());
            String classPrefix = file.split("\\_")[1].split("\\.")[0];
            ans.add(new Table()
                    .classPrefix(classPrefix)
                    .table(reader.read())
                    .yearTerm(year, term)
                    .init()
                    .readTableMap());

        }
        return TableMap.union(ans.toArray(new TableMap[0]));
    }

    @Data
    @NoArgsConstructor
    public static class Table {
        private List<List<Object>> table;

        private static final String CELL_SPLIT = "\\s+";
        private static final String COURSE_SPLIT = "\\-";
        private int rowDataGridOffset = 2;
        private int colDataGridOffset = 2;

        private String year = "";

        private String term = "";

        String classPrefix = "";

        private String[] courseSqs = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

        private String[] classNames = new String[]{};
        private String[] dayNames = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五"};

        protected Table init() {
            initClassCodes();
            return this;
        }

        protected Table classPrefix(String classPrefix) {
            this.classPrefix = classPrefix;
            return this;
        }

        protected Table yearTerm(String year, String term) {
            this.year = year;
            this.term = term;
            return this;
        }

        protected Table table(List<List<Object>> table) {
            this.table = table;
            return this;
        }

        private void initClassCodes() {
            List<Object> classCodeRows = table.get(rowDataGridOffset - 1);

            List<String> classCodeList = new ArrayList<>();
            for (int i = 0; i < classCodeRows.size(); i++) {
                String val = classCodeRows.get(i).toString();
                if (!"".equals(val) && StringUtils.isNotBlank(table.get(rowDataGridOffset).get(i).toString())) {
                    classCodeList.add(classPrefix + val);
                }
            }
            classNames = classCodeList.toArray(new String[0]);
        }


        public TableMap readTableMap() {
            TableMap ans = TableMap.builder()
                    .year(year)
                    .term(term)
                    .classTableMap(new LinkedHashMap<>())
                    .build();

            for (int row = rowDataGridOffset; row < rowDataGridOffset + dayNames.length * courseSqs.length; row++) {
                List<Object> rows = table.get(row);
                for (int col = colDataGridOffset; col < colDataGridOffset + classNames.length; col++) {
                    int dayOfWeek = (row - rowDataGridOffset) / courseSqs.length;
                    int courseOfDay = (row - rowDataGridOffset) % courseSqs.length;
                    int classIndex = (col - colDataGridOffset);


                    String data = rows.get(col).toString();
                    String[] split = data.split(CELL_SPLIT);
                    if (split.length < 2) {
                        String course = split[0];
                        ans.putCourse("", this.classNames[classIndex], course, dayOfWeek, courseOfDay);
                    } else if (split.length == 2) {
                        String course = split[0];
                        String teacher = split[1];
                        ans.putCourse(teacher, this.classNames[classIndex], course, dayOfWeek, courseOfDay);

                    } else {
                        String course1 = split[1].split(COURSE_SPLIT)[0];
                        String teacher1 = split[1].split(COURSE_SPLIT)[2];
                        ans.putCourse(teacher1, this.classNames[classIndex], course1, dayOfWeek, courseOfDay, true);

                        String course2 = split[2].split(COURSE_SPLIT)[0];
                        String teacher2 = split[2].split(COURSE_SPLIT)[2];
                        ans.putCourse(teacher2, this.classNames[classIndex], course2, dayOfWeek, courseOfDay, false);

                    }

                }
            }


            return ans;
        }
    }
}
