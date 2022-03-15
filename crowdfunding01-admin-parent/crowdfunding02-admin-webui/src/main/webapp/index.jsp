<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Title</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort }${pageContext.request.contextPath}/">


    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
<%--  弹窗js样式  必须放到jQuery后面，其引用了jQuery  --%>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function (){
            $("#btn1").click(function (){
                $.ajax({
                    "url":"send/array/btn1.html",
                    "type":"post",
                    "data":{
                        "array":[1,2,3]
                    },
                    "dataType":"text",
                    "success":function (response){
                        alert(response);
                        $("#btn1").innerText = response;
                    },
                    "error":function (response){
                        alert(response);
                    }
                })
            })
        });


        $(function (){
            $("#btn2").click(function (){

                var array = [1,2,3,4,5];
                var resp = JSON.stringify(array);


                $.ajax({
                    "url":"send/array/btn2.json",
                    "type":"post",
                    "data":resp,
                    "dataType":"text",
                    "contentType" : "application/json;charset=UTF-8",
                    "success":function (response){
                        alert(response);
                        $("#btn1").innerText = response;
                    },
                    "error":function (response){
                        alert(response);
                    }
                })

            })
        })

        $(function (){
            $("#btn3").click(function() {
                // 准备要发送的数据
                var student = {
                    "stuId" : 1,
                    "stuName" : "鲁昶材",
                    "address" : {
                        "province" : "山西",
                        "city" : "北京",
                        "street" : "后端"
                    },
                    "subjectList" : [
                        {
                        "subjectName" : "JavaSE",
                        "subjectScore" : 100
                        },
                        {
                        "subjectName" : "SSM",
                        "subjectScore" : 99
                        }
                    ],
                    "map" : {
                        "k1" : "v1",
                        "k2" : "v2"
                    }
                };

                // 将JSON对象转换为JSON字符串
                var requestBody = JSON.stringify(student);

                // 发送Ajax请求
                $.ajax({
                    "url" : "send/array/object.json",
                    "type" : "post",
                    "data" : requestBody,
                    "contentType" : "application/json;charset=UTF-8",
                    "dataType" : "text",
                    "success" : function(response) {
                        console.log('请求成功',response);
                    },
                    "error" : function(response) {
                        console.log('请求失败',response);
                    }
                });

            });
        })

        $(function (){
            $("#btn4").click(function() {
                // 准备要发送的数据
                var student = {
                    "stuId" : 1,
                    "stuName" : "鲁昶材",
                    "address" : {
                        "province" : "山西",
                        "city" : "北京",
                        "street" : "后端"
                    },
                    "subjectList" : [
                        {
                            "subjectName" : "JavaSE",
                            "subjectScore" : 100
                        },
                        {
                            "subjectName" : "SSM",
                            "subjectScore" : 99
                        }
                    ],
                    "map" : {
                        "k1" : "v1",
                        "k2" : "v2"
                    }
                };

                // 将JSON对象转换为JSON字符串
                var requestBody = JSON.stringify(student);

                // 发送Ajax请求
                $.ajax({
                    "url" : "send/array/AjaxEntity.sss",
                    "type" : "post",
                    "data" : requestBody,
                    "contentType" : "application/json;charset=UTF-8",
                    "dataType" : "json",
                    "success" : function(response) {
                        console.log('请求成功',response);
                    },
                    "error" : function(response) {
                        console.log('请求失败',response);
                    }
                });

            });


            $("#btn5").click(function (){
                layer.msg("已添加成功")
            //    还有许多丰富功能
            })
        })


    </script>
</head>
<body>
    <a href="test/ssm.html">SSM</a>
    <br>
    <button id="btn1">btn1</button>
    <br>
    <button id="btn2">btn2</button>
    <br>
    <button id="btn3">btn3</button>
    <br>
    <button id="btn4">btn4</button>
    <br>
    <button id="btn5">btn5</button>

    <a href="admin/to/login/page.html">start admin login</a>
</body>
</html>
