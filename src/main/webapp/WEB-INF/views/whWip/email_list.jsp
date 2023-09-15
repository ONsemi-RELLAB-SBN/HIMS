<%-- 
    Document   : user_email_list
    Created on : Aug 10, 2023, 3:20:36 PM
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
            span.mode2 {
                rotate: 270deg;
                margin-bottom: 15px;
            }
        </style>
    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>WIP Management</h1>
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box clearfix">
                        <div class="clearfix">
                            <h2 class="pull-left">Email List</h2>
                            <div class="filter-block pull-right">
                                <a href="${contextPath}/whWip/emailList/add" class="btn btn-primary pull-right">
                                    <i class='bx bxs-user-plus bx-md bx-fw' ></i> Add User
                                </a>
                            </div>
                            <div class="filter-block pull-right">
                            </div>
                        </div>
                        <hr/>
                        <div class="clearfix">
                            <div class="form-group pull-left">
                                <select id="dt_spml_rows" class="form-control">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select>
                            </div>
                            <div class="filter-block pull-right">
                                <div id="dt_spml_tt" class="form-group pull-left" style="margin-right: 5px;">
                                </div>
                                <div class="form-group pull-left" style="margin-right: 0px;">
                                    <input id="dt_spml_search" type="text" class="form-control" placeholder="<f:message key="general.label.search"/>">
                                    <i class="fa fa-search search-icon"></i>
                                </div>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table id="dt_spml" class="table">
                                <thead>
                                    <tr>
                                        <th><span>No</span></th>
                                        <th><span>User Name</span></th>
                                        <th><span>Email Address</span></th>
                                        <th><span class="mode2">Stress<br>Receive</span></th>
                                        <th><span class="mode2">Stress<br>Verify</span></th>
                                        <th><span class="mode2">Stress<br>Loading</span></th>
                                        <th><span class="mode2">Stress<br>Unloading</span></th>
                                        <th><span class="mode2">Stress<br>Ship</span></th>
                                        <th><span class="mode2">Storage<br>Inventory</span></th>
                                        <th><span class="mode2">Storage<br>Ship</span></th>
                                        <th><span class="mode2">Admin</span></th>
                                        <th><span class="mode2">System</span></th>
                                        <th><span class="mode2">Active</span></th>
                                        <th><span>Manage</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${emailList}" var="wipList" varStatus="whLoop">
                                        <tr>
                                            <td><c:out value="${whLoop.index+1}"/></td>
                                            <td><c:out value="${wipList.username}"/></td>
                                            <td><c:out value="${wipList.email}"/></td>
                                            <c:if test="${wipList.isReceive == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isReceive == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isVerify == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isVerify == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isLoad == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isLoad == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isUnload == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isUnload == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isShip == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isShip == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isReceiveStorage == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isReceiveStorage == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isShipStorage == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isShipStorage == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isAdmin == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isAdmin == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isSystem == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isSystem == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isActive == 1}">
                                                <td><i class='bx bx-pull-right bx-checkbox-checked bx-lg bx-fw'></i></i></td>
                                            </c:if>
                                            <c:if test="${wipList.isActive == 0}">
                                                <td><i class='bx bx-pull-right bx-checkbox bx-lg bx-fw' ></i></td>
                                            </c:if>
                                            <td>
                                                <a href="${contextPath}/whWip/emailList/edit/${wipList.id}" >
                                                    <i class='bx bx-edit bx-md bx-fw' ></i>
                                                </a>
                                                <a href="${contextPath}/whWip/emailList/remove/${wipList.id}" >
                                                    <i class='bx bx-trash-alt bx-md bx-fw bx-burst-hover' ></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <hr class="separator">
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
                oTable = $('#dt_spml').DataTable({
                    dom: 'Brtip',
                    columnDefs: [{
                            sortable: false,
                            targets: [3]
                        }],
                    buttons: [
                        {
                            extend: 'copy',
                            exportOptions: {
                                columns: [0, 1, 2, 3]
                            }
                        },
                        {
                            extend: 'excel',
                            exportOptions: {
                                columns: [0, 1, 2, 3]
                            }
                        },
                        {
                            extend: 'pdf',
                            exportOptions: {
                                columns: [0, 1, 2, 3]
                            }
                        },
                        {
                            extend: 'print',
                            exportOptions: {
                                columns: [0, 1, 2, 3]
                            },
                            customize: function (win) {
                                $(win.document.body)
                                        .css('font-size', '10pt'),
                                        $(win.document.body).find('table')
                                        .addClass('compact')
                                        .css('font-size', 'inherit');
                            }
                        }
                    ]
                });

                $('#dt_spml_search').keyup(function () {
                    oTable.search($(this).val()).draw();
                });

                $("#dt_spml_rows").change(function () {
                    oTable.page.len($(this).val()).draw();
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>