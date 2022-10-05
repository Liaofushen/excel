package com.lfs.excel.vo;

import com.lfs.excel.constans.SystemConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseTable
 *
 * @author fushenliao
 * @version 1.0.0
 * @project tecwealth
 * @create 2022/10/3
 * @modify 2022/10/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTable {
    private String year = "";
    private String term = "";
    private CourseVo[][] dcTable = new CourseVo[SystemConst.DAYS_OF_WEEK][SystemConst.CLASSES_OF_DAY];

    public List<CourseVo> getCourse(int cid) {
        List<CourseVo> ans = new ArrayList<>();
        for (int i = 0; i < dcTable.length; i++) {
            ans.add(dcTable[i][cid] == null ? new CourseVo() : dcTable[i][cid]);
        }
        return ans;
    }

    public CourseTable put(int dayOfWeek, int idxOfDay, String course, String tname) {
        dcTable[dayOfWeek][idxOfDay] = dcTable[dayOfWeek][idxOfDay] == null ?
                CourseVo.builder().cname(course).tname(tname).build() :
                dcTable[dayOfWeek][idxOfDay].toBuilder().cname(course).tname(tname).build();
        return this;
    }

    public CourseTable putSingle(int dayOfWeek, int idxOfDay, String course, String tname) {
        dcTable[dayOfWeek][idxOfDay] = dcTable[dayOfWeek][idxOfDay] == null ?
                CourseVo.builder().cname1(course).tname1(tname).build() :
                dcTable[dayOfWeek][idxOfDay].toBuilder().cname1(course).tname1(tname).build();
        return this;
    }

    public CourseTable putDouble(int dayOfWeek, int idxOfDay, String course, String tname) {
        dcTable[dayOfWeek][idxOfDay] = dcTable[dayOfWeek][idxOfDay] == null ?
                CourseVo.builder().cname2(course).tname2(tname).build() :
                dcTable[dayOfWeek][idxOfDay].toBuilder().cname2(course).tname2(tname).build();

        return this;
    }
}
