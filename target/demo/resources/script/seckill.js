//存放秒杀交互式代码逻辑
//javascript 模块化
var seckill = {
    //封装秒杀相关的Ajax的url
    url : {
        now : function(){
            return "/seckill/time/now";
        },
        exposer : function(seckillId){
            return "/seckill/"+seckillId+"/exposer";
        },
        execution : function(seckillId,md5){
            return "/seckill/"+seckillId+"/"+md5+"/execution";
        }
    },
    //执行秒杀操作
    handleSeckillKill : function(seckillId,node){
        //执行秒杀逻辑 TODO
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.url.exposer(seckillId),{},function(result){
            //在回调函数中执行交互逻辑
            if(result && result['success']){
                var exposer = result['data'];
                //判断是否开启秒杀
                if(exposer['exposed']){
                    //开启秒杀,获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.url.execution(seckillId,md5);
                    console.log('killUrl:'+killUrl);
                    $('#killBtn').one("click",function(){
                        //执行秒杀请求
                        //1.先禁用按钮
                        $(this).addClass("disabled");
                        //发送请求执行秒杀
                        $.post(killUrl,{},function(result){
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                console.log("stateInfo :"+stateInfo);
                                console.log("state :"+state);
                                //显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }
                        });
                    });
                    node.show();
                }else{
                    //未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countdown(seckillId,now,start,end);
                }
            }else{
                console.log('result:'+result);
            }
        });
    },
    //验证手机号
    validatePhone : function(phone){
        if(phone && phone.length == 11 && !isNaN(phone)){ //undefine && length =12 && !isNaN(phone)表示是数字
            return true;
        }else{
            return false;
        }
    },
    countdown : function(seckillId,nowTime,startTime,endTime){
        var seckillBox = $("#seckill-box");
        //时间判断
        if(nowTime>endTime){//秒杀结束
            seckillBox.html('秒杀结束!');
        }else if(nowTime<startTime){//秒杀还未开始 开启计时事件绑定
            var killTime = new Date(startTime + 1000); //防止用户端时间偏移
            seckillBox.countdown(killTime,function(event){
                //控制时间的格式
                var format = event.strftime("秒杀倒计时: %D天 %H小时 %M分 %S秒");
                seckillBox.html(format);
            }).on('finish.countdown',function(){//时间完成后回调事件
                //获取秒杀地址，实现显示逻辑，执行秒杀
                seckill.handleSeckillKill(seckillId,seckillBox);//秒杀seckillId和标签节点对象
            });
        }else{//秒杀开始
            seckill.handleSeckillKill(seckillId,seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail : {
        //详情页初始化
        init : function (params) {
            //手机验证和登陆,计时交互
            //规划我们的交互流程
            //在cookie中查询手机号
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if(!seckill.validatePhone(killPhone)){
                //没有登陆，请绑定手机号
                //控制输出
                var killPhoneModel = $("#killPhoneModel");
                //将modal显示出来
                killPhoneModel.modal({
                    show : true, //显示弹出层
                    backdrop : "static", //禁止位置关闭
                    keyboard : false //关闭键盘事件
                });
                //点击提交按钮关闭modal框
                $("#killPhoneBtn").click(function(){
                    var inputPhone = $('#killPhoneKey').val();
                    console.log('inputPhone = ' + inputPhone);//TODO
                    if(seckill.validatePhone(inputPhone)){ //电话填写正确
                        //将电话写入cookie当中
                        $.cookie("killPhone",inputPhone,{
                            expires : 7, //cookie保存的时间为7天
                            path : "/seckill" //表示存放在那个路径下(即在那个路径下有效)
                        });
                        //刷新页面
                        window.location.reload();
                    }else{ //电话填写错误
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show('300');
                    }
                });
            }
            //用户已经登陆
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //计时交互
            $.get(seckill.url.now(),{},function(result) {
                if(result && result['success']){
                    var nowTime = result['data'];
                    //时间判断
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                }else{
                    console.log('result = '+result)
                }
            });
        }
    }
}