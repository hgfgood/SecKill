// javascript 模块化

var seckill = {
    ERROR: {
        PHONE_ERROR: '手机号错误',
    },
    //封装秒杀相关的地址信息
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execute: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execute';
        }
    },
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        }
        else {
            console.log(phone);
            // console.log(phone.length == 11);
            console.log(isNaN(phone));
            return false;
        }
    },
    handlerSecKill: function (seckillid, node) {
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>')

        //获取秒杀地址
        $.post(seckill.URL.exposer(seckillid), function (result) {
            if (result && result['success']) {
                var data = result['data'];
                if (data['canExpose']) {//开启秒杀
                    var md5 = data['md5'];//秒杀路径
                    var killUrl = seckill.URL.execute(seckillid, md5);
                    //设置按钮的一次性链接
                    $('#killBtn').one('click', function () {
                        $(this).addClass('disable');//设置按钮不可见
                        //发送秒杀请求
                        $.post(killUrl,{}, function(result){
                            if(result && result['success']){
                                //请求成功
                                var data = result['data'];
                                var status = data['status'];
                                var killInfo = data['secKillInfo'];

                                node.html('<label class="label label-success">'+killInfo+'</label>');
                                if(status<=0){// 没有秒杀成功

                                }
                                else if(status == 1){// 秒杀成功

                                }
                            }
                            else{
                                console.log(result);//TODO
                            }
                        });
                    });
                    node.show();
                }
                else {//有可能是用户的浏览器计时过快
                    var now = data['now'];
                    var start = data['start'];
                    var end = data['end'];
                    seckill.countdown(seckillid, now, start, end);
                }

            }
            else {
                console.log(result);//TODO
            }
        });
    },
    countdown: function (seckillid, now, starttime, endtime) {
        var seckillBox = $('#seckill-box');

        if (now < starttime) {    //还没有开始
            //倒计时
            //计时结束时处理
            var killTime = new Date(starttime + 1000);
            /**
             * countdown(倒计时借宿时间毫秒，倒计时事件）
             */
            seckillBox.countdown(killTime, function (event) {
                var formatedtime = event.strftime(' 秒杀倒计时：%D天%H小时%M分钟%S秒');
                seckillBox.html(formatedtime);
            }).on('finish-countdown', function (event) { //即使结束后，显示秒杀界面
                seckill.handlerSecKill(seckillid, seckillBox);
            });

        }
        if (endtime < now) { // 秒杀已经结束
            //返回结束了
            seckillBox.html('秒杀结束！');
        }
        if (starttime <= now && endtime >= now) {//正在秒杀过程中
            seckill.handlerSecKill(seckillid, seckillBox);
        }
    },
    //详情页秒杀详情
    detail: {
        //详情页初始化
        init: function (param) {
            //手机验证和计时交互
            //规划交互流程

            //在cookie中查找手机号
            var killphone = $.cookie('killPhone');
            var seckillid = param['seckillid'];
            var starttime = param['starttime'];
            var endtime = param['endtime'];
            if (seckill.validatePhone(killphone)) {
                //成功登录

                //1. 获取系统时间
                $.get(seckill.URL.now(), {}, function (result) {
                    if (result && result['success']) {
                        var now = result['data'];
                        seckill.countdown(seckillid, now, starttime, endtime);

                    }
                    else {
                        console.log('获取时间失败！>>');//TODO
                    }
                })
            }
            else {
                //登录失败,绑定手机号
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,  //显示modal div
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false //键盘时间关闭
                })

                $('#killPhoneBtn').click(function () {
                    var inputphone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputphone)) {
                        //写入cookie:设置cookie的有效时间和路径
                        $.cookie('killPhone', inputphone, {expire: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    }
                    else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">' + seckill.ERROR.PHONE_ERROR + '</label>').show(300);
                    }
                });
            }

        }
    }
}