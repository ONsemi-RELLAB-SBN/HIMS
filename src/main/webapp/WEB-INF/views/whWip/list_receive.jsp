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
            <h1>WIP Management - Data from RMS 02</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Scan GTS Number</h2>
                        <form id="add_mp_list_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateReceive" method="post">
                            <div class="form-group">
                                <label for="boxNo" class="col-lg-3 control-label">GTS Number *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="boxNo" name="boxNo" placeholder="" value="" autofocus="autofocus">
                                    <!--<input id="dt_spml_search" type="text" class="form-control" placeholder="<f:message key="general.label.search"/>">-->
                                </div>
                            </div>
                            <a href="${contextPath}/whWip/listNew" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>
                            <div class="pull-right">
                                <button type="reset" class="btn btn-secondary cancel">Reset</button>
                                <!--<button type="button" class="btn btn-primary" onclick="searchData()">Check List</button>-->
                                <button type="submit" class="btn btn-primary">Receive</button>
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
                            <h2 class="pull-left">HIMS – WIP List</h2>
<!--                            <div class="filter-block pull-right">
                                <a href="#delete_modal" data-toggle="modal" class="btn btn-danger danger group_delete pull-right" onclick="modalDelete(this);" id="deleteAll">
                                    <i class="fa fa-trash-o fa-lg"></i> Delete All
                                </a>
                            </div>-->
                        </div>
<!--                        <div class="alert alert-success alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                            *Please delete all data after print the shipping material pass number list.
                        </div>    -->
                        <hr/>
                        <div class="clearfix">
<!--                            <div class="form-group pull-left">
                                <select id="dt_spml_rows" class="form-control">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select>
                            </div>-->
                            <div class="filter-block pull-right">
                                <div id="dt_spml_tt" class="form-group pull-left" style="margin-right: 5px;">
                                </div>
<!--                                <div class="form-group pull-left" style="margin-right: 0px;">
                                    <input id="dt_spml_search" type="text" class="form-control" placeholder="<f:message key="general.label.search"/>">
                                    <i class="fa fa-search search-icon"></i>
                                </div>-->
                            </div>
                        </div>
<!--                        <div class="col-lg-12">
                            <a href="${contextPath}/wh/whShipping/email" class="btn btn-info pull-right" id="print"><i class="fa fa-print"></i> Print</a>
                        </div>-->
                        <div class="table-responsive">
                            <input type="hidden" class="form-control" id="count" name="count" value="${count}">
                            <input type="hidden" class="form-control" id="countAll" name="countAll" value="${countAll}">
                            <table id="dt_spml" class="table">
                                <thead>
                                    <tr>
                                        <th><span>No</span></th>
                                        <th><span>Request ID</span></th>
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
                                            <td><c:out value="${wipList.requestId}"/></td>
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
//                oTable.search($(this).val()).draw();
                
                oTable = $('#dt_spml').DataTable({
//                    dom: 'Brtip'
                });
                
                $('#dt_spml_search').keyup(function () {
                    oTable.search($(this).val()).draw();
                });
                
                $('#boxNo').keyup(function () {
                    console.log("masuk ke sini");
                    oTable.search($(this).val()).draw();
                    console.log("log tigger the function again");
                });
                
                $("#dt_spml_rows").change(function () {
                    oTable.page.len($(this).val()).draw();
                });
                
                if ($('#countAll').val() === '0') {
                    $('#print').hide();
                } else {
                    $('#print').show();
                }
                
                if ($('#countAll').val() === '0') {
                    $('#deleteAll').hide();
                } else {
                    $('#deleteAll').show();
                }
                
//                var validator = $("#add_mp_list_form").validate({
//                    rules: {
//                        mpNo: {
//                            required: true
//                        }
//                    }
//                });
                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                $(".viewData").click(function () {
                    $("#data").show();
                });
            });
            
//            $('#submit1').on('click', function () {
//                location.reload();
//                // kat sini gantikan supaya dia masuk ke data 
////                window.open('${contextPath}/whWip/viewPackingListPdf', 'Packing List', 'width=1600,height=1100').print();
//            });
//            COMMENT HERE BECAUSE WE DO NOT USE IT
//            function modalDelete(e) {
//                var deleteUrl = "${contextPath}/wh/whShipping/deleteAll";
//                var deleteMsg = "Are you sure want to delete all? All related data will be deleted.";
//                $("#delete_modal .modal-body").html(deleteMsg);
//                $("#modal_delete_button").attr("href", deleteUrl);
//            }
            
        </script>
    </s:layout-component>
</s:layout-render>