package com.atguigu.gmall.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ddf.EscherColorRef.SysIndexProcedure;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;

/**
 * @Author: dengdagui
 * @Description: 字符串替换主要替换相应参数文件
 * @Date: Created in 2018-7-20
 */
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileReplayUtils {


    /***
     * 修改脚本中时用jar,java,class,参烽的各种文件:的调用时修改其绝对路径:
     */
    public static void modiyFilePathParameter(String sourceFile, String findStr,
                                              String replaceStr) {

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuilder strBuf = new StringBuilder(5000);

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains(findStr) && tmp.contains("elementType")) {
                    //   String findStrold = (String) tmp.substring(tmp.indexOf(":") - 1, tmp.lastIndexOf(findStr)) + findStr;
                    String findStrold = tmp.split("name=\"")[1].split("\"")[0];
                    tmp = tmp.replace(findStrold, replaceStr);

                } else if (tmp.contains(findStr)) {
                    String findStrold = tmp.split(">")[1].split("<")[0];
                    tmp = tmp.replace(findStrold, replaceStr);

                }


                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));


            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.setLength(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***
     * 修改脚本并发，开始时间，持续运行时间：
     */
  
     /****
    public static void modiyScenarioConfig(String sourceFile, String threads,
                                           long startTime, String duration, String sleepTime, String delaytime) {
        //化成秒
        long durationTime = (long) Float.valueOf(duration).floatValue() * 60;

        //化成毫秒时间：
        long endTime = startTime + (long) Float.valueOf(duration).floatValue() * 60 * 1000;


        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面

                // 替换线程:
                if (tmp.contains("ThreadGroup.num_threads")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.num_threads\">"
                                    + threads + "</stringProp>");
                    // 替换开始时间:
                } else if (tmp.contains("ThreadGroup.start_time")) {
                    tmp = tmp.replace(tmp,
                            "<longProp name=\"ThreadGroup.start_time\">"
                                    + startTime + "</longProp>");
                    // 替换结束时间:
                } else if (tmp.contains("ThreadGroup.end_time")) {
                    // <longProp
                    // name="ThreadGroup.end_time">1408611986000</longProp>
                    tmp = tmp.replace(tmp,
                            "<longProp name=\"ThreadGroup.end_time\">"
                                    + endTime + "</longProp>");
                    //
                } else if (tmp.contains("ThreadGroup.scheduler")) {
                    // <boolProp name="ThreadGroup.scheduler">true</boolProp>
                    tmp = tmp.replace(tmp,
                            "<boolProp name=\"ThreadGroup.scheduler\">"
                                    + "true" + "</boolProp>");
                    // 替换持续时间:
                } else if (tmp.contains("ThreadGroup.duration")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.duration\">"
                                    + durationTime + "</stringProp>");
                    // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：:
                } else if (tmp.contains("LoopController.loops")) {
                    tmp = tmp.replace(tmp,
                            "<intProp name=\"LoopController.loops\">" + "-1"
                                    + "</intProp>");
                    //替换思考时间  (可优化)
                } else if (tmp.contains("ThreadGroup.ramp_time")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.ramp_time\">"
                                    + sleepTime + "</stringProp>");
                    //替换延迟场景时间 （可优化）
                } else if (tmp.contains("ThreadGroup.delay")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.delay\">" + delaytime + "</stringProp>");

                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.setLength(0);
            ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       
    ***/

        /**
 * 修改脚本并发，开始时间，持续运行时间（跳过SetupThreadGroup和PostThreadGroup） 
 */

/**
  public static boolean modiyScenarioConfig(String sourceFile, String threads,
                                         long startTime, String duration, String sleepTime, String delaytime) {

    log.info("开始修改JMeter脚本: " + sourceFile);
    
    File file = new File(sourceFile);
    if (!file.exists()) {
        log.error("文件不存在: " + sourceFile);
        return false;
    }
    
    log.info("文件路径: " + file.getAbsolutePath());
    log.info("参数 - 线程数: " + threads + ", 持续时间: " + duration + "分钟, 开始时间: " + startTime);

    // 使用临时文件
    String tempFile = sourceFile + ".tmp";
    boolean success = false;
    int modificationCount = 0;
    
    try (BufferedReader bufReader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), "utf-8"));
         PrintWriter printWriter = new PrintWriter(tempFile, "utf-8")) {
        
        String line;
        StringBuilder content = new StringBuilder();

        while ((line = bufReader.readLine()) != null) {
            String modifiedLine = line;
            
            // 调试：输出包含线程数的行
            if (line.contains("ThreadGroup.num_threads")) {
                System.out.println("=== 找到线程数配置 ===");
                System.out.println("原始行: " + line);
            }
            
            // 修复线程数替换 - 使用更简单的匹配逻辑
            if (line.contains("ThreadGroup.num_threads")) {
                String originalLine = line;
                
                // 方法1: 直接替换数字内容，保留标签结构
                modifiedLine = line.replaceAll(">(\\s*)\\d+(\\s*)</", ">$1" + threads + "$2</");
                
                // 方法2: 如果方法1没生效，尝试更激进的替换
                if (modifiedLine.equals(originalLine)) {
                    modifiedLine = line.replaceAll(">\\s*\\d+\\s*<", ">" + threads + "<");
                }
                
                // 方法3: 最后的手段 - 直接构建新行
                if (modifiedLine.equals(originalLine)) {
                    if (line.contains("<intProp")) {
                        modifiedLine = "<intProp name=\"ThreadGroup.num_threads\">" + threads + "</intProp>";
                    } else if (line.contains("<stringProp")) {
                        modifiedLine = "<stringProp name=\"ThreadGroup.num_threads\">" + threads + "</stringProp>";
                    } else if (line.contains("<longProp")) {
                        modifiedLine = "<longProp name=\"ThreadGroup.num_threads\">" + threads + "</longProp>";
                    }
                }
                
                if (!modifiedLine.equals(originalLine)) {
                    System.out.println("线程数修改成功:");
                    System.out.println("修改前: " + originalLine);
                    System.out.println("修改后: " + modifiedLine);
                    modificationCount++;
                } else {
                    System.out.println("线程数修改失败，尝试备用方案...");
                    // 备用方案：分析具体内容
                    analyzeThreadNumLine(line);
                }
            }
            
            // 其他配置项的修改保持不变...
            else if (line.contains("ThreadGroup.same_user_on_next_iteration")) {
                String newLine = line.replaceAll(">(true|false)<", ">true<");
                if (!newLine.equals(line)) {
                    modifiedLine = newLine;
                    modificationCount++;
                }
            }
            else if (line.contains("ThreadGroup.duration")) {
                long durationTime = (long) Float.valueOf(duration).floatValue() * 60;
                String newLine = line.replaceAll(">\\s*\\d+\\s*<", ">" + durationTime + "<");
                if (!newLine.equals(line)) {
                    modifiedLine = newLine;
                    modificationCount++;
                }
            }
            else if (line.contains("ThreadGroup.start_time")) {
                String newLine = line.replaceAll(">\\s*\\d+\\s*<", ">" + startTime + "<");
                if (!newLine.equals(line)) {
                    modifiedLine = newLine;
                    modificationCount++;
                }
            }
            else if (line.contains("ThreadGroup.end_time")) {
                long endTime = startTime + (long) Float.valueOf(duration).floatValue() * 60 * 1000;
                String newLine = line.replaceAll(">\\s*\\d+\\s*<", ">" + endTime + "<");
                if (!newLine.equals(line)) {
                    modifiedLine = newLine;
                    modificationCount++;
                }
            }
            else if (line.contains("ThreadGroup.ramp_time")) {
                String newLine = line.replaceAll(">\\s*\\d+\\s*<", ">" + sleepTime + "<");
                if (!newLine.equals(line)) {
                    modifiedLine = newLine;
                    modificationCount++;
                }
            }
            else if (line.contains("ThreadGroup.delay")) {
                String newLine = line.replaceAll(">\\s*\\d+\\s*<", ">" + delaytime + "<");
                if (!newLine.equals(line)) {
                    modifiedLine = newLine;
                    modificationCount++;
                }
            }
            else if (line.contains("ThreadGroup.scheduler")) {
                String newLine = line.replaceAll(">(true|false)<", ">true<");
                if (!newLine.equals(line)) {
                    modifiedLine = newLine;
                    modificationCount++;
                }
            }
            else if (line.contains("LoopController.loops")) {
                // 简单处理循环次数
                String newLine = line.replaceAll(">-?\\d+<", ">-1<");
                if (!newLine.equals(line)) {
                    modifiedLine = newLine;
                    modificationCount++;
                }
            }

            content.append(modifiedLine).append(System.getProperty("line.separator"));
        }

        printWriter.write(content.toString());
        printWriter.flush();
        success = true;
        
        System.out.println("总共修改了 " + modificationCount + " 处配置");
        
        if (modificationCount == 0) {
            System.err.println("警告：没有找到需要修改的配置项！");
            // 增强调试
            enhancedDebugJMXFile(sourceFile);
        }
        
        // 替换原文件
        File temp = new File(tempFile);
        if (temp.exists()) {
            Files.move(Paths.get(tempFile), Paths.get(sourceFile), 
                      StandardCopyOption.REPLACE_EXISTING);
            System.out.println("成功修改JMeter脚本: " + sourceFile);
        }
        
    } catch (IOException e) {
        System.err.println("修改JMeter脚本失败: " + e.getMessage());
        e.printStackTrace();
        // 清理临时文件
        File temp = new File(tempFile);
        if (temp.exists()) {
            temp.delete();
        }
    }
    
    return success && modificationCount > 0;
}  **/



 
/**
 public static boolean modiyScenarioConfig(String sourceFile, String threads,
                                         long startTime, String duration, String sleepTime, String delaytime) {

    System.out.println("开始修改JMeter脚本: " + sourceFile);
    
    // 增强权限检查和设置
    File file = new File(sourceFile);
    if (!file.exists()) {
        System.err.println("文件不存在: " + sourceFile);
        return false;
    }
    
    // 详细的文件信息
    System.out.println("文件路径: " + file.getAbsolutePath());
    System.out.println("文件是否存在: " + file.exists());
    System.out.println("文件可读: " + file.canRead());
    System.out.println("文件可写: " + file.canWrite());
    System.out.println("文件权限: " + getFilePermissions(file));
    
    // 递归设置父目录权限
    try {
        setParentDirectoryPermissions(file);
    } catch (Exception e) {
        System.err.println("设置父目录权限失败: " + e.getMessage());
    }
    
    // 设置文件可写权限 - 多种方式尝试
    boolean setWritableSuccess = false;
    try {
        // 方法1: 直接设置可写
        setWritableSuccess = file.setWritable(true, false);
        if (!setWritableSuccess) {
            // 方法2: 使用Runtime执行chmod命令
            setWritableSuccess = setFileWritableWithChmod(file);
        }
    } catch (Exception e) {
        System.err.println("设置文件可写权限异常: " + e.getMessage());
    }
    
    if (!setWritableSuccess) {
        System.err.println("警告: 无法设置文件可写权限，尝试继续处理: " + sourceFile);
    }

    // 化成秒
    long durationTime = (long) Float.valueOf(duration).floatValue() * 60;

    // 化成毫秒时间：
    long endTime = startTime + (long) Float.valueOf(duration).floatValue() * 60 * 1000;

    System.out.println("参数 - 线程数: " + threads + ", 持续时间: " + durationTime + "秒, 开始时间: " + startTime + ", 结束时间: " + endTime);

    // 标记是否在SetupThreadGroup或PostThreadGroup中
    boolean inSetupThreadGroup = false;
    boolean inPostThreadGroup = false;
    boolean inMainController = false;

    // 使用临时文件避免数据丢失
    String tempFile = sourceFile + ".tmp";
    boolean success = false;
    int modificationCount = 0;
    
    try (BufferedReader bufReader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), "utf-8"));
         PrintWriter printWriter = new PrintWriter(tempFile, "utf-8")) {
        
        StringBuffer strBuf = new StringBuffer();
        String line;

        while ((line = bufReader.readLine()) != null) {
            String tmp = line;
            boolean lineModified = false;
            if (tmp.contains("name=\"ThreadGroup.main_controller\"")) {
                inMainController = true;
            }
            if (inMainController && tmp.contains("</elementProp>")) {
                inMainController = false;
            }
            
            // 检查是否进入SetupThreadGroup
            if (tmp.contains("<SetupThreadGroup")) {
                inSetupThreadGroup = true;
                System.out.println("进入SetupThreadGroup，跳过修改");
            }
            
            // 检查是否进入PostThreadGroup
            if (tmp.contains("<PostThreadGroup")) {
                inPostThreadGroup = true;
                System.out.println("进入PostThreadGroup，跳过修改");
            }
            
            // 检查是否退出SetupThreadGroup
            if (tmp.contains("</SetupThreadGroup>")) {
                inSetupThreadGroup = false;
                System.out.println("退出SetupThreadGroup");
            }
            
            // 检查是否退出PostThreadGroup
            if (tmp.contains("</PostThreadGroup>")) {
                inPostThreadGroup = false;
                System.out.println("退出PostThreadGroup");
            }



            // 替换线程数 - 修复：支持多种标签类型
            if (tmp.contains("ThreadGroup.num_threads")) {
                String newLine;
                if (tmp.contains("<intProp")) {
                    newLine = tmp.replaceAll("<intProp name=\"ThreadGroup.num_threads\">\\d+</intProp>", 
                                           "<intProp name=\"ThreadGroup.num_threads\">" + threads + "</intProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.num_threads\">\\d+<", 
                                           "ThreadGroup.num_threads\">" + threads + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改线程数: " + threads);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 替换开始时间
            else if (tmp.contains("ThreadGroup.start_time")) {
                String newLine;
                if (tmp.contains("<longProp")) {
                    newLine = tmp.replaceAll("<longProp name=\"ThreadGroup.start_time\">\\d+</longProp>", 
                                           "<longProp name=\"ThreadGroup.start_time\">" + startTime + "</longProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.start_time\">\\d+<", 
                                           "ThreadGroup.start_time\">" + startTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改开始时间: " + startTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 替换结束时间
            else if (tmp.contains("ThreadGroup.end_time")) {
                String newLine;
                if (tmp.contains("<longProp")) {
                    newLine = tmp.replaceAll("<longProp name=\"ThreadGroup.end_time\">\\d+</longProp>", 
                                           "<longProp name=\"ThreadGroup.end_time\">" + endTime + "</longProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.end_time\">\\d+<", 
                                           "ThreadGroup.end_time\">" + endTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改结束时间: " + endTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 启用调度器
            else if (tmp.contains("ThreadGroup.scheduler")) {
                String newLine = tmp.replaceAll("ThreadGroup.scheduler\">(true|false)<", 
                                               "ThreadGroup.scheduler\">true<");
                if (!newLine.equals(tmp)) {
                    System.out.println("启用调度器");
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 替换持续时间 - 修复：支持多种标签类型
            else if (tmp.contains("ThreadGroup.duration")) {
                String newLine;
                if (tmp.contains("<longProp")) {
                    newLine = tmp.replaceAll("<longProp name=\"ThreadGroup.duration\">\\d+</longProp>", 
                                           "<longProp name=\"ThreadGroup.duration\">" + durationTime + "</longProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.duration\">\\d+<", 
                                           "ThreadGroup.duration\">" + durationTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改持续时间: " + durationTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 设置循环次数：Setup/Post 为 1，其他为 -1，仅修改 ThreadGroup.main_controller
            else if (tmp.contains("LoopController.loops")) {
                if (inMainController) {
                    String targetLoops = (inSetupThreadGroup || inPostThreadGroup) ? "1" : "-1";
                    String newLine = tmp.replaceAll("LoopController.loops\">-?\\d+<", 
                                               "LoopController.loops\">" + targetLoops + "<");
                    if (!newLine.equals(tmp)) {
                        System.out.println("设置LoopController.loops为: " + targetLoops);
                        tmp = newLine;
                        lineModified = true;
                        modificationCount++;
                    }
                }
            }
            // 替换思考时间（ramp_time）- 修复：支持多种标签类型
            else if (tmp.contains("ThreadGroup.ramp_time")) {
                String newLine;
                if (tmp.contains("<intProp")) {
                    newLine = tmp.replaceAll("<intProp name=\"ThreadGroup.ramp_time\">\\d+</intProp>", 
                                           "<intProp name=\"ThreadGroup.ramp_time\">" + sleepTime + "</intProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.ramp_time\">\\d+<", 
                                           "ThreadGroup.ramp_time\">" + sleepTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改思考时间: " + sleepTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 替换延迟时间
            else if (tmp.contains("ThreadGroup.delay")) {
                String newLine = tmp.replaceAll("ThreadGroup.delay\">\\d+<", 
                                               "ThreadGroup.delay\">" + delaytime + "<");
                if (!newLine.equals(tmp)) {
                    System.out.println("修改延迟时间: " + delaytime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }

            strBuf.append(tmp);
            strBuf.append(System.getProperty("line.separator"));
            
            // 调试输出修改的行
            if (lineModified) {
                System.out.println("修改行: " + tmp.trim());
            }
        }

        printWriter.write(strBuf.toString().toCharArray());
        printWriter.flush();
        success = true;
        
        System.out.println("总共修改了 " + modificationCount + " 处配置");
        
        if (modificationCount == 0) {
            System.err.println("警告：没有找到需要修改的配置项！");
            debugJMXFile(sourceFile);
        }
        
        // 设置临时文件权限
        File temp = new File(tempFile);
        setFileWritableWithChmod(temp);
        
        // 替换原文件
        Files.move(Paths.get(tempFile), Paths.get(sourceFile), 
                  StandardCopyOption.REPLACE_EXISTING);
        
        // 最终确认文件权限
        File finalFile = new File(sourceFile);
        setFileWritableWithChmod(finalFile);
        
        System.out.println("成功修改JMeter脚本: " + sourceFile);
        
    } catch (IOException e) {
        System.err.println("修改JMeter脚本失败: " + e.getMessage());
        e.printStackTrace();
        // 清理临时文件
        File temp = new File(tempFile);
        if (temp.exists()) {
            temp.delete();
        }
    }
    
    return success && modificationCount > 0;
}
***/


// 增加    ThreadGroup.same_user_on_next_iteration\">true<     20251129   20251130(优化日志）
public static boolean modiyScenarioConfig(String sourceFile, String threads,
                                         long startTime, String duration, String sleepTime, String delaytime) {

    System.out.println("🚀 开始修改JMeter脚本: " + sourceFile);
    
    // 增强权限检查和设置
    File file = new File(sourceFile);
    if (!file.exists()) {
        System.err.println("❌ 文件不存在: " + sourceFile);
        return false;
    }
    
    // 详细的文件信息
    System.out.println("📁 文件信息:");
    System.out.println("   📍 路径: " + file.getAbsolutePath());
    System.out.println("   ✅ 存在: " + file.exists());
    System.out.println("   📖 可读: " + file.canRead());
    System.out.println("   ✏️  可写: " + file.canWrite());
    System.out.println("   🔐 权限: " + getFilePermissions(file));
    
    // 递归设置父目录权限
    try {
        setParentDirectoryPermissions(file);
    } catch (Exception e) {
        System.err.println("⚠️  设置父目录权限失败: " + e.getMessage());
    }
    
    // 设置文件可写权限 - 多种方式尝试
    boolean setWritableSuccess = false;
    try {
        setWritableSuccess = file.setWritable(true, false);
        if (!setWritableSuccess) {
            setWritableSuccess = setFileWritableWithChmod(file);
        }
    } catch (Exception e) {
        System.err.println("⚠️  设置文件可写权限异常: " + e.getMessage());
    }
    
    if (!setWritableSuccess) {
        System.err.println("⚠️  警告: 无法设置文件可写权限，尝试继续处理: " + sourceFile);
    }

    // 时间计算
    long durationTime = (long) Float.valueOf(duration).floatValue() * 60;
    long endTime = startTime + (long) Float.valueOf(duration).floatValue() * 60 * 1000;

    System.out.println("📊 修改参数:");
    System.out.println("   👥 线程数: " + threads);
    System.out.println("   ⏱️  持续时间: " + durationTime + "秒");
    System.out.println("   🕐 开始时间: " + startTime);
    System.out.println("   🕒 结束时间: " + endTime);
    System.out.println("   ⏳ 思考时间: " + sleepTime);
    System.out.println("   ⏰ 延迟时间: " + delaytime);

    // 标记是否在SetupThreadGroup或PostThreadGroup中
    boolean inSetupThreadGroup = false;
    boolean inPostThreadGroup = false;
    boolean inMainController = false;
    boolean inThreadGroup = false;
    
    // 用于跟踪配置项是否存在
    boolean sameUserPropertyExists = false;
    boolean schedulerPropertyExists = false;
    boolean delayedStartPropertyExists = false;
    int threadGroupCount = 0;

    // 使用临时文件避免数据丢失
    String tempFile = sourceFile + ".tmp";
    boolean success = false;
    int modificationCount = 0;
    
    // 用于统计各类型修改数量
    Map<String, Integer> modificationStats = new HashMap<>();
    modificationStats.put("线程数", 0);
    modificationStats.put("调度器", 0);
    modificationStats.put("循环次数", 0);
    modificationStats.put("时间配置", 0);
    modificationStats.put("用户迭代", 0);
    modificationStats.put("其他", 0);
    
    try (BufferedReader bufReader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), "utf-8"));
         PrintWriter printWriter = new PrintWriter(tempFile, "utf-8")) {
        
        StringBuffer strBuf = new StringBuffer();
        String line;

        while ((line = bufReader.readLine()) != null) {
            String tmp = line;
            boolean lineModified = false;
            String modificationType = null;
            
            if (tmp.contains("name=\"ThreadGroup.main_controller\"")) {
                inMainController = true;
            }
            if (inMainController && tmp.contains("</elementProp>")) {
                inMainController = false;
            }
            
            // 检查是否进入线程组
            if ((tmp.contains("<ThreadGroup") && !tmp.contains("</ThreadGroup>")) || 
                tmp.contains("<SetupThreadGroup") || tmp.contains("<PostThreadGroup")) {
                inThreadGroup = true;
                threadGroupCount++;
                sameUserPropertyExists = false;
                schedulerPropertyExists = false;
                delayedStartPropertyExists = false;
                
                if (tmp.contains("<SetupThreadGroup")) {
                    inSetupThreadGroup = true;
                    System.out.println("\n🎯 进入 SetupThreadGroup (线程组 " + threadGroupCount + ") - 跳过调度器修改");
                } else if (tmp.contains("<PostThreadGroup")) {
                    inPostThreadGroup = true;
                    System.out.println("\n🎯 进入 PostThreadGroup (线程组 " + threadGroupCount + ") - 跳过调度器修改");
                } else {
                    System.out.println("\n🎯 进入 ThreadGroup (线程组 " + threadGroupCount + ")");
                }
            }
            
            // 检查是否退出线程组
            if (tmp.contains("</ThreadGroup>") || tmp.contains("</SetupThreadGroup>") || tmp.contains("</PostThreadGroup>")) {
                // 在退出线程组之前，检查并添加缺失的配置项
                if (inThreadGroup) {
                    // 添加same_user_on_next_iteration（如果不存在）
                    if (!sameUserPropertyExists) {
                        System.out.println("   ✅ 添加: same_user_on_next_iteration = true");
                        strBuf.append("        <boolProp name=\"ThreadGroup.same_user_on_next_iteration\">true</boolProp>\n");
                        modificationCount++;
                        modificationStats.put("用户迭代", modificationStats.get("用户迭代") + 1);
                    }
                    
                    // 添加scheduler（如果不存在）- 只在普通ThreadGroup中添加
                    if (!schedulerPropertyExists && !inSetupThreadGroup && !inPostThreadGroup) {
                        System.out.println("   ✅ 添加: scheduler = true");
                        strBuf.append("        <boolProp name=\"ThreadGroup.scheduler\">true</boolProp>\n");
                        modificationCount++;
                        modificationStats.put("调度器", modificationStats.get("调度器") + 1);
                    }
                    
                    // 添加开始时间和结束时间（如果调度器启用）
                    if (!inSetupThreadGroup && !inPostThreadGroup) {
                        System.out.println("   ✅ 添加: 调度时间配置");
                        strBuf.append("        <longProp name=\"ThreadGroup.start_time\">").append(startTime).append("</longProp>\n");
                        strBuf.append("        <longProp name=\"ThreadGroup.end_time\">").append(endTime).append("</longProp>\n");
                        strBuf.append("        <longProp name=\"ThreadGroup.duration\">").append(durationTime).append("</longProp>\n");
                        modificationCount += 3;
                        modificationStats.put("时间配置", modificationStats.get("时间配置") + 3);
                    }
                }
                
                String groupType = inSetupThreadGroup ? "SetupThreadGroup" : (inPostThreadGroup ? "PostThreadGroup" : "ThreadGroup");
                System.out.println("🔚 退出 " + groupType + " (线程组 " + threadGroupCount + ")");
                
                inThreadGroup = false;
                inSetupThreadGroup = false;
                inPostThreadGroup = false;
                sameUserPropertyExists = false;
                schedulerPropertyExists = false;
                delayedStartPropertyExists = false;
            }
            
            // 检查各种配置项是否存在
            if (inThreadGroup) {
                if (tmp.contains("ThreadGroup.same_user_on_next_iteration")) {
                    sameUserPropertyExists = true;
                    
                    if (tmp.contains("ThreadGroup.same_user_on_next_iteration\">false<") || 
                        tmp.contains("ThreadGroup.same_user_on_next_iteration\">false</boolProp>")) {
                        
                        String newLine = tmp.replaceAll("ThreadGroup.same_user_on_next_iteration\">false<", 
                                                       "ThreadGroup.same_user_on_next_iteration\">true<");
                        newLine = newLine.replaceAll("ThreadGroup.same_user_on_next_iteration\">false</boolProp>", 
                                                    "ThreadGroup.same_user_on_next_iteration\">true</boolProp>");
                        
                        if (!newLine.equals(tmp)) {
                            System.out.println("   🔄 修改: same_user_on_next_iteration false → true");
                            tmp = newLine;
                            lineModified = true;
                            modificationCount++;
                            modificationStats.put("用户迭代", modificationStats.get("用户迭代") + 1);
                        }
                    } else if (tmp.contains("ThreadGroup.same_user_on_next_iteration\">true<") || 
                              tmp.contains("ThreadGroup.same_user_on_next_iteration\">true</boolProp>")) {
                        System.out.println("   ℹ️  保持: same_user_on_next_iteration = true");
                    }
                }
                
                // 检查调度器配置
                if (tmp.contains("ThreadGroup.scheduler")) {
                    schedulerPropertyExists = true;
                    String newLine;
                    if (tmp.contains("<boolProp")) {
                        newLine = tmp.replaceAll("<boolProp name=\"ThreadGroup.scheduler\">(true|false)</boolProp>", 
                                               "<boolProp name=\"ThreadGroup.scheduler\">true</boolProp>");
                    } else {
                        newLine = tmp.replaceAll("ThreadGroup.scheduler\">(true|false)<", 
                                               "ThreadGroup.scheduler\">true<");
                    }
                    if (!newLine.equals(tmp)) {
                        System.out.println("   ✅ 启用: scheduler = true");
                        tmp = newLine;
                        lineModified = true;
                        modificationCount++;
                        modificationStats.put("调度器", modificationStats.get("调度器") + 1);
                    }
                }
                
                // 检查延迟创建配置并删除
                if (tmp.contains("ThreadGroup.delayedStart")) {
                    delayedStartPropertyExists = true;
                    System.out.println("   🗑️  删除: delayedStart 配置");
                    // 跳过这一行，不添加到strBuf中
                    continue;
                }
            }

            // 替换线程数
            if (tmp.contains("ThreadGroup.num_threads")) {
                String newLine;
                if (tmp.contains("<intProp")) {
                    newLine = tmp.replaceAll("<intProp name=\"ThreadGroup.num_threads\">\\d+</intProp>", 
                                           "<intProp name=\"ThreadGroup.num_threads\">" + threads + "</intProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.num_threads\">\\d+<", 
                                           "ThreadGroup.num_threads\">" + threads + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("   👥 修改: 线程数 = " + threads);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationStats.put("线程数", modificationStats.get("线程数") + 1);
                }
            }
            // 替换开始时间
            else if (tmp.contains("ThreadGroup.start_time")) {
                String newLine;
                if (tmp.contains("<longProp")) {
                    newLine = tmp.replaceAll("<longProp name=\"ThreadGroup.start_time\">\\d+</longProp>", 
                                           "<longProp name=\"ThreadGroup.start_time\">" + startTime + "</longProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.start_time\">\\d+<", 
                                           "ThreadGroup.start_time\">" + startTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("   🕐 修改: 开始时间 = " + startTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationStats.put("时间配置", modificationStats.get("时间配置") + 1);
                }
            }
            // 替换结束时间
            else if (tmp.contains("ThreadGroup.end_time")) {
                String newLine;
                if (tmp.contains("<longProp")) {
                    newLine = tmp.replaceAll("<longProp name=\"ThreadGroup.end_time\">\\d+</longProp>", 
                                           "<longProp name=\"ThreadGroup.end_time\">" + endTime + "</longProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.end_time\">\\d+<", 
                                           "ThreadGroup.end_time\">" + endTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("   🕒 修改: 结束时间 = " + endTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationStats.put("时间配置", modificationStats.get("时间配置") + 1);
                }
            }
            // 替换持续时间
            else if (tmp.contains("ThreadGroup.duration")) {
                String newLine;
                if (tmp.contains("<longProp")) {
                    newLine = tmp.replaceAll("<longProp name=\"ThreadGroup.duration\">\\d+</longProp>", 
                                           "<longProp name=\"ThreadGroup.duration\">" + durationTime + "</longProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.duration\">\\d+<", 
                                           "ThreadGroup.duration\">" + durationTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("   ⏱️  修改: 持续时间 = " + durationTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationStats.put("时间配置", modificationStats.get("时间配置") + 1);
                }
            }
            // 设置循环次数
            else if (tmp.contains("LoopController.loops")) {
                if (inMainController) {
                    String targetLoops = (inSetupThreadGroup || inPostThreadGroup) ? "1" : "-1";
                    String newLine = tmp.replaceAll("LoopController.loops\">-?\\d+<", 
                                               "LoopController.loops\">" + targetLoops + "<");
                    if (!newLine.equals(tmp)) {
                        String groupType = inSetupThreadGroup ? "SetupThreadGroup" : (inPostThreadGroup ? "PostThreadGroup" : "ThreadGroup");
                        System.out.println("   🔄 修改: " + groupType + " 循环次数 = " + targetLoops);
                        tmp = newLine;
                        lineModified = true;
                        modificationCount++;
                        modificationStats.put("循环次数", modificationStats.get("循环次数") + 1);
                    }
                }
            }
            // 替换思考时间
            else if (tmp.contains("ThreadGroup.ramp_time")) {
                String newLine;
                if (tmp.contains("<intProp")) {
                    newLine = tmp.replaceAll("<intProp name=\"ThreadGroup.ramp_time\">\\d+</intProp>", 
                                           "<intProp name=\"ThreadGroup.ramp_time\">" + sleepTime + "</intProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.ramp_time\">\\d+<", 
                                           "ThreadGroup.ramp_time\">" + sleepTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("   ⏳ 修改: 思考时间 = " + sleepTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationStats.put("其他", modificationStats.get("其他") + 1);
                }
            }
            // 替换延迟时间
            else if (tmp.contains("ThreadGroup.delay")) {
                String newLine = tmp.replaceAll("ThreadGroup.delay\">\\d+<", 
                                               "ThreadGroup.delay\">" + delaytime + "<");
                if (!newLine.equals(tmp)) {
                    System.out.println("   ⏰ 修改: 延迟时间 = " + delaytime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationStats.put("其他", modificationStats.get("其他") + 1);
                }
            }

            strBuf.append(tmp);
            strBuf.append(System.getProperty("line.separator"));
        }

        printWriter.write(strBuf.toString().toCharArray());
        printWriter.flush();
        success = true;
        
        // 输出详细的修改摘要
        System.out.println("\n📊 ========== 修改摘要 ==========");
        System.out.println("📈 总共修改了 " + modificationCount + " 处配置");
        System.out.println("🔢 处理的线程组数量: " + threadGroupCount);
        System.out.println("\n📋 各类型修改详情:");
        modificationStats.entrySet().stream()
            .filter(entry -> entry.getValue() > 0)
            .forEach(entry -> System.out.println("   • " + entry.getKey() + ": " + entry.getValue() + " 处"));
        System.out.println("================================\n");
        
        if (modificationCount == 0) {
            System.err.println("⚠️  警告：没有找到需要修改的配置项！");
            debugJMXFile(sourceFile);
        }
        
        // 设置临时文件权限并替换原文件
        File temp = new File(tempFile);
        setFileWritableWithChmod(temp);
        
        Files.move(Paths.get(tempFile), Paths.get(sourceFile), 
                  StandardCopyOption.REPLACE_EXISTING);
        
        File finalFile = new File(sourceFile);
        setFileWritableWithChmod(finalFile);
        
        System.out.println("🎉 成功修改JMeter脚本: " + sourceFile);
        
    } catch (IOException e) {
        System.err.println("❌ 修改JMeter脚本失败: " + e.getMessage());
        e.printStackTrace();
        // 清理临时文件
        File temp = new File(tempFile);
        if (temp.exists()) {
            temp.delete();
        }
    }
    
    return success && modificationCount > 0;
}

// 新增辅助方法
private static String getFilePermissions(File file) {
    try {
        Process process = Runtime.getRuntime().exec(new String[]{"ls", "-la", file.getAbsolutePath()});
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        process.waitFor();
        return line != null ? line : "未知权限";
    } catch (Exception e) {
        return "权限检查失败: " + e.getMessage();
    }
}

private static void setParentDirectoryPermissions(File file) {
    try {
        File parent = file.getParentFile();
        while (parent != null && parent.exists()) {
            Runtime.getRuntime().exec(new String[]{"chmod", "777", parent.getAbsolutePath()}).waitFor();
            parent = parent.getParentFile();
        }
    } catch (Exception e) {
        System.err.println("设置父目录权限异常: " + e.getMessage());
    }
}

private static boolean setFileWritableWithChmod(File file) {
    try {
        Process process = Runtime.getRuntime().exec(new String[]{"chmod", "666", file.getAbsolutePath()});
        int exitCode = process.waitFor();
        return exitCode == 0;
    } catch (Exception e) {
        System.err.println("使用chmod设置文件权限失败: " + e.getMessage());
        return false;
    }
}





private static void debugJMXFile(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        int lineCount = 0;
        System.out.println("=== JMX文件内容调试 ===");
        while ((line = reader.readLine()) != null && lineCount < 50) {
            if (line.contains("ThreadGroup") || line.contains("LoopController")) {
                System.out.println("第" + lineCount + "行: " + line.trim());
            }
            lineCount++;
        }
        System.out.println("=== 调试结束 ===");
    } catch (Exception e) {
        System.err.println("调试JMX文件失败: " + e.getMessage());
    }
}



 /***
 public static boolean modiyScenarioConfig(String sourceFile, String threads,
                                         long startTime, String duration, String sleepTime, String delaytime) {

    // 关键：权限检查和设置
    File file = new File(sourceFile);
    if (!file.exists()) {
        System.err.println("文件不存在: " + sourceFile);
        return false;
    }
    
    // 设置文件可写权限
    if (!file.setWritable(true, false)) {
        System.err.println("无法设置文件可写权限: " + sourceFile);
        return false;
    }

    // 化成秒
    long durationTime = (long) Float.valueOf(duration).floatValue() * 60;

    // 化成毫秒时间：
    long endTime = startTime + (long) Float.valueOf(duration).floatValue() * 60 * 1000;

    System.out.println("开始修改JMeter脚本: " + sourceFile);
    System.out.println("参数 - 线程数: " + threads + ", 持续时间: " + durationTime + "秒, 开始时间: " + startTime + ", 结束时间: " + endTime);

    // 标记是否在SetupThreadGroup或PostThreadGroup中
    boolean inSetupThreadGroup = false;
    boolean inPostThreadGroup = false;

    // 使用临时文件避免数据丢失
    String tempFile = sourceFile + ".tmp";
    boolean success = false;
    int modificationCount = 0;
    
    try (BufferedReader bufReader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), "utf-8"));
         PrintWriter printWriter = new PrintWriter(tempFile, "utf-8")) {
        
        StringBuffer strBuf = new StringBuffer();
        String line;

        while ((line = bufReader.readLine()) != null) {
            String tmp = line;
            boolean lineModified = false;
            
            // 检查是否进入SetupThreadGroup
            if (tmp.contains("<SetupThreadGroup")) {
                inSetupThreadGroup = true;
                System.out.println("进入SetupThreadGroup，跳过修改");
            }
            
            // 检查是否进入PostThreadGroup
            if (tmp.contains("<PostThreadGroup")) {
                inPostThreadGroup = true;
                System.out.println("进入PostThreadGroup，跳过修改");
            }
            
            // 检查是否退出SetupThreadGroup
            if (tmp.contains("</SetupThreadGroup>")) {
                inSetupThreadGroup = false;
                System.out.println("退出SetupThreadGroup");
            }
            
            // 检查是否退出PostThreadGroup
            if (tmp.contains("</PostThreadGroup>")) {
                inPostThreadGroup = false;
                System.out.println("退出PostThreadGroup");
            }

            // 如果在SetupThreadGroup或PostThreadGroup中，不做任何修改
            if (inSetupThreadGroup || inPostThreadGroup) {
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
                continue;
            }

            // 替换线程数 - 修复：支持多种标签类型
            if (tmp.contains("ThreadGroup.num_threads")) {
                String newLine;
                if (tmp.contains("<intProp")) {
                    newLine = tmp.replaceAll("<intProp name=\"ThreadGroup.num_threads\">\\d+</intProp>", 
                                           "<intProp name=\"ThreadGroup.num_threads\">" + threads + "</intProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.num_threads\">\\d+<", 
                                           "ThreadGroup.num_threads\">" + threads + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改线程数: " + threads);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 替换开始时间
            else if (tmp.contains("ThreadGroup.start_time")) {
                String newLine;
                if (tmp.contains("<longProp")) {
                    newLine = tmp.replaceAll("<longProp name=\"ThreadGroup.start_time\">\\d+</longProp>", 
                                           "<longProp name=\"ThreadGroup.start_time\">" + startTime + "</longProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.start_time\">\\d+<", 
                                           "ThreadGroup.start_time\">" + startTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改开始时间: " + startTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 替换结束时间
            else if (tmp.contains("ThreadGroup.end_time")) {
                String newLine;
                if (tmp.contains("<longProp")) {
                    newLine = tmp.replaceAll("<longProp name=\"ThreadGroup.end_time\">\\d+</longProp>", 
                                           "<longProp name=\"ThreadGroup.end_time\">" + endTime + "</longProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.end_time\">\\d+<", 
                                           "ThreadGroup.end_time\">" + endTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改结束时间: " + endTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 启用调度器
            else if (tmp.contains("ThreadGroup.scheduler")) {
                String newLine = tmp.replaceAll("ThreadGroup.scheduler\">(true|false)<", 
                                               "ThreadGroup.scheduler\">true<");
                if (!newLine.equals(tmp)) {
                    System.out.println("启用调度器");
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 替换持续时间 - 修复：支持多种标签类型
            else if (tmp.contains("ThreadGroup.duration")) {
                String newLine;
                if (tmp.contains("<longProp")) {
                    newLine = tmp.replaceAll("<longProp name=\"ThreadGroup.duration\">\\d+</longProp>", 
                                           "<longProp name=\"ThreadGroup.duration\">" + durationTime + "</longProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.duration\">\\d+<", 
                                           "ThreadGroup.duration\">" + durationTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改持续时间: " + durationTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 设置循环为永远（-1）
            else if (tmp.contains("LoopController.loops")) {
                String newLine = tmp.replaceAll("LoopController.loops\">-?\\d+<", 
                                               "LoopController.loops\">-1<");
                if (!newLine.equals(tmp)) {
                    System.out.println("设置循环为永远");
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 替换思考时间（ramp_time）- 修复：支持多种标签类型
            else if (tmp.contains("ThreadGroup.ramp_time")) {
                String newLine;
                if (tmp.contains("<intProp")) {
                    newLine = tmp.replaceAll("<intProp name=\"ThreadGroup.ramp_time\">\\d+</intProp>", 
                                           "<intProp name=\"ThreadGroup.ramp_time\">" + sleepTime + "</intProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.ramp_time\">\\d+<", 
                                           "ThreadGroup.ramp_time\">" + sleepTime + "<");
                }
                if (!newLine.equals(tmp)) {
                    System.out.println("修改思考时间: " + sleepTime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }
            // 替换延迟时间
            else if (tmp.contains("ThreadGroup.delay")) {
                String newLine = tmp.replaceAll("ThreadGroup.delay\">\\d+<", 
                                               "ThreadGroup.delay\">" + delaytime + "<");
                if (!newLine.equals(tmp)) {
                    System.out.println("修改延迟时间: " + delaytime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                }
            }

            strBuf.append(tmp);
            strBuf.append(System.getProperty("line.separator"));
            
            // 调试输出修改的行
            if (lineModified) {
                System.out.println("修改行: " + tmp.trim());
            }
        }

        printWriter.write(strBuf.toString().toCharArray());
        printWriter.flush();
        success = true;
        
        System.out.println("总共修改了 " + modificationCount + " 处配置");
        
        if (modificationCount == 0) {
            System.err.println("警告：没有找到需要修改的配置项！");
            debugJMXFile(sourceFile);
        }
        
        // 设置临时文件权限
        File temp = new File(tempFile);
        temp.setWritable(true, false);
        
        // 替换原文件
        Files.move(Paths.get(tempFile), Paths.get(sourceFile), 
                  StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("成功修改JMeter脚本: " + sourceFile);
        
    } catch (IOException e) {
        System.err.println("修改JMeter脚本失败: " + e.getMessage());
        e.printStackTrace();
        // 清理临时文件
        File temp = new File(tempFile);
        if (temp.exists()) {
            temp.delete();
        }
    }
    
    return success && modificationCount > 0;
}

***/

// 调试方法
/***
private static void debugJMXFile(String sourceFile) {
    try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
        String line;
        System.out.println("=== JMX文件调试信息 ===");
        while ((line = reader.readLine()) != null) {
            if (line.contains("ThreadGroup") || line.contains("LoopController")) {
                System.out.println("找到配置行: " + line.trim());
            }
        }
    } catch (IOException e) {
        System.err.println("读取JMX文件失败: " + e.getMessage());
    }
}
***/


 /***
 public static boolean modiyScenarioConfig(String sourceFile, String threads,
                                         long startTime, String duration, String sleepTime, String delaytime) {

    // 关键：权限检查和设置 - 优化逻辑
    File file = new File(sourceFile);
    if (!file.exists()) {
        System.err.println("文件不存在: " + sourceFile);
        return false;
    }
    
    // 设置所有人在容器中都可以写
    boolean permissionSet = setFullWritePermissions(file);
    if (!permissionSet) {
        System.err.println("无法设置文件可写权限: " + sourceFile);
        return false;
    }

    // 化成秒
    long durationTime = (long) Float.valueOf(duration).floatValue() * 60;

    // 化成毫秒时间：
    long endTime = startTime + (long) Float.valueOf(duration).floatValue() * 60 * 1000;

    System.out.println("开始修改JMeter脚本: " + sourceFile);
    System.out.println("参数 - 线程数: " + threads + ", 持续时间: " + durationTime + "秒, 开始时间: " + startTime + ", 结束时间: " + endTime);

    // 标记是否在SetupThreadGroup或PostThreadGroup中
    boolean inSetupThreadGroup = false;
    boolean inPostThreadGroup = false;

    // 使用临时文件避免数据丢失
    String tempFile = sourceFile + ".tmp";
    boolean success = false;
    
    try (BufferedReader bufReader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), "utf-8"));
         PrintWriter printWriter = new PrintWriter(tempFile, "utf-8")) {
        
        StringBuffer strBuf = new StringBuffer();
        String line;

        while ((line = bufReader.readLine()) != null) {
            String tmp = line;
            
            // 检查是否进入SetupThreadGroup
            if (tmp.contains("<SetupThreadGroup")) {
                inSetupThreadGroup = true;
                System.out.println("进入SetupThreadGroup，跳过修改");
            }
            
            // 检查是否进入PostThreadGroup
            if (tmp.contains("<PostThreadGroup")) {
                inPostThreadGroup = true;
                System.out.println("进入PostThreadGroup，跳过修改");
            }
            
            // 检查是否退出SetupThreadGroup
            if (tmp.contains("</SetupThreadGroup>")) {
                inSetupThreadGroup = false;
                System.out.println("退出SetupThreadGroup");
            }
            
            // 检查是否退出PostThreadGroup
            if (tmp.contains("</PostThreadGroup>")) {
                inPostThreadGroup = false;
                System.out.println("退出PostThreadGroup");
            }

            // 如果在SetupThreadGroup或PostThreadGroup中，不做任何修改
            if (inSetupThreadGroup || inPostThreadGroup) {
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
                continue;
            }

            // 替换线程数（仅对普通线程组）- 修复正则表达式
            if (tmp.contains("ThreadGroup.num_threads")) {
                String newLine = tmp.replaceAll("ThreadGroup.num_threads\">\\d+<", 
                                               "ThreadGroup.num_threads\">" + threads + "<");
                if (!newLine.equals(tmp)) {
                    System.out.println("修改线程数: " + threads);
                    tmp = newLine;
                }
            }
            // 替换开始时间
            else if (tmp.contains("ThreadGroup.start_time")) {
                String newLine = tmp.replaceAll("ThreadGroup.start_time\">\\d+<", 
                                               "ThreadGroup.start_time\">" + startTime + "<");
                if (!newLine.equals(tmp)) {
                    System.out.println("修改开始时间: " + startTime);
                    tmp = newLine;
                }
            }
            // 替换结束时间
            else if (tmp.contains("ThreadGroup.end_time")) {
                String newLine = tmp.replaceAll("ThreadGroup.end_time\">\\d+<", 
                                               "ThreadGroup.end_time\">" + endTime + "<");
                if (!newLine.equals(tmp)) {
                    System.out.println("修改结束时间: " + endTime);
                    tmp = newLine;
                }
            }
            // 启用调度器
            else if (tmp.contains("ThreadGroup.scheduler")) {
                String newLine = tmp.replaceAll("ThreadGroup.scheduler\">(true|false)<", 
                                               "ThreadGroup.scheduler\">true<");
                if (!newLine.equals(tmp)) {
                    System.out.println("启用调度器");
                    tmp = newLine;
                }
            }
            // 替换持续时间
            else if (tmp.contains("ThreadGroup.duration")) {
                String newLine = tmp.replaceAll("ThreadGroup.duration\">\\d+<", 
                                               "ThreadGroup.duration\">" + durationTime + "<");
                if (!newLine.equals(tmp)) {
                    System.out.println("修改持续时间: " + durationTime);
                    tmp = newLine;
                }
            }
            // 设置循环为永远（-1）
            else if (tmp.contains("LoopController.loops")) {
                String newLine = tmp.replaceAll("LoopController.loops\">-?\\d+<", 
                                               "LoopController.loops\">-1<");
                if (!newLine.equals(tmp)) {
                    System.out.println("设置循环为永远");
                    tmp = newLine;
                }
            }
            // 替换思考时间（ramp_time）
            else if (tmp.contains("ThreadGroup.ramp_time")) {
                String newLine = tmp.replaceAll("ThreadGroup.ramp_time\">\\d+<", 
                                               "ThreadGroup.ramp_time\">" + sleepTime + "<");
                if (!newLine.equals(tmp)) {
                    System.out.println("修改思考时间: " + sleepTime);
                    tmp = newLine;
                }
            }
            // 替换延迟时间
            else if (tmp.contains("ThreadGroup.delay")) {
                String newLine = tmp.replaceAll("ThreadGroup.delay\">\\d+<", 
                                               "ThreadGroup.delay\">" + delaytime + "<");
                if (!newLine.equals(tmp)) {
                    System.out.println("修改延迟时间: " + delaytime);
                    tmp = newLine;
                }
            }

            strBuf.append(tmp);
            strBuf.append(System.getProperty("line.separator"));
        }

        printWriter.write(strBuf.toString().toCharArray());
        printWriter.flush();
        success = true;
        
        // 设置临时文件权限，确保所有人都可以写
        File temp = new File(tempFile);
        setFullWritePermissions(temp);
        
        // 替换原文件
        Files.move(Paths.get(tempFile), Paths.get(sourceFile), 
                  StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("成功修改JMeter脚本: " + sourceFile);
        
    } catch (IOException e) {
        System.err.println("修改JMeter脚本失败: " + e.getMessage());
        e.printStackTrace();
        // 清理临时文件
        File temp = new File(tempFile);
        if (temp.exists()) {
            temp.delete();
        }
    }
    
    return success;
}

***/

/**
 * 设置文件完全可写权限，确保容器中所有人都可以写
 */
private static boolean setFullWritePermissions(File file) {
    if (file == null || !file.exists()) {
        return false;
    }
    
    System.out.println("设置文件权限: " + file.getAbsolutePath());
    
    // 方法1：使用Java设置所有用户可写
    boolean javaSuccess = file.setWritable(true, false);  // 所有用户可写
    
    // 方法2：设置完整权限（读、写、执行）
    boolean fullPermissions = file.setReadable(true, false) && 
                             file.setWritable(true, false) && 
                             file.setExecutable(true, false);
    
    // 方法3：在Linux容器中使用chmod命令（最可靠）
    boolean linuxSuccess = false;
    try {
        Process process = Runtime.getRuntime().exec(new String[]{
            "chmod", "666", file.getAbsolutePath()
        });
        linuxSuccess = (process.waitFor() == 0);
        if (linuxSuccess) {
            System.out.println("Linux chmod权限设置成功");
        }
    } catch (Exception e) {
        System.err.println("Linux权限设置失败: " + e.getMessage());
    }
    
    // 最终验证权限
    boolean canWrite = file.canWrite();
    
    if (canWrite) {
        System.out.println("文件权限设置成功，当前可写: " + canWrite);
    } else {
        System.err.println("文件权限设置失败，Java方法: " + javaSuccess + 
                          ", 完整权限: " + fullPermissions + 
                          ", Linux方法: " + linuxSuccess);
    }
    
    return canWrite;
}

 /**
public static void modiyScenarioConfig(String sourceFile, String threads,
                                       long startTime, String duration, String sleepTime, String delaytime) {
    // 化成秒
    long durationTime = (long) Float.valueOf(duration).floatValue() * 60;

    // 化成毫秒时间：
    long endTime = startTime + (long) Float.valueOf(duration).floatValue() * 60 * 1000;

    // 标记是否在SetupThreadGroup或PostThreadGroup中
    boolean inSetupThreadGroup = false;
    boolean inPostThreadGroup = false;

    try {
        BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(
                        sourceFile)), "utf-8"));
        StringBuffer strBuf = new StringBuffer();

        for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
            
            // 检查是否进入SetupThreadGroup
            if (tmp.contains("<SetupThreadGroup")) {
                inSetupThreadGroup = true;
            }
            
            // 检查是否进入PostThreadGroup
            if (tmp.contains("<PostThreadGroup")) {
                inPostThreadGroup = true;
            }
            
            // 检查是否退出SetupThreadGroup
            if (tmp.contains("</SetupThreadGroup>")) {
                inSetupThreadGroup = false;
            }
            
            // 检查是否退出PostThreadGroup
            if (tmp.contains("</PostThreadGroup>")) {
                inPostThreadGroup = false;
            }

            // 如果在SetupThreadGroup或PostThreadGroup中，不做任何修改
            if (inSetupThreadGroup || inPostThreadGroup) {
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
                continue;
            }

            // 替换线程数（仅对普通线程组）
            if (tmp.contains("ThreadGroup.num_threads")) {
                tmp = tmp.replace(tmp,
                        "<stringProp name=\"ThreadGroup.num_threads\">"
                                + threads + "</stringProp>");
            // 替换开始时间
            } else if (tmp.contains("ThreadGroup.start_time")) {
                tmp = tmp.replace(tmp,
                        "<longProp name=\"ThreadGroup.start_time\">"
                                + startTime + "</longProp>");
            // 替换结束时间
            } else if (tmp.contains("ThreadGroup.end_time")) {
                tmp = tmp.replace(tmp,
                        "<longProp name=\"ThreadGroup.end_time\">"
                                + endTime + "</longProp>");
            // 启用调度器
            } else if (tmp.contains("ThreadGroup.scheduler")) {
                tmp = tmp.replace(tmp,
                        "<boolProp name=\"ThreadGroup.scheduler\">"
                                + "true" + "</boolProp>");
            // 替换持续时间
            } else if (tmp.contains("ThreadGroup.duration")) {
                tmp = tmp.replace(tmp,
                        "<stringProp name=\"ThreadGroup.duration\">"
                                + durationTime + "</stringProp>");
            // 设置循环为永远（-1）
            } else if (tmp.contains("LoopController.loops")) {
                tmp = tmp.replace(tmp,
                        "<intProp name=\"LoopController.loops\">" + "-1"
                                + "</intProp>");
            // 替换思考时间（ramp_time）
            } else if (tmp.contains("ThreadGroup.ramp_time")) {
                tmp = tmp.replace(tmp,
                        "<stringProp name=\"ThreadGroup.ramp_time\">"
                                + sleepTime + "</stringProp>");
            // 替换延迟时间
            } else if (tmp.contains("ThreadGroup.delay")) {
                tmp = tmp.replace(tmp,
                        "<stringProp name=\"ThreadGroup.delay\">" + delaytime + "</stringProp>");
            }

            strBuf.append(tmp);
            strBuf.append(System.getProperty("line.separator"));
        }

        PrintWriter printWriter = new PrintWriter(sourceFile);
        printWriter.write(strBuf.toString().toCharArray());
        bufReader.close();
        printWriter.flush();
        printWriter.close();
        strBuf.setLength(0);

    } catch (IOException e) {
        e.printStackTrace();
    }
}




    /***
     * 修改脚本并发，默认运行次数（1次），为接口测试写的方法：
     */


    /**
    public static void modiyScenarioInterDefault(String sourceFile, String threads,
                                                 String delaytime) {

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains("ThreadGroup.num_threads")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.num_threads\">"
                                    + threads + "</stringProp>");

                } else if (tmp.contains("ThreadGroup.scheduler")) {
                    tmp = tmp.replace(tmp,
                            "<boolProp name=\"ThreadGroup.scheduler\">"
                                    + "false" + "</boolProp>");

                } else if (tmp.contains("LoopController.continue_forever")) {  //禁勾上"永远"，
                    tmp = tmp.replace(tmp,
                            "<boolProp name=\"LoopController.continue_forever\">" + "false"
                                    + "</boolProp>\n");
                } else if (tmp.contains("ThreadGroup.delay")) {  //替换延迟场景时间 （可优化）
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.delay\">" + delaytime + "</stringProp>");

                } else if (tmp.contains("LoopController.loops")) {  //默认运行一次
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"LoopController.loops\">1</stringProp>");

                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    **/


    //参考性能测试方法写的修改接口测试重试次数的方法：（接口测试专用）
   //参考性能测试方法写的修改接口测试重试次数的方法：  增加勾上：ThreadGroup.same_user_on_next_iteration 为 true   20251129(禁用延时)
   public static boolean modiyScenarioInterDefault(String sourceFile, String threads, String delaytime, String Iterationes) {
    log.info("开始修改JMeter脚本配置: " + sourceFile);
    log.info("参数 - 线程数: " + threads + ", 延迟时间: " + delaytime + ", 运行次数: " + Iterationes);
    
    // 权限检查和文件验证
    File file = new File(sourceFile);
    if (!file.exists()) {
        log.error("文件不存在: " + sourceFile);
        return false;
    }
    
    log.info("文件路径: " + file.getAbsolutePath());
    log.info("文件可读: " + file.canRead());
    log.info("文件可写: " + file.canWrite());
    
    // 设置文件权限
    boolean setWritableSuccess = setFileWritable(file);
    if (!setWritableSuccess) {
        System.err.println("警告: 无法设置文件可写权限，尝试继续处理: " + sourceFile);
    }
    
    // 使用临时文件避免数据丢失
    String tempFile = sourceFile + ".tmp";
    boolean success = false;
    int modificationCount = 0;
    
    // 标记当前线程组类型和状态
    boolean inSetupThreadGroup = false;
    boolean inPostThreadGroup = false;
    boolean inThreadGroup = false;
    boolean sameUserPropertyExists = false;
    boolean delayedStartPropertyExists = false;
    int threadGroupCount = 0;
    
    // 用于统计各类型修改数量
    Map<String, Integer> modificationSummary = new HashMap<>();
    modificationSummary.put("SetupThreadGroup", 0);
    modificationSummary.put("ThreadGroup", 0);
    modificationSummary.put("PostThreadGroup", 0);
    
    try (BufferedReader bufReader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), "utf-8"));
         PrintWriter printWriter = new PrintWriter(tempFile, "utf-8")) {
        
        StringBuffer strBuf = new StringBuffer();
        String line;

        while ((line = bufReader.readLine()) != null) {
            String tmp = line;
            boolean lineModified = false;

            // 检测线程组类型 - 使用更精确的匹配逻辑
            if (tmp.contains("<SetupThreadGroup")) {
                inSetupThreadGroup = true;
                inThreadGroup = true;
                sameUserPropertyExists = false;
                delayedStartPropertyExists = false;
                threadGroupCount++;
                System.out.println("🎯 [" + threadGroupCount + "] 进入 SetupThreadGroup");
            } else if (tmp.contains("<PostThreadGroup")) {
                inPostThreadGroup = true;
                inThreadGroup = true;
                sameUserPropertyExists = false;
                delayedStartPropertyExists = false;
                threadGroupCount++;
                System.out.println("🎯 [" + threadGroupCount + "] 进入 PostThreadGroup");
            } else if (tmp.contains("<ThreadGroup guiclass=\"ThreadGroupGui\"") && 
                      !tmp.contains("SetupThreadGroup") && !tmp.contains("PostThreadGroup")) {
                inThreadGroup = true;
                sameUserPropertyExists = false;
                delayedStartPropertyExists = false;
                threadGroupCount++;
                System.out.println("🎯 [" + threadGroupCount + "] 进入 ThreadGroup");
            }
            
            // 检查是否退出线程组
            if (tmp.contains("</SetupThreadGroup>")) {
                // 在退出线程组之前，检查并添加缺失的配置项
                if (inThreadGroup) {
                    if (!sameUserPropertyExists) {
                        System.out.println("✅ [" + threadGroupCount + "] SetupThreadGroup - 添加: same_user_on_next_iteration = true");
                        strBuf.append("        <boolProp name=\"ThreadGroup.same_user_on_next_iteration\">true</boolProp>\n");
                        modificationCount++;
                        modificationSummary.put("SetupThreadGroup", modificationSummary.get("SetupThreadGroup") + 1);
                    }
                    // 确保延迟创建被禁用（如果不存在delayedStart属性，默认就是立即创建）
                    if (!delayedStartPropertyExists) {
                        System.out.println("ℹ️  [" + threadGroupCount + "] SetupThreadGroup - 延迟创建已禁用（默认立即创建）");
                    }
                }
                inSetupThreadGroup = false;
                inThreadGroup = false;
                sameUserPropertyExists = false;
                delayedStartPropertyExists = false;
                System.out.println("🔚 [" + threadGroupCount + "] 退出 SetupThreadGroup");
            } else if (tmp.contains("</PostThreadGroup>")) {
                // 在退出线程组之前，检查并添加缺失的配置项
                if (inThreadGroup) {
                    if (!sameUserPropertyExists) {
                        System.out.println("✅ [" + threadGroupCount + "] PostThreadGroup - 添加: same_user_on_next_iteration = true");
                        strBuf.append("        <boolProp name=\"ThreadGroup.same_user_on_next_iteration\">true</boolProp>\n");
                        modificationCount++;
                        modificationSummary.put("PostThreadGroup", modificationSummary.get("PostThreadGroup") + 1);
                    }
                    // 确保延迟创建被禁用
                    if (!delayedStartPropertyExists) {
                        System.out.println("ℹ️  [" + threadGroupCount + "] PostThreadGroup - 延迟创建已禁用（默认立即创建）");
                    }
                }
                inPostThreadGroup = false;
                inThreadGroup = false;
                sameUserPropertyExists = false;
                delayedStartPropertyExists = false;
                System.out.println("🔚 [" + threadGroupCount + "] 退出 PostThreadGroup");
            } else if (tmp.contains("</ThreadGroup>") && !tmp.contains("SetupThreadGroup") && !tmp.contains("PostThreadGroup")) {
                // 在退出线程组之前，检查并添加缺失的配置项
                if (inThreadGroup) {
                    if (!sameUserPropertyExists) {
                        System.out.println("✅ [" + threadGroupCount + "] ThreadGroup - 添加: same_user_on_next_iteration = true");
                        strBuf.append("        <boolProp name=\"ThreadGroup.same_user_on_next_iteration\">true</boolProp>\n");
                        modificationCount++;
                        modificationSummary.put("ThreadGroup", modificationSummary.get("ThreadGroup") + 1);
                    }
                    // 确保延迟创建被禁用
                    if (!delayedStartPropertyExists) {
                        System.out.println("ℹ️  [" + threadGroupCount + "] ThreadGroup - 延迟创建已禁用（默认立即创建）");
                    }
                }
                inThreadGroup = false;
                sameUserPropertyExists = false;
                delayedStartPropertyExists = false;
                System.out.println("🔚 [" + threadGroupCount + "] 退出 ThreadGroup");
            }

            // 检查各种配置项
            if (inThreadGroup) {
                // 标记delayedStart属性是否存在
                if (tmp.contains("ThreadGroup.delayedStart")) {
                    delayedStartPropertyExists = true;
                    // 删除延迟创建线程设置
                    System.out.println("🗑️  [" + threadGroupCount + "] " + getThreadGroupType(inSetupThreadGroup, inPostThreadGroup) + " - 删除: 延迟创建线程设置");
                    // 跳过这一行，不添加到strBuf中
                    continue;
                }
                
                // 设置 Same user on each iteration 为 true
                if (tmp.contains("ThreadGroup.same_user_on_next_iteration")) {
                    sameUserPropertyExists = true;
                    
                    // 只有当当前值为false时才改为true
                    if (tmp.contains("ThreadGroup.same_user_on_next_iteration\">false<") || 
                        tmp.contains("ThreadGroup.same_user_on_next_iteration\">false</boolProp>")) {
                        
                        String newLine = tmp.replaceAll("ThreadGroup.same_user_on_next_iteration\">false<", 
                                                       "ThreadGroup.same_user_on_next_iteration\">true<");
                        newLine = newLine.replaceAll("ThreadGroup.same_user_on_next_iteration\">false</boolProp>", 
                                                    "ThreadGroup.same_user_on_next_iteration\">true</boolProp>");
                        
                        if (!newLine.equals(tmp)) {
                            String threadGroupType = getThreadGroupType(inSetupThreadGroup, inPostThreadGroup);
                            System.out.println("✅ [" + threadGroupCount + "] " + threadGroupType + " - 修改: same_user_on_next_iteration = true");
                            tmp = newLine;
                            lineModified = true;
                            modificationCount++;
                            modificationSummary.put(threadGroupType, modificationSummary.get(threadGroupType) + 1);
                        }
                    } else if (tmp.contains("ThreadGroup.same_user_on_next_iteration\">true<") || 
                              tmp.contains("ThreadGroup.same_user_on_next_iteration\">true</boolProp>")) {
                        String threadGroupType = getThreadGroupType(inSetupThreadGroup, inPostThreadGroup);
                        System.out.println("ℹ️  [" + threadGroupCount + "] " + threadGroupType + " - 保持: same_user_on_next_iteration 已是 true");
                    }
                }
            }

            // 1. 设置线程数 - 所有线程组都设置
            if (tmp.contains("ThreadGroup.num_threads")) {
                String newLine;
                if (tmp.contains("<intProp")) {
                    newLine = tmp.replaceAll("<intProp name=\"ThreadGroup.num_threads\">\\d+</intProp>", 
                                           "<intProp name=\"ThreadGroup.num_threads\">" + threads + "</intProp>");
                } else if (tmp.contains("<stringProp")) {
                    newLine = tmp.replaceAll("<stringProp name=\"ThreadGroup.num_threads\">\\d+</stringProp>", 
                                           "<stringProp name=\"ThreadGroup.num_threads\">" + threads + "</stringProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.num_threads\">\\d+<", 
                                           "ThreadGroup.num_threads\">" + threads + "<");
                }
                if (!newLine.equals(tmp)) {
                    String threadGroupType = getThreadGroupType(inSetupThreadGroup, inPostThreadGroup);
                    System.out.println("✅ [" + threadGroupCount + "] " + threadGroupType + " - 修改: 线程数 = " + threads);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationSummary.put(threadGroupType, modificationSummary.get(threadGroupType) + 1);
                }
            }
            // 2. 禁勾上"永远" - 设置 continue_forever 为 false (所有线程组)
            else if (tmp.contains("LoopController.continue_forever")) {
                String newLine = tmp.replaceAll("LoopController.continue_forever\">(true|false)<", 
                                               "LoopController.continue_forever\">false<");
                if (!newLine.equals(tmp)) {
                    String threadGroupType = getThreadGroupType(inSetupThreadGroup, inPostThreadGroup);
                    System.out.println("✅ [" + threadGroupCount + "] " + threadGroupType + " - 修改: 禁用永远循环");
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationSummary.put(threadGroupType, modificationSummary.get(threadGroupType) + 1);
                }
            }
            // 3. 设置循环次数 - 只修改普通ThreadGroup，不修改SetupThreadGroup和PostThreadGroup
            else if (tmp.contains("LoopController.loops")) {
                // 只有在普通ThreadGroup中才修改循环次数
                if (inThreadGroup && !inSetupThreadGroup && !inPostThreadGroup) {
                    String newLine;
                    if (tmp.contains("<stringProp")) {
                        newLine = tmp.replaceAll("<stringProp name=\"LoopController.loops\">-?\\d+</stringProp>", 
                                               "<stringProp name=\"LoopController.loops\">" + Iterationes + "</stringProp>");
                    } else if (tmp.contains("<intProp")) {
                        newLine = tmp.replaceAll("<intProp name=\"LoopController.loops\">-?\\d+</intProp>", 
                                               "<intProp name=\"LoopController.loops\">" + Iterationes + "</intProp>");
                    } else {
                        newLine = tmp.replaceAll("LoopController.loops\">-?\\d+<", 
                                               "LoopController.loops\">" + Iterationes + "<");
                    }
                    if (!newLine.equals(tmp)) {
                        System.out.println("✅ [" + threadGroupCount + "] ThreadGroup - 修改: 循环次数 = " + Iterationes);
                        tmp = newLine;
                        lineModified = true;
                        modificationCount++;
                        modificationSummary.put("ThreadGroup", modificationSummary.get("ThreadGroup") + 1);
                    }
                } else {
                    // 在SetupThreadGroup或PostThreadGroup中，输出日志但不修改
                    if (inSetupThreadGroup) {
                        System.out.println("⏭️  [" + threadGroupCount + "] SetupThreadGroup - 跳过: 循环次数修改");
                    } else if (inPostThreadGroup) {
                        System.out.println("⏭️  [" + threadGroupCount + "] PostThreadGroup - 跳过: 循环次数修改");
                    }
                }
            }
            // 4. 禁用调度器
            else if (tmp.contains("ThreadGroup.scheduler")) {
                String newLine = tmp.replaceAll("ThreadGroup.scheduler\">(true|false)<", 
                                               "ThreadGroup.scheduler\">false<");
                if (!newLine.equals(tmp)) {
                    String threadGroupType = getThreadGroupType(inSetupThreadGroup, inPostThreadGroup);
                    System.out.println("✅ [" + threadGroupCount + "] " + threadGroupType + " - 修改: 禁用调度器");
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationSummary.put(threadGroupType, modificationSummary.get(threadGroupType) + 1);
                }
            }
            // 5. 替换延迟时间
            else if (tmp.contains("ThreadGroup.delay")) {
                String newLine;
                if (tmp.contains("<stringProp")) {
                    newLine = tmp.replaceAll("<stringProp name=\"ThreadGroup.delay\">\\d+</stringProp>", 
                                           "<stringProp name=\"ThreadGroup.delay\">" + delaytime + "</stringProp>");
                } else if (tmp.contains("<intProp")) {
                    newLine = tmp.replaceAll("<intProp name=\"ThreadGroup.delay\">\\d+</intProp>", 
                                           "<intProp name=\"ThreadGroup.delay\">" + delaytime + "</intProp>");
                } else {
                    newLine = tmp.replaceAll("ThreadGroup.delay\">\\d+<", 
                                           "ThreadGroup.delay\">" + delaytime + "<");
                }
                if (!newLine.equals(tmp)) {
                    String threadGroupType = getThreadGroupType(inSetupThreadGroup, inPostThreadGroup);
                    System.out.println("✅ [" + threadGroupCount + "] " + threadGroupType + " - 修改: 延迟时间 = " + delaytime);
                    tmp = newLine;
                    lineModified = true;
                    modificationCount++;
                    modificationSummary.put(threadGroupType, modificationSummary.get(threadGroupType) + 1);
                }
            }

            strBuf.append(tmp);
            strBuf.append(System.getProperty("line.separator"));
        }

        printWriter.write(strBuf.toString().toCharArray());
        printWriter.flush();
        success = true;
        
        // 输出详细的修改摘要
        System.out.println("\n📊 ========== 修改摘要 ==========");
        System.out.println("📈 总共修改了 " + modificationCount + " 处配置");
        System.out.println("🔢 处理的线程组数量: " + threadGroupCount);
        System.out.println("\n📋 各线程组修改详情:");
        modificationSummary.forEach((type, count) -> {
            if (count > 0) {
                System.out.println("   • " + type + ": " + count + " 处修改");
            }
        });
        System.out.println("================================\n");
        
        if (modificationCount == 0) {
            System.err.println("⚠️  警告：没有找到需要修改的配置项！");
            debugJMXFile(sourceFile);
        }
        
        // 设置临时文件权限并替换原文件
        File temp = new File(tempFile);
        setFileWritable(temp);
        
        // 替换原文件
        Files.move(Paths.get(tempFile), Paths.get(sourceFile), 
                  StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("🎉 成功修改JMeter脚本: " + sourceFile);
        
    } catch (IOException e) {
        System.err.println("❌ 修改JMeter脚本失败: " + e.getMessage());
        e.printStackTrace();
        // 清理临时文件
        File temp = new File(tempFile);
        if (temp.exists()) {
            temp.delete();
        }
    }
    
    return success && modificationCount > 0;
}




// 辅助方法：获取线程组类型描述
private static String getThreadGroupType(boolean inSetupThreadGroup, boolean inPostThreadGroup) {
    if (inSetupThreadGroup) {
        return "SetupThreadGroup";
    } else if (inPostThreadGroup) {
        return "PostThreadGroup";
    } else {
        return "ThreadGroup";
    }
}

/**
 * 获取线程组类型描述
 */


/**
 * 设置文件可写权限
 */
private static boolean setFileWritable(File file) {
    boolean success = file.setWritable(true, false);
    if (!success) {
        // 使用chmod命令作为备选方案
        try {
            Process process = Runtime.getRuntime().exec("chmod 666 " + file.getAbsolutePath());
            int exitCode = process.waitFor();
            success = (exitCode == 0);
        } catch (Exception e) {
            System.err.println("使用chmod设置权限失败: " + e.getMessage());
        }
    }
    return success;
}

/**
 * 调试JMX文件结构
 */


/**
 * 调试JMX文件结构（可选）
 */

 /** 
private static void debugJMXFile(String sourceFile) {
    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(sourceFile), "utf-8"))) {
        String line;
        int lineCount = 0;
        System.out.println("=== JMX文件结构调试信息 ===");
        while ((line = reader.readLine()) != null && lineCount < 50) {
            if (line.contains("ThreadGroup.") || line.contains("LoopController.") || line.contains("same_user_on_next_iteration")) {
                System.out.println("第" + lineCount + "行: " + line.trim());
            }
            lineCount++;
        }
        System.out.println("=== 调试信息结束 ===");
    } catch (IOException e) {
        System.err.println("调试文件读取失败: " + e.getMessage());
    }
}
**/

    /***
     * 此方法为设置接口测试而写的方法，如果重试次设置为"Yes"则进行接口测试
     */
    public void modiyRetryConfig(String sourceFile, String thread,
                                 String retryNumberStr) {

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面

                // 替换线程:
                if (tmp.contains("ThreadGroup.num_threads")) {
                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"ThreadGroup.num_threads\">"
                                    + thread + "</stringProp>");
                    // 不知能否换,要测试
                } else if (tmp.contains("ThreadGroup.scheduler")) {
                    // <boolProp name="ThreadGroup.scheduler">true</boolProp>
                    tmp = tmp.replace(tmp,
                            "<boolProp name=\"ThreadGroup.scheduler\">"
                                    + "false" + "</boolProp>");
                    // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：:
                } else if (tmp.contains("LoopController.loops")) {
                    tmp = tmp.replace(tmp,
                            "<intProp name=\"LoopController.loops\">" + retryNumberStr
                                    + "</intProp>");
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));


            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());

            printWriter.flush();
            printWriter.close();
            bufReader.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***
     * 为生成测试报告时生成tps曲线：
     */
    public void updateTps(String sourceFile, ArrayList <String> tps,
                          ArrayList <String> runtime, String vuser) {


        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));

            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {
                // 替换线程:
                // 替换开始时间:
                if (tmp.contains("categories")) {
                    String str1 = "categories: [ ";
                    String str2 = "]";
                    for (int i = 0; i < runtime.size(); i++) {
                        if (i == runtime.size() - 1) {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + str2;
                        } else {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + ",";
                        }

                    }
                    tmp = tmp.replace(tmp, str1);
                    // 替换Tps:
                } else if (tmp.contains("data")) {
                    String str1 = "data: [";
                    String str2 = "]";

                    for (int i = 0; i < tps.size(); i++) {
                        if (i == tps.size() - 1) {
                            str1 = str1 + tps.get(i) + str2;
                        } else {
                            str1 = str1 + tps.get(i) + ",";
                        }
                    }
                    tmp = tmp.replace(tmp, str1);
                    // 替换用户:
                } else if (tmp.contains("name:")) {

                    tmp = tmp.replace(tmp, "name:" + "\'" + vuser + " Vuser"
                            + "\'" + ",");
                }
                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 为生成测试报告时生成成功和失败失败的事务/秒曲线：
     */
    public void updatetransactionsPerSecond(String sourceFile,
                                            ArrayList <String> SucessCount, ArrayList <String> ErroyCount,
                                            ArrayList <String> runtime, String vuser) {

        // 控制每行的变量:
        int dataline = 0;

        int nameline = 0;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains("categories")) { // 替换开始时间:

                    String str1 = "categories: [ ";
                    String str2 = "]";

                    for (int i = 0; i < runtime.size(); i++) {
                        if (i == runtime.size() - 1) {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + str2;
                        } else {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + ",";
                        }
                    }
                    tmp = tmp.replace(tmp, str1);

                } else if (tmp.contains("data")) { // 成功事务数每秒:

                    if (dataline == 0) {
                        String str1 = "data: [";
                        String str2 = "]";
                        for (int i = 0; i < SucessCount.size(); i++) {
                            if (i == SucessCount.size() - 1) {
                                str1 = str1 + SucessCount.get(i) + str2;
                            } else {
                                str1 = str1 + SucessCount.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    } else if (dataline == 1) {
                        String str1 = "data: [";
                        String str2 = "]";

                        for (int i = 0; i < ErroyCount.size(); i++) {
                            if (i == ErroyCount.size() - 1) {
                                str1 = str1 + ErroyCount.get(i) + str2;
                            } else {
                                str1 = str1 + ErroyCount.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    }

                } else if (tmp.contains("name:")) { // 替换用户:
                    if (nameline == 0) {
                        tmp = tmp
                                .replace(tmp, "name:" + "\'"
                                        + "SuccessfulTransactionPerSecond"
                                        + "\'" + ",");
                        nameline++;
                    } else if (nameline == 1) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "FailedTransactionsPerSecond" + "\'" + ",");
                        nameline++;
                    }
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.setLength(0);
            ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 生成成功数与失败数柱形对比图:
     */
    public void updateCylindricalContrast(String sourceFile,
                                          ArrayList <String> SucessCount, ArrayList <String> ErroyCount,
                                          ArrayList <String> runtime, String vuser) {

        // 控制每行的变量:
        int dataline = 0;

        int nameline = 0;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains("categories")) { // 替换开始时间:

                    String str1 = "categories: [ ";
                    String str2 = "]";

                    for (int i = 0; i < runtime.size(); i++) {
                        if (i == runtime.size() - 1) {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + str2;
                        } else {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + ",";
                        }
                    }
                    tmp = tmp.replace(tmp, str1);

                } else if (tmp.contains("data")) { // 成功事务数每秒:

                    if (dataline == 0) {
                        String str1 = "data: [";
                        String str2 = "]";
                        for (int i = 0; i < SucessCount.size(); i++) {
                            if (i == SucessCount.size() - 1) {
                                str1 = str1 + SucessCount.get(i) + str2;
                            } else {
                                str1 = str1 + SucessCount.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    } else if (dataline == 1) {
                        String str1 = "data: [";
                        String str2 = "]";

                        for (int i = 0; i < ErroyCount.size(); i++) {
                            if (i == ErroyCount.size() - 1) {
                                str1 = str1 + ErroyCount.get(i) + str2;
                            } else {
                                str1 = str1 + ErroyCount.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    }

                } else if (tmp.contains("name:")) { // 替换用户:
                    if (nameline == 0) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "NumberOfSuccessful" + "\'" + ",");
                        nameline++;
                    } else if (nameline == 1) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "NumberOfFailures" + "\'" + ",");
                        nameline++;
                    }
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 生成成功数与失败数饼图: piechart
     */
    public void piechart(String sourceFile, String sucessRate, String failRate,
                         String vuser) {

        // 控制每行的变量:
        int dataline = 0;

        // int nameline = 0 ;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains("data")) { // 成功事务数每秒:

                    if (dataline == 0) {
                        String str1 = "data: [[";
                        String str2 = "]]";
                        String str3 = "],[";

                        String str5 = str1 + "\'SuccessfulTransactionRate\'"
                                + "," + sucessRate + str3
                                + "\'FailTransactionRate\'" + "," + failRate
                                + str2;

                        tmp = tmp.replace(tmp, str5);
                        dataline++;
                    }

                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * 修改jmeter的系统配置文件: jmeter.properties 参数:summariser.interval
     */
    public static void modiyInterval(String sourceFile, String findString,
                                     String updateString) {

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains(findString)) { // 成功事务数每秒:
                    tmp = tmp.replace(tmp, updateString);
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /***
     * 修改jmeter在linux不能生成报告的bug: user.properties 参数:jmeter.reportgenerator.temp_dir
     */
    public static void modiyCanReportGenerator(String sourceFile, String findString,
                                               String updateString) {
        //最后一行是否存在：不存在则insert  :jmeter.reportgenerator.temp_dir
        String temp_dir = GetFileFirstLastLine.getFileLastLine(sourceFile);
        if (!temp_dir.contains(findString)) {
            try {
                FileWriter writer = new FileWriter(sourceFile, true);
                writer.write("\n");
                writer.write(updateString);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /***
     * 为生成平均响应时间与最大响应时间的曲线:
     */
    public void updateMaxAvgResponsed(String sourceFile,
                                      ArrayList <String> AvgRespond, ArrayList <String> MaxRespond,
                                      ArrayList <String> runtime, String vuser) {

        // 控制每行的变量:
        int dataline = 0;

        int nameline = 0;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains("categories")) { // 替换开始时间:

                    String str1 = "categories: [ ";
                    String str2 = "]";

                    for (int i = 0; i < runtime.size(); i++) {
                        if (i == runtime.size() - 1) {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + str2;
                        } else {
                            str1 = str1 + "\'" + runtime.get(i) + "\'" + ",";
                        }
                    }
                    tmp = tmp.replace(tmp, str1);

                } else if (tmp.contains("data")) { // 成功事务数每秒:

                    if (dataline == 0) {
                        String str1 = "data: [";
                        String str2 = "]";
                        for (int i = 0; i < AvgRespond.size(); i++) {
                            if (i == AvgRespond.size() - 1) {
                                str1 = str1 + AvgRespond.get(i) + str2;
                            } else {
                                str1 = str1 + AvgRespond.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    } else if (dataline == 1) {
                        String str1 = "data: [";
                        String str2 = "]";

                        for (int i = 0; i < MaxRespond.size(); i++) {
                            if (i == MaxRespond.size() - 1) {
                                str1 = str1 + MaxRespond.get(i) + str2;
                            } else {
                                str1 = str1 + MaxRespond.get(i) + ",";
                            }
                        }
                        tmp = tmp.replace(tmp, str1);
                        dataline++;
                    }

                } else if (tmp.contains("name:")) { // 替换用户:
                    if (nameline == 0) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "AvgResponsedTime" + "\'" + ",");
                        nameline++;
                    } else if (nameline == 1) {
                        tmp = tmp.replace(tmp, "name:" + "\'"
                                + "MaxResponsedTime" + "\'" + ",");
                        nameline++;
                    }
                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 为得到网络时间而写得方法,获取请求地址:
     */

    public String getRequestUrlMeth(String sourceFile, String findString) {

        String requestUrl = null;

        // findString  //HTTPSampler.domain

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "UTF-8"));
            //		StringBuffer strBuf = new StringBuffer();


            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                //		if(bufReader.readLine().contains("enabled=\"true\">") ){
                //			 System.out.println("tmp="+tmp) ;

                //	if(tmp.contains("enabled=\"true\">")){

                if (tmp.contains(findString)) { // 成功事务数每秒:

                    requestUrl = tmp.split(">")[1].split("<")[0];

                    if (requestUrl.contains("/")) {

                        requestUrl = tmp.split("/")[0];

                    }

                    if (!requestUrl.equals("") && requestUrl.contains("com")) {

                        //		 System.out.println("requestUrl8=" + requestUrl);
                        bufReader.close();
                        return requestUrl;
                    } else {
                        if (!requestUrl.equals("")) {
                            bufReader.close();
                            return requestUrl;
                        }
                    }
                }

                //    }

            }

            //		strBuf.append(tmp);
            //		strBuf.append(System.getProperty("line.separator"));
            //		}

            //		PrintWriter printWriter = new PrintWriter(sourceFile);
            //		printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            //		printWriter.flush();
            //		printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestUrl;

    }

    /***
     * 修改：
     */

    /***
     * 为得到网络时间而写得方法,获取请求地址:
     */
    public String getRequestUrlMethMody(String sourceFile, String findString) {

        String requestUrl = null;

        BufferedReader reader = null;

        BufferedWriter writer0;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(sourceFile)), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {

            e1.printStackTrace();
        } catch (FileNotFoundException e1) {

            e1.printStackTrace();
        }

        String tempString = null;
        int line = 0;

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile, "utf-8"))));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains(findString)) { // 成功事务数每秒:

                    requestUrl = tmp.split(">")[1].split("<")[0];

                    if (requestUrl.contains("/")) {

                        requestUrl = tmp.split("/")[0];

                    }


                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestUrl;

    }

    /***
     * 得到事务名称:
     */
    public String getTransactionNameMeth(String sourceFile, String findString) {

        String TransactionName = "false";

        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换线程:
                if (tmp.contains(findString)) { // 成功事务数每秒:

                    TransactionName = tmp.split("testname=")[1].split("\"")[1]
                            .split("\"")[0];

                }

                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));
            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return TransactionName;

    }


    /***
     * 修改脚本中高用jar,java,class的调用时修改其绝对路径:
     */
    public void modiyJarClassJavaPath(String sourceFile, String findStr,
                                      String replaceStr) {


        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面
                // 替换线程:
                if (tmp.contains(findStr)) {

                    System.out.println("findStr=" + findStr);
                    System.out.println("replaceStr=" + replaceStr);

                    tmp = tmp.replace(tmp,
                            "<stringProp name=\"TestPlan.user_define_classpath\">"
                                    + replaceStr + "</stringProp>");
                }


                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***
     * 根据jmeter-agent的ip及端口号,修改jmeter.propertes的remove_host:
     */
    public static void modiyJmeterPropertiesIpPort(String sourceFile, String findStr, String replaceStr) {
        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            String targer = "start";
            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面
                // 替换线程:
                if (targer.equals("start") && tmp.contains(findStr)) {
                    tmp = tmp.replace(tmp, replaceStr);
                    targer = "end";
                }


                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /***
     * 根据配置在jmeter启动之前先修改内存JVM:
     */
    public static void modiyJmeterPropertiesJvm(String sourceFile, String findStr, String replaceStr) {
        try {
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            sourceFile)), "utf-8"));
            StringBuffer strBuf = new StringBuffer();

            String targer = "start";
            for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                // 替换勾上"永远"，这样才能控制运行时间生效，这是先决条件：在最后面
                // 替换线程:
                if (targer.equals("start") && tmp.contains(findStr)) {
                    tmp = tmp.replace(tmp, replaceStr);
                    targer = "endover";
                }


                strBuf.append(tmp);
                strBuf.append(System.getProperty("line.separator"));

            }

            PrintWriter printWriter = new PrintWriter(sourceFile);
            printWriter.write(strBuf.toString().toCharArray());
            bufReader.close();
            printWriter.flush();
            printWriter.close();
            strBuf.delete(0, strBuf.length());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 根据事务名得到该事务对应的URL:
     *
     * @param sourceFile
     * @param findStr
     * @param HTTPSamplerPath
     * @return
     */
    public static String rransactionUrl(String sourceFile, String findStr, String HTTPSamplerPath) {

        sourceFile = sourceFile.replace("\\", "/");
        sourceFile = sourceFile.substring(0, sourceFile.lastIndexOf("/"));

        HashMap <String, String> filesResultmap = new HashMap <String, String>();
        List <File> files = searchFiles(new File(sourceFile), ".jxm", filesResultmap);

        for (Map.Entry <String, String> entry : filesResultmap.entrySet()) {
            String key = entry.getKey();
            String jmxPath = entry.getValue();
            jmxPath = jmxPath.replace("\\", "/");
            try {
                BufferedReader bufReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(new File(
                                jmxPath)), "utf-8"));

                for (String tmp = null; (tmp = bufReader.readLine()) != null; ) {

                    if (tmp.contains(findStr)) {

                        for (String tempUrl = null; (tempUrl = bufReader.readLine()) != null; ) {


                            if (tempUrl.contains(HTTPSamplerPath)) {
                                return tempUrl.split(HTTPSamplerPath)[1].split("</stringProp>")[0].replace("//", "/");
                            }
                        }
                    }

                }

                bufReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return null;

    }


    /***
     * 指定目录下搜索指定关键字：
     * @param folder
     * @param keyword
     * @return
     */
    public static List <File> searchFiles(File folder, String keyword, HashMap <String, String> filesResultmap) {

        List <File> result = new ArrayList <File>();
        if (folder.isFile())
            result.add(folder);

        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }

                filesResultmap.put(file.getName(), file.getAbsoluteFile().toString());

                if (file.getName().endsWith(keyword)) {

                    return true;
                }
                return false;
            }
        });

        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile()) {
                    // 如果是文件则将文件添加到结果列表中

                    result.add(file.getAbsoluteFile());
                } else {
                    // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                    result.addAll(searchFiles(file, keyword, filesResultmap));
                }
            }
        }

        return result;
    }





}
