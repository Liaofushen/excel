package com.lfs.excel;

import com.lfs.excel.constans.SystemConst;
import com.lfs.excel.util.ReadUtil;
import com.lfs.excel.util.WriteUtil;
import com.lfs.excel.vo.TableMap;

import java.io.IOException;

/**
 * Excel
 *
 * @author fushenliao
 * @version 1.0.0
 * @project tecwealth
 * @create 2022/8/30
 * @modify 2022/8/30
 */
public class Excel {


    public static void main(String[] args) throws IOException {
        System.out.println("========== start ==========");
        String[] files = {"input/input_初一.xls", "input/input_初二.xls", "input/input_初三.xls"};
        System.out.printf("========== 有%s个输入文件 ==========\n", files.length);
        for (int i = 0; i < files.length; i++) {
            System.out.printf("%d. %s 读取中...\n", i, files[i]);
        }
        TableMap tables = ReadUtil.readTables(files, "2022~2023", "一");

        WriteUtil.write(SystemConst.TEMPLATE_CLASS_XLSX, "班级课表.xlsx", tables.getClassTableMap());
        WriteUtil.write(SystemConst.TEMPLATE_TEACHER_XLSX, "教师课表.xlsx", tables.getTeacherTableMap());
        WriteUtil.writeTotal(SystemConst.TEMPLATE_TOTAL_XLSX, "总课表.xlsx", tables.getClassTableMap());
        System.out.println("========== 已完成 ==========");
        System.out.println("========== 按任意键退出 ==========");

        System.in.read();

    }


}
