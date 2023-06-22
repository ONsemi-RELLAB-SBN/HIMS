<%-- 
    Document   : list_new
    Created on : Jun 21, 2023, 3:17:32 PM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
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
            <h1>WIP Management - Data from RMS</h1>
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box clearfix">
                        <div class="clearfix">
                            <h2 class="pull-left">HW for Shipment to Rel Lab List</h2>
                            <div class="filter-block pull-right">
                                <a href="${contextPath}/whWip/listReceive" class="btn btn-primary pull-right">
                                    <i class="fa fa-pencil-square-o fa-lg"></i> Scan GTS No
                                </a>
                            </div><!--
                            <div class="filter-block pull-right">
                                <a href="${contextPath}/wh/whRequest/ship" data-toggle="modal" class="btn btn-primary pull-right">
                                    <i class="fa fa-bars fa-lg"></i> View Hardware Queue for Shipment
                                </a>
                            </div>-->
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
                                        <th><span>Request ID</span></th> 
                                        <th><span>GTS NO</span></th>
                                        <th><span>RMS Event</span></th>
                                        <th><span>Intervals</span></th>
                                        <th><span>Quantity</span></th>
                                        <th><span>Shipment Date</span></th>
                                        <th><span>Status</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%--<c:forEach items="${whRequestList}" var="whRequest" varStatus="whRequestLoop">--%>
                                    <c:forEach items="${wipList}" var="whWip" varStatus="whWipLoop">
                                        <tr>
                                            <td><c:out value="${whWipLoop.index+1}"/></td>
                                            <td><c:out value="${whWip.requestId}"/></td>
                                            <td><c:out value="${whWip.gtsNo}"/></td>
                                            <td><c:out value="${whWip.rmsEvent}"/></td>
                                            <td><c:out value="${whWip.intervals}"/></td>
                                            <td><c:out value="${whWip.quantity}"/></td>
                                            <td><c:out value="${whWip.shipmentDate}"/></td>
                                            <td><c:out value="${whWip.status}"/></td>
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
                    columnDefs : [{
                        sortable : false,
                        targets : [ 7 ]
                    }],
                    buttons: [
                        {
                            extend: 'copy',
                            exportOptions: {
                                columns: [ 0, 1, 2, 3, 4, 5, 6 ]
                            }
                        },
                        {
                            extend: 'excel',
                            exportOptions: {
                                columns: [ 0, 1, 2, 3, 4, 5, 6 ]
                            }
                        },
                        {
                            extend: 'pdf',
                            exportOptions: {
                                columns: [ 0, 1, 2, 3, 4, 5, 6 ]
                            }
                        },
                        {
                            extend: 'print',
                            exportOptions: {
                                columns: [ 0, 1, 2, 3, 4, 5, 6 ]
                            },
                            customize: function (win) {
                                $(win.document.body)
                                    .css('font-size', '10pt')
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

            function modalDelete(e) {
                var deleteId = $(e).attr("modaldeleteid");
                var deleteInfo = $("#modal_delete_info_" + deleteId).html();
                var deleteUrl = "${contextPath}/wh/whRequest/delete/" + deleteId;
                var deleteMsg = "<f:message key='general.label.delete.confirmation'><f:param value='" + deleteInfo + "'/></f:message>";
                $("#delete_modal .modal-body").html(deleteMsg);
                $("#modal_delete_button").attr("href", deleteUrl);
            }
        </script>
    </s:layout-component>
</s:layout-render>