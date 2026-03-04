package com.atguigu.gmall.common.constant;

public class COM_CONSTANT {

    public static final String DES_CODE = "mars.qa-team@ucredit.com";  // DES用户加密原秘钥
    public static final String SPLIT_REGEX = "##";  //用户加密秘钥盐字符

    public static final String SRC_PATH = "src"; // 附件存储根目录
    public static final String APP_PATH = "app"; // 应用存储目录
    public static final String QRCODE_PATH = "qrcode"; // 二维码存储路径
    public static final String APP_ICON = "appicon"; // 应用图标存储路径
    public static final String APP_ICON_UPDECODED = "updecoded"; // 应用图标转换中间文件存储路径
    public static final String APP_ICON__DOWNDECODED = "downdecoded"; // 应用图标转换中间文件存储路径
    public static final String PLIST_PATH = "plist"; // iOS应用plist文件存储路径

    public static final String API_LOG = "api-log"; // 应用存储目录
    public static final String API_HTML = "api-html"; // 二维码存储路径
    public static final String API_JTL = "api-jtl"; // 应用图标存储路径

    public static final String CER_PATH = "cer"; //移动端证书存储路径



    public static final String APPLICATION_ICON_120 = "application-icon-120";
    public static final String APPLICATION_ICON_160 = "application-icon-160";
    public static final String APPLICATION_ICON_240 = "application-icon-240";
    public static final String APPLICATION_ICON_320 = "application-icon-320";

    public static final String DEPLOY_APP_WITH_MANUAL_UPLOAD = "手工上传";
    public static final String DEPLOY_APP_WITH_AUTOMATION_SCRIPT = "脚本自动上传";
    public static final String DEPLOY_APP_WITH_MANUAL_GIT_PULL = "页面触发git自动编译上传";
    public static final String DEPLOY_APP_WITH_GIT_PUSHHOOK = "系统监听git push hook自动编译上传";

    // GIT WEB_HOOK STATUS
    public static final String X_GITLAB_EVENT = "X-Gitlab-Event";
    public static final String X_GITLAB_EVENT_PUSH_HOOK = "Push Hook";
    public static final String X_GITLAB_EVENT_TAG_PUSH_HOOK = "Tag Push Hook";
    public static final String X_GITLAB_EVENT_ISSUR_HOOK = "Issue Hook";
    public static final String X_GITLAB_EVENT_NOTE_HOOK = "Note Hook";
    public static final String X_GITLAB_EVENT_MERGE_REQUEST_HOOK = "Merge Request Hook";
    public static final String X_GITLAB_EVENT_WIKI_PAGE_HOOK = "Wiki Page Hook";
    public static final String X_GITLAB_EVENT_PIPELINE_HOOK = "Pipeline Hook";
    public static final String X_GITLAB_EVENT_BUILD_HOOK = "Build Hook";

    public static final String OBJECT_KIND = "object_kind";
    public static final String OBJECT_KIND_PUSH = "push";
    public static final String OBJECT_KIND_TAG_PUSH = "tag_push";
    public static final String OBJECT_KIND_ISSUE = "issue";
    public static final String OBJECT_KIND_NOTE = "note";
    public static final String OBJECT_KIND_MERGE_REQUEST = "merge_request";
    public static final String OBJECT_KIND_WIKI_PAGE = "wiki_page";
    public static final String OBJECT_KIND_PIPELINE = "pipeline";
    public static final String OBJECT_KIND_BUILD = "build";




}
