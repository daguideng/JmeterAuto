package com.atguigu.gmall.common.utils.file;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

public class WriteXML {

    public static void writeStr2XML(String file, String content) {
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseWriteStr2XML(String file, String content) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        // System.out.println(reader);
        // 注释：创建一个串的字符输入流
        StringReader in = new StringReader(content);
        Document doc = reader.read(in);
        // System.out.println(doc.getRootElement());
        // 注释：创建输出格式
        OutputFormat formater = OutputFormat.createPrettyPrint();
        //formater=OutputFormat.createCompactFormat();
        // 注释：设置xml的输出编码
        formater.setEncoding("utf-8");
        // 注释：创建输出(目标)
        FileWriter out = new FileWriter(file);
        // 注释：创建输出流
        XMLWriter writer = new XMLWriter(out, formater);
        // 注释：输出格式化的串到目标中，执行后。格式化后的串保存在out中。
        writer.write(doc);

        writer.close();
    }


    public static void main(String[] args) {
        String file = "/Users/ellen/Documents/WorkSpace/java/santiago/apps/sales/android/tmp.xml";
        String file1 = "/Users/ellen/Documents/WorkSpace/java/santiago/apps/sales/android/tmpParsed.xml";
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><hierarchy rotation=\"0\"><android.widget.FrameLayout index=\"0\" text=\"\" class=\"android.widget.FrameLayout\" package=\"com.ucredit.sale.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[0,0][1080,1920]\" resource-id=\"\" instance=\"0\"><android.widget.LinearLayout index=\"0\" text=\"\" class=\"android.widget.LinearLayout\" package=\"com.ucredit.sale.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[0,0][1080,1920]\" resource-id=\"\" instance=\"0\"><android.widget.FrameLayout index=\"0\" text=\"\" class=\"android.widget.FrameLayout\" package=\"com.ucredit.sale.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[0,0][1080,1920]\" resource-id=\"android:id/content\" instance=\"1\"><android.widget.FrameLayout index=\"0\" text=\"\" class=\"android.widget.FrameLayout\" package=\"com.ucredit.sale.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[0,0][1080,1920]\" resource-id=\"com.ucredit.sale.android:id/content_frame\" instance=\"2\"><android.widget.RelativeLayout index=\"0\" text=\"\" class=\"android.widget.RelativeLayout\" package=\"com.ucredit.sale.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[0,0][1080,1920]\" resource-id=\"\" instance=\"0\"><android.widget.ImageView index=\"0\" text=\"\" class=\"android.widget.ImageView\" package=\"com.ucredit.sale.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[0,0][1080,1920]\" resource-id=\"com.ucredit.sale.android:id/welcome_iv\" instance=\"0\"/><android.widget.LinearLayout index=\"1\" text=\"\" class=\"android.widget.LinearLayout\" package=\"com.ucredit.sale.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[384,733][695,1186]\" resource-id=\"\" instance=\"1\"><android.widget.ImageView index=\"0\" text=\"\" class=\"android.widget.ImageView\" package=\"com.ucredit.sale.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[384,733][695,1044]\" resource-id=\"com.ucredit.sale.android:id/app_iv\" instance=\"1\"/><android.widget.TextView index=\"1\" text=\"海豚队长\" class=\"android.widget.TextView\" package=\"com.ucredit.sale.android\" content-desc=\"\" checkable=\"false\" checked=\"false\" clickable=\"false\" enabled=\"true\" focusable=\"false\" focused=\"false\" scrollable=\"false\" long-clickable=\"false\" password=\"false\" selected=\"false\" bounds=\"[395,1089][683,1186]\" resource-id=\"\" instance=\"0\"/></android.widget.LinearLayout></android.widget.RelativeLayout></android.widget.FrameLayout></android.widget.FrameLayout></android.widget.LinearLayout></android.widget.FrameLayout></hierarchy>";
        writeStr2XML(file, content);
        try {
            parseWriteStr2XML(file1, content);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
