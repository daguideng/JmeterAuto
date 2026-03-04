package com.atguigu.gmall.Impl.perfImpl;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-9
 */


/**************************************
 * @author E-mail:34782655@qq.com
 * @version 创建时间：2017年6月23日 下午9:18:19
 * 类说明:
 *     mybatis逆向工程main函数
 ***************************************
 */

public class GenMain {

    /**
    public static void main(String[] args) {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //如果这里出现空指针，直接写绝对路径即可。
        String genCfg = "src/main/resources/templates/common/MyBatisGenerator.xml";

        File configFile = new File(genCfg);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;

        System.out.println("cp="+cp) ;
        try {
            config = cp.parseConfiguration(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        }
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = null;
        try {
            myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            myBatisGenerator.generate(null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

     **/
}
