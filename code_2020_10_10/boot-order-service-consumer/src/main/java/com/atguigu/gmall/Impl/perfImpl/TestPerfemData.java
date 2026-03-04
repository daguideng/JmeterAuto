package com.atguigu.gmall.Impl.perfImpl;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-4
 */

/**
@Scope("prototype")
@Controller
@RequestMapping("/jmeterperf")


public class TestPerfemData {



    @Autowired
    private JmeterPerfPostParameter jmeterPerfPostParameter ;



    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody


    public ResponseResult addUser(HttpServletRequest request,
                                        HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");

        Map <String, Object> map = new HashMap();

        for (String pertype : JmeterPerfType.perfType()) {

            System.out.println("jmeterPerfPostParameter="+jmeterPerfPostParameter) ;

            String xxx = jmeterPerfPostParameter.map.get("并发用户数").toString() ;

            System.out.println("xxxxxx="+xxx) ;

            String value = jmeterPerfPostParameter.getMap().get(pertype).toString();
            System.out.println(pertype + "=" + value);
            map.put(pertype, value);

        }



        return new ResponseResult(ResponseMeta.SUCCESS,map);


    }



}

 ***/
