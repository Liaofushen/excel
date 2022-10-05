package com.lfs.excel.vo;

/**
 * TeacherTable
 *
 * @author fushenliao
 * @version 1.0.0
 * @project tecwealth
 * @create 2022/10/3
 * @modify 2022/10/3
 */
@lombok.Data
@lombok.Builder(toBuilder = true)
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class TeacherTable extends CourseTable {

    private String tname = "";

    public static TeacherTable getMock() {
        String tname = Double.doubleToLongBits(Math.random()) % 1000 + "老师";
        return getMock(tname);
    }

    public static TeacherTable getMock(String tname) {
        TeacherTable teacherTable = new TeacherTable();
        teacherTable.setTname(tname);
        for (int j = 0; j < teacherTable.getDcTable().length; j++) {
            CourseVo[] wClassDay = teacherTable.getDcTable()[j];
            for (int i = 0; i < wClassDay.length; i++) {
                wClassDay[i] = CourseVo.builder()
                        .cname(String.format("周%s第%s", j + 1, i + 1))
                        .tname(tname)
                        .build();
            }
        }
        return teacherTable;
    }

}
