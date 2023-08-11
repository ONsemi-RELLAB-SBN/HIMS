<%-- 
    Document   : email_list_add
    Created on : Aug 11, 2023, 9:35:49 AM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
    </s:layout-component>
    <s:layout-component name="page_css_inline">
    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>Add Email List</h1>
            <div class="row">
                <div class="col-lg-6">
                    <div class="main-box">
                        <h2>User Information</h2>
                        <form class="form-horizontal" id="add_email_form" role="form" action="${contextPath}/whWip/addEmailList" method="post">
                            <div class="form-group">
                                <label for="username" class="col-lg-4 control-label">User Name *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="username" name="username" placeholder="User Name" value="${username}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="col-lg-4 control-label">Email Address *</label>
                                <div class="col-lg-8">
                                    <input type="email" class="form-control" id="email" name="email" placeholder="Email Address" value="${email}">
                                </div>
                            </div>
                                <div class="form-group">
                                <label for="task" class="col-lg-4 control-label">Task *</label>
                                <div class="col-lg-8">
                                    <select id="task" name="task" class="form-control">
                                        <option value="" selected="">Select Task...</option>
                                        <c:forEach items="${statusList}" var="group">
                                            <option value="${group.value}" ${group.selected}>${group.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <a href="${contextPath}/whWip/emailList" class="btn btn-info pull-left"><i class='bx bx-left-arrow bx-fw' ></i> Back</a>
                            <div class="pull-right">
                                <button type="reset" class="btn btn-secondary cancel"><i class='bx bx-reset bx-fw' ></i>Reset</button>
                                <button type="submit" class="btn btn-primary" ><i class='bx bx-save bx-fw' ></i>Save</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="page_js">
        <script src="${contextPath}/resources/validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {
                var validator = $("#add_email_form").validate({
                    rules: {
                        username: {
                            required: true
                        },
                        email: {
                            required: true,
                            email: true
                        },
                        task: {
                            required: true
                        }
                    }
                });
                $(".cancel").click(function () {
                    validator.resetForm();
                });
            });
        </script>
    </s:layout-component>
</s:layout-render> 