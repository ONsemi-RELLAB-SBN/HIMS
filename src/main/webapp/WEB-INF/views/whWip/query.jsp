<%-- 
    Document   : query
    Created on : Jul 7, 2023, 11:38:16 AM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/css/libs/datepicker.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
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
            .wip-progress-container{
                width:100%;
                height:1.5em;
                position:relative;
                /*background-color:#f1f1f1*/
                background-color:#d9d9d9;
            }
            .wip-progressbar{
                /*background-color:#757575;*/
                background-color:#3fcfbb;
                height:100%;
                position:absolute;
                line-height:inherit
            }
            .wip-round-xlarge{
                border-radius:16px!important
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
                        <form id="update_hardwareinventory_form" class="form-horizontal" role="form" action="${contextPath}/whWip/query" method="post" style="width: 100%">
                            <div class="form-group col-lg-12" style="font-style: italic; color: red;" >
                                *Please insert the requirement(s) accordingly.</font
                                <br /><br /><br />
                            </div>
                            <div class="form-group col-lg-12" id = "alert_placeholder"></div>
                            <div class="form-group col-lg-12" >
                                <label for="status" class="col-lg-3 control-label">Status</label>
                                <div class="col-lg-2">
                                    <select id="status" name="status" class="form-control">
                                        <option value="All">All</option>
                                        <c:forEach items="${statusList}" var="list">
                                            <option value="${list.value}">${list.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <label for="shippingList" class="col-lg-1 control-label">Shipping List</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="shippingList" name="shippingList" value='${shippingList}'>
                                </div>
                                <label for="gtsNo" class="col-lg-1 control-label">GTS No.</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="gtsNo" name="gtsNo">
                                </div>
                            </div>
                            <div class="form-group col-lg-12" ></div>
                            <div class="form-group col-lg-12">
                                <label for="shipmentDate1" class="col-lg-3 control-label">Shipment Date (from Rel Lab) between </label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="shipmentDate1" name="shipmentDate1">
                                </div>
                                <label for="shipmentDate2" class="col-lg-1 control-label" style="text-align: center;"> AND </label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="shipmentDate2" name="shipmentDate2">
                                </div>
                                <label for="rmsEvent" class="col-lg-1 control-label">RMS Event</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="rmsEvent" name="rmsEvent">
                                </div>
                            </div>
                            <div class="form-group col-lg-12" ></div>
                            <div class="form-group col-lg-12">
                                <label for="receivedDate1" class="col-lg-3 control-label">Receive Date between </label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="receivedDate1" name="receivedDate1">
                                </div>
                                <label for="receivedDate2" class="col-lg-1 control-label" style="text-align: center;">AND</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="receivedDate2" name="receivedDate2">
                                </div>
                                <label for="intervals" class="col-lg-1 control-label">Intervals</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="intervals" name="intervals">
                                </div>
                            </div>
                            <div class="form-group col-lg-12" ></div>
                            <div class="form-group col-lg-12">
                                <label for="shipDate1" class="col-lg-3 control-label">Ship Date (to Rel Lab) between </label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="shipDate1" name="shipDate1">
                                </div>
                                <label for="shipDate2" class="col-lg-1 control-label" style="text-align: center;">AND</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="shipDate2" name="shipDate2">
                                </div>
                                <label for="quantity" class="col-lg-1 control-label">Quantity</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="quantity" name="quantity">
                                </div>
                            </div>
                            <div class="col-lg-12">
                                <br/>
                            </div>
                            <div class="col-lg-12">
                                <!--<a href="${contextPath}/whWip/listNew" class="btn btn-info pull-left" id="cancel" style="font-family:'Orbitron', monospace;"><i class='bx bxs-chevron-left bx-fw'></i> Back</a>-->
                                <button type="submit" class="btn btn-primary pull-right" name="submit" id="submit" style="font-family:'Orbitron', monospace;"><i class='bx bx-filter-alt bx-fw'></i>Filter Data</button>
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
                                        <th><span>GTS No</span></th>
                                        <th><span>RMS Event</span></th>
                                        <th><span>Intervals</span></th>
                                        <th><span>Quantity</span></th>
                                        <th><span>Shipment Date</span></th>
                                        <th><span>Shipping List</span></th>
                                        <th><span>Ship Date</span></th>
                                        <th><span>Status</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${wipList}" var="wipList" varStatus="whWipLoop">
                                        <tr>
                                            <td><c:out value="${whWipLoop.index+1}"/></td>
                                            <td><c:out value="${wipList.gtsNo}"/></td>
                                            <td><c:out value="${wipList.rmsEvent}"/></td>
                                            <td><c:out value="${wipList.intervals}"/></td>
                                            <td><c:out value="${wipList.quantity}"/></td>
                                            <td><c:out value="${wipList.shipmentDate}"/></td>
                                            <td><c:out value="${wipList.shippingList}"/></td>
                                            <td><time datetime="${wipList.shipDate}"><c:out value="${wipList.shipDate}"/></time></td>
                                            <td>
                                                <c:out value="${wipList.status}"/>
                                                <div class="wip-progress-container wip-round-xlarge">
                                                    <c:if test="${wipList.status == 'WIP New Shipment'}">
                                                        <div class="wip-progressbar wip-round-xlarge" style="width:1%"></div>
                                                    </c:if>
                                                    <c:if test="${wipList.status == 'WIP Received'}">
                                                        <div class="wip-progressbar wip-round-xlarge" style="width:20%"></div>
                                                    </c:if>
                                                    <c:if test="${wipList.status == 'WIP Verified'}">
                                                        <div class="wip-progressbar wip-round-xlarge" style="width:40%"></div>
                                                    </c:if>
                                                    <c:if test="${wipList.status == 'WIP Register For Shipment'}">
                                                        <div class="wip-progressbar wip-round-xlarge" style="width:60%"></div>
                                                    </c:if>
                                                    <c:if test="${wipList.status == 'WIP Ready For Shipment'}">
                                                        <div class="wip-progressbar wip-round-xlarge" style="width:80%"></div>
                                                    </c:if>
                                                    <c:if test="${wipList.status == 'WIP Ship to Rel Lab'}">
                                                        <div class="wip-progressbar wip-round-xlarge" style="width:100%"></div>
                                                    </c:if>
                                                </div>
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
                $('#shipmentDate1').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
                
                $('#shipmentDate2').datepicker({
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
                $('#shipDate1').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
                
                $('#shipDate2').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
                
                var validator = $("#update_hardwareinventory_form").validate({
                    rules: {
                        shipmentDate1: { 
                            required: function (element) {
                                if($('#shipmentDate1').val() === "" && $('#shipmentDate2').val() !== "") {
                                    return true;                            
                                } else {
                                    return false;
                                }
                            }
                        },
                        shipmentDate2: { 
                            required: function (element) {
                                if($('#shipmentDate2').val() === "" && $('#shipmentDate1').val() !== "") {
                                    return true;                            
                                } else {
                                    return false;
                                }
                            }
                        },
                        receivedDate1: { 
                            required: function (element) {
                                if($('#receivedDate1').val() === "" && $('#receivedDate2').val() !== "") {
                                    return true;                            
                                } else {
                                    return false;
                                }
                            }
                        },
                        receivedDate2: { 
                            required: function (element) {
                                if($('#receivedDate2').val() === "" && $('#receivedDate1').val() !== "") {
                                    return true;                            
                                } else {
                                    return false;
                                }
                            }
                        },
                        shipDate1: { 
                            required: function (element) {
                                if($('#shipDate1').val() === "" && $('#shipDate2').val() !== "") {
                                    return true;                            
                                } else {
                                    return false;
                                }
                            }
                        },
                        shipDate2: { 
                            required: function (element) {
                                if($('#shipDate2').val() === "" && $('#shipDate1').val() !== "") {
                                    return true;                            
                                } else {
                                    return false;
                                }
                            }
                        },
                    }
                });
                
                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                $(".submit").click(function () {
                    $("#data").show();
                });
                
                oTable = $('#dt_spml').DataTable({
                    dom: 'Brtip',
                    buttons: [
                        { extend: 'copy' },
                        { extend: 'excel' },
                        { extend: 'pdf' },
                        { extend: 'print',
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