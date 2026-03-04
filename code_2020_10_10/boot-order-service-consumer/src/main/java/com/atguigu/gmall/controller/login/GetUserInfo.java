package com.atguigu.gmall.controller.login;


import com.atguigu.gmall.common.bean.response.Result;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.service.impl.LoginServerImpl;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-8-19
 */
@Slf4j
@RestController
public class GetUserInfo {



    @Autowired
    private LoginServerImpl loginServer ;

    /**
     * 用户信息:
     */
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public Result<?> list(@RequestParam(required = true) String  token, HttpSession session)
            throws Exception {


        Result <Map<String, Object>> result = new Result <>();

        Map <String, Object> map = new HashedMap();

        String userpwd = String.valueOf(loginServer.maptoken.get(token));

        log.info("token---->"+token);
        log.info("userpwd---->"+userpwd);

        if (null != userpwd && !"".equals(userpwd)) {

            //权限：
            map.put("addRole", "新增角色");
            map.put("editPermission", "编辑权限");
            map.put("roles", "admin");  //"admin,editor"
            map.put("switchRoles", "切换权限");
            map.put("tips", "在某些情况下，不适合使用 v-permission。例如：Element-UI 的 Tab 组件或 el-table-column 以及其它动态渲染 dom 的场景。你只能通过手动设置 v-if 来实现");
            map.put("delete", "删除");
            map.put("confirm", "确定");
            map.put("cancel", "取消");
            map.put("code",20000);
            map.put("message","sucess");


           // http://img.account.itpub.net/head/026/84/15/78.jpg
          //  https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif
          // https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568007506951&di=c95114d5741fbfa12675fa6eb1e477d7&imgtype=0&src=http%3A%2F%2Fimg.aiimg.com%2Fuploads%2Fallimg%2F140315%2F263915-140315194H9.jpg

            Map<String,Object> mapdata = new HashedMap();
            mapdata.put("name","admin");
            mapdata.put("avatar","http://img.aiimg.com/uploads/allimg/140315/263915-140315194H9.jpg");
            mapdata.put("introduction","I am a super administrator");


            map.put("data",mapdata);

            result.setData(map);


          //  map.put("data",datamap);

         //   logger.info("roles的map{}",map);

        //    result.setData(map);
        //    result.setStatus(RES_STATUS.SUCCESS);

            return result;

        }


        map.put("msg","token验证不通过,请确认用户名或密码正确");
        result.setData(map);
        result.setStatus(RES_STATUS.SUCCESS);


        return result;
    }



}
