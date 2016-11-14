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
            <h1>View Inventory</h1>
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box">
                        <h2>Search</h2>
                        <form id="update_hardwareinventory_form" class="form-horizontal" role="form" action="${contextPath}/wh/whInventory/viewInventory" method="post" style="width: 100%">
                            <div class="form-group col-lg-12" style="font-style: italic; color: red;" >
                                *Please insert the requirement(s) accordingly.</font
                                <br /><br /><br />
                            </div>

                            <div class="form-group col-lg-12" id = "alert_placeholder"></div>
                            <div class="form-group col-lg-12" >
                                <label for="inventoryRack" class="col-lg-2 control-label">Rack</label>
                                <div class="col-lg-2">
                                    <select id="inventoryRack" name="inventoryRack" class="form-control">
                                        <option value=""></option>
                                        <option value="S-TR01">S-TR01</option>
                                        <option value="S-TJ02">S-TJ02</option>
                                        <option value="S-BB">S-BB</option>
                                        <option value="S-IO">S-IO</option>
                                    </select>
                                </div>
                                <label for="equipmentType" class="col-lg-2 control-label">Hardware Category</label>
                                <div class="col-lg-2">
                                    <select id="equipmentType" name="equipmentType" class="form-control">
                                        <option value=""></option>
                                        <option value="Motherboard">Motherboard</option>
                                        <option value="PCB">PCB</option>
                                        <option value="Stencil">Stencil</option>
                                        <option value="Tray">Tray</option>
                                    </select>
                                </div>
                                <label for="equipmentId" class="col-lg-2 control-label">Hardware ID</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId">
                                </div>
                            </div>
                            <div class="col-lg-12">
                                <br/>
                            </div>
                            <div class="col-lg-12">
                                <a href="${contextPath}/wh/whInventory" class="btn btn-info pull-left" id="cancel"><i class="fa fa-reply"></i>Back</a>
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
                            <h2 class="pull-left">Inventory View</h2>
                        </div>
                        <hr/>
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
                                        <th><span>Inventory</span></th>
                                        <th><span>Status</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${inventoryQueryList}" var="whInventory" varStatus="whInventoryLoop">
                                        <tr>
                                            <td><c:out value="${whInventoryLoop.index+1}"/></td>
                                            <td><c:out value="${whInventory.materialPassNo}"/></td>
                                            <td><c:out value="${whInventory.materialPassExpiry}"/></td>
                                            <td><c:out value="${whInventory.equipmentType}"/></td>
                                            <td><c:out value="${whInventory.equipmentId}"/></td>
                                            <td><c:out value="${whInventory.quantity}"/></td>
                                            <td><c:out value="${whInventory.requestedBy}"/></td>
                                            <td><c:out value="${whInventory.requestedDate}"/></td>
                                            <td><c:out value="${whInventory.inventoryRack}, ${whInventory.inventoryShelf}"/></td>
                                            <td><c:out value="${whInventory.status}"/></td>
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
                
                oTable.buttons().container().appendTo($("#dt_spml_tt", oTable.table().container() ) );
                
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