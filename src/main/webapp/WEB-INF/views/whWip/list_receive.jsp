<%-- 
    Document   : list_receive
    Created on : Jun 22, 2023, 9:38:58 AM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/css/005.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">
        <style>
            @media print {
                table thead {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
                table tbody {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
            }
            .dataTables_wrapper .dt-buttons {
                float:none;
                text-align:right;
            }
        </style>
    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>WIP Management [Stress WIP]</h1>
            <div class="row">
                <div class="col-lg-6">
                    <div class="main-box">
                        <h2>Scan GTS Number</h2>
                        <form id="add_mp_list_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateReceive" method="post">
                            <div class="form-group">
                                <label for="boxNo" class="col-lg-3 control-label">GTS Number *</label>
                                <div class="col-lg-6">
                                    <input type="text" class="form-control" id="boxNo" name="boxNo" placeholder="" value="" autofocus="autofocus" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="location" class="col-lg-3 control-label">Kanban Location *</label>
                                <div class="col-lg-6">
                                    <input type="text" class="form-control" id="location" name="location" placeholder="" value="" autofocus="autofocus" required>
                                </div>
                            </div>
                            <a href="${contextPath}/whWip/listNew" class="btn btn-info pull-left" style="font-family:'Orbitron', monospace;"><i class='bx bxs-chevron-left bx-fw'></i> Back</a>
                            <div class="pull-right">
                                <button type="reset" class="btn btn-secondary cancel" style="font-family:'Orbitron', monospace;"><i class='bx bx-reset bx-fw' ></i> Reset</button>
                                <button type="submit" class="btn btn-primary" style="font-family:'Orbitron', monospace;"><i class='bx bxs-shield-plus bx-fw' style='color:#ffffff'  ></i> Receive</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
        <div class="col-lg-12">
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box clearfix">
                        <div class="clearfix">
                            <h2 class="pull-left">WIP [Stress] List</h2>
                        </div>
                        <hr/>
                        <div class="clearfix">
                            <div class="filter-block pull-right">
                                <div id="dt_spml_tt" class="form-group pull-left" style="margin-right: 5px;"></div>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <input type="hidden" class="form-control" id="count" name="count" value="${count}">
                            <input type="hidden" class="form-control" id="countAll" name="countAll" value="${countAll}">
                            <table id="dt_spml" class="table">
                                <thead>
                                    <tr>
                                        <th><span>No</span></th>
                                        <th><span>GTS Number</span></th>
                                        <th><span>RMS Event</span></th>
                                        <th><span>Intervals</span></th>
                                        <th><span>Quantity</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${wipList}" var="wipList" varStatus="wipLoop">
                                        <tr>
                                            <td><c:out value="${wipLoop.index+1}"/></td>
                                            <td><c:out value="${wipList.gtsNo}"/></td>
                                            <td><c:out value="${wipList.rmsEvent}"/></td>
                                            <td><c:out value="${wipList.intervals}"/></td>
                                            <td><c:out value="${wipList.quantity}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="page_js">
        <!--print-->
        <script src="${contextPath}/resources/private/datatables/js/jquery.dataTables.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/dataTables.buttons.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.print.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.flash.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.html5.min.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {
                jQuery.extend(jQuery.validator.messages, {
                    required: "This field is required.",
                    equalTo: "Value is not match! Please re-scan.",
                    email: "Please enter a valid email.",
                });
                
                var validator = $("#add_mp_list_form").validate({
                    rules: {
                        boxNo: {
                            required: true
                        },
                        location: {
                            required: true
                        }
                    }
                });
                
//                oTable.search($(this).val()).draw();
                
                oTable = $('#dt_spml').DataTable({
                    dom: 'Brtip'
                });
                
                $('#dt_spml_search').keyup(function () {
                    oTable.search($(this).val()).draw();
                });
                
                $('#boxNo').keyup(function () {
                    oTable.search($(this).val()).draw();
                });
                
                $("#dt_spml_rows").change(function () {
                    oTable.page.len($(this).val()).draw();
                });
                
                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                $('#boxNo').bind('copy paste cut', function (e) {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
                $('#location').bind('copy paste cut', function (e) {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>