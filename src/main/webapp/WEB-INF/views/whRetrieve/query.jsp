<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/css/libs/datepicker.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
        <!--<link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/dataTables.tableTools.css" type="text/css" />-->
    </s:layout-component>
    <s:layout-component name="page_css_inline">
        <style>
            @media print {
/*                table  {
                    border-top: #000 solid 1px;
                    border-bottom: #000 solid 1px;
                    border-left: #000 solid 1px;
                    border-right: #000 solid 1px;
                }*/
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
            <h1>Query</h1>
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box">
                        <h2>Query Requirements Search</h2>
                        <form id="update_hardwareinventory_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRetrieve/query" method="post" style="width: 100%">
                            <div class="form-group col-lg-12" style="font-style: italic; color: red;" >
                                *Please insert the requirement(s) accordingly.</font
                                <br /><br /><br />
                            </div>

                            <div class="form-group col-lg-12" id = "alert_placeholder"></div>
                            <div class="form-group col-lg-12" >
                                <label for="equipmentId" class="col-lg-2 control-label">Hardware ID</label>
                                <div class="col-lg-5">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId">
                                </div>
                                <label for="materialPassNo" class="col-lg-2 control-label">Material Pass No.</label>
                                <div class="col-lg-3">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo">
                                </div>
                            </div>
                            <div class="form-group col-lg-12" ></div>
                            <div class="form-group col-lg-12">
                                <label for="materialPassExpiry1" class="col-lg-2 control-label">M/Pass Expiry Date BETWEEN </label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="materialPassExpiry1" name="materialPassExpiry1">
                                </div>
                                <label for="materialPassExpiry2" class="col-lg-1 control-label" style="text-align: center;"> AND </label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="materialPassExpiry2" name="materialPassExpiry2">
                                </div>
                                <label for="equipmentType" class="col-lg-2 control-label">Hardware Category</label>
                                <div class="col-lg-3">
                                    <!--                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType">-->
                                    <select id="equipmentType" name="equipmentType" class="form-control">
                                        <option value=""></option>
                                        <option value="Motherboard">Motherboard</option>
                                        <option value="PCB">PCB</option>
                                        <option value="Stencil">Stencil</option>
                                        <option value="Tray">Tray</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group col-lg-12" ></div>
                            <div class="form-group col-lg-12">
                                <label for="requestedDate1" class="col-lg-2 control-label">Requested Date BETWEEN </label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="requestedDate1" name="requestedDate1">
                                </div>
                                <label for="requestedDate2" class="col-lg-1 control-label" style="text-align: center;">AND</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="requestedDate2" name="requestedDate2">
                                </div>
                                <label for="requestedBy" class="col-lg-2 control-label">Requested By</label>
                                <div class="col-lg-3">
                                    <input type="text" class="form-control" id="requestedBy" name="requestedBy">
                                </div>
                            </div>
                            <div class="form-group col-lg-12" ></div>
                            <div class="form-group col-lg-12">
                                <label for="receivedDate1" class="col-lg-2 control-label">Box Receival Date BETWEEN </label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="receivedDate1" name="receivedDate1">
                                </div>
                                <label for="receivedDate2" class="col-lg-1 control-label" style="text-align: center;">AND</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="receivedDate2" name="receivedDate2">
                                </div>
                                <label for="status" class="col-lg-2 control-label">Status</label>
                                <div class="col-lg-3">
                                    <select id="status" name="status" class="form-control">
                                        <option value="" selected></option>
                                        <option value="New Inventory Request">New Inventory Request</option>
                                        <option value="Verification Pass">Barcode Verification Pass</option>
                                        <option value="Verification Fail">Barcode Verification Fail</option>
                                        <option value="Inventory Valid">Inventory Valid</option>
                                        <option value="Inventory Invalid">Inventory Invalid</option>
                                        <option value="Move to Inventory">Move to Inventory</option>
                                    </select>
                                </div>
                            </div>                            
                            <div class="col-lg-12">
                                <br/>
                            </div>
                            <div class="col-lg-12">
                                <a href="${contextPath}/wh/whRetrieve" class="btn btn-info pull-left" id="cancel"><i class="fa fa-reply"></i>Back</a>
                                <button type="submit" class="btn btn-primary pull-right" name="submit" id="submit" >View Data</button>
                            </div>
                            <div class="clearfix"><br/></div>
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
                            <h2 class="pull-left">Query Search List</h2>
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
                                <div id="dt_spml_tt" class="form-group pull-left" style="margin-right: 5px;"></div>
                                <div class="form-group pull-left" style="margin-right: 0px;">
                                    <input id="dt_spml_search" type="text" class="form-control" placeholder="<f:message key="general.label.search"/>">
                                    <i class="fa fa-search search-icon"></i>
                                </div>
                            </div>
                        </div>
                        <div><br/></div>
                        <div class="table-responsive">            
                            <table id="dt_spml" class="table" style="font-size: 10px;">
                                <thead>
                                    <tr>
                                        <th><span>No</span></th>
                                        <th><span>Material Pass No</span></th> 
                                        <th><span>Material Pass Expiry</span></th>
                                        <th><span>Hardware Type</span></th>
                                        <th><span>Hardware ID</span></th>
                                        <th><span>Quantity</span></th>
                                        <th><span>Requested By</span></th>
                                        <th><span>Requested Date</span></th>
                                        <th><span>HIMS-SF Received Date</span></th>
                                        <th><span>Status</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${retrieveQueryList}" var="whRetrieve" varStatus="whRetrieveLoop">
                                        <tr>
                                            <td><c:out value="${whRetrieveLoop.index+1}"/></td>
                                            <td><c:out value="${whRetrieve.materialPassNo}"/></td>
                                            <td><c:out value="${whRetrieve.materialPassExpiry}"/></td>
                                            <td><c:out value="${whRetrieve.equipmentType}"/></td>
                                            <td><c:out value="${whRetrieve.equipmentId}"/></td>
                                            <td><c:out value="${whRetrieve.quantity}"/></td>
                                            <td><c:out value="${whRetrieve.requestedBy}"/></td>
                                            <td><c:out value="${whRetrieve.requestedDate}"/></td>
                                            <td><c:out value="${whRetrieve.receivedDate}"/></td>
                                            <td><c:out value="${whRetrieve.status}"/></td>
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
        <script src="${contextPath}/resources/validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
        <script src="${contextPath}/resources/private/js/bootstrap-datepicker.js"></script>
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
                $('#requestedDate1').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
                
                $('#requestedDate2').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
                
                $('#materialPassExpiry1').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
                
                $('#materialPassExpiry2').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
                $('#receivedDate1').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
                
                $('#receivedDate2').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
                
                var validator = $("#update_hardwareinventory_form").validate({
                    rules: {
                        requestedDate1: { 
                            required: function (element) {
                                if($('#requestedDate1').val() === "" && $('#requestedDate2').val() !== "") {
                                    return true;                            
                                }
                                else {
                                    return false;
                                }
                            }
                        },
                        requestedDate2: { 
                            required: function (element) {
                                if($('#requestedDate2').val() === "" && $('#requestedDate1').val() !== "") {
                                    return true;                            
                                }
                                else {
                                    return false;
                                }
                            }
                        },
                        materialPassExpiry1: { 
                            required: function (element) {
                                if($('#materialPassExpiry1').val() === "" && $('#materialPassExpiry2').val() !== "") {
                                    return true;                            
                                }
                                else {
                                    return false;
                                }
                            }
                        },
                        materialPassExpiry2: { 
                            required: function (element) {
                                if($('#materialPassExpiry2').val() === "" && $('#materialPassExpiry1').val() !== "") {
                                    return true;                            
                                }
                                else {
                                    return false;
                                }
                            }
                        },
                        receivedDate1: { 
                            required: function (element) {
                                if($('#receivedDate1').val() === "" && $('#receivedDate2').val() !== "") {
                                    return true;                            
                                }
                                else {
                                    return false;
                                }
                            }
                        },
                        receivedDate2: { 
                            required: function (element) {
                                if($('#receivedDate2').val() === "" && $('#receivedDate1').val() !== "") {
                                    return true;                            
                                }
                                else {
                                    return false;
                                }
                            }
                        }
                    }
                });
                
                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                $(".submit").click(function () {
                    $("#data").show();
                });
                
//                oTable = $('#dt_spml').DataTable({
//                    dom: 'Bfrtip',
//                    buttons: [
//                        'print'
//                    ]
//                });
                oTable = $('#dt_spml').DataTable({
                    dom: 'Brtip',
                    buttons: [
                        {
                            extend: 'copy'
                        },
                        {
                            extend: 'excel'
                        },
                        {
                            extend: 'pdf'
                        },
                        {
                            extend: 'print',
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
        </script>
    </s:layout-component>
</s:layout-render>