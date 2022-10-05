package com.lfs.excel.vo;

import com.alibaba.excel.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CourseVo
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
public class CourseVo {
    private String cname = "";
    private String tname = "";
    private String cname1 = null;
    private String tname1 = null;
    private String cname2 = null;
    private String tname2 = null;

    public String getCname() {
        if (StringUtils.isNotBlank(cname1) && StringUtils.isNotBlank(cname2)) {
            return String.format("单周%s/双周%s", cname1, cname2);
        } else if (StringUtils.isNotBlank(cname1)) {
            return String.format("单周%s", cname1);
        } else if (StringUtils.isNotBlank(cname2)) {
            return String.format("双周%s", cname2);
        }
        return cname;
    }

    public String getTname() {
        if (StringUtils.isNotBlank(tname1) && StringUtils.isNotBlank(tname2)) {
            return String.format("单周%s/双周%s", tname1, tname2);
        } else if (StringUtils.isNotBlank(tname1)) {
            return String.format("单周%s", tname1);
        } else if (StringUtils.isNotBlank(tname2)) {
            return String.format("双周%s", tname2);
        }
        return tname;
    }
}
