<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp"%>
<link rel="stylesheet" href="css/assignRole.css">

<body>
<%@ include file="/WEB-INF/include-nav.jsp"%>
<div class="container-fluid">
    <div class="row">
        <%@ include file="/WEB-INF/include-sidebar.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li>
                    <a href="#">首页</a>
                </li>
                <li>
                    <a href="#">数据列表</a>
                </li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="assign/do/role/assign.html" method="post" role="form" class="form-inline" style="width: 700px">
                        <input type="hidden" name="adminId" value="${param.adminId }" />
                        <input type="hidden" name="pageNum" value="${param.pageNum }" />
                        <input type="hidden" name="keyword" value="${param.keyword }" />
                        <div class="form-group">
                            <label for="exampleInputPassword1">未分配角色列表</label>
                            <br>
                            <select class="form-control" multiple="" size="10" style="width: 150px; overflow-y: auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList }" var="role">
                                    <option value="${role.id }">${role.name }</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top: 20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left: 40px;">
                            <label for="exampleInputPassword1">已分配角色列表</label>
                            <br>
                <%--       上传的数据为id组成的list
                            提交的时候，默认由选中的id组成list来提交（黑影代表选中）
                            此处我们要求只要在该处，都提交，
                            所以需要为submit按钮绑定一个单击相应函数，来使所有的option被选中
                --%>
                            <select name="roleIdList" class="form-control" multiple="multiple" size="10" style="width: 150px; overflow-y: auto;">
                                <c:forEach items="${requestScope.assignedRoleList }" var="role">
                                    <option value="${role.id }">${role.name }</option>
                                </c:forEach>
                            </select>
                        </div>
                        <br>
                        <button id="submitBtn" style="width: 120px; height: 50px;float: right" type="submit" class="btn btn-lg btn-success btn-block saveButton">保存</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script type="text/javascript">
    $("#toRightBtn").click(function() {
        // select 是标签选择器
        // :eq(0)表示选择页面上的第一个select
        // :eq(1)表示选择页面上的第二个
        // “>”表示选择子元素
        // :selected 表示选择“被选中的”option
        // appendTo()能够将 jQuery 对象追加到指定的位置
        // 		console.log('右移')
        $("select:eq(0)>option:selected").appendTo("select:eq(1)");
    });

    $("#toLeftBtn").click(function() {
        // select 是标签选择器
        // :eq(0)表示选择页面上的第一个
        // :eq(1)表示选择页面上的第二个
        // “>”表示选择子元素
        // :selected 表示选择“被选中的”option
        // appendTo()能够将 jQuery 对象追加到指定的位置
        // 		console.log('左移')
        $("select:eq(1)>option:selected").appendTo("select:eq(0)");
    });

    $("#submitBtn").click(function() {
        $("select:eq(1)>option").prop("selected", "selected");
    });
</script>
</html>