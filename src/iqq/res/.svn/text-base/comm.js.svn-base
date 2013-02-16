String.prototype.trim=function(){
    return this.replace(/(^\s*)|(\s*$)/g,"")
};
    
var sys={
    $:function(A){
        return document.getElementById(A)
    },
    onload:function(B){
        var A=window.onload;
        window.onload=function(){
            if(typeof (A)=="function"){
                A()
            }
            if(typeof (B)=="function"){
                B()
            }
        }
    },
    getQueryValue:function(C,B){
        var A="";
        if(B){
            A="&"+B
        }else{
            A=window.location.search.replace(/(^\?+)|(#\S*$)/g,"")
        }
        A=A.match(new RegExp("(^|&)"+C+"=([^&]*)(&|$)"));
        return !A?"":decodeURIComponent(A[2])
    }
};

function getCookie(A){
    return pt.cookie.get(A)
}
function setCookie(A,B){
    pt.cookie.set(A.value)
}
var pt={
    uin:0,
    ckNum:{},
    action:[0,0],
    submitN:{},
    err_m:null,
    isHttps:false,
    isIpad:false,
    mibao_css:"",
    needAt:"",
    t_appid:46000101,
    seller_id:703010802,
    needCodeTip:false,
    regmaster:0,
    chkUin:function(A){
        A=A.trim();
        if(A.length==0){
            return false
        }
        if(window.location.hostname.match(/paipai.com$/)){
            if(A.length<64&&(new RegExp(/^[A-Za-z0-9]+@{1}[A-Za-z0-9]+$/).test(A))){
                return true
            }
        }
        if(g_appid==pt.seller_id&&A.length<64&&(new RegExp(/^[A-Za-z0-9]+@{1}[0-9]+$/).test(A))){
            return true
        }
        pt.needAt="";
        var B=pt.chkAccount;
        if(t_appid==g_appid){
            if(B.isQQ(A)||B.isMail(A)){
                return true
            }else{
                if(B.isNick(A)||B.isName(A)){
                    pt.needAt="@"+encodeURIComponent(A);
                    return true
                }else{
                    if(B.isPhone(A)){
                        pt.needAt="@"+A.replace(/^(86|886)/,"");
                        return true
                    }else{
                        if(B.isSeaPhone(A)){
                            pt.needAt="@00"+A.replace(/^(00)/,"");
                            if(/^(@0088609)/.test(pt.needAt)){
                                pt.needAt=pt.needAt.replace(/^(@0088609)/,"@008869")
                            }
                            return true
                        }
                    }
                }
            }
            pt.needAt="";
            return false
        }else{
            if(B.isQQ(A)||B.isMail(A)){
                return true
            }
            if(B.isPhone(A)){
                pt.needAt="@"+A.replace(/^(86|886)/,"");
                return true
            }
            if(B.isNick(A)){
                $("u").value=A+"@qq.com";
                return true
            }
            return false
        }
    },
    chkAccount:{
        isQQ:function(A){
            return/^[1-9]{1}\d{4,9}$/.test(A)
        },
        isNick:function(A){
            return/^[a-zA-Z]{1}([a-zA-Z0-9]|[-_]){0,19}$/.test(A)
        },
        isName:function(A){
            if(A=="<请输入帐号>"){
                return false
            }
            return/[\u4E00-\u9FA5]/.test(A)?(A.length>8?false:true):false
        },
        isPhone:function(A){
            return/^(?:86|886|)1(?:(?:3\d{1})|44|(?:5[012789356]{1})|(?:8[065879]{1}))\d{8}$/.test(A)
        },
        isDXPhone:function(A){
            return/^(?:86|886|)1(?:33|53|80|89)\d{8}$/.test(A)
        },
        isSeaPhone:function(A){
            return/^(00)?(?:852|853|886(0)?\d{1})\d{8}$/.test(A)
        },
        isMail:function(A){
            return/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(A)
        }
    },
    cookie:{
        get:function(B){
            var A=document.cookie.match(new RegExp("(^| )"+B+"=([^;]*)(;|$)"));
            return A?A[2]:""
        },
        set:function(C,E){
            var A=arguments;
            var H=arguments.length;
            var B=(2<H)?A[2].toGMTString():"";
            var G=(3<H)?A[3]:"";
            var D=(4<H)?A[4]:"";
            var F=(5<H)?A[5]:false;
            document.cookie=C+"="+escape(E)+";expires ="+B+";path = "+G+";domain ="+D+((F==true)?";secure":" ")
        }
    },
    html:{
        encode:function(B){
            var A="";
            if(B.length==0){
                return""
            }
            A=B.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/  /g,"&nbsp; ").replace(/'/g,"&apos;").replace(/"/g,"&quot;");
            return A
        }
    },
    init:function(){
        if(pt.t_appid==g_appid){
            if(sys.$("u")){
                sys.$("u").setAttribute("style","")
            }
            sys.$("u").style.cssText=""
        }
        pt.isHttps=(/^https/g.test(window.location));
        sys.onload(function(){
            pt.err_m=sys.$("err_m");
            if(g_appid!=1003903){
                document.body.onclick=function(C){
                    C=C|window.event;
                    pt.action[0]++
                }
            }
            document.body.onkeydown=function(C){
                C=C|window.event;
                pt.action[1]++
            };
    
            if(Math.random()<0.1&&!pt.isHttps){
                pt.loadScript("http://mat1.gtimg.com/www/js/common.js",function(){
                    if(typeof checkNonTxDomain=="function"){
                        checkNonTxDomain(1,5)
                    }
                })
            }
        });
        pt.mibao_css=sys.getQueryValue("mibao_css");
        var A=navigator.userAgent.toLowerCase();
        pt.isIpad=/ipad/i.test(A);
        pt.needCodeTip=window.needCodeTip?needCodeTip:false;
        var B=document.loginform.regmaster?document.loginform.regmaster.value:"";
        if((B==2||B==3)&&!pt.isHttps){
            pt.regmaster=B
        }
    },
    show_err:function(B){
        var A=pt.html.encode(sys.$("u").value);
        B+='<a href="http://support.qq.com/write.shtml?guest=1&fid=713&SSTAG=10011-'+A+'" target="_blank">'+str_yjfk+"</a>";
        if(pt.err_m&&(typeof ptui_notifySize=="function")){
            pt.err_m.innerHTML=B;
            pt.err_m.style.display="block";
            ptui_notifySize("login");
            return
        }else{
            alert(B)
        }
    },
    hide_err:function(){
        if(pt.err_m&&(typeof ptui_notifySize=="function")){
            pt.err_m.innerHTML="";
            pt.err_m.style.display="none";
            ptui_notifySize("login");
            return
        }
    },
    loadScript:function(B,C){
        var A=document.createElement("script");
        A.charset="UTF-8";
        A.onload=A.onreadystatechange=function(){
            if(!this.readyState||this.readyState==="loaded"||this.readyState==="complete"){
                C?C():"";
                A.onload=A.onreadystatechange=null
            }
        };
    
        A.src=B;
        document.getElementsByTagName("head")[0].appendChild(A)
    },
    winName:{
        set:function(C,A){
            var B=window.name||"";
            if(B.match(new RegExp(";"+C+"=([^;]*)(;|$)"))){
                window.name=B.replace(new RegExp(";"+C+"=([^;]*)"),";"+C+"="+A)
            }else{
                window.name=B+";"+C+"="+A
            }
        },
        get:function(C){
            var B=window.name||"";
            var A=B.match(new RegExp(";"+C+"=([^;]*)(;|$)"));
            return A?A[1]:""
        },
        clear:function(B){
            var A=window.name||"";
            window.name=A.replace(new RegExp(";"+B+"=([^;]*)"),"")
        }
    }
};

pt.init();
var checkClock=0;
var lastUin=1;
var t_appid=46000101;
var g_changeNum=0;
var changeimg=false;
var defaultuin="";
var login_param=g_href.substring(g_href.indexOf("?")+1);
function ptui_onEnableLLogin(B){
    var A=B.low_login_enable;
    var C=B.low_login_hour;
    if(A!=null&&C!=null){
        C.disabled=!A.checked
    }
}
function ptui_setDefUin(C,B){
    if(!B){
        var B=unescape(pt.cookie.get("ptui_loginuin"));
        var A=pt.chkAccount;
        if(g_appid!=t_appid&&(A.isNick(B)||A.isName(B))){
            B=pt.cookie.get("pt2gguin").replace(/^o/,"")-0;
            B=B==0?"":B
        }
        defaultuin=B
    }
    if(B){
        C.u.value=B
    }
}
var g_ptredirect=-1;
var g_xmlhttp;
var g_loadcheck=true;
var g_submitting=false;
function ptui_needVC(B,C){
    if(pt.chkAccount.isQQ(B)){
        document.cookie="chkuin="+B+";domain=ptlogin2."+g_domain+";path=/"
    }
    B=pt.needAt?pt.needAt:B;
    if(pt.ckNum[B]){
        if(pt.ckNum[B]==2){
            pt.show_err("网络或服务器出错，请稍后再试");
            return
        }
        pt.ckNum[B]++
    }else{
        pt.ckNum={};
        
        pt.ckNum[B]=1
    }
    var A=(pt.isHttps?"https://ssl.":"http://check.")+"ptlogin2."+g_domain+"/check?";
    if(pt.regmaster==2){
        A="http://check.ptlogin2.function.qq.com/check?regmaster=2&"
    }else{
        if(pt.regmaster==3){
            A="http://check.ptlogin2.crm2.qq.com/check?regmaster=3&"
        }
    }
    A+="uin="+B+"&appid="+C+"&r="+Math.random();
    pt.loadScript(A);
    g_loadcheck=true;
    return
}
function ptui_checkVC(B,E,C){
    clearTimeout(checkClock);
    if(!C){
        g_uin="0";
        check();
        return
    }
    pt.ckNum[pt.needAt?pt.needAt:sys.$("u").value.trim()]=0;
    if(C=="\x10\x27\x00\x00\x00\x00\x00\x00"){
        g_uin="0";
        pt.show_err(str_inv_uin);
        return
    }
    pt.uin=C;
    if(!pt.submitN[C]){
        pt.submitN[C]=1
    }
    var G=new Date();
    g_time.time7=G;
    var F={
        "12":g_time.time7-g_time.time6
    };
        
    if(!curXui){
        ptui_speedReport(F)
    }
    g_loadcheck=false;
    if(g_submitting){
        return
    }
    if(B=="0"){
        $("verifycode").value=E;
        loadVC(false)
    }else{
        $("verifycode").value=pt.needCodeTip?str_codetip:"";
        loadVC(true)
    }
    var D=pt.cookie.get("chkuin");
    var A=pt.cookie.get("confirmuin");
    if(D==A&&D){
        ptui_reportAttr(195390)
    }else{
        if(D==A&&!D){
            ptui_reportAttr(195393)
        }else{
            if(D&&!A){
                ptui_reportAttr(195389)
            }else{
                if(!D&&A){
                    ptui_reportAttr(195392)
                }else{
                    if(D&&A&&D!=A){
                        ptui_reportAttr(195391)
                    }
                }
            }
        }
    }
}
function ptui_changeImg(E,D,H){
    changeimg=true;
    var G=pt.needAt?pt.needAt:g_uin;
    var B=(pt.isHttps?"https://ssl.":"http://")+"captcha."+E+"/getimage?&uin="+G+"&aid="+D+"&"+Math.random();
    var A=$("imgVerify");
    try{
        if(A!=null){
            A.src=B;
            var C=$("verifycode");
            if(C!=null&&C.disabled==false&&H){
                C.focus();
                C.select()
            }
        }
    }catch(F){}
}
function ptui_initFocus(B){
    if(pt.isIpad){
        return
    }
    try{
        var A=B.u;
        var D=B.p;
        var E=B.verifycode;
        if(A.value==""||str_uintip==A.value){
            A.focus();
            return
        }
        if(D.value==""){
            D.focus();
            return
        }
        if(E.value==""){
            E.focus()
        }
    }catch(C){}
}
function ajax_Submit(){
    var E=true;
    var C=document.forms[0];
    var A=C.action;
    var K=(pt.isHttps?A.replace(/^http:\/\//,"https://ssl."):A)+"?";
    var B=document.getElementById("login2qq");
    if(pt.regmaster==2){
        K="http://ptlogin2.function.qq.com/login?regmaster=2&"
    }else{
        if(pt.regmaster==3){
            K="http://ptlogin2.crm2.qq.com/login?regmaster=3&"
        }
    }
    for(var J=0;J<C.length;J++){
        if(C[J].name=="ipFlag"&&!C[J].checked){
            K+=C[J].name+"=-1&";
            continue
        }
        if(C[J].name=="fp"||C[J].type=="submit"){
            continue
        }
        if(C[J].name=="ptredirect"){
            g_ptredirect=C[J].value
        }
        if(C[J].name=="low_login_enable"&&(!C[J].checked)){
            E=false;
            continue
        }
        if(C[J].name=="low_login_hour"&&(!E)){
            continue
        }
        if(C[J].name=="webqq_type"&&!B&&(!C[J].checked)){
            continue
        }
        K+=C[J].name;
        K+="=";
        if(C[J].name=="u"&&pt.needAt){
            K+=pt.needAt+"&";
            continue
        }
        if(C[J].name=="p"){
            var M=C.p.value;
            var I=hexchar2bin(md5(M));
            var H=md5(I+pt.uin);
            var G=md5(H+C.verifycode.value.toUpperCase());
            K+=G
        }else{
            if(C[J].name=="u1"||C[J].name=="ep"){
                var D=C[J].value;
                var L="";
                if(g_appid=="1003903"&&B){
                    L=/\?/g.test(D)?"&":"?";
                    var F=document.getElementById("webqq_type").value;
                    L+="login2qq="+B.value+"&webqq_type="+F
                }
                K+=encodeURIComponent(D+L)
            }else{
                K+=C[J].value
            }
        }
        K+="&"
    }
    K+="fp=loginerroralert&action="+pt.action.join("-")+"-"+(new Date()-g_begTime)+"&mibao_css="+pt.mibao_css+"&t="+pt.submitN[pt.uin]+"&g=1";
    pt.winName.set("login_param",encodeURIComponent(login_param));
    pt.loadScript(K);
    return
}
function ptuiCB(D,B,C,H,G){
    function A(){
        var K=pt.cookie.get("uin");
        var I=pt.cookie.get("skey");
        if(K==""||I==""){
            var J=new Image();
            J.src="http://id.qq.com/cgi-bin/infolog?br="+browser_version()+"&r="+Math.random()+"&u="+$("u").value
        }
    }
    $("p").blur();
    g_time.time13=new Date();
    var F={
        "15":g_time.time13-g_time.time12
    };
    
    ptui_speedReport(F);
    if(C!=""){
        if(H!="0"){
            A()
        }
        pt.hide_err();
        switch(H){
            case"0":
                window.location.href=C;
                break;
            case"1":
                top.location.href=C;
                break;
            case"2":
                parent.location.href=C;
                break;
            default:
                top.location.href=C
        }
        return
    }
    g_submitting=false;
    if(D==0){
        top.location=document.forms[0].ul.value;
        return
    }else{
        if(pt.submitN[pt.uin]){
            pt.submitN[pt.uin]++
        }
        if(B==0){
            if(G&&G!=""){
                pt.show_err(G)
            }else{
                pt.show_err("您的输入有误，请重试。")
            }
        }else{
            pt.show_err(G);
            $("p").value="";
            $("p").focus();
            $("p").select()
        }
        if(isLoadVC){
            ptui_changeImg(g_domain,g_appid,true);
            $("verifycode").value=pt.needCodeTip?str_codetip:"";
            loadVC(true);
            $("verifycode").focus();
            $("verifycode").select()
        }else{
            if(B==0){
                g_uin=0
            }
        }
        if(D==3||D==4){
            if(navigator.userAgent.toLowerCase().indexOf("webkit")>-1){
                $("u").focus()
            }
            if(D==3){
                $("p").value=""
            }
            $("p").focus();
            $("p").select();
            if(D==4){
                try{
                    $("verifycode").focus();
                    $("verifycode").select()
                }catch(E){}
            }
            if(B!=0&&B!=102){
                $("verifycode").value=pt.needCodeTip?str_codetip:"";
                loadVC(true);
                g_submitting=true
            }
        }
    }
}
function browser_version(){
    var A=navigator.userAgent.toLowerCase();
    return A.match(/msie ([\d.]+)/)?1:A.match(/firefox\/([\d.]+)/)?3:A.match(/chrome\/([\d.]+)/)?5:A.match(/opera.([\d.]+)/)?9:A.match(/version\/([\d.]+).*safari/)?7:1
}
function ptui_reportSpeed(D,C){
    if(pt.isHttps||(window.flag2&&Math.random()>0.5)||(!window.flag2&&Math.random()>0.05)){
        return
    }
    var A=browser_version();
    url="http://isdspeed.qq.com/cgi-bin/r.cgi?flag1=6000&flag2="+(window.flag2?window.flag2:1)+"&flag3="+A;
    for(var B=0;B<g_speedArray.length;B++){
        url+="&"+g_speedArray[B][0]+"="+(g_speedArray[B][1]-D)
    }
    if(C!=0){
        url+="&4="+(D-C)
    }
    imgSendTimePoint=new Image();
    imgSendTimePoint.src=url+"&24="+g_appid
}
function ptui_reportAttr(A){
    if(Math.random()>0.05){
        return
    }
    url=(pt.isHttps?"https":"http")+"://ui.ptlogin2."+g_domain+"/cgi-bin/report?id="+A+"&t="+Math.random();
    imgAttr=new Image();
    imgAttr.src=url;
    imgAttr=null
}
function ptui_reportAttr2(A){
    url=(pt.isHttps?"https":"http")+"://ui.ptlogin2."+g_domain+"/cgi-bin/report?id="+A+"&t="+Math.random();
    imgAttr=new Image();
    imgAttr.src=url;
    imgAttr=null
}
function ptui_reportNum(B){
    if(Math.random()>0.05){
        return
    }
    url=(pt.isHttps?"https":"http")+"://ui.ptlogin2."+g_domain+"/cgi-bin/report?id=1000&n="+B;
    var A=new Image();
    A.src=url
}
function imgLoadReport(){
    if(changeimg){
        return
    }
    g_time.time8=new Date();
    var A={
        "11":g_time.time8-g_time.time7
    };
        
    if(!curXui){
        ptui_speedReport(A)
    }
}
function webLoginReport(){
    var A={};
    
    if(g_time.time0&&g_time.time0>0&&g_time.time1&&g_time.time1>0&&g_time.time2&&g_time.time2>0&&g_time.time3&&g_time.time3>0){
        A["18"]=g_time.time1-g_time.time0;
        A["19"]=g_time.time2-g_time.time0;
        A["20"]=g_time.time4-g_time.time0;
        A["21"]=g_time.time5-g_time.time0;
        A["7"]=g_time.time4-g_time.time3;
        A["26"]=g_time.time5-g_time.time3;
        ptui_speedReport(A)
    }
}
function ptui_speedReport(E){
    if(pt.isHttps||(window.flag2&&Math.random()>0.5)||(!window.flag2&&Math.random()>0.1)){
        return
    }
    var B="http://isdspeed.qq.com/cgi-bin/r.cgi?flag1=6000&flag2="+(window.flag2?window.flag2:1)+"&flag3="+browser_version();
    var C=0;
    for(var D in E){
        if(E[D]>300000||E[D]<0){
            continue
        }
        B+="&"+D+"="+E[D];
        C++
    }
    if(C==0){
        return
    }
    var A=new Image();
    A.src=B+"&24="+g_appid
}
function ptui_notifyClose(){
    try{
        if(parent.ptlogin2_onClose){
            parent.ptlogin2_onClose()
        }else{
            if(top==this){
                window.close()
            }
        }
    }catch(A){
        window.close()
    }
}
function ptui_setUinColor(D,B,A){
    var C=$(D);
    if(str_uintip==C.value){
        C.style.color=A
    }else{
        C.style.color=B
    }
}
function ptui_checkPwdOnInput(){
    if($("p").value.length>=16){
        return false
    }
    return true
}
function ptui_onLogin(A){
    try{
        if(parent.ptlogin2_onLogin){
            if(!parent.ptlogin2_onLogin()){
                return false
            }
        }
        if(parent.ptlogin2_onLoginEx){
            var D=A.u.value;
            var B=A.verifycode.value;
            if(str_uintip==D){
                D=""
            }
            if(!parent.ptlogin2_onLoginEx(D,B)){
                return false
            }
        }
    }catch(C){}
    return ptui_checkValidate(A)
}
function ptui_onLoginEx(B,C){
    g_time.time12=new Date();
    if(ptui_onLogin(B)){
        var A=new Date();
        A.setHours(A.getHours()+24*30);
        pt.cookie.set("ptui_loginuin",B.u.value,A,"/",C)
    }
    return false
}
function ptui_onReset(A){
    try{
        if(parent.ptlogin2_onReset){
            if(!parent.ptlogin2_onReset()){
                return false
            }
        }
    }catch(B){}
    return true
}
function ptui_checkValidate(B){
    var A=B.u;
    var D=B.p;
    var E=B.verifycode;
    if(A.value==""||str_uintip==A.value){
        pt.show_err(str_no_uin);
        A.focus();
        return false
    }
    A.value=A.value.trim();
    if(!pt.chkUin(A.value)){
        pt.show_err(str_inv_uin);
        A.focus();
        A.select();
        return false
    }
    if(D.value==""){
        pt.show_err(str_no_pwd);
        D.focus();
        return false
    }
    if(E.value==""){
        if(!isLoadVC){
            loadVC(true);
            g_submitting=true;
            return false
        }
        pt.show_err(str_no_vcode);
        try{
            E.focus()
        }catch(C){}
        if(!g_loadcheck){
            ptui_reportAttr(78028)
        }else{
            ptui_reportAttr(78029)
        }
        return false
    }
    if(E.value.length<4){
        pt.show_err(str_inv_vcode);
        E.focus();
        E.select();
        return false
    }
    if(isLoadVC&&!(/^[a-zA-Z0-9]+$/.test(E.value))){
        pt.show_err("请输入正确的验证码！");
        E.focus();
        E.select();
        return false
    }
    D.setAttribute("maxlength","32");
    ajax_Submit();
    ptui_reportNum(g_changeNum);
    g_changeNum=0;
    return true
}
var hexcase=1;
var b64pad="";
var chrsz=8;
var mode=32;
function md5(A){
    return hex_md5(A)
}
function hex_md5(A){
    return binl2hex(core_md5(str2binl(A),A.length*chrsz))
}
function str_md5(A){
    return binl2str(core_md5(str2binl(A),A.length*chrsz))
}
function hex_hmac_md5(A,B){
    return binl2hex(core_hmac_md5(A,B))
}
function b64_hmac_md5(A,B){
    return binl2b64(core_hmac_md5(A,B))
}
function str_hmac_md5(A,B){
    return binl2str(core_hmac_md5(A,B))
}
function core_md5(K,F){
    K[F>>5]|=128<<((F)%32);
    K[(((F+64)>>>9)<<4)+14]=F;
    var J=1732584193;
    var I=-271733879;
    var H=-1732584194;
    var G=271733878;
    for(var C=0;C<K.length;C+=16){
        var E=J;
        var D=I;
        var B=H;
        var A=G;
        J=md5_ff(J,I,H,G,K[C+0],7,-680876936);
        G=md5_ff(G,J,I,H,K[C+1],12,-389564586);
        H=md5_ff(H,G,J,I,K[C+2],17,606105819);
        I=md5_ff(I,H,G,J,K[C+3],22,-1044525330);
        J=md5_ff(J,I,H,G,K[C+4],7,-176418897);
        G=md5_ff(G,J,I,H,K[C+5],12,1200080426);
        H=md5_ff(H,G,J,I,K[C+6],17,-1473231341);
        I=md5_ff(I,H,G,J,K[C+7],22,-45705983);
        J=md5_ff(J,I,H,G,K[C+8],7,1770035416);
        G=md5_ff(G,J,I,H,K[C+9],12,-1958414417);
        H=md5_ff(H,G,J,I,K[C+10],17,-42063);
        I=md5_ff(I,H,G,J,K[C+11],22,-1990404162);
        J=md5_ff(J,I,H,G,K[C+12],7,1804603682);
        G=md5_ff(G,J,I,H,K[C+13],12,-40341101);
        H=md5_ff(H,G,J,I,K[C+14],17,-1502002290);
        I=md5_ff(I,H,G,J,K[C+15],22,1236535329);
        J=md5_gg(J,I,H,G,K[C+1],5,-165796510);
        G=md5_gg(G,J,I,H,K[C+6],9,-1069501632);
        H=md5_gg(H,G,J,I,K[C+11],14,643717713);
        I=md5_gg(I,H,G,J,K[C+0],20,-373897302);
        J=md5_gg(J,I,H,G,K[C+5],5,-701558691);
        G=md5_gg(G,J,I,H,K[C+10],9,38016083);
        H=md5_gg(H,G,J,I,K[C+15],14,-660478335);
        I=md5_gg(I,H,G,J,K[C+4],20,-405537848);
        J=md5_gg(J,I,H,G,K[C+9],5,568446438);
        G=md5_gg(G,J,I,H,K[C+14],9,-1019803690);
        H=md5_gg(H,G,J,I,K[C+3],14,-187363961);
        I=md5_gg(I,H,G,J,K[C+8],20,1163531501);
        J=md5_gg(J,I,H,G,K[C+13],5,-1444681467);
        G=md5_gg(G,J,I,H,K[C+2],9,-51403784);
        H=md5_gg(H,G,J,I,K[C+7],14,1735328473);
        I=md5_gg(I,H,G,J,K[C+12],20,-1926607734);
        J=md5_hh(J,I,H,G,K[C+5],4,-378558);
        G=md5_hh(G,J,I,H,K[C+8],11,-2022574463);
        H=md5_hh(H,G,J,I,K[C+11],16,1839030562);
        I=md5_hh(I,H,G,J,K[C+14],23,-35309556);
        J=md5_hh(J,I,H,G,K[C+1],4,-1530992060);
        G=md5_hh(G,J,I,H,K[C+4],11,1272893353);
        H=md5_hh(H,G,J,I,K[C+7],16,-155497632);
        I=md5_hh(I,H,G,J,K[C+10],23,-1094730640);
        J=md5_hh(J,I,H,G,K[C+13],4,681279174);
        G=md5_hh(G,J,I,H,K[C+0],11,-358537222);
        H=md5_hh(H,G,J,I,K[C+3],16,-722521979);
        I=md5_hh(I,H,G,J,K[C+6],23,76029189);
        J=md5_hh(J,I,H,G,K[C+9],4,-640364487);
        G=md5_hh(G,J,I,H,K[C+12],11,-421815835);
        H=md5_hh(H,G,J,I,K[C+15],16,530742520);
        I=md5_hh(I,H,G,J,K[C+2],23,-995338651);
        J=md5_ii(J,I,H,G,K[C+0],6,-198630844);
        G=md5_ii(G,J,I,H,K[C+7],10,1126891415);
        H=md5_ii(H,G,J,I,K[C+14],15,-1416354905);
        I=md5_ii(I,H,G,J,K[C+5],21,-57434055);
        J=md5_ii(J,I,H,G,K[C+12],6,1700485571);
        G=md5_ii(G,J,I,H,K[C+3],10,-1894986606);
        H=md5_ii(H,G,J,I,K[C+10],15,-1051523);
        I=md5_ii(I,H,G,J,K[C+1],21,-2054922799);
        J=md5_ii(J,I,H,G,K[C+8],6,1873313359);
        G=md5_ii(G,J,I,H,K[C+15],10,-30611744);
        H=md5_ii(H,G,J,I,K[C+6],15,-1560198380);
        I=md5_ii(I,H,G,J,K[C+13],21,1309151649);
        J=md5_ii(J,I,H,G,K[C+4],6,-145523070);
        G=md5_ii(G,J,I,H,K[C+11],10,-1120210379);
        H=md5_ii(H,G,J,I,K[C+2],15,718787259);
        I=md5_ii(I,H,G,J,K[C+9],21,-343485551);
        J=safe_add(J,E);
        I=safe_add(I,D);
        H=safe_add(H,B);
        G=safe_add(G,A)
    }
    if(mode==16){
        return Array(I,H)
    }else{
        return Array(J,I,H,G)
    }
}
function md5_cmn(F,C,B,A,E,D){
    return safe_add(bit_rol(safe_add(safe_add(C,F),safe_add(A,D)),E),B)
}
function md5_ff(C,B,G,F,A,E,D){
    return md5_cmn((B&G)|((~B)&F),C,B,A,E,D)
}
function md5_gg(C,B,G,F,A,E,D){
    return md5_cmn((B&F)|(G&(~F)),C,B,A,E,D)
}
function md5_hh(C,B,G,F,A,E,D){
    return md5_cmn(B^G^F,C,B,A,E,D)
}
function md5_ii(C,B,G,F,A,E,D){
    return md5_cmn(G^(B|(~F)),C,B,A,E,D)
}
function core_hmac_md5(C,F){
    var E=str2binl(C);
    if(E.length>16){
        E=core_md5(E,C.length*chrsz)
    }
    var A=Array(16),D=Array(16);
    for(var B=0;B<16;B++){
        A[B]=E[B]^909522486;
        D[B]=E[B]^1549556828
    }
    var G=core_md5(A.concat(str2binl(F)),512+F.length*chrsz);
    return core_md5(D.concat(G),512+128)
}
function safe_add(A,D){
    var C=(A&65535)+(D&65535);
    var B=(A>>16)+(D>>16)+(C>>16);
    return(B<<16)|(C&65535)
}
function bit_rol(A,B){
    return(A<<B)|(A>>>(32-B))
}
function str2binl(D){
    var C=Array();
    var A=(1<<chrsz)-1;
    for(var B=0;B<D.length*chrsz;B+=chrsz){
        C[B>>5]|=(D.charCodeAt(B/chrsz)&A)<<(B%32)
    }
    return C
}
function binl2str(C){
    var D="";
    var A=(1<<chrsz)-1;
    for(var B=0;B<C.length*32;B+=chrsz){
        D+=String.fromCharCode((C[B>>5]>>>(B%32))&A)
    }
    return D
}
function binl2hex(C){
    var B=hexcase?"0123456789ABCDEF":"0123456789abcdef";
    var D="";
    for(var A=0;A<C.length*4;A++){
        D+=B.charAt((C[A>>2]>>((A%4)*8+4))&15)+B.charAt((C[A>>2]>>((A%4)*8))&15)
    }
    return D
}
function binl2b64(D){
    var C="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    var F="";
    for(var B=0;B<D.length*4;B+=3){
        var E=(((D[B>>2]>>8*(B%4))&255)<<16)|(((D[B+1>>2]>>8*((B+1)%4))&255)<<8)|((D[B+2>>2]>>8*((B+2)%4))&255);
        for(var A=0;A<4;A++){
            if(B*8+A*6>D.length*32){
                F+=b64pad
            }else{
                F+=C.charAt((E>>6*(3-A))&63)
            }
        }
    }
    return F
}
function hexchar2bin(str){
    var arr=[];
    for(var i=0;i<str.length;i=i+2){
        arr.push("\\x"+str.substr(i,2))
    }
    arr=arr.join("");
    eval("var temp = '"+arr+"'");
    return temp
}
function uin2hex(str){
    var maxLength=16;
    str=parseInt(str);
    var hex=str.toString(16);
    var len=hex.length;
    for(var i=len;i<maxLength;i++){
        hex="0"+hex
    }
    var arr=[];
    for(var j=0;j<maxLength;j+=2){
        arr.push("\\x"+hex.substr(j,2))
    }
    var result=arr.join("");
    eval('result="'+result+'"');
    return result
}
isAbleSubmit=true;
function checkTimeout(){
    var A=$("u").value.trim();
    if(pt.chkAccount.isQQ(A)){
        pt.uin=uin2hex(A);
        $("verifycode").value="";
        loadVC(true)
    }
    ptui_reportAttr2(216082)
}
function check(){
    g_time.time6=new Date();
    g_changeNum++;
    var A=$("u").value.trim();
    $("u").value=A;
    if(g_uin==A||(!pt.chkUin(A))){
        return
    }
    clearTimeout(checkClock);
    checkClock=setTimeout("checkTimeout()",2000);
    g_uin=$("u").value.trim();
    try{
        if(parent.ptui_uin){
            parent.ptui_uin(g_uin)
        }
    }catch(B){}
    ptui_needVC(g_uin,g_appid)
}
function loadVC(A){
    if(isLoadVC==A&&(lastUin==g_uin)){
        return
    }
    lastUin=g_uin;
    isLoadVC=A;
    if(A==true){
        var B=$("imgVerify");
        var F=pt.needAt?pt.needAt:g_uin;
        var E=(pt.isHttps?"https://ssl.":"http://")+"captcha."+g_domain+"/getimage?aid="+g_appid+"&r="+Math.random()+"&uin="+F;
        var D=new Date();
        B.src=E;
        $("verifyinput").style.display="";
        $("verifytip").style.display="";
        $("verifyshow").style.display="";
        ptui_notifySize("login");
        try{
            $("p").focus()
        }catch(C){}
    }else{
        $("verifyinput").style.display="none";
        $("verifytip").style.display="none";
        $("verifyshow").style.display="none";
        ptui_notifySize("login");
        try{
            $("p").focus()
        }catch(C){}
    }
}
function onPageClose(){
    ptui_notifyClose()
}
function onFormReset(A){
    if(ptui_onReset(A)){
        A.u.style.color="#CCCCCC";
        return true
    }
    return false
}
function onClickForgetPwd(){
    var B=$("u");
    var A=$("label_forget_pwd");
    if(sys.getQueryValue("fgt")==2052){
        g_forget=g_forget.replace("1028",2052)
    }
    A.href=g_forget;
    if(B!=null&&B.value!=str_uintip){
        if(A.href.indexOf("?")==-1){
            A.href+="?"
        }else{
            A.href+="&"
        }
        A.href+="aquin="+B.value
    }
    return true
};