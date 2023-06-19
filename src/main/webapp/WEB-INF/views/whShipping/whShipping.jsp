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
            <h1>Warehouse Management - HW for Shipment to Rel Lab</h1>
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box clearfix">
                        <div class="clearfix">
                            <h2 class="pull-left">Hardware Queue for Shipping List</h2>
                            <div class="filter-block pull-right">
                                <a href="${contextPath}/wh/whShipping/query" class="btn btn-primary pull-right">
                                    <i class="fa fa-pencil-square-o fa-lg"></i> Query
                                </a>
                            </div>
                            <div class="filter-block pull-right">
<!--                                <a href="${contextPath}/wh/whShipping/whMpList" class="btn btn-primary pull-right">-->
                                <a href="${contextPath}/wh/whShipping/packingList" data-toggle="modal" class="btn btn-primary pull-right" onclick="modalDelete(this);">
                                    <i class="fa fa-truck fa-lg"></i> Create Shipping Packing List
                                </a>
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
                                        <th><span>Hardware Type</span></th> 
                                        <th><span>Hardware ID</span></th>
                                        <th><span>Box No.</span></th>
                                        <!--<th><span>Material Pass No.</span></th>-->
                                        <th><span>Qty</span></th>
                                        <th><span>Inventory</span></th>
                                        <th><span>Verification Date</span></th>
                                        <th><span>Status</span></th>
                                        <th><span>Manage</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${whShippingList}" var="whShipping" varStatus="whShippingLoop">
                                        <tr>                                            
                                            <td><c:out value="${whShippingLoop.index+1}"/></td>
                                            <td><c:out value="${whShipping.equipmentType}"/></td>
                                            <td id="modal_delete_info_${whShipping.requestId}">
                                                <c:if test="${whShipping.pairingType == 'PAIR'}">
                                                    <c:out value="${whShipping.loadCardId}"/><br><c:out value="${whShipping.progCardId}"/>
                                                </c:if>
                                                <c:if test="${whShipping.pairingType == 'SINGLE' && whShipping.equipmentType == 'Load Card'}">
                                                    <c:out value="${whShipping.loadCardId}"/>
                                                </c:if>
                                                <c:if test="${whShipping.pairingType == 'SINGLE' && whShipping.equipmentType == 'Program Card'}">
                                                    <c:out value="${whShipping.progCardId}"/>
                                                </c:if>
                                                <c:if test="${whShipping.pairingType == null}">
                                                    <c:out value="${whShipping.equipmentId}"/>
                                                </c:if>
                                            </td>
                                            <td><c:out value="${whShipping.boxNo}"/></td>
                                            <!--<td><c:out value="${whShipping.materialPassNo}"/></td>-->
                                            <td><c:out value="${whShipping.quantity}"/></td>
                                            <td><c:out value="${whShipping.inventoryRack}, ${whShipping.inventoryShelf}"/></td>
                                            <td><c:out value="${whShipping.dateVerify}"/></td>
                                            <td><c:out value="${whShipping.status}"/></td>
                                            <td align="center">
                                                <!--<a href="${contextPath}/wh/whShipping/edit/${whShipping.requestId}" class="table-link" title="Update">
                                                    <span class="fa-stack">
                                                        <i class="fa fa-square fa-stack-2x"></i>
                                                        <i class="fa fa-pencil fa-stack-1x fa-inverse"></i>
                                                    </span>
                                                </a>-->
<!--                                                <a href="${contextPath}/wh/whShipping/view/${whShipping.requestId}" class="table-link" title="View">
                                                    <span class="fa-stack">
                                                        <i class="fa fa-square fa-stack-2x"></i>
                                                        <i class="fa fa-search-plus fa-stack-1x fa-inverse"></i>
                                                    </span>
                                                </a>-->
                                                <a href="${contextPath}/wh/whShipping/history/${whShipping.requestId}" class="table-link" title="History">
                                                    <span class="fa-stack">
                                                        <i class="fa fa-square fa-stack-2x"></i>
                                                        <i class="fa fa-book fa-stack-1x fa-inverse"></i>
                                                    </span>
                                                </a>
                                                <!--<a modaldeleteid="${whShipping.requestId}" data-toggle="modal" href="#delete_modal" class="table-link danger group_delete" onclick="modalDelete(this);">
                                                    <span class="fa-stack">
                                                        <i class="fa fa-square fa-stack-2x"></i>
                                                        <i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>
                                                    </span>
                                                </a>-->
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
                        targets : [ 8 ]
                    }],
                    buttons: [
                        {
                            extend: 'copy',
                            exportOptions: {
                                columns: [ 0, 1, 2, 3, 4, 5, 6, 7 ]
                            }
                        },
                        {
                            extend: 'excel',
                            exportOptions: {
                                columns: [ 0, 1, 2, 3, 4, 5, 6, 7 ]
                            }
                        },
                        {
                            extend: 'pdf',
                            exportOptions: {
                                columns: [ 0, 1, 2, 3, 4, 5, 6, 7 ]
                            }
                        },
                        {
                            extend: 'print',
                            exportOptions: {
                                columns: [ 0, 1, 2, 3, 4, 5, 6, 7 ]
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
                
//                oTable.buttons().container().appendTo($("#dt_spml_tt", oTable.table().container() ) );
                
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
                var deleteUrl = "${contextPath}/wh/whShipping/delete/" + deleteId;
                var deleteMsg = "<f:message key='general.label.delete.confirmation'><f:param value='" + deleteInfo + "'/></f:message>";
                $("#delete_modal .modal-body").html(deleteMsg);
                $("#modal_delete_button").attr("href", deleteUrl);
            }
        </script>
    </s:layout-component>
</s:layout-render>