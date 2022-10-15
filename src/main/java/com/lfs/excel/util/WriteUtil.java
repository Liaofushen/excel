package com.lfs.excel.util;

import static com.lfs.excel.constans.SystemConst.TEMPLATE_CLASS_XLSX;
import static com.lfs.excel.constans.SystemConst.TEMPLATE_TEACHER_XLSX;

import java.io.File;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.google.common.collect.Lists;
import com.lfs.excel.constans.SystemConst;
import com.lfs.excel.vo.ClassTable;
import com.lfs.excel.vo.CourseTable;
import com.lfs.excel.vo.CourseVo;
import com.lfs.excel.vo.TeacherTable;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import lombok.SneakyThrows;

/**
 * WriteUtil
 *
 * @author fushenliao
 * @version 1.0.0
 * @project tecwealth
 * @create 2022/10/3
 * @modify 2022/10/3
 */
public class WriteUtil {

    public static void main(String[] args) {
        System.out.println(String.format("d%d%02d", 3, 10));

        String teacherTableFile = "test.xlsx";
        LinkedHashMap<String, TeacherTable> teacherTableMap = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            TeacherTable teacherTable = TeacherTable.getMock(i + "老师");
            teacherTable.setYear("2022~2023");
            teacherTable.setTerm("一");
            teacherTableMap.put(teacherTable.getTname(), teacherTable);
        }
        write(TEMPLATE_TEACHER_XLSX, teacherTableFile, teacherTableMap);

        String classTableFile = "classTable.xlsx";
        LinkedHashMap<String, ClassTable> classTableMap = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            ClassTable classTable = ClassTable.getMock(String.format("初一%s班", i + 1));
            classTable.setYear("2022~2023");
            classTable.setTerm("一");
            classTableMap.put(classTable.getClassName(), classTable);
        }
        write(TEMPLATE_CLASS_XLSX, classTableFile, classTableMap);

    }

    @SneakyThrows
    public static void write(String templateFileName, String destFileName,
            Map<String, ? extends CourseTable> courseTableMap) {
        File copyTemplateFile = getInstantFile(templateFileName, "tpl_" + destFileName);
        Workbook workbook = WorkbookUtil.createBookForWriter(copyTemplateFile);
        courseTableMap.forEach((sheet, teacherTable) -> {
            int template = workbook.getSheetIndex("sheet");
            Sheet copySheet = workbook.cloneSheet(template);
            workbook.setSheetName(workbook.getSheetIndex(copySheet), sheet);
        });
        workbook.removeSheetAt(workbook.getSheetIndex("sheet"));
        workbook.write(Files.newOutputStream(copyTemplateFile.toPath()));

        File file = getInstantFile(templateFileName, destFileName);
        try (ExcelWriter excelWriter = EasyExcel.write(file).withTemplate(copyTemplateFile).build()) {

            courseTableMap.forEach((sheet, courseTable) -> {
                WriteSheet writeSheet = EasyExcel.writerSheet(sheet).build();
                FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
                // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 data1，然后多个list必须用 FillWrapper包裹
                for (int i = 0; i < SystemConst.CLASSES_OF_DAY; i++) {

                    excelWriter.fill(
                            new FillWrapper(String.format("c%s", i + 1), courseTable.getCourse(i)),
                            fillConfig,
                            writeSheet);
                }

                excelWriter.fill(courseTable, writeSheet);
            });

        }
        FileUtil.del(copyTemplateFile);
    }

    @SneakyThrows
    public static void writeTotal(String templateFileName, String destFileName,
            Map<String, ClassTable> courseTableMap) {
        File copyTemplateFile = getInstantFile(templateFileName, "tpl_" + destFileName);
        Workbook workbook = WorkbookUtil.createBookForWriter(copyTemplateFile);
        workbook.write(Files.newOutputStream(copyTemplateFile.toPath()));

        File file = getInstantFile(templateFileName, destFileName);
        try (ExcelWriter excelWriter = EasyExcel.write(file).withTemplate(copyTemplateFile).build()) {

            courseTableMap.forEach((sheet, courseTable) -> {
                int classId = SystemConst.toClassId(sheet);
                WriteSheet writeSheet = EasyExcel.writerSheet(filterSheetName(sheet)).build();
                FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.VERTICAL).build();
                // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 data1，然后多个list必须用 FillWrapper包裹
                for (int i = 0; i < SystemConst.DAYS_OF_WEEK; i++) {

                    String name = String.format("d%d%02d", i + 1, classId + 1);
                    List<CourseVo> course = Lists.newArrayList(courseTable.getDcTable()[i]);
                    List<Map> courseVos = course.stream().map((Function<CourseVo, Map>) courseVo -> {
                        Map<String, Object> ans = new LinkedHashMap<>();
                        if (StringUtils.isNotBlank(courseVo.getCname1())
                                || StringUtils.isNotBlank(courseVo.getCname2())) {
                            if (StringUtils.isNotBlank(courseVo.getCname1())
                                    && StringUtils.isNotBlank(courseVo.getCname2())) {
                                ans.put("cname",
                                        String.format("单周%s/双周%s", courseVo.getCname1(), courseVo.getCname2()));
                            } else if (StringUtils.isNotBlank(courseVo.getCname1())) {
                                ans.put("cname", String.format("单周%s", courseVo.getCname1()));
                            } else if (StringUtils.isNotBlank(courseVo.getCname1())) {
                                ans.put("cname", String.format("双周%s", courseVo.getCname2()));
                            } else {
                                ans.put("cname", courseVo.getCname() + courseVo.getTname());
                            }
                        } else {
                            ans.put("cname", courseVo.getCname() + courseVo.getTname());
                        }
                        return ans;
                    }).collect(Collectors.toList());
                    excelWriter.fill(new FillWrapper(name, courseVos), fillConfig, writeSheet);
                }

                excelWriter.fill(courseTable, writeSheet);
            });

        }
        FileUtil.del(copyTemplateFile);
    }

    private static String filterSheetName(String className) {
        String[] classPrefix = { "初一", "初二", "初三" };
        for (String prefix : classPrefix) {
            if (className.contains(prefix)) {
                return prefix;
            }
        }
        return "";
    }

    private static File getInstantFile(String templateFileName, String destFileName) {

        String templateFilePath = new File(templateFileName).getAbsolutePath();

        String destFilePath = new File("output/" + destFileName).getAbsolutePath();

        FileUtil.copy(templateFilePath, destFilePath, true);

        return new File(destFilePath);
    }
}
